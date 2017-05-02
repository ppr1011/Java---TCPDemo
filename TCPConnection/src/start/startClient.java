package start;

import client.TCPClient;

public class startClient {
	static int port;
	static String host;
	public static void main(String[] args){
		port = 2500;
		host = "localhost";
		
		new TCPClient(port,host);
	}
    
}
