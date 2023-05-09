package com.example.pr_final_dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorSubasta {
    private int puerto;
    private List<PrintWriter> escritoresClientes;

    public ServidorSubasta(int puerto) {
        this.puerto = puerto;
        escritoresClientes = new ArrayList<>();
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de subastas iniciado en el puerto: " + puerto);
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());
                new Thread(new ManejadorCliente(clienteSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ManejadorCliente implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                escritoresClientes.add(out);

                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    System.out.println("Oferta recibida: " + mensaje);
                    for (PrintWriter escritor : escritoresClientes) {
                        escritor.println(mensaje);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    escritoresClientes.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ServidorSubasta servidorSubasta = new ServidorSubasta(8080);
        servidorSubasta.iniciar();
    }
}
