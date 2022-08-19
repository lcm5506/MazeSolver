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

        ArrayList<Integer> temp = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));
        System.out.print("temp: ");
        temp.forEach(i->System.out.print(i+" "));
        System.out.println(" ");
        List<Integer> sub = new ArrayList<>(temp.subList(2,5));
        System.out.print("sub: ");
        sub.forEach(i->System.out.print(i+" "));
        System.out.println(" ");
        temp.clear();
        System.out.print("sub after clear: "+sub.size());
        System.out.println(" ");
        launch();
    }
}