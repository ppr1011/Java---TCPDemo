package client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TCPClient extends JFrame {
	private int port;
	private String host;
	private static JTextField receive;
	private static JTextField send;
	private JButton commit;
	private Socket client;
	private Writer writer;
	private BufferedReader reader;
	private String clientID;
	
    public TCPClient(int port,String host){
    	this.setTitle("Client");
    	// get name representing the running Java virtual machine.    
    	String name = ManagementFactory.getRuntimeMXBean().getName();      
    	// get pid    
    	String clientID = name.split("@")[0];    
    	
    	this.host = host;
    	this.port = port;
    	this.setLayout(new GridLayout(3,1));
    	receive = new JTextField();
    	send = new JTextField();
    	commit = new JButton("提交");
		JLabel label1 = new JLabel("收到");
		label1.setFont(new Font("Dialog", 1,35));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1,2));
		panel1.add(label1);
		panel1.add(receive);
		this.add(panel1);
		JLabel label2 = new JLabel("发送");
		label2.setFont(new Font("Dialog", 1,35));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,2));
		panel2.add(label2);
		panel2.add(send);
		this.add(panel2);
    	this.add(new JPanel().add(commit));
    	this.setVisible(true);
    	this.setBounds(500, 500, 600, 400);
    	this.validate();    	
    	CreateConnection(port,host);
    	
    	commit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(client.isConnected()){
					String text = send.getText();
					try {
						writer.write("客户端进程"+clientID+":");
						writer.write(text);
						writer.write("eof\n");  
						writer.flush();  
						StringBuilder in  = new StringBuilder();
						String temp;
						int index;
						while ((temp=reader.readLine()) != null) {  
							
						    if ((index = temp.indexOf("eof")) != -1) {  
						       in.append(temp.substring(0, index));  
						       break;  
						    }  
						    in.append(temp);  
						 }
						if(in.toString()!=null){
							System.out.println("Client:receive from Server:"+in.toString());
		                    receive.setText(in.toString());
				         }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
    	});
    }
    
    void CreateConnection(int port,String host){
    	try {
			client = new Socket(host,port);			
			writer = new OutputStreamWriter(client.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
