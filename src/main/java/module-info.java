module com.example.image_sorter {
    requires javafx.controls;
    requires javafx.fxml;
    requires metadata.extractor;


    opens com.example.image_sorter to javafx.fxml;
    exports com.example.image_sorter;
}