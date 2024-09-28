module com.grupo2.tpteo1grupo2 {
    //requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.cup;


    opens com.grupo2.tpteo1grupo2 to javafx.fxml;
    exports com.grupo2.tpteo1grupo2;
}