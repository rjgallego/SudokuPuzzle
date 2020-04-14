package sample.GameBuilder;

public class SolverStep {
    private int row;
    private int col;
    private String value;

    public SolverStep(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = Integer.toString(value);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getValue() {
        return value;
    }
}
