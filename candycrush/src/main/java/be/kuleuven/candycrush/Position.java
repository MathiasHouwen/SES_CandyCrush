package be.kuleuven.candycrush;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

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

        // Mogelijke buren: links, rechts, boven, onder, ...
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};    // direct
                              //{-1, -1}, {-1, 1}, {1, -1}, {1, 1}}; // diagonalen

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

    public boolean isNeighbor(Position pos){
        List<Position> neighbors = (List<Position>) this.neighborPositions();
        return neighbors.contains(pos);
    }

    public boolean isLastColumn(){
        return kolom == boardSize().kolommen() - 1;
    }

    public Stream<Position> walkLeft(){
        return this.boardSize.positions().stream()
                .filter(p -> p.rij() == this.rij())
                .filter(p -> p.kolom() <= this.kolom())
                .sorted(Comparator.comparingInt(Position::kolom).reversed());
    }
    public Stream<Position> walkRight(){
        return this.boardSize.positions().stream()
                .filter(p -> p.rij() == this.rij())
                .filter(p -> p.kolom() >= this.kolom())
                .sorted(Comparator.comparingInt(Position::kolom));
    }
    public Stream<Position> walkUp(){
        return this.boardSize.positions().stream()
                .filter(p -> p.kolom() == this.kolom())
                .filter(p -> p.rij() <= this.rij())
                .sorted(Comparator.comparingInt(Position::rij).reversed());
    }
    public Stream<Position> walkDown(){
        return this.boardSize.positions().stream()
                .filter(p -> p.kolom() == this.kolom())
                .filter(p -> p.rij() >= this.rij())
                .sorted(Comparator.comparingInt(Position::rij));
    }
}
