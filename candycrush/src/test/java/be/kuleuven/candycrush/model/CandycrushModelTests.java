package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import be.kuleuven.candycrush.Candy;

import static be.kuleuven.CheckNeighboursInGrid.getSameNeighboursIds;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CandycrushModelTests {
    // gegeven_wanneer_dan
    @Test
    public void NaamVanSpeler_AlsdieCorrectterugkomt_danTrue() {
        CandycrushModel model = new CandycrushModel("Mathias");
        String result = model.getSpeler();
        assert (result.equals("Mathias"));
    }

    @Test
    void grid_calucatesNeighber_correct() {
        ArrayList<Integer> grid = new ArrayList<>();
        grid.add(0);
        grid.add(0);
        grid.add(1);
        grid.add(0);
        grid.add(1);
        grid.add(1);
        grid.add(0);
        grid.add(2);
        grid.add(2);
        grid.add(0);
        grid.add(1);
        grid.add(3);
        grid.add(0);
        grid.add(1);
        grid.add(1);
        grid.add(1);

        ArrayList<Integer> results = (ArrayList<Integer>) getSameNeighboursIds(grid, 4, 4, 5);
        ArrayList<Integer> correctResults = new ArrayList<>();
        correctResults.add(2);
        correctResults.add(4);
        correctResults.add(10);

        assert (results.equals(correctResults));
    }

    @Test
    public void randomList_whenGenrated_isUnieke(){
        CandycrushModel model1 = new CandycrushModel("Naam");
        CandycrushModel model2 = new CandycrushModel("Naam");
        assert (!model2.equals(model1));
    }

    @Test
    public void bezigspel_alsGerest_stopt(){
        CandycrushModel model = new CandycrushModel("Naam");
        model.start();
        model.reset();
        assert (!model.isGestart());
    }

    @Test
    public void gestoptSpel_kanTerug_starten(){
        CandycrushModel model = new CandycrushModel("Naam");
        model.start();
        assert (model.isGestart());
    }

    @Test
    public void spel_DatwordtAangemaakt_correctBreete(){
        int w = 6;
        CandycrushModel model = new CandycrushModel("Naam", w, 6);
        assert (model.getWidth() == w);
    }

    @Test
    public void spel_DatwordtAangemaakt_correctHoogte(){
        int h = 6;
        CandycrushModel model = new CandycrushModel("Naam", 6, h);
        assert (model.getHeight() == h);
    }

    // Position
    @Test
    public void fromIndex_geeftjuistindex_terug(){
        BoardSize size = new BoardSize(4, 3);
        Position goeiePos = new Position(2, 2, size);

        assert (goeiePos.equals(Position.fromIndex(8, size)));
    }
}