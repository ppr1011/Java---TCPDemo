package server;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TCPServer extends JFrame{
	private ServerSocket server;
	private Socket client;
	private int port;
	private static JTextField receive;
	private static JTextField send;
	private static Writer writer;
	private static BufferedReader reader;
	private static int count = 1;
	public TCPServer(int port){
		this.setTitle("Server");
		receive = new JTextField();
		send = new JTextField();
		this.setVisible(true);
		this.setBounds(400, 400, 600, 400);
		this.setLayout(new GridLayout(2,2));
		JLabel label1 = new JLabel("收到");
		label1.setFont(new Font("Dialog", 1,35));
		this.add(label1);
		this.add(receive);
		JLabel label2 = new JLabel("发送");
		label2.setFont(new Font("Dialog", 1,35));
		this.add(label2);
		this.add(send);
		validate();
		this.port = port;
		try {
			
			server = new ServerSocket(port);
			while(true){   //不断监听是否有客户端进程接入
				client = server.accept();
				Thread thread = new Thread(new task(client));
				thread.setName("客户端线程"+count++);
				thread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	static class task implements Runnable{
		Socket client;
		task(Socket client){
			this.client = client;
		}
		@Override
		public void run() {			
			try {
				handleSocket();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void handleSocket() throws Exception{
			try { 
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				while(true){
					StringBuilder stringRead = new StringBuilder();
					String temp;
					int index;
					while((temp =  reader.readLine())!= null){
						System.out.println(temp);
						if((index=temp.indexOf("eof"))!=-1){
							stringRead.append(temp.substring(0, index));
							break;
						}
						stringRead.append(temp);
					}
					System.out.println(stringRead.toString());
					receive.setText(stringRead.toString());
					writer = new OutputStreamWriter(client.getOutputStream());
					if(send.getText()==""){
						writer.write(Thread.currentThread().getName());
					}
					else{
						writer.write("SendTo客户端"+Thread.currentThread().getName()+":");
						writer.write(send.getText());
						System.out.println("Server:send to Client:"+send.getText());
					}
					writer.write("eof\n");
					writer.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
}
