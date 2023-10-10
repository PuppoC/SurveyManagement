package Handlers;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.google.gson.Gson;

public class StorageHandler {

    static final String filesLocation = "Storage";

    static Gson gson = new Gson();

    private static String getStoragePath(String fileName){
        return filesLocation + "/" + fileName;}

    private static String readJsonFromFile(String filePath) {
        Path pathFilePath = Paths.get(getStoragePath(filePath));

        try {
            if (!Files.exists(pathFilePath)) {
                // Create a new file with an empty JSON object if it doesn't exist
                System.err.println("Attempted to read non existent file: " + filePath);
                return "{}";
            } else {
                return new String(Files.readAllBytes(pathFilePath));
            }
        } catch (IOException e) {
            System.out.println("Unable to read JSON from File: " + e.getMessage());
            return "{}"; // Return an empty JSON object in case of an error
        }
    }

    private static void writeJsonToFile(String json, String filePath) {
        Path pathFilePath = Paths.get(getStoragePath(filePath));

        try {
            if (!Files.exists(pathFilePath)) {
                // Create a new file if it doesn't exist
                Files.createFile(pathFilePath);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFilePath.toFile()))) {
                writer.write(json);
            }
        } catch (IOException e) {
            System.out.println("Unable to write JSON to File: " + e.getMessage());
        }
    }


    public static <T> void writeObjectToFile(T object, String filePath) {
        String encodedObject = gson.toJson(object);
        writeJsonToFile(encodedObject, filePath);
    }

    public static <T> T readObjectFromFile(String filePath, Class<T> objectType) {
        String encodedObject = readJsonFromFile(filePath);
        return gson.fromJson(encodedObject, objectType);
    }


    public static <T> List<T> getEachObjectInFolder(String folderPath, Class<T> classType) {
        List<T> objects = new ArrayList<>();

        Path folder = Paths.get(getStoragePath(folderPath));

        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try {
                Files.walkFileTree(folder, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<>() {

                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        // Check if the file ends with .json
                            try (BufferedReader reader = Files.newBufferedReader(file)) {

                                T object = gson.fromJson(reader, classType);
                                if (object == null){
                                    System.err.println("NULL object: " + file.getFileName());
                                }else{
                                    objects.add(object);
                                }


                            } catch (IOException e) {
                                // Print the error message to the standard error stream
                                System.err.println("Error processing file: " + file.getFileName() + " - " + e.getMessage());
                            }


                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                // Print the error message to the standard error stream
                System.err.println("Error walking the file tree: " + e.getMessage());
            }
        } else {
            // Print the error message to the standard error stream
            System.err.println("The specified folder does not exist or is not a directory.");
        }

        return objects;
    }

    public static boolean deleteFile(String filePath) {
        Path pathFilePath = Paths.get(getStoragePath(filePath));

        if (Files.exists(pathFilePath)) {
            try {
                Files.delete(pathFilePath);
                return true; // Deletion was successful
            } catch (IOException e) {
                System.out.println("Failed to delete the file: " + e.getMessage());
                return false; // Deletion failed
            }
        } else {
            System.out.println("File does not exist.");
            return false; // File does not exist
        }
    }
}



