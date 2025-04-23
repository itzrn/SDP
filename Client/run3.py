from prediction_using_opencv import AutoCheckoutSystem

# Instantiate the predictor class
client = AutoCheckoutSystem()

# Set configurations
rest_api_url = "https://autocheckouts.onrender.com/add_item"  # Or your actual deployed URL
live_stream_url = 'http://192.168.137.221:81/stream'
client.set_end_point_url(rest_api_url)
client.set_time_out_threshold(300)
# client.set_video_path("video.mp4")
client.set_video_path(live_stream_url)
client.set_user_id("rmishu2002")

# Run video streaming
client.video_streaming()