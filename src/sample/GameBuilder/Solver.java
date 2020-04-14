package sample.GameBuilder;

import java.util.ArrayList;

public class Solver {

    private ArrayList<SolverStep> solverSteps = new ArrayList<>();

    public static boolean isSafe(int[][] board, int row, int col, int num){
        //check if row has conflict
        for(int i = 0; i < board.length; i++){
            if(board[row][i] == num){
                return false;
            }
        }

        //check if column has conflict
        for (int[] ints : board) {
            if (ints[col] == num) {
                return false;
            }
        }
        //check if grid has conflict
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        for(int i = rowStart ; i < rowStart + 3; i++){
            for(int j = colStart ; j < colStart + 3; j++){
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solveSudoku(int[][] board){
        boolean isEmpty = false;
        int row = 0;
        int col = 0;

        //loop through all of the rows
        for(int i = 0; i < board.length; i++){
            //loop through all the columns
            for(int j = 0; j < board.length; j++){
                //if answer hasn't been set for that location
                if(board[i][j] == 0){
                    isEmpty = true;
                    row = i;
                    col = j;
                    break;
                }
            }
            if(isEmpty) break;
        }

        //if no more empty cells in the grid
        if(!isEmpty){
            //puzzle is solved
            return true;
        }else {
            for (int i = 1; i <= board.length; i++) {
                //if current value does not cause any conflict
                solverSteps.add(new SolverStep(row, col, i));
                if (isSafe(board, row, col, i)) {
                    //assign value to the board
                    board[row][col] = i;
                    //recursively call solveSudoku with updated board
                    //will return true if all spaces filled in and no conflict
                    if (solveSudoku(board)) {
                        return true;
                    } else {
                        //set to 0 so if it has to return to previous call as false
                        //this space will still show as blank
                        board[row][col] = 0;
                        solverSteps.add(new SolverStep(row, col, 0));
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<SolverStep> getSolverSteps(){
        return this.solverSteps;
    }

}
