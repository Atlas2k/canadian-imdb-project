import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class dbScript {
    public static void main(String[] args) {
        try {
            System.out.println("Please enter the input file name (.txt files only):");
            Scanner inputTxt = new Scanner(new FileReader(new File((new Scanner("C:\\Users\\akint\\OneDrive\\Desktop\\movies\\src\\data.tsv")).nextLine())));
            processing(inputTxt);
            System.out.println("Program terminated normally.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("File not found");
       }
    }//main

    private static void process(){

    }

    private static void processing(Scanner file){
        String[] line;
        title newTitle;
        while (file.hasNext()){
                line = file.nextLine().split("\t");
           newTitle = new title(line[0], //tconst
                                        line[1], //titleType
                                        line[2], //primaryTitle
                                        line[3], //originalTitle
                                        line[4], //isAdult
                                        line[5], //startYear
                                        line[6], //endYear
                                        line[7], //run minutes
                                        line[8]); //genre
            System.out.println(newTitle.titleType);
            //System.out.println("The primaryNo "+ file.nextLine().split("\t")[0] + "title " +file.nextLine().split("\t")[1] + );
             //System.out.println(line);
            }
        }
}

