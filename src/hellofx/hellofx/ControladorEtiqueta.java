package hellofx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.ArrayList;

public class ControladorEtiqueta {

    @FXML
    private Label txtConsumo;

    @FXML
    private Label txtFactor;

    @FXML
    private Label txtTipo;
    
    @FXML
    private Label txtMarca;

    @FXML
    private Label txtModelo;

    @FXML
    private ImageView imgGrado;

    @FXML
    private VBox vBoxInfo;

    public void initialize() throws ClassNotFoundException {
        String model = " H6162NT29"; 
        getDataProduct(model);
        getInfoProduct();
        printAll();
    }

    private void getDataProduct(String modelo) {
        Connection cx = null;
        PreparedStatement consulta = null;
        PreparedStatement consulta2 = null;
        ResultSet resultado = null;
        ResultSet resultado2 = null;
    
        try {

            // Verifica que estás usando el puerto correcto
            cx = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");
    
            String consultaSQL = "SELECT consumo, factor, grado, imagen FROM Casa_Producto WHERE cd_casa = ?";
            String consultaProducto = "SELECT tipo, marca FROM Productos WHERE modelo = ?";
    
            consulta = cx.prepareStatement(consultaSQL);
            consulta.setString(1, "1");
    
            consulta2 = cx.prepareStatement(consultaProducto);
            consulta2.setString(1, modelo);
    
            resultado = consulta.executeQuery();
            resultado2 = consulta2.executeQuery();
    
            if (resultado.next() && resultado2.next()) {
                // Obtener datos del primer resultado
                String consumo = resultado.getString("consumo");
                String factor = resultado.getString("factor");
                String imagen = resultado.getString("imagen");
    
                // Asignar datos a los labels
                txtConsumo.setText(consumo + " kWh/mes");
                txtFactor.setText(factor + " (%)");
                txtModelo.setText(modelo);
    
                Image gradoImg = new Image(imagen);
                imgGrado.setImage(gradoImg);
    
                // Obtener datos del segundo resultado
                String tipo = resultado2.getString("tipo");
                String marca = resultado2.getString("marca");
    
                // Asignar datos a los labels
                txtMarca.setText(marca);
                txtTipo.setText(tipo);
            } else {
                System.out.println("No se encontraron resultados para las consultas.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (resultado2 != null) resultado2.close();
                if (consulta != null) consulta.close();
                if (consulta2 != null) consulta2.close();
                if (cx != null) cx.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public class InfoProducto {

      // Atributos específicos para lavadoras
      private String capacidadCarga;
      private String capacidadVolumen;
      private String consumoAgua;
      private String tipoProducto;
  
      // Atributos específicos para refrigeradores
      private String volumenAlmacenamiento;
      private String sistemaDeshielo;
      private String tempAdecuada;
      private String tempMinima;
  
      // Constructor único para lavadoras y refrigeradores
      public InfoProducto(String capacidadCarga, String capacidadVolumen, String consumoAgua, String tipoProducto, 
                          String volumenAlmacenamiento, String sistemaDeshielo, String tempAdecuada, String tempMinima) {
          this.capacidadCarga = capacidadCarga;
          this.capacidadVolumen = capacidadVolumen;
          this.consumoAgua = consumoAgua;
          this.tipoProducto = tipoProducto;
          this.volumenAlmacenamiento = volumenAlmacenamiento;
          this.sistemaDeshielo = sistemaDeshielo;
          this.tempAdecuada = tempAdecuada;
          this.tempMinima = tempMinima;
      }
  
      // Métodos getters para lavadoras
      public String getCapacidadCarga() {
          return capacidadCarga;
      }
  
      public String getCapacidadVolumen() {
          return capacidadVolumen;
      }
  
      public String getConsumoAgua() {
          return consumoAgua;
      }
  
      public String getTipoProducto() {
          return tipoProducto;
      }
  
      // Métodos getters para refrigeradores
      public String getVolumenAlmacenamiento() {
          return volumenAlmacenamiento;
      }
  
      public String getSistemaDeshielo() {
          return sistemaDeshielo;
      }
  
      public String getTempAdecuada() {
          return tempAdecuada;
      }
  
      public String getTempMinima() {
          return tempMinima;
      }
  
      @Override
      public String toString() {
          if (capacidadCarga != null) {
              return "Lavadora - Capacidad de Carga: " + capacidadCarga +
                     ", Capacidad de Volumen: " + capacidadVolumen +
                     ", Consumo de Agua: " + consumoAgua +
                     ", Tipo de Producto: " + tipoProducto;
          } else {
              return "Refrigerador - Volumen de Almacenamiento: " + volumenAlmacenamiento +
                     ", Sistema de Deshielo: " + sistemaDeshielo +
                     ", Temperatura Adecuada: " + tempAdecuada +
                     ", Temperatura Mínima: " + tempMinima;
          }
      }
  }
  
  private List<InfoProducto> getInfoProduct() throws ClassNotFoundException {
    List<InfoProducto> infoProducto = new ArrayList<>();

    Connection cx = null;
    PreparedStatement prepareStatement = null;
    ResultSet resulSet = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cx = DriverManager.getConnection("jdbc:mysql://localhost:8080/Electricity_Manager", "root", "");
        String modelo = "WA16J6710LS";
        String id_producto_node = "2";

        String consulta = "SELECT * FROM Info_Producto WHERE id = ?";
        prepareStatement = cx.prepareStatement(consulta);
        prepareStatement.setString(1, id_producto_node);

        resulSet = prepareStatement.executeQuery();

        while (resulSet.next()) {
            String tipo = resulSet.getString("tipo");

            if (tipo.equals("Lavadora")) {
                String capacidadCarga = resulSet.getString("capacidad_carga");
                String capacidadVolumen = resulSet.getString("capacidad_volumen");
                String consumoAgua = resulSet.getString("consumo_agua");
                String tipoProducto = resulSet.getString("tipo_producto");

                InfoProducto infoLavadoras = new InfoProducto(capacidadCarga, capacidadVolumen, consumoAgua, tipoProducto, 
                                                              null, null, null, null);
                infoProducto.add(infoLavadoras);

            } else if (tipo.equals("Refrigerador")) {
                String volumenAlmacenamiento = resulSet.getString("volumen_almacenamiento");
                String sistemaDeshielo = resulSet.getString("sistema_deshielo");
                String tempAdecuada = resulSet.getString("temp_adecuada");
                String tempMinima = resulSet.getString("temp_minima");

                InfoProducto infoRefrigerador = new InfoProducto(null, null, null, null, 
                                                                volumenAlmacenamiento, sistemaDeshielo, tempAdecuada, tempMinima);
                infoProducto.add(infoRefrigerador);
            }
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

    return infoProducto;
}

public void printAll() throws ClassNotFoundException {
  List<InfoProducto> productos = getInfoProduct();
  for (InfoProducto producto : productos) {
      System.out.println(producto);
  }
}

}
