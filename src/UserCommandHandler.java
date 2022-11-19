public class UserCommandHandler {
    StandardIO myUI;
    Server myServer;

    public UserCommandHandler(StandardIO myUI, Server myServer) {
        this.myUI = myUI;
        this.myServer = myServer;
    }

    public void handleUserCommand(String theCommand) {

        switch (Integer.parseInt(theCommand)) {
            case 1: // Quit
                System.exit(0);
                break;
            case 2: // Start Server and Listen
                myServer.startServer();
                myServer.listen();
                myUI.display("Server is now listening, ...");
                break;
            default:
                break;
        }
    }
}
