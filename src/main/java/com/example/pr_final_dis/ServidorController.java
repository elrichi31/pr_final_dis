package com.example.pr_final_dis;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServidorController extends Application {

    @FXML
    private ListView<String> listaClientes;
    @FXML
    private Label lblArticulo;
    @FXML
    private Label lblDescripcion;
    @FXML
    private TextField txtArticulo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Button btnSetArticulo;
    @FXML
    private Label lblValor;
    @FXML
    private TextField txtValor;


    private ServidorSubasta servidor;

    public void inicializarServidor(int puerto) {
        servidor = new ServidorSubasta(puerto, this);
        new Thread(servidor).start();
    }

    @FXML
    private void setArticulo() {
        String nombreArticulo = txtArticulo.getText();
        String descripcionArticulo = txtDescripcion.getText();
        String valorArticulo = txtValor.getText();
        lblArticulo.setText(nombreArticulo);
        lblDescripcion.setText(descripcionArticulo);
        lblValor.setText(valorArticulo);
    }

    @FXML
    private void iniciarSubasta() {
        servidor.iniciarSubasta(lblArticulo.getText(), lblDescripcion.getText(), lblValor.getText());
        mostrarAlertaInicioSubasta();
    }

    @FXML
    private void terminarSubasta() {
        servidor.terminarSubasta();
        mostrarAlertaFinSubasta();
    }

    public void agregarCliente(String direccionIP) {
        Platform.runLater(() -> listaClientes.getItems().add(direccionIP));
    }

    public void mostrarAlertaInicioSubasta() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Subasta");
            alert.setHeaderText(null);
            alert.setContentText("La subasta ha comenzado!");
            alert.showAndWait();
        });
    }

    public void mostrarAlertaFinSubasta() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Subasta");
            alert.setHeaderText(null);
            alert.setContentText("La subasta ha terminado!");
            alert.showAndWait();
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("servidor.fxml"));
        Parent root = loader.load();
        ServidorController controller = loader.getController();
        controller.inicializarServidor(8080);

        primaryStage.setTitle("Servidor de Subastas");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
