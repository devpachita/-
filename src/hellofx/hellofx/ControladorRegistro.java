package hellofx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class ControladorRegistro {
   
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPass;

    @FXML
    private PasswordField txtPassConf;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtNacionalidad;

    @FXML
    private TextField txtDireccion;
    
    @FXML
    private StackPane mensajeErrorPane;
    
    @FXML
    private GridPane gridFondo;

    @FXML
    private void handleBtnCrearClick(ActionEvent event) {
    
        String nombre = txtNombre.getText();
        String username = txtUsername.getText();
        String password = txtPass.getText();
        String passwordConf = txtPassConf.getText();
        String telefono = txtTelefono.getText();
        String nacionalidad = txtNacionalidad.getText();
        String direccion = txtDireccion.getText();
    
        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || passwordConf.isEmpty()) {
            mostrarError("Hay campos obligatorios sin valores.");
            return;
        }
    
        if (!password.equals(passwordConf)) {
            mostrarError("Las contraseñas son diferentes.");
            return;
        }
    
        crearUsuario(nombre, username, password, telefono, nacionalidad, direccion);
    }
    

    private void mostrarError(String mensaje) {
        Label mensajeErrorLabel = new Label(mensaje);
        mensajeErrorLabel.setStyle("-fx-text-fill: red;");

        mensajeErrorPane.getChildren().add(mensajeErrorLabel);
        StackPane.setAlignment(mensajeErrorLabel, javafx.geometry.Pos.CENTER);
  
        applyShakeAnimation(txtNombre);
        applyShakeAnimation(txtUsername);
        applyShakeAnimation(txtPass);
        applyShakeAnimation(txtPassConf);

        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(3), mensajeErrorLabel);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {

            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(3), mensajeErrorLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.setOnFinished(finishEvent -> {
                mensajeErrorPane.getChildren().remove(mensajeErrorLabel);
            });
            fadeOutTransition.play();
        });

        fadeInTransition.play();
        pause.play();
    }

    private void applyShakeAnimation(TextField textField) {
        TranslateTransition shakeAnimation = new TranslateTransition(Duration.millis(50), textField);
        shakeAnimation.setCycleCount(4);
        shakeAnimation.setAutoReverse(true);
        shakeAnimation.setByX(10);
        shakeAnimation.setOnFinished(event -> textField.setTranslateX(0));

        shakeAnimation.play();
    }

    private void crearUsuario(String nombre, String username, String password, String telefono, String nacionalidad, String direccion){
    
        Connection cx = null;

        try {
            cx = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");

            String crear = "INSERT INTO Usuarios (nombre, username, password, telefono, nac, direccion) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = cx.prepareStatement(crear)) {

                statement.setString(1, nombre);
                statement.setString(2, username);
                statement.setString(3, password);
                statement.setString(4, telefono);
                statement.setString(5, nacionalidad);
                statement.setString(6, direccion);

                statement.executeUpdate();
                hecho();

                System.out.println("Se ha realizado la inserción.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
  }
        private void hecho() {
            try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmacionUsuario.fxml"));
            Parent horario = loader.load();

            StackPane transparencyPane = new StackPane(horario);
            transparencyPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 30, 0.5, 0, 0);");

            AnchorPane.setTopAnchor(transparencyPane, 0.0);
            AnchorPane.setBottomAnchor(transparencyPane, 0.0);
            AnchorPane.setLeftAnchor(transparencyPane, 0.0);
            AnchorPane.setRightAnchor(transparencyPane, 0.0);

            transparencyPane.setOpacity(0.0);

            gridFondo.getChildren().add(transparencyPane);

            FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), transparencyPane);
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);

            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.5), transparencyPane);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.setOnFinished(eventFinish -> {
                gridFondo.getChildren().remove(transparencyPane);
            });

            fadeInTransition.setOnFinished(eventFinish -> {

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(pauseEvent -> fadeOutTransition.play());
                pause.play();
            });

            fadeInTransition.play();

            transparencyPane.setOnMouseClicked(eventClick -> {
                fadeInTransition.stop();
                fadeOutTransition.play();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
