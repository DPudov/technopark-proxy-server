    package com.dpudov.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client implements Runnable {
    private Socket mSocket;
    private Socket mInternetGate;

    private Thread mThread;

    private int mPort;

    public Client(Socket socket, int port) {
        this.mSocket = socket;
        this.mPort = port;
        this.mThread = new Thread(this, "Thread Client");
        mThread.run();

    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            BufferedInputStream clientInputStream = new BufferedInputStream(mSocket.getInputStream());
            clientInputStream.read(buffer);

            String clientRequest = new String(buffer);

            System.out.println(clientRequest);

            String host = getHostFromRequest(clientRequest);

            mInternetGate = new Socket(host, mPort);
            BufferedOutputStream internetOstream = new BufferedOutputStream(mInternetGate.getOutputStream());
            internetOstream.write(clientRequest.getBytes());
            internetOstream.flush();

            BufferedOutputStream clientOutputStream = new BufferedOutputStream(mSocket.getOutputStream());
            BufferedInputStream internetOutputStream = new BufferedInputStream(mInternetGate.getInputStream());

            buffer = new byte[Constants.BUFFER_SIZE];
            int read;
            while ((read = internetOutputStream.read(buffer)) != -1) {
                clientOutputStream.write(buffer, 0, read);
            }
            clientOutputStream.flush();

            clientInputStream.close();
            clientOutputStream.close();
            internetOutputStream.close();
            internetOstream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHostFromRequest(String request) {
        StringTokenizer tok = new StringTokenizer(request, Constants.BODY_END);
        tok.nextToken(); //method
        String host = tok.nextToken();
        host = host.substring("Host: ".length());
        return host;
    }
}
