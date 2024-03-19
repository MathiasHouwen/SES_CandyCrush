package be.kuleuven.candycrush.model;

public record BoardSize(int rijen, int kolommen) {
    public BoardSize {
        if (rijen <= 0) throw new IllegalArgumentException("rijen moet meer dan 0 zijn");
        if (kolommen <= 0) throw new IllegalArgumentException("kolommen moet meer dan 0 zijn");
    }
}

