import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import static pl.coderslab.ConsoleColors.*;

public class TaskManager {

    public static void main(String[] args) throws IOException {

        String fileName="tasks.csv";

        try{

            String[][] tasks = fileToString(fileName);
            tasks = menu(tasks);
            saveFile(tasks, fileName);

        } catch (IllegalArgumentException e){
            System.out.println("Incorrect data format inside " + fileName + " file");
            e.printStackTrace();
        }

    }

    private static String[][] menu(String[][] tasks) {
        printMenu();
        Scanner scannerMenu = new Scanner(System.in);
        String userSelection = scannerMenu.nextLine();

        while(!userSelection.equals("exit")){
            switch (userSelection) {
                case "add" -> tasks = addTask(tasks);
                case "remove" -> tasks = removeTask(tasks);
                case "list" -> printTasksList(tasks);
                default -> System.out.println("Incorrect selection");
            }

            printMenu();
            userSelection = scannerMenu.next();
        }
        return tasks;
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

        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        if(!datePattern.matcher(inputLine[1]).matches()){
            return false;
        }

        return inputLine[2].equals("true") || inputLine[2].equals("false");
    }

    public static void printTasksList(String[][] arrayOfTasks){
        String taskLine;
        System.out.println("List:");
        for(int i=0;i<arrayOfTasks.length;i++){
            taskLine = StringUtils.join(arrayOfTasks[i]," ");
            System.out.println(i + ": " + taskLine);
        }
        System.out.println();
    }

    public static String[][] removeTask(String[][] tasks){

        Scanner scanRemove = new Scanner(System.in);
        System.out.println("Please select task number to remove: ");

        String taskNumber=scanRemove.next();
        while(!NumberUtils.isParsable(taskNumber) || Integer.parseInt(taskNumber)>=tasks.length){
            System.out.println("Incorrect argument. Please select the task number to delete");
            taskNumber=scanRemove.next();
        }

        return ArrayUtils.removeElement(tasks, tasks[Integer.parseInt(taskNumber)]);
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
            System.out.println("Unable to save the file :" + filename);
            ex.printStackTrace();
        }

    }

    public static String[][] addTask(String[][] tasks){

        Scanner scanAdd = new Scanner(System.in);
        String[] newTaskLine=new String[3];

        System.out.println("Please add task description: ");
        newTaskLine[0] = scanAdd.nextLine();

        System.out.println("Please add task due date (valid date format YYYY-MM-DD): ");
        newTaskLine[1] = scanAdd.nextLine();
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        while(!datePattern.matcher(newTaskLine[1]).matches()){
            System.out.println("Incorrect date. Please input task due date in valid date format YYYY-MM-DD");
            newTaskLine[1] = scanAdd.nextLine();
        }

        System.out.println("Is task important: true / false");
        newTaskLine[2] = scanAdd.nextLine();
        while(!newTaskLine[2].equals("true") && !newTaskLine[2].equals("false")){
            System.out.println("Incorrect task importance. Please input true or false");
            newTaskLine[2] = scanAdd.nextLine();
        }


        tasks=Arrays.copyOf(tasks, tasks.length+1);
        tasks[tasks.length-1]=newTaskLine;

        return tasks;
    }

}
