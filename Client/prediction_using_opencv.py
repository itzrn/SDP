import time
import warnings
import requests
import firebase_admin
import torch
import cv2
from torchvision import models
from torchvision.models import ViT_B_16_Weights
from PIL import Image
from firebase_admin import credentials, db
import numpy as np

warnings.filterwarnings("ignore", category=UserWarning)

class AutoCheckoutSystem:
    class_names = ['comb', 'doublemint', 'mouse', 'No Item Detected', 'suns_cream', 'tooth_brush']
    item_price_map = {
        "comb": 30,
        "doublemint": 50,
        "suns_cream": 540,
        "tooth_brush": 30,
        "mouse": 450
    }

    def __init__(self):
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        self.model = models.vit_b_16(weights=None).to(self.device)
        self.model.heads = torch.nn.Linear(in_features=768, out_features=len(self.class_names)).to(self.device)
        self.model.load_state_dict(torch.load("model.pth", map_location=self.device))
        self.model.eval()
        self.weights = ViT_B_16_Weights.DEFAULT
        self.transform = self.weights.transforms()

        self.uniqueItems = set()
        self.video_path = ""
        self.user_id = ""
        self.url = ""
        self.time_out_threshold_value = 600
        self.CV_WINDOW_SIZE = 600

        cred = credentials.Certificate('autocheckouts_firebase_credential.json')
        firebase_admin.initialize_app(cred, {
            'databaseURL': 'https://sdpautocheckouts-default-rtdb.asia-southeast1.firebasedatabase.app/'
        })

    def set_video_path(self, path):
        self.video_path = path

    def set_user_id(self, id):
        self.user_id = id

    def get_all_unique_items(self):
        return self.uniqueItems

    def set_end_point_url(self, url):
        self.url = url

    def set_time_out_threshold(self, time_out):
        self.time_out_threshold_value = float(time_out)

    def db_reference(self, db_name):
        return db.reference(f'/{db_name}/{self.user_id}')

    def predict_frame(self, frame):
        try:
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            image = Image.fromarray(image)
            image = self.transform(image).unsqueeze(0).to(self.device)
            with torch.no_grad():
                outputs = self.model(image)
                _, predicted = torch.max(outputs, 1)
                pred_idx = predicted.item()

            if 0 <= pred_idx < len(self.class_names):
                return self.class_names[pred_idx]
            else:
                return "No Item Detected"

        except Exception:
            return "No Item Detected"

    def custom_push(self, element):
        if element in self.uniqueItems or element == "No Item Detected":
            return True

        self.uniqueItems.add(element)
        payload = {
            "id": self.user_id,
            "item": element,
            "price": self.item_price_map[element]
        }
        response = requests.post(self.url, json=payload)
        print(response.json()['message'])
        return response.json()['status']

    def wait_until_cart_deleted(self):
        start_time = time.time()
        cart_ref = self.db_reference('cart')
        print('⏰ Waiting To check out the items before time out ...')

        while time.time() - start_time < self.time_out_threshold_value:
            if not cart_ref.get():
                print("✔️ Congratulations You have successfully checkout the items! \nReceipt will get e-mail to you. Thanks you!")
                return True
            time.sleep(5)

        print(f"Sorry it's ⏰Timeout reached. Session Ended!")
        cart_ref.delete()

    def video_streaming(self):
        if not self.user_id or not self.video_path or not cv2.VideoCapture(self.video_path).isOpened():
            print("❌ Invalid or empty video path or user is not defined. Exiting function.")
            return

        # cap = cv2.VideoCapture(self.video_path)
        cap = cv2.VideoCapture(self.video_path, cv2.CAP_FFMPEG)
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                break
            square_frame = cv2.resize(frame, (self.CV_WINDOW_SIZE, self.CV_WINDOW_SIZE))

            # pred_class = self.predict_frame(frame)
            pred_class = self.predict_frame(square_frame)
            status = self.custom_push(pred_class)
            if not status:
                cart_ref = self.db_reference('cart')
                if cart_ref.get():
                    cart_ref.delete()
                return

            cv2.putText(square_frame, f"Prediction: {pred_class}", (10, 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

            cv2.imshow(f'Vision Transformer Prediction for id {self.user_id}', square_frame)
            if cv2.waitKey(25) & 0xFF == ord('q'):
                break
                # cart_ref = self.db_reference('cart')
                # if cart_ref.get():
                #     cart_ref.delete()
                # return

        cap.release()
        cv2.destroyAllWindows()
        self.wait_until_cart_deleted()

