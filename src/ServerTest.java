/**
 *
 * @author ferens
 */
public class ServerTest {

    public static void main(String[] args) {

        StandardIO myUI = new StandardIO();
        Server myServer = new Server(4444, 100, myUI);
        UserCommandHandler myUserCommandHandler = new UserCommandHandler(myUI, myServer);
        myUI.setCommandHandler(myUserCommandHandler);
        Thread myUIthread = new Thread(myUI);
        myUIthread.start();
        myUI.display("1:\tQuit\n"
                + "2:\tlisten\n"
                + "3:\tSet Port\n"
                + "4:\tGet Port\n"
                + "5:\tStop listening\n"
                + "6:\tStart Server Socket\n");
    }
}
