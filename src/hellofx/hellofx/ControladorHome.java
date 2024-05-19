package hellofx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ControladorHome {

  @FXML
  private Button btnLogOut;

  @FXML
  private Button btnRefresh;

  @FXML
  private Button btnAdd;

  @FXML
  private Label labelNombreHouse;

  @FXML 
  private AnchorPane  AnchorLocation;
  
  @FXML
  private Label txtTipo;

  private Rectangle currentRectangle;
  
    @FXML
    private void initialize() throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            
            Connection conn = DriverManager.getConnection("jdbc:sqlite:chinook.db");

            String sql = "SELECT Title FROM albums WHERE AlbumId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, 2); 
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String title = rs.getString("title");
                        txtTipo.setText(title);
                    } else {
                        txtTipo.setText("No se encontró el álbum");
                    }
                }
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  @FXML
  private void handleBtnLogOutClick(ActionEvent event) {
      try {

      FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
      Parent root = loader.load();

      Stage mainStage = new Stage();
      Scene scene = new Scene(root);

      String recta = AnchorLocation.getChildren().toString();
      System.out.println(recta);
      
      mainStage.setScene(scene);
      mainStage.show();

      Stage currentStage = (Stage) btnLogOut.getScene().getWindow();
      currentStage.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void hableBtnAddRectangleClick(ActionEvent event) {
    int n = 5;
    int m = 3;
    double rectWidth = 50;
    double rectHeight = 30;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        Rectangle rectangle = new Rectangle(i * rectWidth, j * rectHeight, rectWidth, rectHeight);
        rectangle.setFill(Color.BLUE);

        rectangle.setOnMousePressed(e -> {
          currentRectangle = (Rectangle) e.getSource();
          e.consume();
        });

        rectangle.setOnMouseDragged(e -> {
          double offsetX = e.getX() - currentRectangle.getWidth() / 2;
          double offsetY = e.getY() - currentRectangle.getHeight() / 2;
          currentRectangle.setX(offsetX);
          currentRectangle.setY(offsetY);
          e.consume();
        });

        AnchorLocation.getChildren().add(rectangle);
      }
    }
  }}

