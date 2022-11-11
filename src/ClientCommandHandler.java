import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//Where we will handle commands from client.

public class ClientCommandHandler {

    StandardIO myUI;
    Server myServer;
    generateSQL generateSQLL;
    String intr = "";

    public ClientCommandHandler(Server myServer) {
        this.myServer = myServer;
        generateSQLL = new generateSQL();
    }

    public void handleClientCommand(ClientConnection myClientConnection, String theCommand) throws SQLException {
        if(theCommand.charAt(0) != '/'){
            intr += theCommand;
        }else{
            instructionHandler(myClientConnection,intr);
        }

    }
    
    public void instructionHandler(ClientConnection myClientConnection, String theCommand) throws SQLException {
    byte msg;
        String instruction = theCommand.split(" ")[0];
        if (instruction.equals("d")) {
            myServer.sendMessageToUI("Disconnect command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            myClientConnection.clientDisconnect();
            myServer.sendMessageToUI("\tDisconnect successful. ");
        } else if (instruction.equals("q")) {
            myServer.sendMessageToUI("Quit command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            myClientConnection.clientQuit();
            myServer.sendMessageToUI("\tQuit successful. ");
        } else if (instruction.equals("t")) {
            myServer.sendMessageToUI("Get Time command received from client " + myClientConnection.getClientSocket().getRemoteSocketAddress());
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            for (int i = 0; i < sdf.format(cal.getTime()).length(); i++) {
                msg = (byte) sdf.format(cal.getTime()).charAt(i);
                myClientConnection.sendMessageToClient(msg);
            }
            myServer.sendMessageToUI("\tClient given time: "+sdf.format(cal.getTime()));
        } else if (instruction.equals("a")) {
            String data = generateSQLL.yourSearch(theCommand);
            for (int i = 0; i < data.length(); i++) {
                msg = (byte) data.charAt(i);
                myClientConnection.sendMessageToClient(msg);
            }
            myClientConnection.sendMessageToClient((byte) 0xFFFF);
        }
        else if (instruction.equals("b")) {
            String data = generateSQLL.yourSearch(theCommand);
            for (int i = 0; i < data.length(); i++) {
                msg = (byte) data.charAt(i);
                myClientConnection.sendMessageToClient(msg);
            }
            myClientConnection.sendMessageToClient((byte) 0xFFFF);
        }
    }
}
