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

public class Controller {
    private DataModel model;

    private String imageDirectory;

    private String currentImage;

    private Image nophotos;
    @FXML
    private ImageView imagePicture;

    @FXML
    private TextField Rating;

    @FXML
    private Label unrated;

    public Controller() {
        this.imageDirectory = "./Data";
        this.model = new DataModel(imageDirectory);
    }

    private String makeImagePath(String imageName) {
        Path filePath = Paths.get(imageDirectory, imageName);
        return filePath.toString();
    }
    public void initialize() {
        selectImage();
    }

    private void selectImage() {
        setCount();
        // Load the images during initialization
        try {
            currentImage = this.model.randomUnrated();
            Image temporaryImage = new Image(new FileInputStream(makeImagePath(currentImage)));
            imagePicture.setImage(temporaryImage);
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            imagePicture.setImage(null);
        }
    }


    @FXML
    protected void onSubmitButtonClick() {
        this.model.rateImage(currentImage, Integer.valueOf(Rating.getText()));
        Rating.clear();
        selectImage();
    }
    private void setCount() {
        int count = this.model.countUnrated();
        unrated.setText(String.valueOf(count));
    }


    @FXML
    protected void checkNumber() {
        if (Rating.getText().matches("[0-9]+") &&
            Rating.getText().length() < 2)
        {}
        else Rating.clear();
    }

    @FXML
    protected void onSaveButtonClick() {
        this.model.save();
    }

}
