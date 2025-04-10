package com.example.image_sorter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Controller class for handling user interactions with the image rating application
public class Controller {

    // Model containing the data for the application
    private DataModel model;

    // Directory where images are stored
    private String imageDirectory;

    // Name of the currently selected image
    private String currentImage;

    // Placeholder image for when no photos are available
    private Image nophotos;

    // ImageView component for displaying images
    @FXML
    private ImageView imagePicture;

    // TextField for user input of ratings
    @FXML
    private TextField Rating;

    // Label to display the number of unrated images
    @FXML
    private Label unrated;

    // Constructor
    public Controller() {
        this.imageDirectory = "./Data";
        this.model = new DataModel(imageDirectory);
    }

    // Converts the name of an image to its full file path
    private String makeImagePath(String imageName) {
        Path filePath = Paths.get(imageDirectory, imageName);
        return filePath.toString();
    }

    // Initializes the controller and selects the first image to display
    public void initialize() {
        selectImage();
    }

    // Selects a random unrated image to display
    private void selectImage() {
        setCount(); // Update the count of unrated images
        try {
            currentImage = this.model.randomUnrated(); // Get a random unrated image
            Image temporaryImage = new Image(new FileInputStream(makeImagePath(currentImage)));
            imagePicture.setImage(temporaryImage);
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            imagePicture.setImage(null); // Set to a placeholder image if an error occurs
        }
    }

    // Handles the submission of a rating for the current image
    @FXML
    protected void onSubmitButtonClick() {
        this.model.rateImage(currentImage, Integer.valueOf(Rating.getText())); // Update the rating
        Rating.clear(); // Clear the text field
        selectImage(); // Select the next unrated image
    }

    // Updates the label with the count of unrated images
    private void setCount() {
        int count = this.model.countUnrated();
        unrated.setText(String.valueOf(count));
    }

    // Ensures the rating input is valid
    @FXML
    protected void checkNumber() {
        if (Rating.getText().matches("[0-9]+") &&
                Rating.getText().length() < 2) {
            // Valid input, do nothing
        } else {
            Rating.clear(); // Clear invalid input
        }
    }

    // Saves the current ratings to the CSV file
    @FXML
    protected void onSaveButtonClick() {
        this.model.save();
    }
}
