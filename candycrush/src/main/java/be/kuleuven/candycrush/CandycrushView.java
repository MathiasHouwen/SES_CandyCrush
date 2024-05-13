package be.kuleuven.candycrush;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.Map;

public class CandycrushView extends Region {
    private CandycrushModel model;
    private int widthCandy;
    private int heigthCandy;

    public CandycrushView(CandycrushModel model) {
        this.model = model;
        widthCandy = 30;
        heigthCandy = 30;
        update();
    }

    public void update(){
        getChildren().clear();
        if(model.isGestart()){
            grid();
        }
    }

    public void grid(){
        Map<Position, Candy> cells = model.getSpeelbord().getCells();
        BoardSize size = model.getBoardSize();
        for (Position position : cells.keySet()){
            Rectangle rectangle = new Rectangle(position.kolom() * widthCandy, position.rij() * heigthCandy, widthCandy,heigthCandy);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.BLACK);

            Node node = makeCandyShape(position, model.getSpeelbord().getCellAt(position));
            getChildren().addAll(rectangle,node);
        }
    }

    public int getIndexOfClicked(MouseEvent me){
        int index = -1;
        int row = (int) me.getY()/heigthCandy;
        int column = (int) me.getX()/widthCandy;
        System.out.println(me.getX()+" - "+me.getY()+" - "+row+" - "+column);
        if (row < model.getWidth() && column < model.getHeight()){
            Position pos = new Position(row, column, model.getBoardSize());
            index = pos.getIndex();
            System.out.println(index);
        }
        return index;
    }

    private Node makeCandyShape(Position position, Candy candy){
        return switch(candy) {
            case Tomaat ignored -> makeRec(position, Color.RED);
            case Lente_ui ignored -> makeRec(position, Color.GREEN);
            case Erwt ignored -> makeRec(position, Color.YELLOW);
            case Kropsla ignored -> makeRec(position, Color.BLACK);
            case NormalCandy c -> makeNormalCandy(position, c);
            case noCandy ignored -> makeCirc(position, Color.TRANSPARENT);

            default -> throw new IllegalStateException("Unexpected value: " + candy);
        };
    }

    private Rectangle makeRec(Position position, Paint paint){
        Rectangle r;
        r = new Rectangle(position.kolom() * widthCandy, position.rij() * heigthCandy, widthCandy, heigthCandy);
        r.setFill(paint);
        return r;
    }

    private Circle makeNormalCandy(Position position, NormalCandy candy){
        return switch (candy.colour()){
            case 0 -> makeCirc(position, Color.RED);
            case 1 -> makeCirc(position, Color.BROWN);
            case 2 -> makeCirc(position, Color.YELLOW);
            case 3 -> makeCirc(position, Color.ORANGE);
            default -> throw new IllegalStateException("Unexpected value: " + candy.colour());
        };
    }
    private Circle makeCirc(Position position, Paint paint){
        Circle c;
        c = new Circle(position.kolom() * widthCandy + widthCandy/2, position.rij() * heigthCandy + heigthCandy/2, widthCandy/2);
        c.setFill(paint);
        return c;
    }
}
