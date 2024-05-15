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

    public void setCandyBoard(Board<Candy> board){
        candyBoard = board;
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
        fallDownTo(position, candyBoard);
        updateBoard(candyBoard);
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

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions, Board<Candy> board){
        return positions
                .limit(2)
                .allMatch(p -> board.getCellAt(p).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions(Board<Candy> board){
        return boardSize.positions().stream()
                .filter(p -> {
                    Stream<Position> buren = p.walkLeft();
                    return !firstTwoHaveCandy(board.getCellAt(p), buren, board);
                });
    }

    public Stream<Position> verticalStartingPositions(Board<Candy> board){
        return boardSize.positions().stream()
                .filter(p -> {
                    Stream<Position> buren = p.walkUp();
                    return !firstTwoHaveCandy(board.getCellAt(p), buren, board);
                });
    }

    public List<Position> longestMatchToRight(Position pos, Board<Candy> board){
        Stream<Position> walked = pos.walkRight();
        return walked
                .takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos))
                            && !(board.getCellAt(p) instanceof noCandy) && !(board.getCellAt(pos) instanceof noCandy))
                .toList();
    }

    public List<Position> longestMatchDown(Position pos, Board<Candy> board){
        Stream<Position> walked = pos.walkDown();
        return walked
                .takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos))
                        && !(board.getCellAt(p) instanceof noCandy) && !(board.getCellAt(pos) instanceof noCandy))
                .toList();
    }

    public Set<List<Position>> findAllMatches(Board<Candy> board){
        List<List<Position>> allMatches = Stream.concat(horizontalStartingPositions(board), verticalStartingPositions(board))
                .flatMap(p -> {
                    List<Position> horizontalMatch = longestMatchToRight(p, board);
                    List<Position> verticalMatch = longestMatchDown(p, board);
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

    public void clearMatch(List<Position> match, Board<Candy> board){
        List<Position> copy = new ArrayList<>(match); // Match is immutable dus maak een copy

        if(copy.isEmpty()) return;
        Position first = copy.getFirst();
        board.replaceCellAt(first, new noCandy());
        copy.removeFirst();
        score++;
        clearMatch(copy, board);
    }

    public void fallDownTo(List<Position> match, Board<Candy> board){
        if(horizontalMatch(match)){
            match.forEach(position -> fallDownTo(position, board));
        } else {
            match.stream()
                    .max(Comparator.comparingInt(Position::rij)).ifPresent(position -> fallDownTo(position, board));
        }
    }

    public void fallDownTo(Position pos, Board<Candy> board){
        try{
            Position boven = new Position(pos.rij() - 1, pos.kolom(), boardSize);
            if(board.getCellAt(pos) instanceof noCandy){
                while (board.getCellAt(boven) instanceof noCandy){
                    boven =  new Position(boven.rij() - 1, boven.kolom(), boardSize);
                }
                board.replaceCellAt(pos, board.getCellAt(boven));
                board.replaceCellAt(boven, new noCandy());
                fallDownTo(pos, board);
            } else{
                fallDownTo(boven, board);
            }
        } catch (IllegalArgumentException ignored){return;}
    }

    public boolean horizontalMatch(List<Position> match){
        return match.getFirst().rij() == match.getLast().rij();
    }

    public boolean updateBoard(Board<Candy> board){
        Set<List<Position>> matches = findAllMatches(board);
        if (matches.isEmpty()) return false;

        for(List<Position> match : matches){
            clearMatch(match, board);
            fallDownTo(match, board);
        }

        updateBoard(board);
        return true;
    }

    public void swapCandies(Position pos1, Position pos2, Board<Candy> board){
        if(!pos1.isNeighbor(pos2) || !matchAfterSwitch(pos1, pos2, board)){
            return;
        }
        if(board.getCellAt(pos1) instanceof noCandy || board.getCellAt(pos2) instanceof noCandy){
            return;
        }
        unsafeSwap(pos1, pos2, board);
        updateBoard(board);
    }

    private void unsafeSwap(Position pos1, Position pos2, Board<Candy> board){
        Candy candy1 = board.getCellAt(pos1);
        Candy candy2 = board.getCellAt(pos2);
        board.replaceCellAt(pos1, candy2);
        board.replaceCellAt(pos2, candy1);
    }


    public boolean matchAfterSwitch(Position pos1, Position pos2, Board<Candy> board){
        unsafeSwap(pos1, pos2, board);
        Set<List<Position>> matches = findAllMatches(board);
        unsafeSwap(pos1, pos2, board);
        return !matches.isEmpty();
    }

    private Set<List<Position>> getAllSwaps(Board<Candy> board){
        Set<List<Position>> swaps = new HashSet<>();

        for (Position position : board.getBoardSize().positions()){
            Iterable<Position> neighbours = position.neighborPositions();
            for(Position neighbour : neighbours){
                if(!matchAfterSwitch(neighbour, position, board)){
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
    public Solution maximizeScore(){
        List<List<Position>> moves = new ArrayList<>();
        Solution intialSolution = new Solution(0,candyBoard, moves);

        return findOptimalSolution(intialSolution, null);
    }

    private Solution findOptimalSolution(Solution partialSolution, Solution bestSoFar){
        Set<List<Position>> swaps = getAllSwaps(partialSolution.board());

        if(swaps.isEmpty()){
            System.out.println(partialSolution.score());
            System.out.println(partialSolution.calculateScore());
            System.out.println("*-*");

            if (bestSoFar == null || partialSolution.isBetterThan(bestSoFar)) {
                return partialSolution;
            } else {
                return bestSoFar;
            }
        }

        if(bestSoFar != null && partialSolution.canImproveUpon(bestSoFar)){
            return bestSoFar;
        }

        for(List<Position> swap : swaps){
            Board<Candy> mutableBoard = new Board<>(partialSolution.board().getBoardSize());
            partialSolution.board().copyTo(mutableBoard);

            swapCandies(swap.getFirst(), swap.getLast(), mutableBoard);

            List<List<Position>> newMoves = new ArrayList<>(partialSolution.moves());
            newMoves.add(swap);

            Solution solution = new Solution(0, mutableBoard, newMoves);
            int score = solution.calculateScore();
            bestSoFar = findOptimalSolution(new Solution(score, mutableBoard, newMoves), bestSoFar);
        }

        return bestSoFar;
    }
    public Collection<Solution> solveAll(){
        List<List<Position>> moves = new ArrayList<>();
        Solution intialSolution = new Solution(0,candyBoard, moves);
        ArrayList<Solution> collection = new ArrayList<>();
        return findAllSolutions(intialSolution, collection);
    }

    private Collection<Solution> findAllSolutions(Solution partialSolution, Collection<Solution> solutionsSoFar){
        Set<List<Position>> swaps = getAllSwaps(partialSolution.board());

        if(swaps.isEmpty()){
            System.out.println("Oplossing gevonden B)");
            int score = partialSolution.calculateScore();
            Solution solution = new Solution(score, partialSolution.board(), partialSolution.moves());
            solution.printSolution();
            solutionsSoFar.add(solution);
            return solutionsSoFar;
        }

        for(List<Position> swap : swaps){
            Board<Candy> mutableBoard = new Board<>(partialSolution.board().getBoardSize());
            partialSolution.board().copyTo(mutableBoard);

            swapCandies(swap.getFirst(), swap.getLast(), mutableBoard);

            List<List<Position>> newMoves = new ArrayList<>(partialSolution.moves());
            newMoves.add(swap);

            findAllSolutions(new Solution(0, mutableBoard, newMoves), solutionsSoFar);
        }
        return solutionsSoFar;
    }

    public Solution solveAny(){
        Solution intialSolution = new Solution(0,candyBoard, null);
        return findAnySolution(intialSolution);
    }

    private Solution findAnySolution(Solution partialSolution){
        Set<List<Position>> swaps = getAllSwaps(partialSolution.board());

        if(swaps.isEmpty()) return partialSolution; // Current.isComplete()

        for (List<Position> swap : swaps){
            // SWAP
            // 1. copy board vanuit record
            // 2. swapcandies en update
            // 3. maak partialSolution
            // 4. recursie

            Board<Candy> mutableBoard = new Board<>(partialSolution.board().getBoardSize());
            partialSolution.board().copyTo(mutableBoard);

            swapCandies(swap.getFirst(), swap.getLast(), mutableBoard);
            int score = this.getScore();
            return findAnySolution(new Solution(score, mutableBoard, null));
        }
        return null;
    }
}