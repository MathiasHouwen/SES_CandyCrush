package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static be.kuleuven.CheckNeighboursInGrid.*;

public class CandycrushModel {
    private String speler;
    private ArrayList<Integer> speelbord;
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
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            speelbord.add(randomGetal);
        }
    }

    public CandycrushModel(String speler) {
        this(speler, 4, 4);
    }

    public static void main(String[] args) {
        CandycrushModel model = new CandycrushModel("arne");
        int i = 1;
        Iterator<Integer> iter = model.getSpeelbord().iterator();
        while(iter.hasNext()){
            int candy = iter.next();
            System.out.print(candy);
            if(i% model.getWidth()==0){
                System.out.print("\n");
                i = 1;
            }
            i++;
        }
        System.out.print("\n");

    }
    public String getSpeler() {
        return speler;
    }

    public ArrayList<Integer> getSpeelbord() {
        return speelbord;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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


    public void candyWithIndexSelected(int index){
        if (index != -1){
            ArrayList<Integer> NeighboursIds = (ArrayList<Integer>) getSameNeighboursIds(this.speelbord, this.width, this.height, index);

            Random random = new Random();
            for(int id : NeighboursIds){
                int randomGetal = random.nextInt(5) + 1;
                speelbord.set(id ,randomGetal);
                score = score + 2;
            }
            int randomGetal = random.nextInt(5) + 1;
            speelbord.set(index,randomGetal);
            score++;
        }else{
            System.out.println("model:candyWithIndexSelected:indexWasMinusOne");
        }
    }

    public int getIndexFromRowColumn(int row, int column) {
        return column+row*width;
    }
}
