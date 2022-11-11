/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserCommandHandler {
    StandardIO myUI;
    Server myServer;

    public UserCommandHandler(StandardIO myUI, Server myServer) {
        this.myUI = myUI;
        this.myServer = myServer;
    }

    public void handleUserCommand(String theCommand) {

        switch (Integer.parseInt(theCommand)) {
            case 6: //START SERVER SOCKET
                myServer.startServer();
                myUI.display("Server Socket has been created.");
                break;
            case 2: //LISTEN
                myServer.listen();
                myUI.display("Server is now listening, ...");
                break;
            case 1: //QUIT
                myServer.stopServer();
                myUI.display("Quiting program by User command.");
                System.exit(0);
                break;
            case 3: //SET PORT
                myUI.display("The port number set function is not available at this time.");
                break;
            case 4: //GET PORT
                myUI.display("The port number is: " + String.valueOf(myServer.getPort()));
                break;
            case 5: //Stop Listening
                myServer.stopListening();
                myUI.display("Server is not listening, ...");
                break;
            default:
                break;
        }
    }
}
