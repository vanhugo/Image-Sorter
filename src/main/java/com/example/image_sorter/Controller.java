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

    @FXML
    private Label Choose;

    @FXML

    private ImageView imagePicture;
    private Image currentImage;

    @FXML
    private TextField Rating;

    public Controller() {
        this.imageDirectory = "./Data";
        this.model = new DataModel(imageDirectory);
    }

    private String makeImagePath(String imageName) {
        Path filePath = Paths.get(imageDirectory, imageName);
        return filePath.toString();
    }

    public void initialize() {
        // Load the images during initialization
        try {
            String apple = this.model.Ratings.get(0).name;
            currentImage = new Image(new FileInputStream(makeImagePath(apple)));
            imagePicture.setImage(currentImage);
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }


    @FXML
    protected void onSubmitButtonClick() {
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
