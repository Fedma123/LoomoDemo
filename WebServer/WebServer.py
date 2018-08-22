from flask import Flask, request, make_response
import sys
import io
import os
import threading
import datetime

from cheroot.wsgi import PathInfoDispatcher
from cheroot.wsgi import Server

thismodule = sys.modules[__name__]
web_server = Flask(__name__)
head_process_callback = None
front_process_callback = None
listening = False
server = None

@web_server.route("/head", methods = ["POST"])
def head_image_process():
    global head_process_callback
    #print("Received HTTP request to /head from: %s" % str(request.remote_addr))
    start_time = datetime.datetime.now()
    result = head_process_callback(request.data)
    end_time = datetime.datetime.now()
    elapsed_time = end_time - start_time
    elapsed_milliseconds = elapsed_time.microseconds / 1000 + elapsed_time.seconds * 1000
    print("head elaboration time: %d ms" % elapsed_milliseconds)

    response = make_response(result)
    response.headers.add("Content-Type", "image/png")
    response.status_code = 200
    return response

@web_server.route("/front", methods = ["POST"])
def front_image_process():
    global front_process_callback
    print("Received HTTP request to /front from: %s" % str(request.remote_addr))
    result = front_process_callback(request.data)

    response = make_response(result)
    response.headers.add("Content-Type", "image/png")
    response.status_code = 200
    return response

def stop_listening():
    global listening
    if hasattr(thismodule, "server") and not server is None and listening:
        server.stop()
        listening = False

def start(ip, port, head_callback, front_callback):    
    global listening
    global server
    global head_process_callback
    global front_process_callback

    if hasattr(thismodule, "listening") and listening:
        return

    head_process_callback = head_callback
    front_process_callback = front_callback

    listening = True    

    current_ip = os.environ.get('SERVER_HOST', str(ip))
    try:
        current_port = int(os.environ.get('SERVER_PORT', str(port)))
    except ValueError:
        current_port = port
    
    d = PathInfoDispatcher({'/': web_server})
    server = Server((current_ip, current_port), d)

    server.safe_start()
