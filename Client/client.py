import socket
import threading
import select
import time

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
time.sleep(50) 
result = soc.connect(("server", 59001))
soc.setblocking(0)
quit = False
name = "noname"

def client_in():
	global quit
	global name
	while True:
		clients_input = input("[{}]\n".format(name))
		if "noname" in name:
			name = clients_input
		clients_input += '\n'
		if "quit" in clients_input:
			quit = True;
			break;
		soc.send(clients_input.encode("utf8")) # we must encode the string to bytes
		time.sleep(1)

def client_out():
	global quit
	while True:
		if quit:
			break;
		ready = select.select([soc], [], [], 1)
		if ready[0]:
			result_bytes = soc.recv(4096) # the number means how the response can be in bytes  
			result_string = result_bytes.decode("utf8") # the return will be in bytes, so decode
			print(result_string)

if __name__ == "__main__":
	print("Usage: appoint <date> <task>/ delete <id>/ show / quit\n")
	input_c = threading.Thread(target=client_in)
	output_c = threading.Thread(target=client_out)
	input_c.start()
	output_c.start()
	output_c.join()
	input_c.join()



	
