package com.c57lee.mazesolver;

import com.c57lee.mazesolver.userinterface.MazeUserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new MazeUserInterface(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}