import java.sql.SQLException;
import java.util.Scanner;

public class movies {
    public static void main(String[] args) throws SQLException {
        Scanner console = new Scanner(System.in);
        System.out.print("Welcome! Type h for help. ");
        System.out.print("db > ");
        String line = console.nextLine();
        String[] parts;
        String arg = "";
        generateSQL generateSQLL = new generateSQL();

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");
            if (line.indexOf(" ") > 0)
                arg = line.substring(line.indexOf(" ")).trim();
            if (parts[0].equals("h"))
                printHelp();


            else if (parts[0].equals("a")) {
                System.out.println(generateSQLL.yourSearch("a"));
            }

            else if (parts[0].equals("b")) {
                System.out.println(generateSQLL.yourSearch("b"));
            }

            System.out.print("db > ");
            line = console.nextLine();

        }
        console.close();
    }
    private static void printHelp() {
        System.out.println("Library database");
        System.out.println("Commands:");
        System.out.println("h - Get help");
        System.out.println("s <name> - Search for a name");
        System.out.println("l <id> - Search for a user by id");
        System.out.println("sell <author id> - Search for a stores that sell books by this id");
        System.out.println("notread - Books not read by its own author");
        System.out.println("all - Authors that have read all their own books");
        System.out.println("notsell <author id>  - list of stores that do not sell this author");
        System.out.println("mp - Authors with the most publishers");
        System.out.println("mc - Authors with books in the most cities");
        System.out.println("mr - Most read book by country");
        System.out.println("");
        System.out.println("q - Exit the program");
        System.out.println("---- end help ----- ");
    }
}
