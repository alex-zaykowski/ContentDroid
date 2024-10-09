import asyncio
import websockets

def main():
    # create handler for each connection
    async def handler(websocket, path):
        data = await websocket.recv()
        reply = f"Data recieved as:  {data}!"
        print(data)
        await websocket.send(reply)

    start_server = websockets.serve(handler, "localhost", 8000)

    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
