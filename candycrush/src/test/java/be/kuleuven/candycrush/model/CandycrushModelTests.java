package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static be.kuleuven.CheckNeighboursInGrid.getSameNeighboursIds;

public class CandycrushModelTests {
    // gegeven_wanneer_dan
    @Test
    public void NaamVanSpeler_AlsdieCorrectterugkomt_danTrue() {
        CandycrushModel model = new CandycrushModel("Mathias");
        String result = model.getSpeler();
        assert (result.equals("Mathias"));
    }

    @Test
    public void score_waneerOpCandyCLick_verhoog() {
        CandycrushModel model = new CandycrushModel("Mathias");
        int score = model.getScore();
        model.candyWithIndexSelected(6); // Verhoog score door candy
        assert (score < model.getScore());
    }

    @Test
    void wanneerRowEnCol_bereken_index() {
        CandycrushModel model = new CandycrushModel("Mathias");
        int index = model.getIndexFromRowColumn(1, 0);
        assert (index == model.getWidth());
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
}