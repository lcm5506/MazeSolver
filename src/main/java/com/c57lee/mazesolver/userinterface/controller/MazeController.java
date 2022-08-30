package com.c57lee.mazesolver.userinterface.controller;

import com.c57lee.mazesolver.generation.DepthFirst;
import com.c57lee.mazesolver.generation.GenerationMethod;
import com.c57lee.mazesolver.generation.Kruskal;
import com.c57lee.mazesolver.util.TaskFinishedHandler;
import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.pathfind.*;
import com.c57lee.mazesolver.userinterface.MazeUserInterface;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MazeController {

    private GenerationMethod genLogic;
    private PathFind pfLogic;
    private MazeUserInterface ui;

    // final Strings for Generation Methods
    public static final String GEN_DEPTH_FIRST = "Depth First";
    public static final String GEN_KRUSKAL = "Kruskal's Algorithm";
    public static final String GEN_PRIM = "Prim's Algorithm";

    // final Strings for Path Find Methods
    public static final String PF_DEPTH_FIRST = "Depth First";
    public static final String PF_BREADTH_FIRST = "Breadth First";
    public static final String PF_DIJKSTRA = "Dijkstra";
    public static final String PF_ASTAR = "A*";
    public static final int INITIAL_HORIZONTAL_CELL_NUM = 50;
    public static final int INITIAL_VERTICAL_CELL_NUM = 50;
    public static final int INITIAL_SLEEP_DURATION = 20;    //This is in millisecond.
    private static final Background BACKGROUND_PATH_FOUND = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_PATH_ALL_FOUND = new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_CELL_SEARCHED = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_CELL_UNVISITED = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY,Insets.EMPTY));
    private static final Background BACKGROUND_CELL_VISITED = new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY,Insets.EMPTY));
    public enum State{
        PREBUILD,
        BUILDING,
        BUILT,
        PATHFINDING,
        FOUND
    }
    public State controllerState = State.PREBUILD;



    private List<Cell> pathConsidered;

    public MazeController(MazeUserInterface ui){
        this.ui = ui;
        pathConsidered = new ArrayList<>();
        genLogic = new DepthFirst(INITIAL_HORIZONTAL_CELL_NUM,INITIAL_VERTICAL_CELL_NUM); // default logic place holder
    }

    public void resetGen(){
        genLogic.reset();
        pathConsidered.clear();
        controllerState = State.PREBUILD;
        ui.enableGenUIControls();
        ui.disablePfUIControls();
    }

    public void resetPf(){
        pfLogic.reset();
        controllerState = State.BUILT;
    }

    public void updateCell(Cell cell){
        double top,bottom,left,right;
        top = bottom = left = right = 1.0;
        if (cell.isVisited())
            ui.setBackgroundForPane(cell.getX(), cell.getY(), BACKGROUND_CELL_VISITED);
        else
            ui.setBackgroundForPane(cell.getX(), cell.getY(), BACKGROUND_CELL_UNVISITED);
        if (cell.isTopOpen())
            top = 0;
        if (cell.isBottomOpen())
            bottom = 0;
        if (cell.isLeftOpen())
            left = 0;
        if (cell.isRightOpen())
            right = 0;
        ui.setBorderForPane(cell.getX(), cell.getY(), new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(top,right,bottom,left))));
    }

    public void startMazeGenTask(){
        if (genLogic == null)
            setGenLogic(ui.getHorzCellNum(), ui.getVertCellNum(), GEN_DEPTH_FIRST);
        onMazeGenTaskFinished();
        this.genLogic.startMazeGenTask();
        controllerState = State.BUILDING;
        ui.disablePfUIControls();
        ui.disableGenUIControls();
    }

    public void onMazeGenTaskFinished(){
        genLogic.setTaskFinishedHandler(new TaskFinishedHandler() {
            @Override
            public void onTaskFinished() {
                controllerState = State.BUILT;
                // Enable the UI
                ui.enableGenUIControls();
                ui.enablePfUIControls();
            }
        });
    }

    public void onPfTaskFinished(){
        pfLogic.setTaskFinishedHandler(new TaskFinishedHandler() {
            @Override
            public void onTaskFinished() {
                controllerState = State.FOUND;
                // Enable the UI
                ui.enableGenUIControls();
                ui.enablePfUIControls();
            }
        });
    }
    public void setGenLogic(int width, int height, String method){
        GenerationMethod selectedLogic;

        switch (method){
            case GEN_DEPTH_FIRST -> selectedLogic = new DepthFirst(width, height);
            case GEN_KRUSKAL -> selectedLogic = new Kruskal(width, height);
            case GEN_PRIM -> selectedLogic = new DepthFirst(width, height); // placeholder
            default -> selectedLogic = new DepthFirst(width, height);
        }

        selectedLogic.setController(this);
        selectedLogic.setSleepDuration(INITIAL_SLEEP_DURATION);
        this.genLogic = selectedLogic;
    }

    public void startPathFindTask(){
        if (pfLogic == null)
            setPathFindLogic(PF_DEPTH_FIRST);
        resetPf();
        resetMazeGridForPf();
        pfLogic.setStart(0,0);
        pfLogic.setFinish(pfLogic.getMazeWidth()-1, pfLogic.getMazeHeight()-1);
        onPfTaskFinished();
        pfLogic.findPath();
        controllerState = MazeController.State.PATHFINDING;
        ui.disablePfUIControls();
        ui.disableGenUIControls();

    }

    public void setPathFindLogic(String method){
        PathFind pfLogic;
        switch (method){
            case PF_DEPTH_FIRST -> pfLogic = new DepthFirstSearch(genLogic.getMaze());
            case PF_BREADTH_FIRST -> pfLogic = new BreadthFirstSearch(genLogic.getMaze());
            case PF_DIJKSTRA -> pfLogic = new Dijkstra(genLogic.getMaze());
            case PF_ASTAR -> pfLogic = new Astar(genLogic.getMaze());
            default -> pfLogic = new DepthFirstSearch(genLogic.getMaze());
        }

        pfLogic.setController(this);
        pfLogic.setSleepDuration(INITIAL_SLEEP_DURATION);
        this.pfLogic = pfLogic;

    }

    public void resetMazeGridForPf(){
        int width = ui.getHorzCellNum();
        int height = ui.getVertCellNum();

        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                Pane pane = ui.getPaneFor(i,j);
                ui.setBackgroundForPane(i,j,BACKGROUND_CELL_VISITED);
            }
        }
    }
    public void setGenSleepDuration(int duration){
        if (genLogic != null)
            genLogic.setSleepDuration(duration);
    }

    public void setPfSleepDuration(int duration){
        if (pfLogic != null)
            pfLogic.setSleepDuration(duration);
    }

    public void setPathFound(List<Cell> pathFound){
        if (pathFound == null){
            // no path found.
        } else {
            for (Cell c : pathFound) {
                ui.setBackgroundForPane(c.getX(), c.getY(), BACKGROUND_PATH_FOUND);
            }
        }
    }

    public void setAllPathFound(List<Cell> pathFound){
        if (pathFound == null){
            // no path found.
        } else {
            for (Cell c : pathFound) {
                ui.setBackgroundForPane(c.getX(), c.getY(), BACKGROUND_PATH_ALL_FOUND);
            }
        }
    }

    public void setCellSearched(Cell cell){
        ui.setBackgroundForPane(cell.getX(), cell.getY(), BACKGROUND_CELL_SEARCHED);
    }

    public boolean isGenFinished(){
        return genLogic.isGenFinished();
    }



}
