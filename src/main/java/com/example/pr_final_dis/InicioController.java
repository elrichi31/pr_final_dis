package com.example.pr_final_dis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InicioController {

    @FXML
    private TextField txtIdentificador;

    public void agregarCliente(ActionEvent event) {
        try {
            String identificador = txtIdentificador.getText();

            // Establecer la dirección IP y el puerto del servidor aquí
            String direccionIP = "127.0.0.1";
            int puerto = 8080;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("subasta.fxml"));
            Parent root = loader.load();

            SubastaController subastaController = loader.getController();
            subastaController.inicializarCliente(direccionIP, puerto, identificador);

            Stage stage = new Stage();
            stage.setTitle("Sistema de Subastas - Cliente " + identificador);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

            // Borrar el contenido del campo de texto del identificador
            txtIdentificador.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
