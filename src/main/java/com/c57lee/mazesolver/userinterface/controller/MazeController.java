package com.c57lee.mazesolver.userinterface.controller;

import com.c57lee.mazesolver.generation.DepthFirst;
import com.c57lee.mazesolver.generation.GenerationMethod;
import com.c57lee.mazesolver.generation.Kruskal;
import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
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
    private int genSleepDuration = INITIAL_SLEEP_DURATION;
    private int pfSleepDuration = INITIAL_SLEEP_DURATION;
    private static final Background BACKGROUND_PATH_SEARCHING = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_PATH_STUCK = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_PATH_FOUND = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_PATH_ALL_FOUND = new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_CELL_SEARCHED = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BACKGROUND_CELL_UNVISITED = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY,Insets.EMPTY));
    private static final Background BACKGROUND_CELL_VISITED = new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY,Insets.EMPTY));
    // temp
    private List<Cell> previousNextCells;



    private List<Cell> pathConsidered;

    public MazeController(MazeUserInterface ui){
        this.ui = ui;
        pathConsidered = new ArrayList<>();
        genLogic = new DepthFirst(INITIAL_HORIZONTAL_CELL_NUM,INITIAL_VERTICAL_CELL_NUM); // default logic place holder

    }

    public void resetGen(){
        genLogic.reset();
        pathConsidered.clear();
    }

    public void resetPf(){
        pfLogic.reset();
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
        this.genLogic.startMazeGenTask();
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
        selectedLogic.setSleepDuration(genSleepDuration);
        this.genLogic = selectedLogic;
    }

    public void startPathFindTask(){
        if (pfLogic == null)
            setPathFindLogic(PF_DEPTH_FIRST);
        pfLogic.setStart(0,0);
        pfLogic.setFinish(pfLogic.getMazeWidth()-1, pfLogic.getMazeHeight()-1);
        System.out.println(pfLogic.getFinish());
        pfLogic.findPath();

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
        pfLogic.setSleepDuration(pfSleepDuration);
        this.pfLogic = pfLogic;

    }
    public void checkMaze(){
        Maze logicMaze = genLogic.getMaze();
        Maze uiMaze = createMazeFromUI();

        System.out.println("Dimension of logicMaze: "+logicMaze.getWidth()+","+logicMaze.getHeight());
        System.out.println("Dimension of uiMaze: "+uiMaze.getWidth()+","+uiMaze.getHeight());
        for (int i=0; i<logicMaze.getWidth(); i++){
            for(int j=0; j< logicMaze.getHeight();j++){
                boolean cellEquals = uiMaze.getCell(i,j).equals(logicMaze.getCell(i,j));
                if (!cellEquals)
                    System.out.println(i+","+j+": "+cellEquals);
            }
        }
    }

    public Maze createMazeFromUI(){
        int width = ui.getHorzCellNum();
        int height = ui.getVertCellNum();
        Maze maze = new Maze(width,height);
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                Pane pane = ui.getPaneFor(i,j);
                Insets inset = pane.getBorder().getInsets();
                boolean topOpen = inset.getTop() == 0;
                boolean bottomOpen = inset.getBottom() == 0;
                boolean leftOpen = inset.getLeft() == 0;
                boolean rightOpen = inset.getRight() ==0;
                boolean visited = pane.getBackground().getFills().get(0).getFill().equals(Color.WHITE);
                Cell cell = maze.getCell(i,j);
                cell.setTopOpen(topOpen);
                cell.setBottomOpen(bottomOpen);
                cell.setLeftOpen(leftOpen);
                cell.setRightOpen(rightOpen);
                cell.setVisited(visited);
            }
        }
        return maze;
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

    public void setPathConsidering(List<Cell> pathConsidering){
        for (Cell c:pathConsidering){
            ui.setBackgroundForPane(c.getX(), c.getY(), BACKGROUND_PATH_SEARCHING);
        }
        this.pathConsidered = pathConsidering;
    }
    public void setPathConsidered(){
        for (Cell c: pathConsidered){
            ui.setBackgroundForPane(c.getX(),c.getY(), BACKGROUND_PATH_STUCK);
        }
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


    // temp
    public void setPreviousNextCells(List<Cell> nextCells){
        if (previousNextCells != null){
            for (Cell c: previousNextCells){
                ui.setBackgroundForPane(c.getX(),c.getY(),BACKGROUND_CELL_SEARCHED);
            }
        }
        for (Cell c: nextCells){
            ui.setBackgroundForPane(c.getX(),c.getY(),BACKGROUND_PATH_SEARCHING);
        }
        previousNextCells = nextCells;

    }
}
