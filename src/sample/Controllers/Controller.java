package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import sample.GameBuilder.Builder;
import sample.GameBuilder.Solver;
import sample.GameBuilder.SolverStep;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Controller {

    // Board values and solution
    private int[][] board;
    private int[][] solution;

    @FXML
    GridPane grid = new GridPane();
    @FXML
    Button solveButton = new Button("Solve Puzzle");
    @FXML
    Button checkButton = new Button("Check Solution");
    @FXML
    Button solutionButton = new Button("Show Solution");
    @FXML
    BorderPane borderPane = new BorderPane();
    @FXML
    TilePane tile = new TilePane();
    @FXML
    MenuBar menuBar = new MenuBar();

    Builder game = new Builder();

    public BorderPane buildBoard() {

        // ------------ Build MenuBar --------------
        Menu menuFile = new Menu("Menu");
        MenuItem newPuzzle = new MenuItem("New Game");
        MenuItem quit = new MenuItem("Quit");
        menuFile.getItems().addAll(newPuzzle, quit);
        newPuzzle.setOnAction(e -> newGame());
        quit.setOnAction( e -> handleExit());

        menuBar.getMenus().add(menuFile);
        // ------------ Build MenuBar --------------

        // ------- Build TilePane + Buttons -------
        solveButton.getStyleClass().add("button");
        checkButton.getStyleClass().add("button");
        checkButton.setOnAction(e -> checkSolution());
        solveButton.setOnAction(e -> solvePuzzle());
        solutionButton.setOnAction(e -> showSolution());

        tile.getStyleClass().add("tile-pane");
        tile.getChildren().addAll(solveButton, checkButton, solutionButton);
        // ------- Build TilePane + Buttons -------

        // --- Generate puzzle and solution ---
        generatePuzzle();
        // --- Generate puzzle and solution ---

        // --- Generate textfield for board
        //loop through rows in puzzle board
        for (int i = 0; i < 9; i++) {
            //loop through columns in puzzle board
            for (int j = 0; j < 9; j++) {
                TextField textField = new TextField();
                //setting stylesheets for generating sub-grid borders in the grid
                if (j == 2 || j == 5) {
                    textField.setId("rightsubgrid");
                }
                if (i == 2 || i == 5) {
                    textField.setId("bottomsubgrid");
                }
                if ((i == 2 || i == 5) && (j == 2 || j == 5)) {
                    textField.setId("cornersubgrid");
                }

                //adding listener to textfields to warn user of of invalid entries
                //if user tries to enter a value > 9 or < 1, or tries to enter text instead of an int
                //turn text red and add tooltext to explain issue if cell is hovered over
                textField.textProperty().addListener((observableValue, s, t1) -> {
                    int value = 0;
                    if (!t1.equals("")) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText("Must enter a number between 1-9");
                        try{
                            value = Integer.parseInt(t1);
                        }catch(NumberFormatException e){
                            textField.setStyle("-fx-text-fill: red");
                            textField.setTooltip(tooltip);
                        }
                        if(value > 9 || value < 1){
                            textField.setStyle("-fx-text-fill: red");
                            textField.setTooltip(tooltip);
                        }else{
                            //else statement to ensure any cell with a valid entry does not show red or have a tooltip
                            textField.setStyle("-fx-text-fill: black");
                            textField.setTooltip(null);
                        }
                    }
                });
                grid.getChildren().add(textField);
                GridPane.setConstraints(textField, j, i);
            }
        }
        populateBoard();

        // ------- Put together BorderPane -------
        borderPane.getStyleClass().add("pane");

        borderPane.setTop(menuBar);
        borderPane.setBottom(tile);
        borderPane.setCenter(grid);
        // ------- Put together BorderPane -------

        return borderPane;
    }

    //create new puzzle and puzzle's solution from GameBuilder classes
    public void generatePuzzle() {
        solution = game.getPuzzleSolution();
        board = game.getPuzzle();
    }

    //add numbers from board array to the display
    private void populateBoard() {
        //loop through rows in puzzle board
        for (int i = 0; i < 9; i++) {
            //loop through columns in puzzle board
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    //if this index of the array has a value, set value to that textfield
                    //and disable user's from being able to edit that text field
                    getGridCell(i, j).setText(Integer.toString(board[i][j]));
                    getGridCell(i,j).setEditable(false);
                }
                else{
                    //if this index of the array has value of 0, set textfield to empty string
                    //and enable user's to edit that textfield
                    getGridCell(i, j).setText("");
                    getGridCell(i,j).setEditable(true);
                }
            }
        }
    }

    @FXML
    //method to check the user's entered values in the board
    private void checkSolution() {
        boolean solved = true;
        TextField currentField;
        //loop through rows
        for (int i = 0; i < solution.length; i++) {
            //loop through columns
            for (int j = 0; j < solution.length; j++) {
                //get current textfield for this location on the board
                currentField = getGridCell(i, j);
                //make sure the current field is not null
                assert currentField != null;
                try {
                    //if there is a blank space, then puzzle is not solved
                    if (currentField.getText().equals("")) {
                        solved = false;
                        //if there is a value on the board that is not the same as the stored value of the solution,
                        //then the puzzle is not solved
                    } else if (solution[i][j] != Integer.parseInt(currentField.getText())) {
                        solved = false;
                        //highlight cell text to indicate incorrect value in that cell
                        currentField.setStyle("-fx-text-fill: red");
                    }
                    //catch in case user entered text instead of an int
                    //will open an alert window to indivate the invalid value
                }catch(NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.NONE, "Invliad Entry", ButtonType.OK);
                    alert.showAndWait();
                }

            }
        }
        //if neither of the previous if statements were entered, then the puzzle is solved
        if(solved) {
            String s = "You solved the puzzle!\nNew game?";
            //new alert to allow user to start a new game
            Alert alert = new Alert(Alert.AlertType.NONE, s, ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Congratulations!");
            Optional<ButtonType> result = alert.showAndWait();
            //if user hits OK button, then generate a new puzzle and repopulate board with new puzzle
            if(result.isPresent() && (result.get() == ButtonType.OK)) {
                newGame();
            }
        }
    }


    @FXML
    //shows the algorithm in action
    //when new puzzle is generated, the steps to solve that puzzle are stored in an ArrayList of Solver Steps
    //This method will loop through the SolverSteps and display the values as they are set by the algorithm in real time
    //activated by pressing the 'Solve Puzzle' button
    private void solvePuzzle() {
        //disable other buttons so they do not conflict with algorithm running
        checkButton.setOnAction(null);
        solveButton.setOnAction(null);
        Solver solver = new Solver();
        //creates new solution based on the puzzle board generated at the beginning
        //needed as the original solution is based on a different board
        solver.solveSudoku(board);
        ArrayList<SolverStep> steps = solver.getSolverSteps();
        //starts new thread in in setNewValue method
        setNewValue(steps).start();
    }

    public Thread setNewValue(ArrayList<SolverStep> steps){
        //return new thread
        return new Thread(() -> {
            //loop through each of the steps to solve the puzzle
            for (SolverStep step : steps) {
                //get the correct grid cell
                for (Node cell : grid.getChildren()) {
                    if ((cell instanceof TextField) && (GridPane.getColumnIndex(cell) == step.getCol()) && (GridPane.getRowIndex(cell) == step.getRow())) {
                        //display the value from each step in real time on the scene
                        if(step.getValue().equals(Integer.toString(0))){
                            Platform.runLater(() -> ((TextField) cell).setText(""));
                        }else{
                            Platform.runLater(() -> ((TextField) cell).setText(step.getValue()));
                        }
                        break;
                    }
                }
                try{
                    //wait for .2 seconds so the algorithm doesn't run too fast to see
                    Thread.sleep(200);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

        });
    }

    @FXML
    //displays solution on the board
    private void showSolution(){
        checkButton.setOnAction(null);
        solveButton.setOnAction(null);
        for(int i = 0; i < solution.length; i++){
            for(int j = 0; j < solution.length; j++){
                Objects.requireNonNull(getGridCell(i, j)).setText(Integer.toString(solution[i][j]));
            }
        }
    }

    //method to get the corresponding grid cell based on the row / column in the board
    private TextField getGridCell(int i, int j){
        for(Node cell : grid.getChildren()){
            if((cell instanceof TextField) && (GridPane.getColumnIndex(cell) == j) && (GridPane.getRowIndex(cell) == i)){
                return (TextField) cell;
            }
        }
        return null;
    }

    @FXML
    //generates new game and repopulates the board
    private void newGame(){
        //re-enables buttons in case they were disabled with an earlier method
        checkButton.setOnAction(e -> checkSolution());
        solveButton.setOnAction(e -> showSolution());
        //generate new puzzle and solution
        generatePuzzle();
        //print new puzzle on the board
        populateBoard();
    }

    @FXML
    //exit scene if user goes to user -> exit
    public void handleExit(){
        Platform.exit();
    }

}
