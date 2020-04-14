package sample.GameBuilder;

public class Builder {

    private int[][] puzzleSolution;
    private int[][] puzzle;
    Solver solver = new Solver();

    public int[][] getPuzzleSolution(){
        puzzleSolution = generateSolution();
        return puzzleSolution;
    }

    public int[][] getPuzzle(){
        if(puzzleSolution != null){
            puzzle = generatePuzzle(puzzleSolution);
            return puzzle;
        }
        else{
            System.out.println("Puzzle solution hasn't been generated yet");
            return null;
        }
    }

    private int[][] generateSolution(){
        int[][] board = new int[9][9];
        int row = 0; int col = 0;
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for(int z = 0; z < 3; z++) {
            for (int i = row; i < row + 3; i++) {
                for (int j = col; j < col + 3; j++) {
                    int idx = (int) (Math.random() * 9);
                    if(values[idx] == 0){
                        while(values[idx] == 0){
                            idx = (int) (Math.random() * 9);
                        }
                    }
                     board[i][j] = values[idx];
                     values[idx] = 0;
                }
            }
            row += 3;
            col += 3;
            for(int i = 1; i <= 9; i++){
                values[i-1] = i;
            }
        }
        if(solver.solveSudoku(board)){
            return board;
        }else{
            System.out.println("Board has no solution");
            return null;
        }
    }

    private int[][] generatePuzzle(int[][] solution){
        //initiate new board array of same size as the solution
        int[][] board = new int[solution.length][solution.length];

        //loop through board rows
        for(int i = 0; i < board.length; i++){
            //loop through board columns
            for(int j = 0; j < board.length; j++){
                //only want to generate about 30% of the numbers on the board
                //if Math.random is higher than 0.3 (~70% of the time) then enter a 0
                //else add an actual number from the solution
                if(Math.random() > 0.3){
                    board[i][j] = 0;
                }else{
                    board[i][j] = solution[i][j];
                }
            }
        }
        return board;
    }

}
