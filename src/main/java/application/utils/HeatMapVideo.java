package application.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HeatMapVideo {

    Stage stage;

    private List<HeatPoint> points = new ArrayList<>();
    private Iterator<HeatPoint> pointIterator;
    private HeatPoint nextPoint;
    private int pointIndex = 1;
    private double scaleFactor = 1.0;
    private long startTimestamp = -1;
    private List<HeatPoint> drawnPoints = new ArrayList<>();

    public HeatMapVideo(Stage primryStage){
        stage = primryStage;
    }

    public void launchHeatMapVideo(File jsonFile, File videoFile) throws Exception{
        loadJson(jsonFile);
        points.sort(Comparator.comparingDouble(p -> p.timestamp));
        pointIterator = points.iterator();
        nextPoint = pointIterator.hasNext() ? pointIterator.next() : null;

        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(mediaView, canvas);
        Scene scene = new Scene(root);

        mediaPlayer.setOnReady(() -> {
            double width = media.getWidth();
            double height = media.getHeight();
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(0);
            stage.setY(0);
            canvas.setWidth(width);
            canvas.setHeight(height);
        });

        mediaPlayer.setOnEndOfMedia(() -> enableZoom(canvas));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                while (nextPoint != null && nextPoint.timestamp <= currentTime) {
                    drawHeatPoint(gc, nextPoint.x, nextPoint.y, pointIndex++);
                    drawnPoints.add(nextPoint);
                    nextPoint = pointIterator.hasNext() ? pointIterator.next() : null;
                }
            }
        };

        mediaPlayer.play();
        timer.start();

        stage.setScene(scene);
        stage.show();
    }

    private void loadJson(File jsonFile) throws Exception {
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(new FileReader(jsonFile), JsonObject.class);
        JsonArray fixationSequences = root.getAsJsonArray("fixationSequence");

        for (JsonElement seqElement : fixationSequences) {
            JsonArray sequence = seqElement.getAsJsonArray();
            for (JsonElement pointElement : sequence) {
                JsonObject point = pointElement.getAsJsonObject();
                long timeGaze = point.get("timeGaze").getAsLong();
                double x = point.get("y").getAsDouble();
                double y = point.get("x").getAsDouble();
                if (startTimestamp == -1) startTimestamp = timeGaze;
                double relativeTime = (timeGaze - startTimestamp) / 1000.0; // en secondes
                points.add(new HeatPoint(relativeTime, x, y));
            }
        }
    }

    private void drawHeatPoint(GraphicsContext gc, double x, double y, int index) {
        if (!drawnPoints.isEmpty()) {
            HeatPoint last = drawnPoints.get(drawnPoints.size() - 1);
            gc.setStroke(Color.rgb(255, 255, 0, 0.6));
            gc.setLineWidth(2);
            gc.strokeLine(last.x, last.y, x, y);
        }
        gc.setFill(Color.rgb(255, 0, 0, 0.2));
        gc.fillOval(x - 15, y - 15, 30, 30);
        gc.setFill(Color.rgb(255, 0, 0, 0.4));
        gc.fillOval(x - 5, y - 5, 10, 10);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(12));
        gc.fillText(String.valueOf(index), x + 5, y - 5);
    }

    private void enableZoom(Canvas canvas) {
        canvas.setOnScroll(event -> {
            double delta = event.getDeltaY();
            double newScale = scaleFactor + (delta > 0 ? 0.1 : -0.1);
            if (newScale >= 1.0) {
                scaleFactor = newScale;
                Scale scale = new Scale(scaleFactor, scaleFactor, 0, 0);
                canvas.getTransforms().setAll(scale);
            }
        });
    }

    public static class HeatPoint {
        double timestamp;
        double x;
        double y;

        public HeatPoint(double timestamp, double x, double y) {
            this.timestamp = timestamp;
            this.x = x;
            this.y = y;
        }
    }
}
