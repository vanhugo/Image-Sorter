module com.example.image_sorter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.image_sorter to javafx.fxml;
    exports com.example.image_sorter;
}