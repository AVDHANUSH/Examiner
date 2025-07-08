# 🧠 Examiner – AI-Powered Online Exam Evaluation System

Examiner is a full-stack Java Spring Boot + React.js web application that automates online exam grading, detects plagiarism, and provides advanced evaluation analytics using GPT-4, TF-IDF, and Sentence-BERT.

---

## 📚 What It Does

✍️ Supports multiple-choice and descriptive questions  
🧮 Automatically grades objective and subjective answers  
📊 Visualizes content overlap via plagiarism heatmaps  
🔐 Role-based access (Admin / Instructor / Student)  
⚡ Fast and scalable with modular API integration  

---

## ✨ Features

### 🔑 Authentication & User Roles
✔️ JWT-based login for Admin, Instructors & Students  
✔️ Secure session handling with refresh tokens  

### 📝 Quiz & Question Management
✔️ Instructors can create quizzes with MCQ & Descriptive questions  
✔️ Quiz deadlines and instructions support  
✔️ Stores and fetches quiz results securely  

### 🧠 AI-Based Evaluation & Plagiarism Detection
✔️ Uses GPT-4 Turbo for descriptive answer grading  
✔️ TF-IDF + Sentence-BERT hybrid model for semantic similarity  
✔️ Real-time similarity matrix with heatmaps  
✔️ Detects paraphrased and exact plagiarism  

### 👨‍🏫 Instructor Dashboard
✔️ Upload quiz, manage answers  
✔️ Review grading results and feedback  
✔️ View plagiarism reports and student performance  

### 👩‍🎓 Student Dashboard
✔️ View available quizzes  
✔️ Submit answers manually
✔️ Get real-time feedback and marks  

### 📊 Visualization
✔️ Heatmaps to flag copied responses  
✔️ Scorecards and performance analytics  
✔️ Interactive UI with role-specific views  

### 🕵️‍♂️ Proctoring (Client-Side)
✔️ Detects tab-switching, copy-paste, dev tools  
✔️ Real-time alerts during quiz attempt  

---

## ⚙️ Installation & Setup

### 🛠️ 1. Clone the Repository  
```bash
git clone https://github.com/AVDHANUSH/Examiner.git
cd Examiner
```

### 🔧 2. Backend (Java Spring Boot)  
```bash
cd backend
./mvnw spring-boot:run
```

### 🌐 3. Frontend (React)  
```bash
cd frontend
npm install
npm start
```

### 🧪 4. Test with Localhost  
Frontend: [http://localhost:3000](http://localhost:3000)  
Backend API: [http://localhost:8080](http://localhost:8080)

---

## 📡 API Endpoints (Backend Spring Boot)

| Method | Endpoint                        | Description                           |
|--------|----------------------------------|----------------------------------------|
| GET    | /api/v1/quizzes/available        | List all quizzes                       |
| POST   | /api/v1/quizzes                  | Create a new quiz                      |
| POST   | /api/v1/quizzes/evaluate         | Evaluate quiz answers                  |
| GET    | /api/v1/quizzes/quiz-results     | Fetch quiz results for logged-in user  |

---

## 🧠 AI Integration

✅ Descriptive Answer Evaluation using **OpenAI GPT-4 Turbo**  
✅ Plagiarism Detection via **TF-IDF + Sentence-BERT**  
✅ Real-time scoring with heatmaps and similarity scores  

---

## 🔐 Security & Compliance

- JWT-based token authentication  
- HTTPS-secured endpoints  
- Role-based access control  
- Data sanitized and discarded post-processing  

---

## 📈 Performance

- Avg grading time (GPT): **1.2s per answer**  
- End-to-end grading per student: **~6.3s**  
- Plagiarism Precision: **95%**, Recall: **92%**  
- Tab-switch detection: **98%**, Copy-paste block: **100%**

---

## 🧩 Future Improvements

- Webcam-based proctoring  
- Multi-language support  
- LMS integration (Moodle, Google Classroom)  
- Explainable AI for transparent grading  

---

## 👨‍💻 Authors

- T V N Harsha Vardhan  
- G V M J Deepthi  
- A V Dhanush 

🎓 Vellore Institute of Technology – AP  
📅 May 2025


