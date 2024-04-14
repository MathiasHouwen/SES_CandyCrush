package be.kuleuven.candycrush;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Random;

import static be.kuleuven.CheckNeighboursInGrid.*;

public class CandycrushModel {
    private String speler;
    private ArrayList<Candy> speelbord;
    private int score;
    private boolean gestart;
    private BoardSize boardSize;

    public CandycrushModel(String speler, int width, int height){
        this.speler = speler;
        speelbord = new ArrayList<>();
        boardSize = new BoardSize(height, width);

        score = 0;
        gestart = false;

        for (int i = 0; i < width*height; i++){
            speelbord.add(randomCandy());
        }
    }

    public CandycrushModel(String speler) {
        this(speler, 4, 4);
    }

    public String getSpeler() {
        return speler;
    }

    public BoardSize getBoardSize(){
        return boardSize;
    }

    public ArrayList<Candy> getSpeelbord() {
        return speelbord;
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
        int randomGetal = random.nextInt(5) + 1;

        Candy c;
        switch (randomGetal){
            case 4:
                c = new Erwt();
                break;
            case 5:
                c = new Kropsla();
                break;
            case 6:
                c = new Lente_ui();
                break;
            case 7:
                c = new Tomaat();
                break;
            default:
                c = new NormalCandy(randomGetal);
                break;
        }
        return c;
    }

    public void candyWithIndexSelected(Position position){
        Iterable<Position> Neighbours = getSameNeighbourPositions(position);

        for(Position Neighbour : Neighbours){
            speelbord.set(Neighbour.getIndex(), randomCandy());
            score = score + 2;
        }
        speelbord.set(position.getIndex(), randomCandy());
        score++;
    }

    Iterable<Position> getSameNeighbourPositions(Position position){
        Iterable<Position> neighbors = position.neighborPositions();
        ArrayList<Position> result = new ArrayList<>();

        for(Position neighbor : neighbors){
            Candy candy = speelbord.get(position.getIndex());
            Candy neighborCandy = speelbord.get(neighbor.getIndex());
            if(candy.equals(neighborCandy)){
                result.add(neighbor);
            }
        }
        return result;
    }
}