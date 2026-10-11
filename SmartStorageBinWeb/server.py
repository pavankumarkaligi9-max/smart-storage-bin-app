import http.server
import socketserver
import os
import threading
import sys

DIRECTORY = os.path.dirname(os.path.abspath(__file__))

class NoCacheHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def end_headers(self):
        self.send_header('Cache-Control', 'no-cache, no-store, must-revalidate, max-age=0')
        self.send_header('Pragma', 'no-cache')
        self.send_header('Expires', '0')
        self.send_header('Access-Control-Allow-Origin', '*')
        super().end_headers()

def serve_on(port):
    class ReuseAddressTCPServer(socketserver.TCPServer):
        allow_reuse_address = True

    try:
        with ReuseAddressTCPServer(("0.0.0.0", port), NoCacheHTTPRequestHandler) as httpd:
            print(f"Serving SmartStorageBin at http://0.0.0.0:{port} (Zero-Cache)...", flush=True)
            httpd.serve_forever()
    except Exception as e:
        print(f"Failed to bind port {port}: {e}", flush=True)

if __name__ == '__main__':
    t1 = threading.Thread(target=serve_on, args=(8085,), daemon=True)
    t2 = threading.Thread(target=serve_on, args=(8088,), daemon=True)
    t1.start()
    t2.start()
    print("Dual servers active on port 8085 and 8088.", flush=True)
    t1.join()
