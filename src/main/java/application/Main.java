package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Image backgroundImage;
    private List<DataPoint> dataPoints;
    private int currentIndex = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(1800, 1000);
        gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Trace Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();

        openFileChooser(primaryStage);
    }

    private void openFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File jsonFile = fileChooser.showOpenDialog(stage);
        if (jsonFile != null) {
            loadJSONData(jsonFile);

            fileChooser.getExtensionFilters().set(0, new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File imageFile = fileChooser.showOpenDialog(stage);
            if (imageFile != null) {
                loadImage(imageFile);
            }
        }
    }

    private void loadJSONData(File file) {
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            FixationData fixationData = gson.fromJson(reader, FixationData.class);
            if (fixationData.fixationSequence != null && !fixationData.fixationSequence.isEmpty()) {
                dataPoints = fixationData.fixationSequence.get(0); // Prend la première séquence
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(File file) {
        backgroundImage = new Image(file.toURI().toString());
        gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        startAnimation();
    }

    private void startAnimation() {
        if (dataPoints == null || dataPoints.size() < 2) return;
        currentIndex = 1;
        playNextStep();
    }

    private void playNextStep() {
        if (currentIndex >= dataPoints.size()) return;

        DataPoint p1 = dataPoints.get(currentIndex - 1);
        DataPoint p2 = dataPoints.get(currentIndex);
        double delay = p2.timeGaze - p1.timeGaze; // Différence en millisecondes

        Timeline step = new Timeline(new KeyFrame(Duration.millis(delay), e -> {
            drawStep(p1, p2);
            playNextStep();
        }));
        step.setCycleCount(1);
        step.play();
    }

    private void drawStep(DataPoint p1, DataPoint p2) {

        /*if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        }*/

        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        System.out.println("Dessiner ligne de (" + p1.x + ", " + p1.y + ") à (" + p2.x + ", " + p2.y + ")");
        gc.strokeLine(p1.y, p1.x, p2.y, p2.x);

        currentIndex++;
    }

    static class FixationData {
        List<List<DataPoint>> fixationSequence;
    }

    static class DataPoint {
        long timeGaze;
        int x;
        int y;
    }
}
