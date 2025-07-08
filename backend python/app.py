from fastapi import FastAPI, UploadFile, File, HTTPException
from pydantic import BaseModel
from typing import List
import numpy as np
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sentence_transformers import SentenceTransformer
import matplotlib.pyplot as plt
import seaborn as sns
import io
import uuid
import os
from fastapi.responses import FileResponse, JSONResponse
from fastapi.staticfiles import StaticFiles

app = FastAPI(title="Student Answer Plagiarism Detector")

# Mount static files for storing generated reports
os.makedirs("reports", exist_ok=True)
app.mount("/reports", StaticFiles(directory="reports"), name="reports")

# Load models at startup
model = SentenceTransformer('all-MiniLM-L6-v2')

class AnalysisRequest(BaseModel):
    answers: List[str]
    threshold: float = 0.95
    include_heatmap: bool = True

class AnalysisResult(BaseModel):
    report_id: str
    plagiarism_scores: List[float]
    plagiarism_flags: List[bool]
    heatmap_url: str = None
    similarity_matrix: List[List[float]]

def preprocess(text: str) -> str:
    text = text.lower()
    text = ''.join([c for c in text if c.isalpha() or c == ' '])
    return text

def generate_heatmap(similarity_matrix: np.ndarray, report_id: str):
    plt.figure(figsize=(10, 8))
    sns.heatmap(similarity_matrix, 
                annot=True, 
                fmt=".2f",
                cmap="YlOrRd",
                vmin=0, 
                vmax=1,
                xticklabels=[f"Ans {i+1}" for i in range(len(similarity_matrix))],
                yticklabels=[f"Ans {i+1}" for i in range(len(similarity_matrix))])
    plt.title("Pairwise Answer Similarity Scores")
    filepath = f"reports/{report_id}_heatmap.png"
    plt.savefig(filepath, bbox_inches='tight')
    plt.close()
    return filepath

@app.post("/analyze", response_model=AnalysisResult)
async def analyze_answers(request: AnalysisRequest):
    try:
        # Preprocessing
        preprocessed = [preprocess(ans) for ans in request.answers]
        
        # Feature Extraction
        tfidf = TfidfVectorizer(ngram_range=(1, 2))
        tfidf_vectors = tfidf.fit_transform(preprocessed)
        bert_vectors = model.encode(preprocessed)
        
        # Similarity Matrices
        tfidf_sim = cosine_similarity(tfidf_vectors)
        bert_sim = cosine_similarity(bert_vectors)
        hybrid_sim = 0.6 * bert_sim + 0.4 * tfidf_sim
        
        # Plagiarism Detection
        plagiarism_scores = []
        for i in range(len(request.answers)):
            row = hybrid_sim[i].copy()
            row[i] = 0  # Ignore self-comparison
            plagiarism_scores.append(round(np.max(row), 4))
        
        plagiarism_flags = [score >= request.threshold for score in plagiarism_scores]
        
        # Generate report
        report_id = str(uuid.uuid4())
        heatmap_path = None
        
        if request.include_heatmap:
            heatmap_path = generate_heatmap(hybrid_sim, report_id)
            heatmap_url = f"/reports/{os.path.basename(heatmap_path)}"
        else:
            heatmap_url = None
        
        return {
            "report_id": report_id,
            "plagiarism_scores": plagiarism_scores,
            "plagiarism_flags": plagiarism_flags,
            "heatmap_url": heatmap_url,
            "similarity_matrix": hybrid_sim.tolist()
        }
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/analyze/csv")
async def analyze_csv(file: UploadFile = File(...), threshold: float = 0.95):
    try:
        contents = await file.read()
        df = pd.read_csv(io.StringIO(contents.decode('utf-8')))
        if 'answer' not in df.columns:
            raise HTTPException(status_code=400, detail="CSV must contain 'answer' column")
        
        request = AnalysisRequest(
            answers=df['answer'].tolist(),
            threshold=threshold
        )
        return await analyze_answers(request)
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/report/{report_id}")
async def get_report(report_id: str):
    heatmap_path = f"reports/{report_id}_heatmap.png"
    if os.path.exists(heatmap_path):
        return FileResponse(heatmap_path)
    raise HTTPException(status_code=404, detail="Report not found")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)