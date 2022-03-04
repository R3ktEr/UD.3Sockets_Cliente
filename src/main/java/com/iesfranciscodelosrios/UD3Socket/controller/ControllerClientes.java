package com.iesfranciscodelosrios.UD3Socket.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.iesfranciscodelosrios.UD3Socket.App;

import Model.Cuenta;
import Model.Mensaje;
import Model.Usuario;
import Utils.Dialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ControllerClientes {
	@FXML
	public AnchorPane CentroClientes;

	@FXML
	public AnchorPane CentroOperario;

	// -----------------------------Operarios-----------------------------------------------

	@FXML
	public AnchorPane LateralCliente;

	@FXML
	public AnchorPane LateralOperario;

	@FXML
	public Button Opc21;

	@FXML
	public Button Opc22;

	@FXML
	public Button Opc23;

	@FXML
	public Button Opc24;

	// Tabla de cuentas
	@FXML
	public TextField opCuentaBuscar;

	@FXML
	public TableView<Cuenta> opCuenta;

	@FXML
	public TableColumn<Cuenta, Float> opCuentaDinero;

	@FXML
	public TableColumn<Cuenta, Integer> opCuentaid;

	// Tabla de usuarios
	@FXML
	public TextField opUserBuscar;

	@FXML
	public TableView<Usuario> opUser;

	@FXML
	public TableColumn<Usuario, String> opUserApellido;

	@FXML
	public TableColumn<Usuario, String> opUserUsername;

	@FXML
	public TableColumn<Usuario, Integer> opUserid;

	@FXML
	public TableColumn<Usuario, String> opUsernombre;

	@FXML
	public TableColumn<Usuario, Boolean> opUserop;

	// Datos para crear nuevo usuario
	@FXML
	public TextField opNewApellido;

	@FXML
	public TextField opNewNombre;

	@FXML
	public TextField opNewPassword;

	@FXML
	public TextField opNewUsername;

	@FXML
	public CheckBox opNewUserop;

	@FXML
	public TextArea optxttransaccion;
	// -------------------------------------------------------------------------------------

	// BOTON(ROJO) DE EXIT ARRIBA-DERECHA
	@FXML
	public ImageView Exit;

	// BOTONERA IZQUIERDA EN ORDE DE ARRIBA A BAJO
	@FXML
	public Button Opc1;

	@FXML
	public Button Opc2;

	@FXML
	public Button Opc3;

	@FXML // misma funcion que exit
	public Button Opc4;
	// ----------------------------------------------------

	// DINERO QUE SE VA A INGRESAR
	@FXML
	public TextField TxtDineroIngresar;
	// DINERO QUE SE VA A RETIRAR
	@FXML
	public TextField TxtDineroRetirar;

	// BOTONERA SUPERIOR PARA SETEAR DATOS DEL USUARIO
	@FXML
	public Button btnAdministrador;

	@FXML
	public Button btnNombre;

	@FXML
	public Button btnApellido;

	// MENSAJE MOSTRADO DEBAJO DE LAS TABLAS.
	@FXML
	public Button btnDescripcion;

	@FXML
	public TableView<Cuenta> cuentas;

	@FXML
	public TableColumn<Cuenta, Integer> id;

	@FXML
	public TableColumn<Cuenta, Float> dinero;

	@FXML
	public TextArea txttransaccion;

	@FXML
	private static AnchorPane stagecentro;

	@FXML // NI CASO, COSAS DEL CSS
	private HBox datos;

	@FXML // NI CASO, COSAS DEL CSS
	private Button transaccion;

	// CONEXION DE SOCKET Y ENTRADA/SALIDA
	private static Socket socket;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	// OBJETOS QUE SE USAN PARA LA/LAS VISTAS
	public static Usuario usuario = new Usuario();
	public static Cuenta cuenta = new Cuenta(0, 0.0F, "Transacciones");
	public static Mensaje mensaje = new Mensaje();
	public static HiloCliente messageReceiver;

	public static Stage controllerClientStage;
	public ObservableList<Cuenta> CuentaObs;
	public ObservableList<Usuario> UsuarioObs;

	/**
	 * MÉTODO DE CONEXIÓN DEL SOCKET Y ESCUCHA DE LOS BOTONES DE LA VISTA.
	 */
	public void initialize() {
		try {
			Platform.runLater(() -> {
				controllerClientStage = (Stage) Opc4.getScene().getWindow();
			});
			mensaje.setDescripcion("");
			mensaje.setUser(new Usuario());
			mensaje.setCuenta(new Cuenta());
			if (usuario != null) {
				btnNombre.setText("Nombre: " + usuario.getName());
				btnApellido.setText("Apellido: " + usuario.getApellido());
				txttransaccion.setText("Selecciona cuenta");
				if (usuario.getAdministrador()) {
					configureOperario();
					btnAdministrador.setText("Administrador");
					LateralOperario.setVisible(true);
					CentroOperario.setVisible(true);
					Opc21.setOnMouseClicked(event -> {
						OpcionesOperario(1);
					});
					Opc22.setOnMouseClicked(event -> {
						OpcionesOperario(2);
					});
					Opc23.setOnMouseClicked(event -> {
						OpcionesOperario(3);
						configureOperario();
					});
					Opc24.setOnMouseClicked(event -> {
						OpcionesOperario(4);
						configureOperario();

					});
				} else {
					configureCliente();
					btnAdministrador.setText("Cliente");
					LateralCliente.setVisible(true);
					CentroClientes.setVisible(true);
					Opc1.setOnMouseClicked(event -> {
						OpcionesCliente(1);
					});
					Opc2.setOnMouseClicked(event -> {
						OpcionesCliente(2);
					});
					Opc3.setOnMouseClicked(event -> {
						OpcionesCliente(3);
					});
				}
				Opc4.setOnMouseClicked(event -> {
					Opc4();
				});
				Exit.setOnMouseClicked(event -> {
					System.exit(0);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setValues(Socket socket, ObjectInputStream in, ObjectOutputStream out, Mensaje mensaje,
			HiloCliente messageReceiver) {
		ControllerClientes.socket = socket;
		ControllerClientes.out = out;
		ControllerClientes.in = in;
		ControllerClientes.mensaje = mensaje;
		ControllerClientes.usuario = mensaje.getUser();
		ControllerClientes.messageReceiver = messageReceiver;

	}

	public static void cofigureOnServerResponse() {
		if (controllerClientStage != null) {

			if (usuario != null && !usuario.getAdministrador()) {
				TableView<Cuenta> tablecuentas = (TableView<Cuenta>) controllerClientStage.getScene()
						.lookup("#cuentas");
				tablecuentas.getItems().clear();
				tablecuentas.getItems().add(cuenta);

				TextArea transaccion = (TextArea) controllerClientStage.getScene().lookup("#txttransaccion");
				transaccion.setText(cuenta.getTransactions());

			} else {
				if (mensaje.getCuenta() != null) {
					TableView<Cuenta> OPtablecuentas = (TableView<Cuenta>) controllerClientStage.getScene()
							.lookup("#opCuenta");
					OPtablecuentas.getItems().clear();
					OPtablecuentas.getItems().add(mensaje.getCuenta());
					
					TextArea optransaccion = (TextArea) controllerClientStage.getScene().lookup("#optxttransaccion");
					optransaccion.setText(mensaje.getCuenta().getTransactions().toString());					
				}
				if (mensaje.getUser()!=null) {
					TableView<Usuario> OPtableusuarios = (TableView<Usuario>) controllerClientStage.getScene()
							.lookup("#opUser");
					OPtableusuarios.getItems().clear();
					OPtableusuarios.getItems().add(mensaje.getUser());					
				}
			}

		}
	}

	public void configureCliente() {
		if (mensaje.getCuenta() != null) {
			try {
				this.CuentaObs = FXCollections.observableArrayList(new ArrayList<Cuenta>());
				this.CuentaObs.add(cuenta);
				id.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getId());
				});
				dinero.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getMoney());
				});

				cuentas.setItems(CuentaObs);
				txttransaccion.setText(cuenta.getTransactions());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void configureOperario() {
		if (mensaje.getCuenta() != null) {
			try {
				this.CuentaObs = FXCollections.observableArrayList(new ArrayList<Cuenta>());
				this.CuentaObs.add(mensaje.getCuenta());
				opCuentaid.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getId());
				});
				opCuentaDinero.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getMoney());
				});

				opCuenta.setItems(CuentaObs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (mensaje.getUser() != null) {
			try {
				this.UsuarioObs = FXCollections.observableArrayList(new ArrayList<Usuario>());
				this.UsuarioObs.add(mensaje.getUser());
				opUserid.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getId());
				});
				opUsernombre.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getName());
				});
				opUserApellido.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getApellido());
				});
				opUserUsername.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getUsername());
				});
				opUserop.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getAdministrador());
				});
				opUserid.setCellValueFactory(cellData -> {
					return new SimpleObjectProperty<>(cellData.getValue().getId());
				});

				opUser.setItems(UsuarioObs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void OpcionesCliente(int opc) {
		try {
			switch (opc) {

			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 1 PARA RECIBIR DATOS DE LA CUENTA/S.
			case 1:
				ControllerClientes.mensaje.setComando(1);
				sendObject(mensaje);
				break;

			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 3 ENVIANDO EL DINERO A INGRESAR
			// INDICADO POR TxtDineroRetirar E INTRODUCIDO EN mensaje.dinerotransaccion.
			case 2:
				if (TxtDineroIngresar.getText().length() >= 1) {
					int dinero = 0;
					try {
						dinero = Integer.parseInt(TxtDineroIngresar.getText());
						if (dinero >= 1) {
							ControllerClientes.mensaje.setComando(3);
							ControllerClientes.mensaje.setDineroTransaccion(dinero);
							TxtDineroIngresar.setText("");
							sendObject(mensaje);
						}
					} catch (Exception e) {
						Platform.runLater(() -> {
							Dialog.showWarning("Introduce dinero a ingresar", "Introduce Numeros", null);
						});
					}

				} else {
					Platform.runLater(() -> {
						Dialog.showWarning("Introduce dinero a ingresar", "Campo de ingresar dinero vacio", null);
					});
				}
				break;

			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 2 ENVIANDO EL DINERO A RETIRAR
			// INDICADO POR TxtDineroIngresar E INTRODUCIDO EN mensaje.dinerotransaccion.
			case 3:
				if (TxtDineroRetirar.getText().length() >= 1) {
					int dinero = 0;
					try {
						dinero = Integer.parseInt(TxtDineroRetirar.getText());
						if (dinero >= 1) {
							ControllerClientes.mensaje.setComando(2);
							ControllerClientes.mensaje.setDineroTransaccion(dinero);
							TxtDineroRetirar.setText("");
							sendObject(mensaje);

						}
					} catch (Exception e) {
						Platform.runLater(() -> {
							Dialog.showWarning("Introduce dinero a retirar", "Introduce Numeros", null);
						});
					}
				} else {
					Platform.runLater(() -> {
						Dialog.showWarning("Introduce dinero a retirar", "Campo de retirar dinero vacio", null);
					});
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void OpcionesOperario(int opc) {
		try {
			switch (opc) {
			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 1 PARA RECIBIR DATOS DE LA CUENTA/S.
			case 1:
			case 2:
				if (opNewNombre.getText().length() >= 4 && opNewApellido.getText().length() >= 3
						&& opNewPassword.getText().length() >= 4 && opNewUsername.getText().length() >= 4) {
					try {
						String name = opNewNombre.getText();
						String surname = opNewApellido.getText();	
						Boolean op = opNewUserop.isSelected();
						String username = opNewUsername.getText();
						String password = opNewPassword.getText();
						opNewNombre.setText("");
						opNewApellido.setText("");
						opNewUsername.setText("");
						opNewPassword.setText("");
						ControllerClientes.mensaje.setComando(opc);
						ControllerClientes.mensaje.setCuenta(null);
						ControllerClientes.mensaje.setUser(new Usuario(-1, name, surname, op, username, password));
						opCuentaBuscar.setText("");
						sendObject(mensaje);

					} catch (Exception e) {
						e.printStackTrace();
						Platform.runLater(() -> {
							Dialog.showWarning("Error a crear usuario", "", null);
						});
					}

				} else {
					Platform.runLater(() -> {
						Dialog.showWarning("Rellene todos los campos",
								"Todos los campos deben estar completos" , "longitud minima de 4 caracteres (Todo)");
					});
				}

				break;

			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 3 EN BUSCA DE CUENTA
			case 3:
				if (opCuentaBuscar.getText().length() >= 1) {
					int id = 0;
					try {
						id = Integer.parseInt(opCuentaBuscar.getText());
						if (id >= 1) {
							ControllerClientes.mensaje.setComando(3);
							ControllerClientes.mensaje.setCuenta(new Cuenta(id, 0F, ""));
							opCuentaBuscar.setText("");
							sendObject(mensaje);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Platform.runLater(() -> {
							Dialog.showWarning("Introduce id_cuenta a buscar", "Introduce Numeros", null);
						});
					}

				} else {
					Platform.runLater(() -> {
						Dialog.showWarning("Introduce id_cuenta a buscar", "Campo de id vacio", null);
					});
				}
				break;

			// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 4 EN BUSCA DE CLIENTES
			case 4:
				if (opUserBuscar.getText().length() >= 1) {
					int id = 0;
					try {
						id = Integer.parseInt(opUserBuscar.getText());
						if (id >= 1) {
							ControllerClientes.mensaje.setComando(4);
							ControllerClientes.mensaje.setUser(new Usuario(id, "", "", false, "", ""));
							opUserBuscar.setText("");
							sendObject(mensaje);

						}
					} catch (Exception e) {
						e.printStackTrace();
						Platform.runLater(() -> {
							Dialog.showWarning("Introduce id_cliente a buscar", "Introduce Numeros", null);
						});
					}
				} else {
					Platform.runLater(() -> {
						Dialog.showWarning("Introduce id_cliente a buscar", "Campo de id vacio", null);
					});
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ENVIAR MENSAJE AL SERVIDOR CON COMANDO 4 CERRANDO EL SOCKET VOLVIENDO AL
	// LOGIN.
	private void Opc4() {
		try {
			if (usuario != null) {
				if (usuario.getAdministrador()) {
					LateralOperario.setVisible(false);
					CentroOperario.setVisible(false);
					messageReceiver.stop();
					ControllerClientes.mensaje.setComando(6);
					sendObject(mensaje);
					App.setRoot("controller/Login");

				} else {
					LateralCliente.setVisible(false);
					CentroClientes.setVisible(false);
					messageReceiver.stop();
					ControllerClientes.mensaje.setComando(4);
					sendObject(mensaje);
					App.setRoot("controller/Login");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static void showError(String m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Dialog.showError("Servidor", m, null);
			}
		});
	}
	
	public static void showInfo(String m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Dialog.showWarning("Servidor", m, null);
			}
		});
	}
	
}
