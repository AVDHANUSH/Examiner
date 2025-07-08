package com.TicketXChange.TicketXChange.chatgptverify.controller;

import com.TicketXChange.TicketXChange.chatgptverify.dto.ChatGPTRequest;
import com.TicketXChange.TicketXChange.chatgptverify.dto.ChatGptResponse;
import com.TicketXChange.TicketXChange.chatgptverify.dto.UploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@ModelAttribute UploadRequest uploadRequest) throws IOException {
        // Prepare the request body as multipart form-data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("model", model);
//        body.add("file", new ByteArrayMultipartFile(uploadRequest.getFile().getBytes(), uploadRequest.getFile().getOriginalFilename()));
        body.add("description", uploadRequest.getDescription());
//        body.add("purpose", uploadPurpose); // Add purpose to the body

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the request
        ResponseEntity<String> responseEntity = template.exchange(apiURL, HttpMethod.POST, requestEntity, String.class);

        // Return the response
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
    }
    @GetMapping("/chat")
    public String chat(@RequestBody UploadRequest prompt) throws UnsupportedEncodingException {
        // Create the request object
//        String encodedPrompt = URLEncoder.encode(prompt.getDescription(), StandardCharsets.UTF_8.toString());
        ChatGPTRequest request=new ChatGPTRequest(model, prompt.getDescription());
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }


}
