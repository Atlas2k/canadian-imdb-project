/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ferens
 */
public class ClientConnection implements Runnable {

    InputStream input;
    OutputStream output;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    StandardIO myUI;
    ClientCommandHandler myClientCommandHandler;
    Server myServer;
    boolean stopThisThread = false;

    public ClientConnection(Socket clientSocket, ClientCommandHandler myClientCommandHandler, Server myServer) {
        this.clientSocket = clientSocket;
        this.myClientCommandHandler = myClientCommandHandler;
        this.myServer = myServer;
        try {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            myServer.sendMessageToUI("Cannot create IO streams; exiting program.");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        byte msg;
        String theString;
        while (stopThisThread == false) {
            try {
                msg = (byte) input.read();
                theString = Character.toString(msg);
                myClientCommandHandler.handleClientCommand(this, theString);
            } catch (IOException | SQLException e) {
                if(e.toString().contains("Connection reset"))
                    myServer.sendMessageToUI("Connection was unexpectedly reset by remote host; stopping thread and disconnecting client: "+ clientSocket.getRemoteSocketAddress());
                else
                    myServer.sendMessageToUI("Cannot read from socket; stopping thread and disconnecting client." + clientSocket.getRemoteSocketAddress() + "error message is: "+ e);
                disconnectClient();
                stopThisThread = true;
            }
        }
    }

    public void sendMessageToClient(byte msg) {
        try {
            output.write(msg);
            output.flush();
        } catch (IOException e) {
            myServer.sendMessageToUI("cannot send to socket; exiting program.");
            System.exit(0);
        } finally {
        }
    }

    public void clientQuit() {
        disconnectClient();
    }

    public void clientDisconnect() {
        disconnectClient();
    }

    public void disconnectClient() {
        try {
            stopThisThread = true;
            clientSocket.close();
            clientSocket = null;
            input = null;
            output = null;
        } catch (IOException e) {
            myServer.sendMessageToUI("cannot close client socket; exiting program.");
            System.exit(0);
        } finally {
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
