package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.BoardSize;
import be.kuleuven.candycrush.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PositionTest {
    @Test
    public void neighborPositions_alsLinkseBovenHoek(){
        BoardSize size = new BoardSize(4, 3);
        Position linkseHoek = new Position(0, 0, size);
        ArrayList<Position> neighbors = (ArrayList<Position>) linkseHoek.neighborPositions();
        ArrayList<Position> test = new ArrayList<>();
        test.add(new Position(1, 0, size));
        test.add(new Position(0,1, size));
        test.add(new Position(1,1,size));
        assert (neighbors.equals(test));
    }

    @Test
    public void neighborPositions_alsRechtseBovenHoek(){
        BoardSize size = new BoardSize(4, 3);
        Position linkseHoek = new Position(0, 2, size);
        ArrayList<Position> neighbors = (ArrayList<Position>) linkseHoek.neighborPositions();
        ArrayList<Position> test = new ArrayList<>();
        test.add(new Position(1,2, size));
        test.add(new Position(0, 1, size));
        test.add(new Position(1,1,size));
        assert (neighbors.equals(test));
    }

    @Test
    public void neighborPositions_alsLinkseOnderHoek(){
        BoardSize size = new BoardSize(4, 3);
        Position linkseHoek = new Position(3, 0, size);
        ArrayList<Position> neighbors = (ArrayList<Position>) linkseHoek.neighborPositions();
        ArrayList<Position> test = new ArrayList<>();
        test.add(new Position(2, 0, size));
        test.add(new Position(3,1, size));
        test.add(new Position(2,1,size));
        assert (neighbors.equals(test));
    }

    @Test
    public void neighborPositions_alsRechtseOnderHoek(){
        BoardSize size = new BoardSize(3, 3);
        Position linkseHoek = new Position(3, 2, size);
        ArrayList<Position> neighbors = (ArrayList<Position>) linkseHoek.neighborPositions();
        ArrayList<Position> test = new ArrayList<>();
        test.add(new Position(2,2, size));
        test.add(new Position(3, 1, size));
        test.add(new Position(2,1,size));
        assert (neighbors.equals(test));
    }

    @Test
    public void walkLeft_Normal(){
        BoardSize size = new BoardSize(10,10);
        Position pos = new Position(5,5, size);

        Stream<Position> stream = pos.walkLeft();
        List<Position> output = stream.toList();
        System.out.println(output);
    }
    /*
    @Test
    public void welkLeft_edge(){
        BoardSize size = new BoardSize(10,10);
        Position pos = new Position(0,0, size);

        Stream<Position> stream = pos.walkLeft();
        System.out.println(stream.toList());
    }

    @Test
    public void walkRightTest(){
        BoardSize size = new BoardSize(10,10);
        Position pos = new Position(5,5, size);

        Stream<Position> stream = pos.walkRight();
        System.out.println(stream.toList());
    }

    @Test
    public void walkUpTest(){
        BoardSize size = new BoardSize(10,10);
        Position pos = new Position(5,5, size);

        Stream<Position> stream = pos.walkUp();
        System.out.println(stream.toList());
    }

    @Test
    public void walkDownTest(){
        BoardSize size = new BoardSize(10,10);
        Position pos = new Position(5,5, size);

        Stream<Position> stream = pos.walkDown();
        System.out.println(stream.toList());
    }*/
}
