package be.kuleuven;

import java.util.ArrayList;

public class CheckNeighboursInGrid {
    /* rij dan  kolom*/

    /**
     *
     * @param width  - Specifies the width of the grid.
     * @param height - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     * @param index  - Specifies the index of the element which neighbours that need to be checked
     * @return -Returns 1D array of parameters, 0: Row start, 1: Row end, 2: Column start, 3: Column end
     */
    public static int[] getParameters(int width, int height, int index) {
        if (index == 0) {
            return new int[]{0, 1, 0, 1};
        }
        if (index == width - 1) {
            return new int[]{0, 1, -1, 0};
        }
        if (index == (height - 1) * width) {
            return new int[]{-1, 0, 0, 1};
        }
        if (index == (height * width) - 1) {
            return new int[]{-1, 0, -1, 0};
        }
        if (index < width) {
            return new int[]{0, 1, -1, 1};
        }
        if ((index % width) == 0) {
            return new int[]{-1, 1, 0, 1};
        }
        if (((index + 1) % width) == 0) {
            return new int[]{-1, 1, -1, 0};
        }
        if (index > (height * width) - width) {
            return new int[]{-1, 0, -1, 1};
        }
        return new int[]{-1, 1, -1, 1};
    }

    /*public static void main(String[] args) {
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

        getSameNeighboursIds(grid, 4, 4,5);
    }*/

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *
     * @param grid         - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     * @param width        - Specifies the width of the grid.
     * @param height       - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     * @param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     * @return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     */
    public static Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid, int width, int height, int indexToCheck) {
        // throw expection when index is outo
        if (indexToCheck<0){
            throw new RuntimeException("Index kan niet lager zijn dan 0");
        }
        else if(indexToCheck >= width*height){
            throw new RuntimeException("Index is grooter dan de max");
        }

        // Krijg de parameters voor de dubble for lus
        int[] parameters = getParameters(width, height, indexToCheck);

        // Zet Iterable om in een arraylist
        ArrayList<Integer> arrayGrid = new ArrayList<>();
        for (Integer num : grid) {
            arrayGrid.add(num);
        }

        int value = arrayGrid.get(indexToCheck); // Waarde van de techecken iddex
        ArrayList<Integer> result = new ArrayList<>();
        for (int rij = parameters[0]; rij <= parameters[1]; rij++){
            for (int col = parameters[2]; col <= parameters[3]; col++){
                int index = indexToCheck + rij*width + col;

                // Dit is de waarden waarvan we de buren checken
                if (index == indexToCheck){
                    continue;
                }

                // Als de waarden van buren gelijk is aan de index
                if (value == arrayGrid.get(index)){
                    result.add(index);
                }
            }
        }
        return result;
    }
}
