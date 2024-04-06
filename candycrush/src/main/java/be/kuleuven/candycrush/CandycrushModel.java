package be.kuleuven.candycrush;

import java.util.ArrayList;
import java.util.Random;

import static be.kuleuven.CheckNeighboursInGrid.*;

public class CandycrushModel {
    private String speler;
    private ArrayList<Candy> speelbord;
    private ArrayList<Integer> intBoard;
    private int score;
    private boolean gestart;
    private BoardSize boardSize;

    public CandycrushModel(String speler, int width, int height){
        this.speler = speler;
        speelbord = new ArrayList<>();
        intBoard = new ArrayList<>();
        boardSize = new BoardSize(height, width);

        score = 0;
        gestart = false;

        for (int i = 0; i < width*height; i++){
            Random random = new Random();
            int randomGetal = random.nextInt(7) + 1;

            speelbord.add(randomCandy(randomGetal));
            intBoard.add(randomGetal);
        }
    }

    public Candy randomCandy(int randomGetal){

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

    public void reset(){
        this.score =0;
        this.gestart = false;
    }

    public boolean isGestart(){
        return this.gestart;
    }

    public void start(){
        this.gestart = true;
    }

    //TODO
    // DEZE HELE FUNC MOET AANGEPAST WARDEN AAAH PIJN
    public void candyWithIndexSelected(Position position){
        ArrayList<Integer> NeighboursIds
                = (ArrayList<Integer>) getSameNeighboursIds(this.intBoard, getWidth(), getHeight(), position.getIndex());

        Random random = new Random();
        for(int id : NeighboursIds){
            int randomGetal = random.nextInt(5) + 1;
            speelbord.set(id, randomCandy(randomGetal));
            intBoard.set(id, randomGetal);
            score = score + 2;
        }
        int randomGetal = random.nextInt(5) + 1;
        speelbord.set(position.getIndex(), randomCandy(randomGetal));
        intBoard.set(position.getIndex(), randomGetal);
        score++;
    }
}