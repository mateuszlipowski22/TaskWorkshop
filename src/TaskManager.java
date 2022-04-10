import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) throws IOException {

        String fileName="tasks.csv";
        String[][] taskListTable = fileToString(fileName);

        System.out.println(Arrays.deepToString(taskListTable));

    }

    public static String[][] fileToString(String filename) throws IOException {
        Path filePath = Paths.get("src/"+filename);
        if(!Files.exists(filePath)){
            Files.createFile(filePath);
        }

        File file = new File("src/"+filename);
        Scanner scan = new Scanner(file);
        String[][] stringList = new String[0][];
        String[] nextLine;
        while(scan.hasNextLine()){
            nextLine=scan.nextLine().split(", ");

            if (inputTblValidation(nextLine)) {
                stringList = Arrays.copyOf(stringList, stringList.length+1);
                stringList[stringList.length-1] =nextLine;
            }else{
                throw new IllegalArgumentException();
            }

        }

        return stringList;
    }

    public static boolean inputTblValidation(String[] inputLine) {
        if(inputLine.length!=3){
            return false;
        }

        return inputLine[2].equals("true") || inputLine[2].equals("false");
    }

}
