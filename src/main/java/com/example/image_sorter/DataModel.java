package com.example.image_sorter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;

class Rating {
    public int value;
    public String name;
    public String date;
}

public class DataModel {
    private String imageDirectory;
    public ArrayList<Rating> Ratings;

    public DataModel(String imageDirectory) {
        this.imageDirectory = imageDirectory;
        Ratings = new ArrayList<>();
        Path filePath = makeCSVName();
        if (Files.exists(filePath)) {
            readCSVFile(filePath.toString());
        }
        scanFolder(imageDirectory);
    }

    private Path makeCSVName(){
        Path filePath = Paths.get(imageDirectory, "Ratings.csv");
        return filePath;
    }

    private void readCSVFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Split the line by comma, assuming "value,name" format
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Rating rating = new Rating();
                    rating.value = Integer.parseInt(parts[0].trim());
                    rating.name = parts[1].trim();
                    rating.date = parts[2].trim();
                    Ratings.add(rating);  // Add to Ratings list
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing CSV file: " + e.getMessage());
        }
    }

    private void scanFolder (String folderName){
        File folder = new File(folderName);
        if (folder.isDirectory()) {
            // Filter for image files only
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        boolean existsInRatings = false;
                        for (Rating rating : Ratings) {
                            if (rating.name.equalsIgnoreCase(fileName)) {
                                existsInRatings = true;
                                break;
                            }
                        }

                        // If not found, add a new Rating entry for this image
                        if (!existsInRatings) {
                            Path filePath = Paths.get(imageDirectory, fileName);
                            File jpegFile = new File(filePath.toString());
                            String date = "N/A";
                            try {

                                Metadata metadata = JpegMetadataReader.readMetadata(file);
                                ExifSubIFDDirectory directory
                                        = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                                Date rawDate
                                        = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                                date = rawDate.toString();
                            } catch(Exception e){
                                System.out.println("Exception");
                            }
                            Rating newRating = new Rating();
                            newRating.value = -1;  // Default value is -1
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

    public void save() {
        Path csvName = makeCSVName();
        try {
            BufferedWriter writer = Files.newBufferedWriter(csvName);
            for (Rating rating : Ratings) {
                writer.write(rating.value + "," + rating.name + "," + rating.date);
                writer.newLine();
            }
            writer.close();

        } catch(Exception e) {}
    }
    public int countUnrated() {
        int unrated = 0;
        for (Rating rating : Ratings) {
            if (rating.value == -1) {
                unrated++;
            }
        }
        return unrated;
    }
    public String randomUnrated() throws Exception {
        int unrated = countUnrated();
        int choice = (int) (Math.random() * unrated);
        for (Rating rating : Ratings) {
            if (rating.value == -1) {
                if (choice == 0) {
                    return rating.name;

                }
                choice--;
            }
        }
        throw new Exception("No Image Found");
    }

    public void rateImage(String imageName, int value) {
        for (Rating rating : Ratings) {
            if (rating.name == imageName) {
                rating.value = value;
            }
        }
    }
}