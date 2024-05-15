package be.kuleuven.candycrush;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
public class Board<E> {
    private Map<Position, E> cells;
    private Map<E, Position> positions;
    private BoardSize boardSize;

    public Board(BoardSize boardSize) {
        this.boardSize = boardSize;
        this.cells = new ConcurrentHashMap<>();
        this.positions = new ConcurrentHashMap<>();
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }

    public Map<Position, E> getCells(){
        return Collections.unmodifiableMap(cells);
    }

    public synchronized E getCellAt(Position position){
        return cells.get(position);
    }

    public synchronized void replaceCellAt(Position position, E newCell){
        cells.put(position, newCell);
        positions.put(newCell, position);
    }

    public void fill(Function<Position, E> cellCreator){
        for (int i = 0; i < boardSize.kolommen() * boardSize.rijen(); i++) {
            Position position = Position.fromIndex(i, boardSize);
            E cell = cellCreator.apply(position);

            replaceCellAt(position, cell);
        }
    }

    public void copyTo(Board<E> otherBoard){
        if(!(boardSize.equals(otherBoard.getBoardSize()))){
            throw new RuntimeException("Boards not same size");
        }
        for (Position position : cells.keySet()){
            otherBoard.replaceCellAt(position, this.cells.get(position));
        }
    }

    public List<E> getPositionsOfElement(E element){
        ArrayList<E> list = new ArrayList<>();
        for (E cell : positions.keySet()){
            if(element.equals(cell)){
                list.add(cell);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public void printBoard() {
        for (int row = 0; row < boardSize.rijen(); row++) {
            for (int col = 0; col < boardSize.kolommen(); col++) {
                Position position = new Position(row, col, boardSize);
                E cell = getCellAt(position);
                System.out.print(cell != null ? cell.toString() : " ");
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    @Override // Deze methode is gegenegeerd door Intellij
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board<?> board = (Board<?>) o;
        return Objects.equals(cells, board.cells) && Objects.equals(boardSize, board.boardSize);
    }
}
