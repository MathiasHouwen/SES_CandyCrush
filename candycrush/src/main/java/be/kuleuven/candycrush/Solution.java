package be.kuleuven.candycrush;

import java.util.List;

public record Solution(int score, Board<Candy> board, List<List<Position>> moves) {
    public Solution {
        if(score<0) throw new IllegalArgumentException("score moet > 0 >:(");
    }

    public void printSolution(){
        System.out.println(score);
        board.printBoard();

        for (List<Position> move : moves) {
            for (Position position : move) {
                System.out.print("(" + position.rij() + "," + position.kolom() + ") ");
            }
            System.out.println();
        }
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
