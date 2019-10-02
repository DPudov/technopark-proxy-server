package com.dpudov.proxy;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    // ubuntu
    // localhost, 127.0.0.0/8, ::1
    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(8000);
            System.out.println("Proxy started..");
            while (true) {
                try {
                    Socket client = listener.accept();
                    Client handler = new Client(client, 80);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
