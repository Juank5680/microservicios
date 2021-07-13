package com.prueba.microservicios.app.config;

import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketsApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SocketsApplication.class, args);
		SocketsApplication es = new SocketsApplication();
		es.init();
	}
	
	public static final int PORT = 3400;
	
	private ServerSocket listener;
	private Socket serverSideSocket;
	
	public SocketsApplication() {
		System.out.println("Echo TCP server is running on port: "+ PORT);
	}
	
	private void init() throws Exception {
		listener = new ServerSocket(PORT);
		
		while(true) {
			serverSideSocket = listener.accept();
			EchoTCPServerProtocol.protocol(serverSideSocket);
		}
	}
	

}
