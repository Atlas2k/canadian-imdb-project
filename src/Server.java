import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{

    InputStream input;
    OutputStream output;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    ClientCommandHandler myClientCommandHandler;
    StandardIO myUI;
    int portNumber = 4444, backlog = 100;
    boolean doListen = false;

    public Server(int portNumber, int backlog, StandardIO myUI) {
        this.portNumber = portNumber;
        this.backlog = backlog;
        this.myUI = myUI;
        this.myClientCommandHandler = new ClientCommandHandler(this);
    }

    public synchronized void setDoListen(boolean doListen){
        this.doListen = doListen;
    }
    public void startServer() {
        if (serverSocket != null) {
            sendMessageToUI("Server socket has already been created.");
        } else {
            try {
                serverSocket = new ServerSocket(portNumber, backlog);
            } catch (IOException e) {
                sendMessageToUI("Cannot create "
                        + "ServerSocket, because "
                        + e +". Exiting program.");
                System.exit(1);
            } finally {
            }
        }
    }

    public void stopServer() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                sendMessageToUI("Cannot close ServerSocket, because " + e +". Exiting program.");
                System.exit(0);
            } finally {
            }

        }
    }

    public void listen() {
        try {
            setDoListen(true);
            serverSocket.setSoTimeout(500);
            //To allow a future stopListening user command
            Thread myListenerThread = new Thread(this);
            myListenerThread.start();
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log
                    (Level.SEVERE, null, ex);
        }
    }

    public void stopListening() {
        setDoListen(false);
    }

    @Override
    public void run() {
        while (true) {
            if (doListen == true) {
                try {
                    clientSocket = serverSocket.accept();
                    ClientConnection myCC = new ClientConnection(clientSocket, myClientCommandHandler, this);
                    Thread myCCthread = new Thread(myCC);
                    myCCthread.start();
                    sendMessageToUI("Client connected:\n\tRemote Socket Address = " + clientSocket.getRemoteSocketAddress() + "\n\tLocal Socket Address = " + clientSocket.getLocalSocketAddress());
                } catch (IOException e) {
                    //check doListen.
                } finally {
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {}
            }
        }
    }

    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPort() {
        return this.portNumber;
    }

    public void sendMessageToUI(String theString) {
        myUI.display(theString);
    }
}