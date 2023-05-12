package com.example.pr_final_dis;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

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
    @FXML
    private Label lblArticulo;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblValor;

    private Cliente cliente;

    public void inicializarCliente(String direccionIP, int puerto, String identificador) {
        cliente = new Cliente(direccionIP, puerto, identificador);
        try {
            cliente.conectar();

            new Thread(() -> {
                String mensaje;
                try {
                    while ((mensaje = cliente.recibirMensaje()) != null) {
                        String[] partes = mensaje.split(",");
                        if (partes[0].equals("INICIAR_SUBASTA")) {
                            Platform.runLater(() -> {
                                lblArticulo.setText(partes[1]);
                                lblDescripcion.setText(partes[2]);
                                lblValor.setText(partes[3]);  // Agregar esta línea
                                btnOfertar.setDisable(false);
                                btnTerminar.setDisable(false);
                            });
                            mostrarAlertaInicioSubasta();
                        } else if (mensaje.equals("TERMINAR_SUBASTA")) {
                            Platform.runLater(() -> {
                                btnOfertar.setDisable(true);
                                btnTerminar.setDisable(true);
                            });
                            mostrarAlertaFinSubasta();
                        } else {
                            String oferta = mensaje;
                            Platform.runLater(() -> listaOfertas.getItems().add(oferta));
                        }
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

    }


    private void mostrarAlertaInicioSubasta() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Subasta");
            alert.setHeaderText(null);
            alert.setContentText("La subasta ha comenzado!");
            alert.showAndWait();
        });
    }

    private void mostrarAlertaFinSubasta() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Subasta");
            alert.setHeaderText(null);
            alert.setContentText("La subasta ha terminado!");
            alert.showAndWait();
        });
    }
}
