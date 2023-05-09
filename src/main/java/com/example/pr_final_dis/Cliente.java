package com.example.pr_final_dis;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String host;
    private int puerto;
    private String identificador;

    public Cliente(String host, int puerto, String identificador) {
        this.host = host;
        this.puerto = puerto;
        this.identificador = identificador;
    }

    public void conectar() throws IOException {
        socket = new Socket(host, puerto);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void enviarMensaje(String mensaje) {
        out.println(mensaje);
    }

    public String recibirMensaje() throws IOException {
        return in.readLine();
    }

    public void cerrarConexion() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

}
