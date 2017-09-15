package inhibition;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InhibitionControl {

    private static final String FILENAME = "results.txt";

    private PrintWriter printWriter;

    private static final String SQUARE = "inhibition/pictures/square.jpg";
    private static final String STAR = "inhibition/pictures/star.png";

    private static final double SIZE = 75.0;
    private static final double DELTA_SIZE = 40;

    private static final int NB_FIGURE = 20;
    private static final int RATE = 1000;
    private static final int WAIT_OUT = 1000;

    private List<ImageView> imageViews = new ArrayList<>();
    private List<Long> begins = new ArrayList<>();
    private long end;

    private List<Double> starsTime = new ArrayList<>();
    private List<Double> squaresTime = new ArrayList<>();

    @FXML
    private AnchorPane anchorP;

    @FXML
    private Label testL;

    @FXML
    public void initialize() {
        testL.setOnMouseClicked(event -> {
            anchorP.getChildren().remove(testL);
            inhibitionTest();
        });
    }

    /**
     * Runs the inhibition test
     */
    private void inhibitionTest() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < NB_FIGURE; i++) {
                    Platform.runLater(() -> {
                        Random random = new Random();
                        double coin = random.nextDouble();
                        if (coin <= 0.5) addFigure(SQUARE);
                        else addFigure(STAR);
                    });
                    Thread.sleep(RATE);
                }

                Thread.sleep(WAIT_OUT);

                printResults();

                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void printResults() {

        try {
            printWriter = new PrintWriter(FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        printWriter.println("Stars:");
        stats(starsTime);

        printWriter.println("\nSquares:");
        stats(squaresTime);

        printWriter.close();
    }

    /**
     * Adds a figure to the anchorpane
     */
    private void addFigure(String figure) {
        ImageView imageView = new ImageView(figure);

        //definition of the size
        double size = figureSize();
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        imageViews.add(imageView);
        begins.add(System.nanoTime());

        imageView.setOnMouseClicked(event -> {
            end = System.nanoTime();
            double time = (end - begins.get(imageViews.indexOf(imageView)));
            time /= 1000000000;
            if (figure.equals(SQUARE)) squaresTime.add(time);
            else if (figure.equals(STAR)) starsTime.add(time);

            anchorP.getChildren().remove(imageView);
        });
        anchorP.getChildren().add(imageView);

        //determination of the location of the figure
        anchorP.setTopAnchor(imageView, figureLocation_y());
        anchorP.setLeftAnchor(imageView, figureLocation_x());
    }

    /**
     * Process the x location of the next figure
     */
    private double figureLocation_x() {
        double res = new Random().nextDouble() * (anchorP.getWidth() - (SIZE + DELTA_SIZE));
        return res;
    }

    /**
     * Process the y location of the next figure
     */
    private double figureLocation_y() {
        double res = new Random().nextDouble() * (anchorP.getHeight() - (SIZE + DELTA_SIZE));
        return res;
    }

    /**
     * Process the size of the next figure
     */
    private double figureSize() {
        Random random = new Random();
        double delta = random.nextDouble() * DELTA_SIZE;
        double coin = random.nextDouble();
        if (coin > 0.5) delta *= -1;
        return SIZE + delta;
    }

    /**
     * Prints a list of statistics for a given list of double
     */
    private void stats(List<Double> list) {

        if (list.isEmpty()) {
            printWriter.println("list empty");
            return;
        }

        Collections.sort(list);
        double min = list.get(0);
        double max = list.get(list.size() - 1);
        double med = list.get(list.size() / 2);
        double avg = 0.0;
        for (double e : list) {
            avg += e;
        }
        avg /= list.size();

        double std_dev = 0.0;
        for (double e : list) {
            std_dev += Math.pow(e - avg, 2);
        }
        std_dev /= list.size();
        std_dev = Math.sqrt(std_dev);

        printWriter.println("number of elements: " + list.size());
        printWriter.println("min = " + min);
        printWriter.println("max = " + max);
        printWriter.println("med = " + med);
        printWriter.println("avg = " + avg);
        printWriter.println("std dev = " + std_dev);
    }

}
