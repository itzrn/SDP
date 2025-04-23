import cv2

url = 'video1.mp4'  # replace with your actual IP
cap = cv2.VideoCapture(url, cv2.CAP_FFMPEG)

WINDOW_SIZE = 600
while True:
    ret, frame = cap.read()
    if not ret:
        print("Failed to grab frame")
        break
    bright_frame = cv2.convertScaleAbs(frame, alpha=1, beta=10)
    square_frame = cv2.resize(bright_frame, (WINDOW_SIZE, WINDOW_SIZE))
    cv2.imshow('ESP32-CAM Stream', square_frame)
    if cv2.waitKey(1) == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
