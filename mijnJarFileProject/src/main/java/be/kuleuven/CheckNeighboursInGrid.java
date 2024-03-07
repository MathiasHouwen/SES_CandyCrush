package be.kuleuven;

import java.util.ArrayList;

public class CheckNeighboursInGrid {
    /* rij dan  kolom*/
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
        int[] a = getParameters(5, 6, 25);
        for (int i=0; i<4; i++){
            System.out.printf(String.valueOf(a[i]));
        }
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

    }
}
