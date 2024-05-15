package be.kuleuven.candycrush;

public record Solution(int score, Board<Candy> board) {
    public Solution {
        if(score<0) throw new IllegalArgumentException("score > 0 moet >:(");
    }

    public void printSolution(){
        System.out.println(score);
        board.printBoard();
    }

    public int calculateScore(){
        /*return (int) boardSize.positions().stream()
                .filter(p-> board.getCellAt(p) instanceof noCandy)
                .count();*/
        return (int) board.getCells().values().stream()
                .filter(c -> c instanceof noCandy)
                .count();
    }
}
