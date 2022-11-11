import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//Where we will handle commands from client.

public class ClientCommandHandler {

    StandardIO myUI;
    Server myServer;
    generateSQL generateSQLL;

    public ClientCommandHandler(Server myServer) {
        this.myServer = myServer;
        generateSQLL = new generateSQL();
    }

    public void handleClientCommand(ClientConnection myClientConnection, String theCommand) throws SQLException {
        byte msg;
        if (theCommand.equals("d")) {
            myServer.sendMessageToUI("Disconnect command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            myClientConnection.clientDisconnect();
            myServer.sendMessageToUI("\tDisconnect successful. ");
        } else if (theCommand.equals("q")) {
            myServer.sendMessageToUI("Quit command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            myClientConnection.clientQuit();
            myServer.sendMessageToUI("\tQuit successful. ");
        } else if (theCommand.equals("t")) {
            myServer.sendMessageToUI("Get Time command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            for (int i = 0; i < sdf.format(cal.getTime()).length(); i++) {
                msg = (byte) sdf.format(cal.getTime()).charAt(i);
                myClientConnection.sendMessageToClient(msg);
            }
            myServer.sendMessageToUI("\tClient given time: "+sdf.format(cal.getTime()));
        } else if (theCommand.equals("a")) {
            String data = generateSQLL.yourSearch("a");
            for (int i = 0; i < data.length(); i++) {
                msg = (byte) data.charAt(i);
                myClientConnection.sendMessageToClient(msg);
            }
            myClientConnection.sendMessageToClient((byte) 0xFFFF);
        }
    }
}
