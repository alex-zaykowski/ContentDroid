#!/usr/bin/env python
import pika, sys, os, subprocess, json, psycopg2, websockets
from psycopg2.extras import RealDictCursor
#docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management
def main():
    queue_name = 'videos_task_queue'

    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    channel.queue_declare(queue=queue_name, durable=True)

    def callback(ch, method, properties, body):
        conn = psycopg2.connect(database="videos",
                        host="127.0.0.1",
                        user="postgres",
                        password="white1999",
                        port="5432")
            
        videoId = int(body.decode())
        cursor = conn.cursor(cursor_factory=RealDictCursor)
        cursor.execute(f"SELECT * FROM videos WHERE id = {videoId}")

        videoData = cursor.fetchone()

        session_id = '9c01069da754e9032b13902648d16086'

        f = open("./dialogue.txt", "w")
        f.write(videoData["dialogue"])
        f.close()

        audio_name= 'audio.mp3'

        subprocess.call(['chmod', '+x', 'generate_audio.sh'])
        subprocess.call(['chmod', '+x', 'generate_video.sh'])

        subprocess.check_call(['./generate_audio.sh', './dialogue.txt', session_id, videoData["voice"], audio_name])
        subprocess.check_call(['./generate_video.sh', audio_name, 'English',videoData["path"]])

        updateRow = """ UPDATE videos
                SET completed = %s
                WHERE id = %s"""
        
        cursor.execute(updateRow, (True, videoId))

        conn.commit()

        print(f" [x] Generated Video")
        
        ch.basic_ack(delivery_tag = method.delivery_tag)

    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue=queue_name, on_message_callback=callback)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
