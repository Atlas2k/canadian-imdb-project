public class RunServer {

    public static void main(String[] args) {

        StandardIO myUI = new StandardIO();
        Server myServer = new Server(4444, 100, myUI);
        UserCommandHandler myUserCommandHandler = new UserCommandHandler(myUI, myServer);
        myUI.setCommandHandler(myUserCommandHandler);
        Thread myUIthread = new Thread(myUI);
        myUIthread.start();
        myUI.display("1:\tQuit\n"
                + "2:\tStart Server and Listen");
    }
}
