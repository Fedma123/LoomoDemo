import threading
import socket

class UdpServer(threading.Thread):
    """description of class"""

    def __init__(self, ip, port, callback):

        self.ip = ip
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)        
        self.image_process_callback = callback

        threading.Thread.__init__(self)

    def stop_listening(self):
        self.listen = False

    def run(self):
        if hasattr(self, "listen") and self.listen == True:
            return

        self.listen = True
        self.sock.bind((self.ip, self.port))

        while self.listen:
            max_receive_size = 2 * 1280 * 720 * 3
            data, addr = self.sock.recvfrom(max_receive_size)
            result = self.image_process_callback(data)
            print("Received UDP request from: %s" % addr[0])

            self.sock.sendto(result, addr)