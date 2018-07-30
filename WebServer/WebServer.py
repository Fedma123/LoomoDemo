from flask import Flask, request, make_response
import sys
import io
import os
import threading
from cheroot.wsgi import PathInfoDispatcher
from cheroot.wsgi import Server

thismodule = sys.modules[__name__]
web_server = Flask(__name__)
image_process_callback = None
listening = False
server = None

@web_server.route("/", methods = ["POST"])
def image_process():
    result = image_process_callback(request.data)

    response = make_response(result.tobytes())
    response.headers.add("Content-Type", "image/png")
    response.status_code = 200
    return response

def stop_listening():
    global listening
    if hasattr(thismodule, "server") and not server is None and listening:
        server.stop()
        listening = False

def start(ip, port, callback):    
    global listening
    global server
    global image_process_callback

    if hasattr(thismodule, "listening") and listening:
        return

    image_process_callback = callback
    
    listening = True

    current_ip = os.environ.get('SERVER_HOST', str(ip))
    try:
        current_port = int(os.environ.get('SERVER_PORT', str(port)))
    except ValueError:
        current_port = port
    
    d = PathInfoDispatcher({'/': web_server})
    server = Server((current_ip, current_port), d)

    server.safe_start()
