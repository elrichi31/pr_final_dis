package com.example.pr_final_dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorSubasta implements Runnable {
    private int puerto;
    private List<ManejadorCliente> clientesConectados;
    private ServidorController controlador;

    public ServidorSubasta(int puerto, ServidorController controlador) {
        this.puerto = puerto;
        this.controlador = controlador;
        clientesConectados = new ArrayList<>();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de subastas iniciado en el puerto: " + puerto);
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());
                ManejadorCliente cliente = new ManejadorCliente(clienteSocket);
                new Thread(cliente).start();
                clientesConectados.add(cliente);
                controlador.agregarCliente(clienteSocket.getInetAddress().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarSubasta(String nombre, String descripcion, String valor) {
        for (ManejadorCliente cliente : clientesConectados) {
            cliente.enviarMensaje("INICIAR_SUBASTA," + nombre + "," + descripcion + "," + valor);
        }
    }

    public void terminarSubasta() {
        for (ManejadorCliente cliente : clientesConectados) {
            cliente.enviarMensaje("TERMINAR_SUBASTA");
        }
    }


    private class ManejadorCliente implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    System.out.println("Oferta recibida: " + mensaje);
                    for (ManejadorCliente cliente : clientesConectados) {
                        cliente.enviarMensaje(mensaje);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void enviarMensaje(String mensaje) {
            out.println(mensaje);
        }
    }
}
