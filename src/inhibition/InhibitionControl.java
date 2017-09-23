package inhibition;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileWriter;
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

    private static final int RATE = 1000;
    private static final int WAIT_OUT = 4000;

    private static int index = 0;

    private List<Figure> figures = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();

    private List<Double> starsTime = new ArrayList<>();
    private List<Double> squaresTime = new ArrayList<>();

    @FXML
    private AnchorPane anchorP;

    @FXML
    private Label testL;
    @FXML
    private Label firstTestL;
    @FXML
    private Label secondTestL;
    @FXML
    private Label thirdTestL;
    @FXML
    private Label inhibitionTestL;

    @FXML
    public void initialize() {
        testL.setOnMouseClicked(event -> {
        });

        firstTestL.setOnMouseClicked(event -> {
            testL.setText("Start first mini test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                firstMiniTest();
            });
        });

        secondTestL.setOnMouseClicked(event -> {
            testL.setText("Start 2nd mini test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                secondMiniTest();
            });
        });

        thirdTestL.setOnMouseClicked(event -> {
            testL.setText("Start third mini test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                thirdMiniTest();
            });
        });

        inhibitionTestL.setOnMouseClicked(event -> {
            testL.setText("Start inhibition test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                inhibitionTest();
            });
        });
    }

    /**
     * Runs the first mini test
     */
    private void firstMiniTest() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                generateFigures(1, 1);
                Platform.runLater(() -> {
                    addFigure();
                    addFigure();
                });

                Thread.sleep(WAIT_OUT);
                printResults();
                Platform.runLater(() -> cleanUp());
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void cleanUp() {
        figures.clear();
        index = 0;

        anchorP.getChildren().remove(0, anchorP.getChildren().size());

        testL.setText("Test over");
        testL.setOnMouseClicked(event -> {
        });
        anchorP.getChildren().add(testL);

        starsTime.clear();
        squaresTime.clear();
        imageViews.clear();
    }

    /**
     * Runs the second mini test
     */

    private void secondMiniTest() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int nb_fig = 6;
                generateFigures(nb_fig/2, nb_fig/2);
                for (int i = 0; i < nb_fig; i++) {
                    Platform.runLater(() -> addFigure());
                }

                Thread.sleep(WAIT_OUT * 2);
                printResults();
                Platform.runLater(() -> cleanUp());

                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    /**
     * Runs the third mini test
     */

    private void thirdMiniTest() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int nb_fig = 6;
                generateFigures(nb_fig/2, nb_fig/2);
                for (int i = 0; i < nb_fig; i++) {
                    Platform.runLater(() -> addFigure());
                    Thread.sleep(RATE);
                }

                Thread.sleep(WAIT_OUT);
                printResults();
                Platform.runLater(() -> cleanUp());

                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    /**
     * Runs the inhibition test
     */
    private void inhibitionTest() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int nb_fig = 20;
                generateFigures(nb_fig/2, nb_fig/2);
                for (int i = 0; i < figures.size() - 1; i++) {
                    Platform.runLater(() -> addFigure());
                    Thread.sleep(RATE);
                }

                Thread.sleep(WAIT_OUT);
                printResults();
                Platform.runLater(() -> cleanUp());

                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void generateFigures(int squares, int stars) {

        for (int i = 0; i < squares; i++) {
            figures.add(new Figure(figureSize(), figureLocation_x(), figureLocation_y(), SQUARE));
        }

        for (int i = 0; i < stars; i++) {
            figures.add(new Figure(figureSize(), figureLocation_x(), figureLocation_y(), STAR));
        }

        Collections.shuffle(figures);
    }

    /**
     * Adds a figure to the anchorpane
     */
    private void addFigure() {
        int indexLocal = index;

        ImageView imageView = new ImageView(figures.get(indexLocal).getType());

        //definition of the size
        double size = figures.get(indexLocal).getSize();
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        imageViews.add(imageView);

        imageView.setOnMouseClicked(event -> {

            figures.get(indexLocal).setDestruction(System.nanoTime());
            double time = figures.get(indexLocal).lifespan();
            time /= 1000000000;

            if (figures.get(indexLocal).getType().equals(SQUARE)) squaresTime.add(time);
            else if (figures.get(indexLocal).getType().equals(STAR)) starsTime.add(time);

            anchorP.getChildren().remove(imageView);
        });

        figures.get(indexLocal).setCreation(System.nanoTime());

        anchorP.getChildren().add(imageView);

        //determination of the location of the figure
        anchorP.setTopAnchor(imageView, figures.get(index).getLocation_y());
        anchorP.setLeftAnchor(imageView, figures.get(index).getLocation_x());

        index++;
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
     * Print the results in the result file
     */
    private void printResults() {

        try {
            FileWriter temp = new FileWriter(FILENAME, true);
            printWriter = new PrintWriter(temp);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }

        printWriter.println("\n\n");

        printWriter.println("Stars:");
        stats(starsTime);

        printWriter.println("\n\nSquares:");
        stats(squaresTime);

        printWriter.println("\n\n");
        printWriter.println("\n#####");
        printWriter.close();
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

        min = (double) Math.round(min * 100) / 100;
        max = (double) Math.round(max * 100) / 100;
        med = (double) Math.round(med * 100) / 100;
        avg = (double) Math.round(avg * 100) / 100;
        std_dev = (double) Math.round(std_dev * 100) / 100;

        printWriter.println("number of elements: " + list.size());
        printWriter.println("min = " + min);
        printWriter.println("max = " + max);
        printWriter.println("med = " + med);
        printWriter.println("avg = " + avg);
        printWriter.println("std dev = " + std_dev);
    }

}
