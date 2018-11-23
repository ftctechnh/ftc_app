import websocket
import json
import time

import threading
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from matplotlib import style

style.use("fivethirtyeight")

fig = plt.figure()
ax1 = fig.add_subplot(1,1,1)

SOCKET_URL="ws://127.0.0.1:8080"
FILE = "pidReadout.txt"
'''
.put("ts",System.currentTimeMillis())
.put("error",e)
.put("de",de)
.put("dt",dt)
.put("p",P)
.put("i",runningI)
.put("d",D)
.put("output",output)
'''

def animate(i):
    file = open(FILE,'r').read()
    lines = file.split('\n')
    times = []
    errors = []
    outputs = []

    for line in lines:
        j = json.loads(line)
        ts = j.get("ts")
        error = j.get("error")
        de = j.get("de")
        dt = j.get("dt")
        p = j.get("p")
        i = j.get("i")
        d = j.get("d")
        output = j.get("output")

        times.append(ts)
        outputs.append(output)
        errors.append(error)
    ax1.clear()
    # print(errors)
    # print(outputs)
    # print(times)
    ax1.plot(times, errors,lineWidth=1,color='b',label='error')
    ax1.plot(times, outputs,lineWidth=1,color='r',label='output')

def connectWS():
    while(True):
        try:
            ws = websocket.WebSocket()
            ws.connect(SOCKET_URL)
            break
        except Exception as e:
            print(e)

    print(f"connected to websocket: {SOCKET_URL}")
    return ws

def recieveFromWS(ws):
    while(True):
        line = ws.recv()
        print(f"Recieved data: {line}")
        with open(FILE, 'a+') as file:
            file.write(line + "\n")
            print("Wrote to file.")

def clearFile(name):
    with open(name, 'w') as file:
        file.write("")
        print("Cleared file")

if __name__ == "__main__":
    if(input(f"Clear existing file: {FILE} ? y/n\n") == "y"):
        print("clearing file")
        clearFile(FILE)

    if(input(f"Connect websocket: {SOCKET_URL} ? y/n\n") == "y"):
        ws = connectWS()
        threading.Thread(target=recieveFromWS,args=[ws]).start()

    ani = animation.FuncAnimation(fig, animate, interval=100)
    plt.show()


