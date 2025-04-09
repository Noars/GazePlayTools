package application.utils;

import application.Main;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainButton extends Button {

    public MainButton(){};

    public MainButton(String name) {
        super(name);
        getStyleClass().add("customizedButton");
        this.applyCss();
    }

    public static ImageView createButtonImageView(String url) {
        ImageView image = new ImageView(new Image(url));
        image.setPreserveRatio(true);
        image.setFitWidth(495. / 6);
        return image;
    }

    public Button createImageHeatMapButton(Main main, Stage primaryStage, Parent pane) {
        Button image = new MainButton("Image");
        image.setGraphic(createButtonImageView("images/image.png"));
        image.getStyleClass().add("green");
        image.setContentDisplay(ContentDisplay.TOP);
        image.setPrefHeight(200);
        image.setPrefWidth(495. / 5);
        image.setOnAction((e) -> {
            main.goTo(primaryStage, pane);
        });
        return image;
    }

    public Button createVideoHeatMapButton(Main main, Stage primaryStage, TextUtils textUtils, Parent pane){
        Button video = new MainButton(textUtils.setUtf8("Vidéo"));
        video.setGraphic(createButtonImageView("images/video.png"));
        video.getStyleClass().add("blue");
        video.setContentDisplay(ContentDisplay.TOP);
        video.setPrefHeight(200);
        video.setPrefWidth(495. / 5);
        video.setOnAction((e) -> {
            main.goTo(primaryStage, pane);
        });

        return video;
    }

    public Button createBackButton(Main main, Stage primaryStage) {
        Button back = new MainButton("Retour");
        back.setGraphic(createButtonImageView("images/back.png"));
        back.getStyleClass().add("grey");
        back.setContentDisplay(ContentDisplay.TOP);
        back.setPrefHeight(200);
        back.setPrefWidth(495. / 5);
        back.setOnAction((e) -> {
            main.backToMainPane(primaryStage);
        });
        return back;
    }
}
