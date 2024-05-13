package be.kuleuven.candycrush;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CandycrushModel {
    private String speler;
    // private ArrayList<Candy> speelbord;
    private Board<Candy> candyBoard;
    private int score;
    private boolean gestart;
    private BoardSize boardSize;

    public CandycrushModel(String speler, int width, int height){
        this.speler = speler;
        boardSize = new BoardSize(height, width);
        candyBoard = new Board<>(boardSize);

        score = 0;
        gestart = false;

        Function<Position, Candy> candyCreator = position -> randomCandy();
        candyBoard.fill(candyCreator);
    }

    public CandycrushModel(String speler) {
        this(speler, 10, 10);
    }

    public String getSpeler() {
        return speler;
    }

    public BoardSize getBoardSize(){
        return boardSize;
    }

    public Board<Candy> getSpeelbord() {
        return candyBoard;
    }

    public int getWidth() {
        return boardSize.kolommen();
    }

    public int getHeight() {
        return boardSize.rijen();
    }

    public int getScore(){
        return this.score;
    }

    public boolean isGestart(){
        return this.gestart;
    }

    public void start(){
        this.gestart = true;
    }

    public void reset(){
        this.score =0;
        this.gestart = false;
    }

    public Candy randomCandy(){
        Random random = new Random();
        int randomGetal = random.nextInt(8);

        return switch (randomGetal) {
            case 4 -> new Erwt();
            case 5 -> new Kropsla();
            case 6 -> new Lente_ui();
            case 7 -> new Tomaat();
            default -> new NormalCandy(randomGetal);
        };
    }

    public void candyWithIndexSelected(Position position){
        Iterable<Position> Neighbours = getSameNeighbourPositions(position);

        for(Position Neighbour : Neighbours){
            candyBoard.replaceCellAt(Neighbour, randomCandy());
            score = score + 2;
        }
        candyBoard.replaceCellAt(position, randomCandy());
        score++;
    }

    Iterable<Position> getSameNeighbourPositions(Position position){
        Iterable<Position> neighbors = position.neighborPositions();
        ArrayList<Position> result = new ArrayList<>();

        for(Position neighbor : neighbors){
            Candy candy = candyBoard.getCellAt(position);
            Candy neighborCandy = candyBoard.getCellAt(neighbor);
            if(candy.equals(neighborCandy)){
                result.add(neighbor);
            }
        }

        return result;
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions){
        return positions
                .limit(2)
                .allMatch(p -> candyBoard.getCellAt(p).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions(){
        return boardSize.positions().stream()
                .filter(p -> {
                    Stream<Position> buren = p.walkLeft();
                    return !firstTwoHaveCandy(candyBoard.getCellAt(p), buren);
                });
    }

    public Stream<Position> verticalStartingPositions(){
        return boardSize.positions().stream()
                .filter(p -> {
                    Stream<Position> buren = p.walkUp();
                    return !firstTwoHaveCandy(candyBoard.getCellAt(p), buren);
                });
    }

    public List<Position> longestMatchToRight(Position pos){
        Stream<Position> walked = pos.walkRight();
        return walked
                .takeWhile(p -> getSpeelbord().getCellAt(p).equals(getSpeelbord().getCellAt(pos))
                            && getSpeelbord().getCellAt(p) != null && getSpeelbord().getCellAt(pos) != null)
                .toList();
    }

    public List<Position> longestMatchDown(Position pos){
        Stream<Position> walked = pos.walkDown();
        return walked
                .takeWhile(p -> getSpeelbord().getCellAt(p).equals(getSpeelbord().getCellAt(pos))
                        && getSpeelbord().getCellAt(p) != null && getSpeelbord().getCellAt(pos) != null)
                .toList();
    }

    public Set<List<Position>> findAllMatches(){
        List<List<Position>> matches = Stream.concat(horizontalStartingPositions(), verticalStartingPositions())
                .flatMap(p -> {
                    List<Position> horizontalMatch = longestMatchToRight(p);
                    List<Position> verticalMatch = longestMatchDown(p);
                    return Stream.of(horizontalMatch, verticalMatch);
                })
                .filter(m -> m.size() > 2)
                .sorted((match1, match2) -> match2.size() - match1.size())
                .toList();

        return matches.stream()
                .filter(match -> matches.stream()
                        .noneMatch(longerMatch -> longerMatch.size() > match.size() && new HashSet<>(longerMatch).containsAll(match)))
                .collect(Collectors.toSet());
    }

    public void clearMatch(List<Position> match){
        if(match.isEmpty()) return;
        Position first = match.getFirst();
        candyBoard.replaceCellAt(first, null); // ZOU NULL WERKEN OF HEEFT DEZE EEN EMPTY CANDY TYPE NODIG??
        match.removeFirst();
        clearMatch(match);
    }

    public void fallDownTo(Position pos){
        // ALS CUR POS EEN CANDY IS KAN DIE NIET VALLEN
        // -> DUS GA NAAR VOLGENDE
        // ALS POS GEEN CANDY HEEFT DAN KAN DE BOVENSTE VALLEN
        // -> WISSEL CUR POS MET DE POSITIE ER BOVEN
        // -> BLIJF DAT DOEN TOT HET EEN CANDY HEEFT

        // RETURN ALS U BOVENSTE POS EEN RIJ = 0 HEEFT
        // RETURN ALS ER EEN TIME OUT IS, gaat met return 1??
        try{
            Position boven = new Position(pos.rij() - 1, pos.kolom(), boardSize);
            if(candyBoard.getCellAt(pos) == null){
                candyBoard.replaceCellAt(pos, candyBoard.getCellAt(boven));
                fallDownTo(boven);

                // ALS DE CELL WAAR DIE ME VERPLAATS != NULL IS DO BOVENSTE
                // ANDERS IDK

            } else{
                fallDownTo(boven);
            }
        } catch (IllegalArgumentException ignored){
            return;
        }
    }

    public static void main(String[] args) {
        CandycrushModel model = new CandycrushModel("Speler", 3, 3);
        Board<Candy> board = model.getSpeelbord();

        List<Position> positionStream = model.horizontalStartingPositions().toList();
        int a = 0;
    }
}