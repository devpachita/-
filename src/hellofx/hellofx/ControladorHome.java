package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

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
  private StackPane StackFondo;
  
  @FXML
  private GridPane GridFondo;

  @FXML
  private Label txtNombre;

  @FXML
  private Label txtTipo;
  
  @FXML
  private Label txtEstrato;

  @FXML
  private Label txtDireccion;
  
  @FXML
  private Label txtResidentes;

  private Rectangle currentRectangle;
    
  public void initialize() throws ClassNotFoundException {
    getThemeFromDatabase();
    getListProducts();
  }

  public String getThemeFromDatabase() throws ClassNotFoundException {
        String color = "";

        Connection cx = null;
        PreparedStatement consulta = null;
        PreparedStatement consulta2 = null;
        ResultSet resultado = null;
        ResultSet resultado2 = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cx = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");

            //Agregar logica para la seleccion de estudiante y que casa mostrar dependiendo del usuario
            //String estudiante = utilisatrice
            String consultaSQL = "SELECT tipo, estrato, direccion, residentes FROM Casas WHERE cd_casa = ?";
            String productos = "SELECT tipo from Productos where id_casa = ?";

            consulta = cx.prepareStatement(consultaSQL);
            consulta.setString(1, "2");
            consulta2 = cx.prepareStatement(productos);

            resultado = consulta.executeQuery();
            resultado2 = consulta2.executeQuery();

            if (resultado.next() && resultado2.next()) {

                String tipo = resultado.getString("tipo");
                String estrato = resultado.getString("estrato");
                String direccion = resultado.getString("direccion");
                String residentes = resultado.getString("residentes");
                txtTipo.setText(tipo);
                txtEstrato.setText(estrato);
                txtDireccion.setText(direccion);
                txtResidentes.setText(residentes);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (consulta != null) {
                    consulta.close();
                }
                if (cx != null) {
                    cx.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return color;
    }

    public class Producto{

        private String tipo;
        private Integer consumo;

        public Producto(String tipo, Integer consumo){
            this.tipo=tipo;
            this.consumo=consumo;
        }

        public String getTipo(){
          return tipo;
        }

        public int getConsumo(){
          return consumo;
        }
    }

    private List<Producto> getListProducts() throws ClassNotFoundException{
        List<Producto> productos = new ArrayList<>();

        Connection cx = null;
        PreparedStatement prepareStatement = null;
        ResultSet resulSet = null;

        try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          cx = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");
          String id_casa = "1";
  
          String consulta = "SELECT * FROM Productos WHERE id_casa = ?";
          prepareStatement = cx.prepareStatement(consulta);
          prepareStatement.setString(1, id_casa);
  
          resulSet = prepareStatement.executeQuery();
  
          while (resulSet.next()) {
              String tipo = resulSet.getString("tipo");
              Integer consumo = resulSet.getInt("consumo");

              Producto producto = new Producto(tipo, consumo);
              productos.add(producto);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          try {
              if (resulSet != null) resulSet.close();
              if (prepareStatement != null) prepareStatement.close();
              if (cx != null) cx.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }

        return productos;
    }

    @FXML
    public void mostrarEtiqueta(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EtiquetaDeConsumoView.fxml"));
            Parent etiqueta = loader.load();

            StackPane transparencyPane = new StackPane(etiqueta);
            transparencyPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 30, 0.5, 0, 0);");

            AnchorPane.setTopAnchor(transparencyPane, 0.0);
            AnchorPane.setBottomAnchor(transparencyPane, 0.0);
            AnchorPane.setLeftAnchor(transparencyPane, 0.0);
            AnchorPane.setRightAnchor(transparencyPane, 0.0);

            transparencyPane.setOpacity(0.0);

            StackFondo.getChildren().add(transparencyPane);

            FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), transparencyPane);
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);
            fadeInTransition.play();

            transparencyPane.setOnMouseClicked(eventClick -> {
                FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.5), transparencyPane);
                fadeOutTransition.setFromValue(1.0);
                fadeOutTransition.setToValue(0.0);
                fadeOutTransition.setOnFinished(eventFinish -> {
                    StackFondo.getChildren().remove(transparencyPane); 
                });
                fadeOutTransition.play();
            });
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
}

