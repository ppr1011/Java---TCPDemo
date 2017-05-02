package start;

import client.TCPClient;
import server.TCPServer;

public class Main {
	static int port;
	public static void main(String[] args){
		port = 2500;
		new TCPServer(port);
	}
    
}
