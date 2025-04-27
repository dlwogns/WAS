import paho.mqtt.client as mqtt


# 브로커 설정
BROKER = "127.0.0.1"    # 예: "127.0.0.1" or "mqtt.example.com"
PORT = 1883
TOPIC = "cmd/device123"
CLIENT_ID = "python-subscriber-001"
USERNAME = "your_username"
PASSWORD = "your_password"

# 메시지 도착 시 콜백
def on_message(client, userdata, msg):
    print(f"Message received on topic {msg.topic}: {msg.payload.decode()}")

# 연결 완료 시 콜백
def on_connect(client, userdata, flags, rc, properties):
    if rc == 0:
        print("Connected to MQTT Broker!")
        client.subscribe(TOPIC)
        print(f"Subscribed to topic: {TOPIC}")
    else:
        print(f"Failed to connect. Error code: {rc}")

# 클라이언트 설정
#client = mqtt.Client(CLIENT_ID)
client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
#client.username_pw_set(USERNAME, PASSWORD)  # 인증 필요한 경우만!
client.on_connect = on_connect
client.on_message = on_message

# 브로커 연결
client.connect(BROKER, PORT,60)

# 메시지 루프 시작
client.loop_forever()
