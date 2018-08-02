import cv2
import numpy as np

import WebServer
import UdpServer

def process_image(img):
    raw_image_bytes = np.frombuffer(img, dtype=np.uint8)
    image = cv2.imdecode(raw_image_bytes, cv2.IMREAD_COLOR)    
    canny_image = cv2.Canny(image, 30, 120)    
    retval, encoded_png_canny = cv2.imencode(".png", canny_image)
    return encoded_png_canny

if __name__ == '__main__':
    ip_address = '0.0.0.0'

    web_server_port = 49000
    udp_server_port = 49001    

    udp_server = UdpServer.UdpServer(ip_address, udp_server_port, process_image)
    
    print("Server IP: %s" % ip_address)
    udp_server.start()
    print("UDP port: %d" % udp_server_port)
    print("HTTP port: %d" % web_server_port)
    WebServer.start(ip_address, web_server_port, process_image)
    