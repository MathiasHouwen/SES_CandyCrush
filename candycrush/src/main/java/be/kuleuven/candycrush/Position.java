package be.kuleuven.candycrush;

import java.util.ArrayList;

public record Position(int rij, int kolom, BoardSize boardSize) {
    public Position {
        if(rij <0 || rij > boardSize.rijen()-1) throw new IllegalArgumentException("Rij moet groter dan nul zijn en lager dan het aantal rijen van de boardsize");
        if(kolom <0 || kolom > boardSize.kolommen()-1) throw new IllegalArgumentException("Rij moet groter dan nul zijn en lager dan het aantal rijen van de boardsize");
    }

    public int getIndex() {
        return kolom + rij * boardSize.kolommen();
    }

    public static Position fromIndex(int index, BoardSize size){
        if(index >= size.kolommen()*size.rijen()) throw new IllegalArgumentException("Index out of bounds");

        int rij = index / size.kolommen();
        int kolom = index % size.kolommen();

        return new Position(rij, kolom, size);
    }

    public static void main(String[] args) {
        BoardSize size = new BoardSize(4, 3);
        Position linkseHoek = new Position(0, 0, size);
        ArrayList<Position> neighbors = (ArrayList<Position>) linkseHoek.neighborPositions();
        ArrayList<Position> test = new ArrayList<>();
        test.add(new Position(1,2, size));
        test.add(new Position(0, 1, size));
        assert (neighbors.equals(test));
    }

    private boolean isValidPosition(int newRow, int newCol) {
        try {
            Position testPos = new Position(newRow, newCol, boardSize);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public Iterable<Position> neighborPositions(){
        ArrayList<Position> neighbors = new ArrayList<>();

        // Mogelijke buren: links, rechts, boven, onder
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = rij + dir[0];
            int newCol = kolom + dir[1];

            // Controleren of de nieuwe positie binnen het grid ligt
            if (isValidPosition(newRow, newCol)) {
                neighbors.add(new Position(newRow, newCol, boardSize));
            }
        }

        return neighbors;
    }

    public boolean isLastColumn(){
        return kolom == boardSize().kolommen() - 1;
    }
}
