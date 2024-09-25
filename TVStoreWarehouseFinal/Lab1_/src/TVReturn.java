/**
 * @author Obinna Elemuo
 * CMSY265 Fall 2023
 * 12/07/2023
 * TVReturn.java - Controller Class
 * The TVReturn class handles the inventory of TVs, represented as a map
 * with TV IDs as keys and TV objects as values. This class provides functionalities
 * to initialize the TV inventory from a file, process returns, and update the inventory file.
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class TVReturn {
    private Map<String, TV> tvMap;
    private static String filePath;

    // method to pass the file to the path, to use elements in the map
    public TVReturn(String path) {
        if (filePath == null) {
            filePath = path;
        }
        tvMap = new HashMap<>();
        initializeTVMap();
    }

    // method to initialize TVMap
    private void initializeTVMap() {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String id = scanner.nextLine().trim(); // Read the ID (first line)
                TV tv = new TV(id);
                tvMap.put(id, tv); // Add the TV to the map using its ID as the key
                System.out.println("TV added to map with ID: " + tv.getID()); // Print the ID directly

                // Skip the next three lines for brand, model, and price
                for (int i = 0; i < 3 && scanner.hasNextLine(); i++) {
                    scanner.nextLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }





    // method to process the return
    public TV processReturn(String tvId) {
        // Debug: Print all keys in the map
        System.out.println("Current keys in the map: " + tvMap.keySet());
        if (!tvMap.containsKey(tvId)) {
            System.out.println("Error: TV with ID " + tvId + " not found.");
            return null;
        }

        TV tv = tvMap.remove(tvId);
        updateFile();
        return tv;
    }

    private void updateFile() {
        // Update the tvsold.txt file to reflect the removal of the TV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (TV tv : tvMap.values()) {
                writer.write(tv.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
