package be.kuleuven.candycrush;

import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CandycrushModel {
    private String speler;
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

        Function<Position, Candy> candyCreator = position -> randomCandy();
        candyBoard.fill(candyCreator);
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

        /*for(Position Neighbour : Neighbours){
            candyBoard.replaceCellAt(Neighbour, randomCandy());
            score = score + 2;
        }
        candyBoard.replaceCellAt(position, randomCandy());
        score++;*/

        candyBoard.replaceCellAt(position, new noCandy());
        score++;
        fallDownTo(position);
        updateBoard();
        System.out.println(getAllSwaps(candyBoard));
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
                            && !(getSpeelbord().getCellAt(p) instanceof noCandy) && !(getSpeelbord().getCellAt(pos) instanceof noCandy))
                .toList();
    }

    public List<Position> longestMatchDown(Position pos){
        Stream<Position> walked = pos.walkDown();
        return walked
                .takeWhile(p -> getSpeelbord().getCellAt(p).equals(getSpeelbord().getCellAt(pos))
                        && !(getSpeelbord().getCellAt(p) instanceof noCandy) && !(getSpeelbord().getCellAt(pos) instanceof noCandy))
                .toList();
    }

    public Set<List<Position>> findAllMatches(){
        List<List<Position>> allMatches = Stream.concat(horizontalStartingPositions(), verticalStartingPositions())
                .flatMap(p -> {
                    List<Position> horizontalMatch = longestMatchToRight(p);
                    List<Position> verticalMatch = longestMatchDown(p);
                    return Stream.of(horizontalMatch, verticalMatch);
                })
                .filter(m -> m.size() > 2)
                .sorted((match1, match2) -> match2.size() - match1.size())
                .toList();

        return allMatches.stream()
                .filter(match -> allMatches.stream()
                        .noneMatch(longerMatch -> longerMatch.size() > match.size() && new HashSet<>(longerMatch).containsAll(match)))
                .collect(Collectors.toSet());
    }

    public void clearMatch(List<Position> match){
        List<Position> copy = new ArrayList<>(match); // Match is immutable dus maak een copy

        if(copy.isEmpty()) return;
        Position first = copy.getFirst();
        candyBoard.replaceCellAt(first, new noCandy());
        copy.removeFirst();
        score++;
        clearMatch(copy);
    }

    public void fallDownTo(List<Position> match){
        if(horizontalMatch(match)){
            match.forEach(this::fallDownTo);
        } else {
            match.stream()
                    .max(Comparator.comparingInt(Position::rij)).ifPresent(this::fallDownTo);
        }
    }

    public void fallDownTo(Position pos){
        try{
            Position boven = new Position(pos.rij() - 1, pos.kolom(), boardSize);
            if(candyBoard.getCellAt(pos) instanceof noCandy){
                while (candyBoard.getCellAt(boven) instanceof noCandy){
                    boven =  new Position(boven.rij() - 1, boven.kolom(), boardSize);
                }
                candyBoard.replaceCellAt(pos, candyBoard.getCellAt(boven));
                candyBoard.replaceCellAt(boven, new noCandy());
                fallDownTo(pos);
            } else{
                fallDownTo(boven);
            }
        } catch (IllegalArgumentException ignored){return;}
    }

    public boolean horizontalMatch(List<Position> match){
        return match.getFirst().rij() == match.getLast().rij();
    }

    public boolean updateBoard(){
        Set<List<Position>> matches = findAllMatches();
        if (matches.isEmpty()) return false;

        for(List<Position> match : matches){
            clearMatch(match);
            fallDownTo(match);
        }

        updateBoard();
        return true;
    }

    public void swapCandies(Position pos1, Position pos2){
        if(!pos1.isNeighbor(pos2) || !matchAfterSwitch(pos1, pos2)){
            return;
        }
        if(candyBoard.getCellAt(pos1) instanceof noCandy || candyBoard.getCellAt(pos2) instanceof noCandy){
            return;
        }
        unsafeSwap(pos1, pos2);
        updateBoard();
    }

    private void unsafeSwap(Position pos1, Position pos2){
        Candy candy1 = candyBoard.getCellAt(pos1);
        Candy candy2 = candyBoard.getCellAt(pos2);
        candyBoard.replaceCellAt(pos1, candy2);
        candyBoard.replaceCellAt(pos2, candy1);
    }


    public boolean matchAfterSwitch(Position pos1, Position pos2){
        unsafeSwap(pos1, pos2);
        Set<List<Position>> matches = findAllMatches();
        unsafeSwap(pos1, pos2);
        return !matches.isEmpty();
    }

    private Set<List<Position>> getAllSwaps(Board board){
        Set<List<Position>> swaps = new HashSet<>();

        for (Position position : board.getBoardSize().positions()){
            Iterable<Position> neighbours = position.neighborPositions();
            for(Position neighbour : neighbours){
                if(!matchAfterSwitch(neighbour, position)){
                    continue;
                }
                if(board.getCellAt(position) instanceof noCandy || board.getCellAt(neighbour) instanceof noCandy){
                    continue;
                }
                List<Position> swap = Arrays.asList(position, neighbour);
                // Verwijderd duplicaten in de lijst, want
                // r1c2-r1c3 == r1c3-r1c2
                List<Position> reverseSwap = Arrays.asList(neighbour, position);
                if(swaps.contains(swap) || swaps.contains(reverseSwap)){
                    continue;
                }
                swaps.add(swap);
            }
        }
        return swaps;
    }

    private void findAnySolution(Board pertialBoard){
        Set<List<Position>> swaps = getAllSwaps(pertialBoard);

    }
    /*public Solution solve() {
        PartialSolution initial = createInitialSolution();
        return findAnySolution(initial);
    }

    private Solution findAnySolution(PartialSolution current) {
        if (current.isComplete()) return current.toSolution();
        if (current.shouldAbort()) return null;
        for (var extension : current.extensions()) {
            extension.apply(current);
            var solution = findAnySolution(current);
            if (solution != null) {
                return solution;
            } else {
                extension.undo(current);
            }
        }
        return null;
    }
    public static List<String> findAny(String string, List<String> tokens) {
    return findAny(string, tokens, new ArrayList<>());
    }

    private static List<String> findAny(String remainingString, List<String> allTokens, List<String> usedTokens) {
        if (remainingString.isEmpty()) return usedTokens;
        // if (allTokens.stream().noneMatch(remainingString::startsWith)) return null; // overbodig
        for (String tok : allTokens) {
            if (remainingString.startsWith(tok)) {
                usedTokens.add(tok);
                var shorterString = remainingString.substring(tok.length());
                var solution = findAny(shorterString, allTokens, usedTokens);
                if (solution != null) {
                    return solution;
                } else {
                    usedTokens.removeLast();
                }
            }
        }
        return null;
    }
    */
}