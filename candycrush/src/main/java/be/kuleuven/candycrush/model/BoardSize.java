package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record BoardSize(int rijen, int kolommen) {
    public BoardSize {
        if (rijen <= 0) throw new IllegalArgumentException("rijen moet meer dan 0 zijn");
        if (kolommen <= 0) throw new IllegalArgumentException("kolommen moet meer dan 0 zijn");
    }

    public Iterable<Position> positions(){
        ArrayList<Position> result = new ArrayList<>();
        BoardSize size = new BoardSize(rijen, kolommen);
        for(int i = 0; i < rijen*kolommen; i++){
            result.add(Position.fromIndex(i, size));
        }
        return result;
    }
}

