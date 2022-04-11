import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import static pl.coderslab.ConsoleColors.*;

public class TaskManager {

    public static void main(String[] args) throws IOException {

        String fileName="tasks.csv";
        String[][] tasks = fileToString(fileName);

        printMenu();
        Scanner scannerMenu = new Scanner(System.in);
        String userSelection = scannerMenu.nextLine();

        while(!userSelection.equals("exit")){
                switch (userSelection){
                    case "add":
    //                    add option
                        break;
                    case "remove":
                        tasks = removeTask(tasks);
                        break;
                    case "list":
                        printList(tasks);
                        break;
                    default:
                        System.out.println("Incorrect selection");
                }

            printMenu();
            userSelection= scannerMenu.next();
        }

        saveFile(tasks, fileName);
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

    public static void printList(String[][] arrayOfTasks){
        String taskLine;
        System.out.println("List:");
        for(int i=0;i<arrayOfTasks.length;i++){
            taskLine = StringUtils.join(arrayOfTasks[i]," ");
            System.out.println(i + ": " + taskLine);
        }
    }

    public static String[][] removeTask(String[][] tasks){

        Scanner scanRemove = new Scanner(System.in);
        System.out.println("Please select task number to remove: ");

        int taskNumber;
        while(!scanRemove.hasNextInt()){
            System.out.println("Incorrect argument. Please select the task number to delete");
            scanRemove.next();
        }
        taskNumber=scanRemove.nextInt();

        return ArrayUtils.removeElement(tasks, tasks[taskNumber]);
    }

    public static void printMenu(){
        String[] menuList=new String[]{"add", "remove", "list", "exit"};
        System.out.println(BLUE + "Please select an option" + RESET);
        for(String element : menuList){
            System.out.println(element);
        }
    }

    public static void saveFile(String[][] tasksList, String filename) {

        String[] output = new String[tasksList.length];
        for(int i=0; i<tasksList.length; i++){
            output[i] = StringUtils.join(tasksList[i], ", ");
        }
        String outputString = StringUtils.join(output, "\n");

        Path filePath = Paths.get("src/"+filename);

        try {
            Files.writeString(filePath, outputString);
        } catch (IOException ex) {
            System.out.println("Nie można zapisać pliku.");
        }

    }

}
