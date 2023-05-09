package com.example.pr_final_dis;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SubastaController {

    @FXML
    private Button btnOfertar;
    @FXML
    private Button btnTerminar;
    @FXML
    private TextField txtOferta;
    @FXML
    private ListView<String> listaOfertas;

    private Cliente cliente;

    public void inicializarCliente(String direccionIP, int puerto, String identificador) {
        cliente = new Cliente(direccionIP, puerto, identificador);
        try {
            cliente.conectar();
            btnOfertar.setDisable(false);
            btnTerminar.setDisable(false);

            new Thread(() -> {
                String mensaje;
                try {
                    while ((mensaje = cliente.recibirMensaje()) != null) {
                        String oferta = mensaje;
                        Platform.runLater(() -> listaOfertas.getItems().add(oferta));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void realizarOferta() {
        String oferta = txtOferta.getText();
        cliente.enviarMensaje(cliente.getIdentificador() + ": " + oferta);
        txtOferta.clear();
    }

    @FXML
    private void terminarSubasta() {
        btnOfertar.setDisable(true);
        btnTerminar.setDisable(true);
        try {
            cliente.cerrarConexion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
