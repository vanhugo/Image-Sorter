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
    // requires: none
    // modifies: this
    // effects: initializes the controller with the default image directory and creates a DataModel instance
    public Controller() {
        this.imageDirectory = "./Data";
        this.model = new DataModel(imageDirectory);
    }

    // Converts the name of an image to its full file path
    // requires: imageName is a valid file name
    // modifies: none
    // effects: returns the absolute path to the given image in the directory
    private String makeImagePath(String imageName) {
        Path filePath = Paths.get(imageDirectory, imageName);
        return filePath.toString();
    }

    // Initializes the controller and selects the first image to display
    // requires: none
    // modifies: this
    // effects: sets up the view with a random unrated image, or a placeholder if none are available
    public void initialize() {
        selectImage();
    }

    // Selects a random unrated image to display
    // requires: none
    // modifies: currentImage, imagePicture
    // effects: updates the view with a random unrated image, or sets a placeholder image if no images are unrated
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
    // requires: Rating text field contains a valid integer between 0 and 9
    // modifies: model, Rating, currentImage
    // effects: updates the rating for the current image and selects a new unrated image
    @FXML
    protected void onSubmitButtonClick() {
        this.model.rateImage(currentImage, Integer.valueOf(Rating.getText())); // Update the rating
        Rating.clear(); // Clear the text field
        selectImage(); // Select the next unrated image
    }

    // Updates the label with the count of unrated images
    // requires: none
    // modifies: unrated
    // effects: sets the text of the unrated label to the number of unrated images in the model
    private void setCount() {
        int count = this.model.countUnrated();
        unrated.setText(String.valueOf(count));
    }

    // Ensures the rating input is valid
    // requires: Rating text field contains a valid integer
    // modifies: Rating
    // effects: clears the text field if the input is invalid
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
    // requires: none
    // modifies: model
    // effects: writes all ratings to a CSV file in the directory
    @FXML
    protected void onSaveButtonClick() {
        this.model.save();
    }
}
