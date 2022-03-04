package com.iesfranciscodelosrios.UD3Socket.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Model.Mensaje;

public class Cliente {

	private static Scanner scanner = new Scanner(System.in);
	private static Socket socket;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	private static Mensaje mensaje = new Mensaje();

	public static void main(String[] args) {
		try {
			socket = new Socket("localhost", 9999);
			out = new ObjectOutputStream(socket.getOutputStream());
			in  = new ObjectInputStream(socket.getInputStream());
			
			ClientMessageReceiver messageReceiver = new ClientMessageReceiver(socket, mensaje, in, out);
			new Thread(messageReceiver).start();
			
			int input = 0;
			do {
				try {
					input = Integer.parseInt(scanner.nextLine());
	 				mensaje.setComando(input);
	 				mensaje.setDescripcion("h");
					mensaje.setUsuario("maria");
					mensaje.setPassword("1234");
					sendObject(mensaje);
				} catch (Exception e) {
					System.out.println("Error de comando");
				}
			} while (!"salir".equals(input));
			messageReceiver.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * @throws IOException
	 */
	private static void sendObject(Mensaje mensaje) throws IOException {
		out.writeObject(new Mensaje(mensaje));
	}
	
}


class ClientMessageReceiver implements Runnable {
	
	private Socket cliente;
	private boolean timeToStop = false;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Mensaje mensaje;

	public ClientMessageReceiver(Socket socket, Mensaje mensaje, ObjectInputStream in, ObjectOutputStream out) throws IOException {
		this.cliente = socket;
		this.mensaje = mensaje;
		this.out = out;
		this.in = in;
	}

	public void stop() {
		timeToStop = true;
	}

	public void run() {
		while (!timeToStop) {

			try {
				mensaje= (Mensaje) in.readObject();
				System.out.println(mensaje.getDescripcion());					

			} catch (IOException e) {
				if ("Connection reset".equals(e.getMessage())) {
					System.out.println("�La conexi�n al servidor ha sido interrumpida!");
					break;
				}
				if (!timeToStop) {
					e.printStackTrace();					
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	

}