package be.kuleuven.candycrush;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CandycrushController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label Label;

    @FXML
    private Label scoreBoard;

    @FXML
    private Button btn;

    @FXML
    private Button reset;

    @FXML
    private AnchorPane paneel;

    @FXML
    private AnchorPane speelbord;

    @FXML
    private TextField textInput;

    private CandycrushModel model;
    private CandycrushView view;
    @FXML

    void initialize() {
        assert Label != null : "fx:id=\"Label\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert btn != null : "fx:id=\"btn\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert paneel != null : "fx:id=\"paneel\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert speelbord != null : "fx:id=\"speelbord\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert textInput != null : "fx:id=\"textInput\" was not injected: check your FXML file 'candycrush-view.fxml'.";
        assert scoreBoard != null : "fx:id=\"scoreBoard\"was not injected: check your FXML file 'candycrush-view.fxml'.";

        CandycrushModel model1 = createBoardFromString("""
                                                               @@o#
                                                               o*#o
                                                               @@**
                                                               *#@@""");

        CandycrushModel model2 = createBoardFromString("""
                                                               #oo##
                                                               #@o@@
                                                               *##o@
                                                               @@*@o
                                                               **#*o""");

        CandycrushModel model3 = createBoardFromString("""
                                                               #@#oo@
                                                               @**@**
                                                               o##@#o
                                                               @#oo#@
                                                               @*@**@
                                                               *#@##*""");

        model = model3;
        //model = new CandycrushModel("Mathias", 10, 10);
        view = new CandycrushView(model);
        speelbord.getChildren().add(view);
        view.setOnMouseClicked(this::onCandyClicked);
        btn.setOnMouseClicked(this::onStartClicked);
        reset.setOnMouseClicked(this::onResetClicked);
        reset.setDisable(true);

        /*Solution solution = model.solveAny();
        solution.printSolution();
        model.setCandyBoard(solution.board());*/

        //Collection<Solution> solutions = model.solveAll();

        Solution solution = model.maximizeScore();
        solution.printSolution();
    }

    public void update(){
        scoreBoard.setText(String.format("Score: %d", model.getScore()));
        view.update();
    }

    public void onCandyClicked(MouseEvent me){
        int candyIndex = view.getIndexOfClicked(me);
        model.candyWithIndexSelected(Position.fromIndex(candyIndex, model.getBoardSize()));
        update();
    }

    public void onStartClicked(MouseEvent me){
        btn.setDisable(true);
        reset.setDisable(false);
        if(textInput.getText().equals("Mathias")){
            paneel.setStyle("-fx-background-color: blue;");
            if (!model.isGestart()){
                model.start();
            }
        }
        else {
            paneel.setStyle("-fx-background-color: red;");
        }
        update();
    }

    private void onResetClicked(MouseEvent me) {
        model.reset();
        btn.setDisable(false);
        reset.setDisable(true);
        paneel.setStyle("-fx-background-color: white");
        update();
    }

    public static CandycrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandycrushModel("Speler", size.rijen(), size.kolommen());
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.getSpeelbord().replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }

    private static Candy characterToCandy(char c) {
        return switch(c) {
            case '.' -> null;
            case 'o' -> new NormalCandy(0);
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }



}
