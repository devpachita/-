package hellofx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControladorLogin {
  
  @FXML
  private TextField txtUsuario;

  @FXML
  private PasswordField txtPassword;
  
  @FXML
  private Button btnLogin;
  
  @FXML
  private StackPane mensajeErrorPane;

  //Agregar logica de animacion cuando los campos estan vacios o la password es incorrecta. Usar un pane como sobre el txtUsuario para saber error. 
  @FXML
  private void handleBtnLoginClick(ActionEvent event) throws ClassNotFoundException {
      String usuario = txtUsuario.getText();
      String pass = txtPassword.getText();

      if (usuario.isEmpty() || pass.isEmpty()) {
        System.out.println("Campos vacíos.");
      } else if (autenticarUsuario(usuario, pass)){
        doorSky();
      }
  }
  
  private void doorSky(){
    try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeView.fxml"));
      Parent root = loader.load();

      Stage mainStage = new Stage();
      Scene scene = new Scene(root);

      mainStage.setScene(scene);
      mainStage.show();

      Stage currentStage = (Stage) btnLogin.getScene().getWindow();
      currentStage.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

  private boolean autenticarUsuario(String usuario, String contraseña) throws ClassNotFoundException {

        Connection conexión = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexión = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");

            String consultaSQL = "SELECT * FROM Usuarios WHERE username = ? AND password = ?";
            consulta = conexión.prepareStatement(consultaSQL);
            consulta.setString(1, usuario);
            consulta.setString(2, contraseña);

            resultado = consulta.executeQuery();

            return resultado.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (consulta != null) {
                    consulta.close();
                }
                if (conexión != null) {
                    conexión.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void mostrarVentanaRegistro(MouseEvent event) {
    try {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FormularioUsuario.fxml"));
        Parent root = fxmlLoader.load();
        
        Scene scene = new Scene(root);
       
        Stage stage = new Stage();
        stage.setTitle("Registro de Usuario"); 
        stage.setScene(scene); 

        stage.show();
    } catch (Exception e) {
        e.printStackTrace(); 
    }
}


    @FXML
    private void onMouseEntered(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), (Node) event.getSource());
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }

    @FXML
    private void onMouseExited(MouseEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), (Node) event.getSource());
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
    }
}
