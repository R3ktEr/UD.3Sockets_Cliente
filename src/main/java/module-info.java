module com.iesfranciscodelosrios.UD3Socket {
	requires javafx.graphics;
	requires javafx.base;
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.iesfranciscodelosrios.UD3Socket.controller to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens com.iesfranciscodelosrios.UD3Socket to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    opens Model to javafx.fxml, javafx.base, javafx.graphics, javafx.controls;
    exports com.iesfranciscodelosrios.UD3Socket.controller;
    exports com.iesfranciscodelosrios.UD3Socket;
    exports Model;

}
