import java.sql.SQLException;

public class ClientCommandHandler {

    StandardIO myUI;
    Server myServer;
    generateSQL generateSQL;
    String recievedCommand = "";

    public ClientCommandHandler(Server myServer) {
        this.myServer = myServer;
        generateSQL = new generateSQL();
    }

    public void handleClientCommand(ClientConnection myClientConnection, String theCommand) throws SQLException {
        if (theCommand.charAt(0) != '\0') {
            recievedCommand += theCommand;
        } else {
            instructionHandler(myClientConnection, recievedCommand);
        }

    }

    public void instructionHandler(ClientConnection myClientConnection, String recievedCommand) throws SQLException {
        String instruction = recievedCommand.split(" ")[0];
        if (instruction.equals("m")) { // Search for a movie
            String data = generateSQL.searchMovie(recievedCommand);
            sendData(data, myClientConnection);
        } else if (instruction.equals("s")) { // Search for a show
            String data = generateSQL.searchShow(recievedCommand);
            sendData(data, myClientConnection);
        } else if (instruction.equals("e")) { // Search for a show
            String data = generateSQL.searchEpisode(recievedCommand);
            sendData(data, myClientConnection);
        } else if (instruction.equals("a")) { // Search for a person
            String data = generateSQL.searchPerson(recievedCommand);
            sendData(data, myClientConnection);
        } else if (instruction.equals("b")) { // All media by a person
            String data = generateSQL.allMediaForPerson(recievedCommand);
            sendData(data, myClientConnection);
        } else if (instruction.equals("c")) { // All people who played a character
            String data = generateSQL.allPeoplePlayingACharacter(recievedCommand);
            sendData(data, myClientConnection);
        }
    }

    public static void sendData(String data, ClientConnection myClientConnection) {
        for (int i = 0; i < data.length(); i++) {
            byte msg = (byte) data.charAt(i);
            myClientConnection.sendMessageToClient(msg);
        }
        myClientConnection.sendMessageToClient((byte) '\0');
    }
}
