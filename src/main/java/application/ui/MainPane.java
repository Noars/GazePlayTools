package application.ui;

import application.Main;
import application.utils.MainButton;
import application.utils.TextUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainPane extends BorderPane {

    HBox hbox;

    public MainPane(Main main, Stage primaryStage, TextUtils textUtils, MainButton mainButton) {
        super();
        this.setWidth(main.width);
        this.setHeight(main.height);

        Button imageHeatMap = mainButton.createImageHeatMapButton(main, primaryStage, main.imagePane);
        Button videoHeatmap = mainButton.createVideoHeatMapButton(main, primaryStage, textUtils, main.videoPane);

        hbox = new HBox(imageHeatMap, videoHeatmap);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hbox, Pos.CENTER);
        this.setCenter(hbox);

        this.setStyle("-fx-background-color: #535e65; -fx-background-radius: 0 0 15 15");
    }
}
