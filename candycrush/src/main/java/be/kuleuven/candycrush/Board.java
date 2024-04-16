package be.kuleuven.candycrush;

import java.util.function.Function;
public class Board<E> {
    private E[] cells;
    private BoardSize boardSize;
    private CandycrushModel model;

    public BoardSize getBoardSize() {
        return boardSize;
    }

    public E getCellAt(Position position){
        return cells[position.getIndex()];
    }

    public void replaceCellAt(Position position, E newCell){
        cells[position.getIndex()] = newCell;
    }

    public void fill(Function<Position, E> cellCreator){
        for (int i = 0; i <boardSize.kolommen() *  boardSize.rijen(); i++) {
            cells[i] = cellCreator.apply(Position.fromIndex(i, boardSize));
        }
    }

    public void copyTo(Board<E> otherBoard){
        if(!(boardSize.equals(otherBoard.getBoardSize()))){
            throw new RuntimeException("Boards not same size");
        }
        for (int i = 0; i <boardSize.kolommen() *  boardSize.rijen(); i++){
            otherBoard.cells[i] = this.cells[i];
        }
    }
}
