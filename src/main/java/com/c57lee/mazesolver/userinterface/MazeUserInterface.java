package com.c57lee.mazesolver.userinterface;

import com.c57lee.mazesolver.userinterface.controller.MazeController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;

public class MazeUserInterface {
    Stage stage;
    Scene scene;
    BorderPane root;
    HashMap<String,Pane> paneHashMap;

    MazeController controller;

    public static final double GRID_WIDTH = 1000;
    public static final double GRID_HEIGHT = 1000;

    private int horzCellNum,vertCellNum;
    private String genMethod,pfMethod;

    public MazeUserInterface(Stage stage){
        this.paneHashMap = new HashMap<>();
        controller = new MazeController(this);
        this.stage = stage;
        stage.setTitle("Maze Generator");
        root = new BorderPane();
        horzCellNum = MazeController.INITIAL_HORIZONTAL_CELL_NUM;
        vertCellNum = MazeController.INITIAL_VERTICAL_CELL_NUM;
        drawTabPane();
        drawGrid();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void drawGrid(){
        GridPane grid = new GridPane();
        grid.setPrefSize(GRID_WIDTH,GRID_HEIGHT);
        paneHashMap.clear();
        grid.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderStroke.THIN)));
        double cellWidth = GRID_WIDTH/horzCellNum;
        double cellHeight = GRID_HEIGHT/vertCellNum;
        Background initialBg = new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY,Insets.EMPTY));
        for (int i=0; i<horzCellNum; i++){
            for (int j=0; j<vertCellNum; j++){
                Pane pane = new Pane();
                pane.setPrefSize(cellWidth,cellHeight);
                pane.setBackground(initialBg);
                paneHashMap.put(i+","+j,pane);
                grid.add(pane,i,j);
            }
        }
        root.setCenter(grid);
    }

    public void drawTabPane(){
        Tab genTab = new Tab("Generation", getGenToolBar());
        Tab pfTab = new Tab("PathFind", getPfToolBar());

        TabPane toolbarTabPane = new TabPane(genTab,pfTab);
        toolbarTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        root.setTop(toolbarTabPane);
    }
    public HBox getPfToolBar(){
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.resetPf();
                controller.resetMazeGridForPf();
            }
        });

        Button pathFindButton = new Button("Find Path");
        pathFindButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.setPathFindLogic(pfMethod);
                controller.startPathFindTask();
            }
        });

        ComboBox<String> methodComboBox = new ComboBox<>();
        methodComboBox.getItems().add(MazeController.PF_DEPTH_FIRST);
        methodComboBox.getItems().add(MazeController.PF_BREADTH_FIRST);
        methodComboBox.getItems().add(MazeController.PF_DIJKSTRA);
        methodComboBox.getItems().add(MazeController.PF_ASTAR);
        methodComboBox.getSelectionModel().selectFirst();
        pfMethod = methodComboBox.getSelectionModel().getSelectedItem();
        methodComboBox.setOnAction(e->{
            pfMethod = methodComboBox.getSelectionModel().getSelectedItem();
            System.out.println(pfMethod + " Selected!");
        });

        Label sleepDurationLabel = new Label("Animation Delay: ");
        Spinner<Integer> sleepDurationSpinner = new Spinner<>();
        sleepDurationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10,10000,MazeController.INITIAL_SLEEP_DURATION));
        sleepDurationSpinner.setPrefWidth(100);
        sleepDurationSpinner.valueProperty().addListener((observable, oldValue, newValue) -> controller.setPfSleepDuration(newValue));

        HBox pfToolbar = new HBox(clearButton,pathFindButton,methodComboBox,sleepDurationLabel,sleepDurationSpinner);
        pfToolbar.setSpacing(5.0);
        pfToolbar.setPadding(new Insets(1.0));
        pfToolbar.setAlignment(Pos.CENTER_LEFT);
        return pfToolbar;
    }
    public HBox getGenToolBar(){

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.resetGen();
                drawGrid();
            }
        });
        Button startButton = new Button("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.setGenLogic(horzCellNum,vertCellNum, genMethod);
                controller.startMazeGenTask();
            }
        });

        ComboBox<String> methodComboBox = new ComboBox<>();
        methodComboBox.getItems().add(MazeController.GEN_DEPTH_FIRST);
        methodComboBox.getItems().add(MazeController.GEN_KRUSKAL);
        methodComboBox.getItems().add(MazeController.GEN_PRIM);
        //methodComboBox.setPromptText("Select a generation method");
        methodComboBox.getSelectionModel().selectFirst();
        genMethod = (String)methodComboBox.getSelectionModel().getSelectedItem();
        methodComboBox.setOnAction(e->{
            genMethod = (String) methodComboBox.getSelectionModel().getSelectedItem();
        });

        Label horzCellNumLabel = new Label("H Length: ");

        Label vertCellNumLabel = new Label("V Length: ");
        Spinner<Integer> horzCellNumSpinner = new Spinner<>();
        Spinner<Integer> vertCellNumSpinner = new Spinner<>();
        horzCellNumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10,1000,MazeController.INITIAL_HORIZONTAL_CELL_NUM,10));
        vertCellNumSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10,1000,MazeController.INITIAL_VERTICAL_CELL_NUM,10));
        horzCellNumSpinner.setPrefWidth(100);
        vertCellNumSpinner.setPrefWidth(100);

        horzCellNumSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            horzCellNum = newValue;
            drawGrid();
        }));
        vertCellNumSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            vertCellNum = newValue;
            drawGrid();
        }));

        Label sleepDurationLabel = new Label("Animation Delay: ");
        Spinner<Integer> sleepDurationSpinner = new Spinner<>();
        sleepDurationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10,10000,MazeController.INITIAL_SLEEP_DURATION));
        sleepDurationSpinner.setPrefWidth(100);
        sleepDurationSpinner.valueProperty().addListener((observable, oldValue, newValue) -> controller.setGenSleepDuration(newValue));

        HBox genToolbar = new HBox(clearButton,startButton,methodComboBox,horzCellNumLabel,horzCellNumSpinner,vertCellNumLabel,vertCellNumSpinner,sleepDurationLabel,sleepDurationSpinner);
        genToolbar.setSpacing(5.0);
        genToolbar.setPadding(new Insets(1.0));
        genToolbar.setAlignment(Pos.CENTER_LEFT);
        return genToolbar;
    }

    public void setBackgroundForPane(int x, int y, Background background){
        paneHashMap.get(x+","+y).setBackground(background);
    }
    public void setBorderForPane(int x, int y, Border border){
        paneHashMap.get(x+","+y).setBorder(border);
    }

    public int getHorzCellNum(){
        return horzCellNum;
    }

    public int getVertCellNum(){
        return vertCellNum;
    }

    public Pane getPaneFor(int x, int y){
        return paneHashMap.get(x+","+y);
    }


}
