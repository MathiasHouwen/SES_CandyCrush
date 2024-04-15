package be.kuleuven.candycrush;

public class Board<E> {
    private E[] cells;
    private BoardSize boardSize;
    private CandycrushModel model;

    public E getCellAt(Position position){
        return cells[position.getIndex()];
    }

    public void replaceCellAt(Position position, E newCell){
        cells[position.getIndex()] = newCell;
    }


}
