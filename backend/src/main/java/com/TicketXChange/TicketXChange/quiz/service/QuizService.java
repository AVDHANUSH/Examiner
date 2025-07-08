package com.TicketXChange.TicketXChange.quiz.service;


import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.chatgptverify.dto.ChatGPTRequest;
import com.TicketXChange.TicketXChange.chatgptverify.dto.ChatGptResponse;
import com.TicketXChange.TicketXChange.quiz.dto.*;
import com.TicketXChange.TicketXChange.quiz.enums.QuestionType;

import com.TicketXChange.TicketXChange.quiz.model.Question;
import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import com.TicketXChange.TicketXChange.quiz.model.QuizResult;
import com.TicketXChange.TicketXChange.quiz.model.UserAnswers;
import com.TicketXChange.TicketXChange.quiz.repository.QuizRepository;
import com.TicketXChange.TicketXChange.quiz.repository.QuizResultRepository;
import com.TicketXChange.TicketXChange.quiz.repository.UserAnswersRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserProfileRepository userProfileRepository;

    private final QuizResultRepository quizResultRepository;

    private final UserAnswersRepository userAnswersRepository;

    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String chatGptApiUrl;

    @Value("${openai.model}")
    private String model;

    public Quiz createQuiz(QuizDTO quizDTO, UserProfile userProfile) {
        Quiz quiz = Quiz.builder()
                .quizNumber(quizDTO.getQuizNumber())
                .createdAt(LocalDateTime.now())
                .user(userProfile)
                .build();

        List<Question> questions = quizDTO.getQuestions().stream()
                .map(q -> Question.builder()
                        .text(q.getText())
                        .type(q.getType())
                        .options(q.getType() == QuestionType.MULTIPLE_CHOICE ? q.getOptions() : null)
                        .answer(q.getAnswer())
                        .quiz(quiz)
                        .build())
                .collect(Collectors.toList());

        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }

    public List<QuizResponseDTO> getQuestionsByQuizId(String quizId) {
        Quiz quiz = quizRepository.findByQuizNumber(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return quiz.getQuestions().stream()
                .map(question -> new QuizResponseDTO(
                        question.getText(),
                        question.getType(),
                        question.getOptions()
                ))
                .collect(Collectors.toList());
    }

    // Fetch all available quiz IDs on the platform
    public List<String> getAllQuizIds() {
        return quizRepository.findAll().stream()
                .map(Quiz::getQuizNumber)
                .collect(Collectors.toList());
    }



    public String evaluateQuiz(QuizSubmissionDTO quizSubmissionDTO) {
        // Extract user profile
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = currentUser.getUsername();
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserEmail(userEmail);
        UserProfile userProfile = userProfileOptional.orElseThrow(() -> new RuntimeException("User profile not found"));

        // Find the quiz by quiz number
        Quiz quiz = quizRepository.findByQuizNumber(quizSubmissionDTO.getQuizNumber())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<String> descriptiveQuestions = new ArrayList<>();
        List<String> descriptiveAnswers = new ArrayList<>();
        String quizNumber = quiz.getQuizNumber();
        int count = 0;
        for (Question question : quiz.getQuestions()) {
            String userAnswer = quizSubmissionDTO.getQuestions().get(count).getAnswer();
            count++;
            if (question.getType() == QuestionType.DESCRIPTIVE) {
                // Use ChatGPT API to evaluate descriptive answers
                descriptiveQuestions.add(question.getText());
                descriptiveAnswers.add(userAnswer);
            }
        }

        UserAnswers userAnswers = UserAnswers
                .builder()
                .quizNumber(quizNumber)
                .questions(descriptiveQuestions)
                .answers(descriptiveAnswers)
                .build();
        userAnswersRepository.save(userAnswers);

        count = 0;
        // Initialize the total marks
        double totalMarks = 0.0;
        List<Double> marksForEachQuestion = new ArrayList<>();
        // Evaluate each question

        for (Question question : quiz.getQuestions()) {
            String userAnswer = quizSubmissionDTO.getQuestions().get(count).getAnswer();
            double marks = 0.0;
            count++;
            if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                // Straightforward comparison for multiple-choice questions
                marks = question.getAnswer().equals(userAnswer) ? 5.0 : 0.0;
            } else if (question.getType() == QuestionType.DESCRIPTIVE) {
                // Use ChatGPT API to evaluate descriptive answers
                String relevancePrompt = buildRelevancePrompt(question.getText(), question.getAnswer(), userAnswer);
                marks = evaluateDescriptiveAnswer(relevancePrompt);
            }

            totalMarks += marks;

            marksForEachQuestion.add(marks);
        }
        System.out.println(totalMarks);
        Optional<List<UserAnswers>> optionalUserAnswers = userAnswersRepository.findByQuizNumber(quizNumber);
        if (optionalUserAnswers.isPresent()) {
            List<UserAnswers> userAnswersList = optionalUserAnswers.get();
            int numberOfQuestions = userAnswersList.get(0).getQuestions().size();
            for (int i = 0; i < numberOfQuestions; i++) {
                List<String> answersForQuestion = new ArrayList<>();
                for (UserAnswers ua : userAnswersList) {
                    if (ua.getAnswers().size() > i) {
                        answersForQuestion.add(ua.getAnswers().get(i));
                    }
                }
                boolean isPlagiarized = checkPlagiarism(answersForQuestion, 0.95);
                if (isPlagiarized && marksForEachQuestion.size() > i) {
                    double currentMarks = marksForEachQuestion.get(i);
                    marksForEachQuestion.set(i, Math.max(currentMarks - 1.0, 0.0));
                }
            }
            totalMarks = marksForEachQuestion.stream().mapToDouble(Double::doubleValue).sum();
        }

        // Save the quiz result
        QuizResult quizResult = new QuizResult();
        quizResult.setQuizNumber(quizSubmissionDTO.getQuizNumber());
        quizResult.setMarksForEachQuestion(marksForEachQuestion);
        quizResult.setTotalMarks(totalMarks);
        quizResult.setUser(userProfile);
        quizResultRepository.save(quizResult);

        return "Quiz evaluated successfully!";
    }

    private boolean checkPlagiarism(List<String> answers, double threshold) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("answers", answers);
        requestBody.put("threshold", threshold);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8000/analyze", requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Boolean> flags = (List<Boolean>) response.getBody().get("plagiarism_flags");
                return flags != null && !flags.isEmpty() && flags.get(flags.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String buildRelevancePrompt(String questionText, String predefinedAnswer, String userAnswer) {
        return "Evaluate the relevance of the following user answer compared to the predefined answer for the given question.\n\n" +
                "Question: " + questionText + "\n" +
                "Predefined Answer: " + predefinedAnswer + "\n" +
                "User Answer: " + userAnswer + "\n\n" +
                "Respond ONLY in this exact JSON format: { \"relevance\": <score between 0 and 100> }.";
    }


    private double evaluateDescriptiveAnswer(String prompt) {
        // Create request to ChatGPT API
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatGPTRequest> requestEntity = new HttpEntity<>(request, headers);

        // Send request and get response
        ChatGptResponse chatGptResponse = restTemplate.postForObject(chatGptApiUrl, requestEntity, ChatGptResponse.class);

        // Extract JSON string from ChatGPT response
        String content = chatGptResponse.getChoices().get(0).getMessage().getContent();
        System.out.println("ChatGPT raw content: " + content);

        double relevanceScore = extractRelevanceScore(content);

        // Assign marks based on relevance score
        if (relevanceScore >= 100) return 5;
        if (relevanceScore >= 90) return 4;
        if (relevanceScore >= 80) return 3;
        if (relevanceScore >= 70) return 2;
        if (relevanceScore >= 60) return 1;
        return 0;
    }


    private double extractRelevanceScore(String content) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(content);
            return rootNode.get("relevance").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse relevance score from ChatGPT response", e);
        }
    }


    public List<QuizResultDTO> getQuizResults(UserProfile userProfile) {
        List<QuizResult> quizResults = quizResultRepository.findByUser(userProfile);

        return quizResults.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private QuizResultDTO convertToDTO(QuizResult quizResult) {
        QuizResultDTO dto = new QuizResultDTO();
        dto.setQuizNumber(quizResult.getQuizNumber());
        dto.setMarksForEachQuestion(quizResult.getMarksForEachQuestion());
        dto.setTotalMarks(quizResult.getTotalMarks());
        dto.setCreatedAt(quizResult.getCreatedAt());
        return dto;
    }
}

