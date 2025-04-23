# AutoCheckout: AI-powered Smart Billing System 🛒🤖

AutoCheckout is a smart billing system that automates the checkout process in retail environments using a Vision Transformer (ViT) model, live video streaming via ESP32-CAM, a real-time REST API backend, Firebase database integration, and a companion Android application.

---

## 🌟 Features
- Real-time object detection using a ViT model.
- Live streaming via ESP32-CAM.
- RESTful API for sending prediction results.
- Firebase Realtime Database to store billing data securely.
- Android app that receives itemized billing info and history.

---

## 📂 Repository Structure

```
AutoCheckout/
├── VisionTransformer/        # Model training & architecture
├── client/                   # Prediction using ViT model on live stream
├── CameraWebServer/          # ESP32-CAM live streaming code
├── AuoCheckoutApp/           # Java-based Android application
├── media/                    # Screenshots, predictions, app flow diagrams (Add here)
└── README.md                 # This file
```

---

## 🧠 VisionTransformer
This folder contains the ViT-B/16 based architecture and training logic:
- Trained on a custom dataset with 6 classes.
- Optimized using AdamW optimizer.
- Cosine annealing learning rate.
- Evaluation metrics: Accuracy, Precision, Recall, F1 Score.

### 🔧 Training Steps
1. Load and preprocess dataset.
2. Load pretrained ViT-B/16 model from torchvision.
3. Freeze base layers, modify classification head.
4. Train using label smoothing loss.
5. Save model (`vit_model_quantized.pth`).

---

## 🔮 Live Prediction (client/)
- Loads the `vit_model_quantized.pth` model.
- Connects to ESP32 video stream using OpenCV.
- Processes each frame and performs inference.
- Sends prediction results to REST API.

### ⚡ Run Prediction
```bash
cd client
python predict_stream.py  # Assumes stream is live and model is downloaded
```

### 📸 Screenshot of Live Prediction
> 🖼️ Place your screenshot here: `media/live_prediction.png`

```md
![Live Prediction](media/live_prediction.png)
```

---

## 🎥 ESP32-CAM Streaming (CameraWebServer/)
This folder contains the Arduino sketch to stream video locally.

### 🛠️ Setup & Upload with Arduino IDE
1. Open Arduino IDE.
2. Select Board: `AI Thinker ESP32-CAM`.
3. Set correct COM port.
4. Open `CameraWebServer.ino`.
5. Update WiFi SSID and Password.
6. Upload sketch and open Serial Monitor.

Live stream will be available at: `http://<ESP_IP_ADDRESS>:81/stream`

---

## 🌐 Backend (REST API)
- FastAPI used for real-time communication.
- Supports WebSockets for live data push to Android.
- Hosted using Render or similar platforms.

### ⚙️ Run Locally
```bash
uvicorn main:app --host 0.0.0.0 --port 8000
```

---

## 🔥 Firebase Integration
- Firebase Realtime Database used to store cart & history.
- Firebase Auth used for login.

### ✅ Security Rules
Only the user with the corresponding UID can read/write:
```json
{
  "rules": {
    "cart": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "history": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    ".read": false,
    ".write": false
  }
}
```

---

## 📱 Android App (AndroidApp/)
- Built with Java.
- Uses Firebase Auth for login.
- Listens to cart/history updates via Realtime Database.
- Shows billing history and itemized cart info.

### 🔐 Firebase Auth Logic
```java
mAuth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        }
    });
```

### 📲 App Flow Screenshot
> 🖼️ Add UI flow/image here: `media/app_flow.png`

```md
![App Flow](media/app_flow.png)
```

---

## 🚀 How to Run Entire Project
1. **ESP32**: Upload sketch, start stream.
2. **Backend API**: Start FastAPI or deploy on Render.
3. **Client**: Run prediction code.
4. **Android App**: Install, login with Firebase, and receive billing info.

---

## 📬 Contributing
Open to issues and pull requests! Feel free to enhance features, model, or documentation.

---

## 🙌 Acknowledgements
- TorchVision
- RestAPI
- Firebase
- OpenCV
- ESP32 Community

---

> 🔐 Don't forget to secure your `.json` credentials file (for Firebase) and exclude it in `.gitignore`!

---
