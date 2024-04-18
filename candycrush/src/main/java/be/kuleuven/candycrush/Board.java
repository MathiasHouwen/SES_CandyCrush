package be.kuleuven.candycrush;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Function;
public class Board<E> {
    private ArrayList<E> cells;
    private BoardSize boardSize;
    private CandycrushModel model;

    public Board(BoardSize boardSize) {
        this.boardSize = boardSize;
        cells = new ArrayList<>(9);
    }
    public BoardSize getBoardSize() {
        return boardSize;
    }

    public E getCellAt(Position position){
        return cells.get(position.getIndex());
    }

    public void replaceCellAt(Position position, E newCell){
        cells.set(position.getIndex(), newCell);
    }

    public void fill(Function<Position, E> cellCreator){
        for (int i = 0; i  <boardSize.kolommen() *  boardSize.rijen(); i++) {
            cells.add(cellCreator.apply(Position.fromIndex(i, boardSize)));
        }
    }

    /*public void copyTo(Board<E> otherBoard){
        if(!(boardSize.equals(otherBoard.getBoardSize()))){
            throw new RuntimeException("Boards not same size");
        }
        for (int i = 0; i <boardSize.kolommen() *  boardSize.rijen(); i++){
            otherBoard.cells[i] = this.cells[i];
        }
    }*/
}
