package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.Board;
import be.kuleuven.candycrush.BoardSize;
import be.kuleuven.candycrush.Candy;
import be.kuleuven.candycrush.Position;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void TestFillBoard(){
        BoardSize size = new BoardSize(3,3);
        Board<Integer> board = new Board<>(size);

        Function<Position, Integer> IntegerCreator = position -> position.rij() + position.kolom();
        board.fill(IntegerCreator);

        assertNotNull(board);
    }

    @Test
    public void testDatBoardKopierd(){
        BoardSize size = new BoardSize(3,3);
        Board<Integer> board = new Board<>(size);
        Board<Integer> otherBoard = new Board<>(size);

        Function<Position, Integer> IntegerCreator = position -> position.rij() + position.kolom();
        board.fill(IntegerCreator);

        board.copyTo(otherBoard);
        assert board.equals(otherBoard);
    }

    @Test
    public void GetRightCellUsingPosition(){
        BoardSize size = new BoardSize(3,3);
        Board<Integer> board = new Board<>(size);

        Function<Position, Integer> IntegerCreator = position -> position.rij() + position.kolom();
        board.fill(IntegerCreator);

        Position position = new Position(1,1,size);
        int cell = board.getCellAt(position);

        assert cell == 2;
    }
}
