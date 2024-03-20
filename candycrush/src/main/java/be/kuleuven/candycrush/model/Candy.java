package be.kuleuven.candycrush.model;

public sealed interface Candy permits NormalCandy, Kropsla, Tomaat, Lente_ui, Erwt {

}

record NormalCandy(int colour) implements Candy{
    NormalCandy {
        if(colour < 0 || colour > 3) throw new IllegalArgumentException();
    }
}
record Kropsla() implements Candy{}
record Tomaat() implements Candy{}
record Lente_ui() implements Candy{}
record Erwt() implements Candy{}
