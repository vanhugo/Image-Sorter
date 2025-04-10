package com.example.image_sorter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

// Represents the rating for an image
class Rating {
    public int value;  // Rating value (-1 indicates unrated)
    public String name;  // Image file name
    public String date;  // Date the image was created (from metadata)
}

// Manages image ratings and metadata in the application
public class DataModel {

    private String imageDirectory;  // Directory containing images
    public ArrayList<Rating> Ratings;  // List of image ratings

    // Constructor
    // requires: imageDirectory is a valid directory path
    // modifies: this
    // effects: initializes the data model and loads existing ratings from a CSV file if it exists
    public DataModel(String imageDirectory) {
        this.imageDirectory = imageDirectory;
        Ratings = new ArrayList<>();
        Path filePath = makeCSVName();  // Generate CSV file path

        // Load ratings from CSV if file exists
        if (Files.exists(filePath)) {
            readCSVFile(filePath.toString());
        }

        // Scan the folder for images to update ratings
        scanFolder(imageDirectory);
    }

    // Generates the file path for the ratings CSV file
    // requires: none
    // modifies: none
    // effects: returns the path to "Ratings.csv" in the image directory
    private Path makeCSVName() {
        return Paths.get(imageDirectory, "Ratings.csv");
    }

    // Reads ratings from a CSV file
    // requires: fileName points to a valid CSV file
    // modifies: Ratings
    // effects: populates the Ratings list with data from the CSV file
    private void readCSVFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Parse line into Rating object if it has exactly three parts
                if (parts.length == 3) {
                    Rating rating = new Rating();
                    rating.date = parts[0].trim();
                    rating.value = Integer.parseInt(parts[1].trim());
                    rating.name = parts[2].trim();
                    Ratings.add(rating);  // Add to the list
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing CSV file: " + e.getMessage());
        }
    }

    // Scans a folder for images and updates the ratings list
    // requires: folderName points to a valid directory path
    // modifies: Ratings
    // effects: adds new images as unrated entries in the Ratings list
    private void scanFolder(String folderName) {
        File folder = new File(folderName);

        if (folder.isDirectory()) {
            // Retrieve all files in the folder
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();

                    // Process JPEG files only
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        boolean existsInRatings = false;

                        // Check if the image is already rated
                        for (Rating rating : Ratings) {
                            if (rating.name.equalsIgnoreCase(fileName)) {
                                existsInRatings = true;
                                break;
                            }
                        }

                        // Add a new unrated image to the list
                        if (!existsInRatings) {
                            String date = "N/A";

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


                            // Extract metadata for the image
                            try {
                                Metadata metadata = JpegMetadataReader.readMetadata(file);
                                ExifSubIFDDirectory directory =
                                        metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

                                Date rawDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                                if (rawDate != null) {
                                    date = formatter.format(rawDate);
                                }
                            } catch (Exception e) {
                                System.out.println("Metadata extraction failed for: " + fileName);
                            }

                            Rating newRating = new Rating();
                            newRating.value = -1;  // Default to unrated
                            newRating.name = fileName;
                            newRating.date = date;
                            Ratings.add(newRating);
                        }
                    }
                }
            }
        } else {
            System.out.println("Provided path is not a directory: " + folderName);
        }
    }

    // Saves all ratings to a CSV file
    // requires: none
    // modifies: filesystem
    // effects: writes the ratings to "Ratings.csv" in the image directory
    public void save() {
        Path csvName = makeCSVName();
        try (BufferedWriter writer = Files.newBufferedWriter(csvName)) {
            for (Rating rating : Ratings) {
                writer.write(rating.date + "," + rating.value + "," + rating.name);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving ratings: " + e.getMessage());
        }
    }

    // Counts the number of unrated images
    // requires: none
    // modifies: none
    // effects: returns the number of unrated images
    public int countUnrated() {
        int unrated = 0;
        for (Rating rating : Ratings) {
            if (rating.value == -1) {
                unrated++;
            }
        }
        return unrated;
    }

    // Selects a random unrated image
    // requires: at least one unrated image exists
    // modifies: none
    // effects: returns the file name of a random unrated image
    public String randomUnrated() throws Exception {
        int unrated = countUnrated();
        if (unrated == 0) {
            throw new Exception("No unrated images available.");
        }

        int choice = (int) (Math.random() * unrated);
        for (Rating rating : Ratings) {
            if (rating.value == -1) {
                if (choice == 0) {
                    return rating.name;
                }
                choice--;
            }
        }

        throw new Exception("No unrated image found.");
    }

    // Assigns a rating to a specific image
    // requires: imageName is a valid file name, and value is a valid rating (0-9)
    // modifies: Ratings
    // effects: updates the rating of the specified image
    public void rateImage(String imageName, int value) {
        for (Rating rating : Ratings) {
            if (rating.name.equals(imageName)) {
                rating.value = value;
                return;  // Exit once the image is rated
            }
        }
    }
}
