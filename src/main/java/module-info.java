module com.c57lee.mazesolver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.c57lee.mazesolver to javafx.fxml;
    exports com.c57lee.mazesolver;
}