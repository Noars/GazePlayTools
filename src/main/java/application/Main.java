package application;


import application.ui.DecoratedPane;
import application.ui.ImagePane;
import application.ui.MainPane;
import application.ui.VideoPane;
import application.utils.HeatMapVideo;
import application.utils.MainButton;
import application.utils.TextUtils;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    TextUtils textUtils;
    MainButton mainButton;
    HeatMapVideo heatMapVideo;

    public DecoratedPane decoratedPane;
    public ImagePane imagePane;
    public VideoPane videoPane;
    public MainPane mainPane;

    public int width = 600;
    public int height = 300;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setWidth(this.width);
        primaryStage.setHeight(this.height);
        primaryStage.setTitle("GazePlayTools");
        primaryStage.setResizable(false);

        textUtils = new TextUtils();
        mainButton = new MainButton();
        heatMapVideo = new HeatMapVideo(primaryStage);

        decoratedPane = new DecoratedPane(this, primaryStage);
        imagePane = new ImagePane(this, primaryStage, textUtils, mainButton);
        videoPane = new VideoPane(this, primaryStage, textUtils, mainButton, heatMapVideo);
        mainPane = new MainPane(this, primaryStage, textUtils, mainButton);

        decoratedPane.setCenter(mainPane);

        Scene scene = new Scene(decoratedPane, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add("style.css");
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        this.loadMainPane(primaryStage);
    }

    public void loadMainPane(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setFullScreen(false);
        primaryStage.getScene().setRoot(decoratedPane);
        primaryStage.setX((primaryScreenBounds.getWidth() - this.width)/2);
        primaryStage.setY((primaryScreenBounds.getHeight() - this.height)/2);
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
    }

    public void backToMainPane(Stage primaryStage){
        decoratedPane.setCenter(mainPane);
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
    }

    public void goTo(Stage primaryStage, Parent pane){
        decoratedPane.setCenter(pane);
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
    }
}
