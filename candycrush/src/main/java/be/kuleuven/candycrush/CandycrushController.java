package be.kuleuven.candycrush;

import java.net.URL;
import java.util.ResourceBundle;

import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.view.CandycrushView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

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
        model = new CandycrushModel("Test");
        view = new CandycrushView(model);
        speelbord.getChildren().add(view);
        view.setOnMouseClicked(this::onCandyClicked);
        btn.setOnMouseClicked(this::onStartClicked);
        reset.setOnMouseClicked(this::onResetClicked);
        reset.setDisable(true);
    }

    public void update(){
        scoreBoard.setText(String.format("Score: %d", model.getScore()));
        view.update();
    }

    public void onCandyClicked(MouseEvent me){
        int candyIndex = view.getIndexOfClicked(me);
        model.candyWithIndexSelected(candyIndex);
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
        model.resetScore();
        model.stop();
        btn.setDisable(false);
        reset.setDisable(true);
        paneel.setStyle("-fx-background-color: white");
        update();
    }

}
