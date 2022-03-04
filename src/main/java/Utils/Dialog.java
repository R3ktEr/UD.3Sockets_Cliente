package Utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Dialog {
	/**
	 * Class for modal windows.
	 * @author JF
	 *
	 */
	/**
	 * Model windows to notify user an error.
	 * @param title of the window.
	 * @param header of the window.
	 * @param description about error.
	 */
	public static void showError(String title, String header, String description) {
		showDialog(Alert.AlertType.ERROR, title, header, description);
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param description
	 */
	public static void showWarning(String title, String header, String description) {
		showDialog(Alert.AlertType.WARNING, title, header, description);
		
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param description
	 * @return
	 */
	public static Boolean showConfirm(String title, String header, String description) {
		Boolean result=showDialog(Alert.AlertType.CONFIRMATION, title, header, description);
		return result;
	}
	
	/**
	 * Code used to open all modal windows
	 * @param type of dialog
	 * @param title of the window.
	 * @param header of the window.
	 * @param description about error.
	 * @return true if press confirm button or false if press cancel (only for confirm dialog).
	 */
	public static Boolean showDialog(Alert.AlertType type, String title, String header, String description) {
		Boolean result=false;
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(description);
		Optional<ButtonType> action=alert.showAndWait();
		if (action.get() == ButtonType.OK) {
		    result=true;
		}
		return result;
	}
}
