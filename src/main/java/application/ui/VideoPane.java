package application.ui;

import application.Main;
import application.utils.HeatMapVideo;
import application.utils.MainButton;
import application.utils.TextUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class VideoPane extends BorderPane {

    private File jsonFile = null;
    private File videoFile = null;
    private Button launchButton;

    public VideoPane(Main main, Stage primaryStage, TextUtils textUtils, MainButton mainButton, HeatMapVideo heatMapVideo) {
        super();
        this.setWidth(main.width);
        this.setHeight(main.height);

        Button backButton = mainButton.createBackButton(main, primaryStage);

        Label jsonLabel = new Label(textUtils.setUtf8("Aucun fichier JSON sélectionné"));
        Label videoLabel = new Label(textUtils.setUtf8("Aucune vidéo sélectionnée"));

        Button jsonButton = new Button(textUtils.setUtf8("Sélectionner un fichier JSON"));
        jsonButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                jsonFile = selectedFile;
                jsonLabel.setText("JSON : " + selectedFile.getName());
                updateLaunchButton();
            }
        });

        Button imageButton = new Button(textUtils.setUtf8("Sélectionner une vidéo"));
        imageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Vidéo", "*.wav", "*.mp4"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                videoFile = selectedFile;
                videoLabel.setText("Vidéo : " + selectedFile.getName());
                updateLaunchButton();
            }
        });

        launchButton = new Button("Lancer");
        launchButton.setOnAction(e -> {
            try {
                heatMapVideo.launchHeatMapVideo(jsonFile, videoFile);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        launchButton.setDisable(true);

        VBox vBox = new VBox(10, jsonButton, jsonLabel, imageButton, videoLabel, launchButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 20px; -fx-font-size: 14px;");

        HBox hBox = new HBox(50, backButton, vBox);
        hBox.setAlignment(Pos.CENTER);
        this.setCenter(hBox);

        this.setStyle("-fx-background-color: #535e65; -fx-background-radius: 0 0 15 15");
    }

    private void updateLaunchButton() {
        launchButton.setDisable(jsonFile == null || videoFile == null);
    }
}
