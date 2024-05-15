package be.kuleuven.candycrush;

public record Solution(int score, Board<Candy> board) {
    public Solution {
        if(score<0) throw new IllegalArgumentException("score > 0 moet >:(");
    }
}
