package hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorLogin {
  
  @FXML
  private TextField txtUsuario;

  @FXML
  private PasswordField txtPassword;
  
  @FXML
  private Button btnLogin;

  
  @FXML
  private void handleBtnLoginClick(ActionEvent event) {
      String usuario = txtUsuario.getText();
      String pass = txtPassword.getText();

      if (usuario.isEmpty() || pass.isEmpty()) {
        System.out.println("Campos vacíos.");
      } else {
        System.out.println(usuario);
        System.out.println(pass);
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

}
