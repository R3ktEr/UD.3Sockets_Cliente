package com.iesfranciscodelosrios.UD3Socket.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Model.Cuenta;
import Model.Mensaje;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

public class HiloCliente implements Runnable {
	private boolean timeToStop = false;
	private ObjectInputStream in;
	private Mensaje mensaje;

	public HiloCliente(Mensaje mensaje, ObjectInputStream in) throws IOException {
		this.mensaje = mensaje;
		this.in = in;
	}

	public void stop() {
		timeToStop = true;
	}

	public void run() {
		while (!timeToStop) {
			try {

				mensaje = (Mensaje) in.readObject();
				/**
				 * El servidor solo devuelve un comando 100 como respuesta cuando se ha realizado una operacion con un usuario o cuenta
				 * Cuando es una comprobacion para el login de si el usuario existe en la base de datos no devuelve comando alguno
				 */
				if (mensaje.getComando()==100) {
					cliente(!mensaje.getUser().getAdministrador());					
				}else {
					login(mensaje.getDescripcion());					
				}


			} catch (IOException e) {
				if ("Connection reset".equals(e.getMessage())) {
					System.out.println("La conexion al servidor ha sido interrumpida");
					break;
				}
				if (!timeToStop) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
			} catch (Exception e) {
				System.out.println("Error hilo cliente");
			}
		}

	}

	//Setea los datos que recibe del servidore en el controllerclientes en funcion de si es un cliente o un operario
	private void cliente(boolean b) {
		Platform.runLater(() -> {
			try {
				// MENU DE ACCIONES PARA LOS CLIENTES.
				if (!mensaje.getUser().getAdministrador()) {
					switch (mensaje.getComando()) {
					case 100: //Setea el controller para un cliente con datos actualizados
						if (mensaje.getCuenta() != null) {
							ControllerClientes.cuenta = mensaje.getCuenta();
							ControllerClientes.mensaje = mensaje;
							ControllerClientes.cofigureOnServerResponse();
						}
						break;

					default:
						break;
					}
				} else { 
					
					if (mensaje.getDescripcion().equals("Error al crear a un usuario")
							|| mensaje.getDescripcion().equals("Error al borrar cuenta a un usuario")
							|| mensaje.getDescripcion().equals("Cliente con cuenta asignada")) {
						ControllerClientes.showError(mensaje.getDescripcion());
					} else {
						
						//Setea el controller para un operario con datos actualizados
							if (mensaje != null) {
								if (mensaje.getDescripcion().equals("Nueva cuenta asignada al usuario")) {
									ControllerClientes.showInfo(mensaje.getDescripcion());
								}
								ControllerClientes.mensaje = mensaje;
								ControllerClientes.cofigureOnServerResponse();
							}
						
						
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	// CONEXION CON EL LOGIN
	public void login(String mesagge) {

		switch (mesagge) {
		case "Usuario no registrado":
		case "Usuario ya conectado":
		case "Error al logear.":
			String m = mensaje.getDescripcion();
			LoginController.showWarning(m);
			System.out.println(m);
			break;
		// EN CASO DE INICIAR SESION BIEN MUESTRA LA PANTALLA CON LOS DATOS.
		case "logeado":
			LoginController.mensaje = mensaje;
			LoginController.usuario = mensaje.getUser();
			LoginController.showView();
			break;

		default:
			break;
		}
	}

}
