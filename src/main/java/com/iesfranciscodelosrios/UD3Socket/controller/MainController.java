package com.iesfranciscodelosrios.UD3Socket.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.iesfranciscodelosrios.UD3Socket.App;

import Model.Mensaje;
import Model.Usuario;
import Utils.Dialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainController {

	@FXML
	private ImageView Exit;

	@FXML
	private Button btnInicioSesion;

	@FXML
	private ImageView btnver;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtUsuario;

	// CONEXION DE SOCKET Y ENTRADA/SALIDA
	private static Socket socket;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	// OBJETOS QUE SE USAN PARA LA/LAS VISTAS
	public static Usuario usuario = new Usuario();
	public static Mensaje mensaje = new Mensaje();
	private static HiloCliente messageReceiver;

	/**
	 * MÉTODO DE CONEXIÓN DEL SOCKET Y ESCUCHA DE LOS BOTONES DE LA VISTA.
	 */
	public void initialize() {
		try {
			MainController.socket = new Socket("localhost", 9999);
			MainController.out = new ObjectOutputStream(socket.getOutputStream());
			MainController.in = new ObjectInputStream(socket.getInputStream());
			MainController.mensaje = new Mensaje();
			MainController.usuario = mensaje.getUser();

			messageReceiver = new HiloCliente(mensaje, in);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					new Thread(messageReceiver).start();
				}
			});

			Exit.setOnMouseClicked(event -> {
				System.exit(0);
			});
			btnInicioSesion.setOnAction(event -> {
				InicioSesion();

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia objeto para verficar el ususario.
	 * 
	 * @return true si es correcto, false en caso contrario.
	 */
	private void InicioSesion() {
		if (txtUsuario.getText().length() >= 2 && txtPassword.getText().length() >= 2) {
			try {
				mensaje.setUsuario(txtUsuario.getText());
				mensaje.setPassword(txtPassword.getText());
				txtUsuario.setText("");
				txtPassword.setText("");
				sendObject(mensaje);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Dialog.showWarning("Rellena todos los campo", "Introduce el ususario y contraseña correctamente",
					"Gracias!!");
		}

	}

	public static void showView() {
		Platform.runLater(() -> {
			try {
				ControllerClientes.setValues(socket, in, out, mensaje, messageReceiver);
				App.setRoot("controller/new");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL SERVIDOR MEDIANTE EL SOCKET.
	 * 
	 * @throws IOException
	 */
	private static void sendObject(Mensaje mensaje) throws IOException {
		out.flush();
		out.writeObject(new Mensaje(mensaje));
	}

	public static void showWarning(String m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Dialog.showWarning("Servidor", m, null);
			}
		});
	}

}
