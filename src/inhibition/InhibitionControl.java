package inhibition;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InhibitionControl {

    ResWriter resWriter = new ResWriter();

    private static final String SQUARE = "inhibition/pictures/square.jpg";
    private static final String TEDDYBEAR = "inhibition/pictures/TeddyBear.jpg";

    private static final double SIZE = 115.0;
    private static final double DELTA_SIZE = 12.5;

    private static final int RATE = 1000;
    private static final int LIFESPAN = 3000;
    private static final int WAIT_OUT = 3000;

    private static int index = 0;

    private List<Boolean> figuresToGenerate = new ArrayList<>();
    private List<Figure> figures = new ArrayList<>();

    private List<Double> teddyBearTime = new ArrayList<>();
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
                macroTest(2, 1, false, true);
            });
        });

        secondTestL.setOnMouseClicked(event -> {
            testL.setText("Start 2nd mini test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                macroTest(6, 2, false, false);
            });
        });

        thirdTestL.setOnMouseClicked(event -> {
            testL.setText("Start third mini test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                macroTest(10, 1, true, true);
            });
        });

        inhibitionTestL.setOnMouseClicked(event -> {
            testL.setText("Start inhibition test");
            testL.setOnMouseClicked(event1 -> {
                anchorP.getChildren().remove(testL);
                macroTest(20, 1, true, true);
            });
        });
    }

    /**
     * This method allows to run multiple tests according to the parameters
     * @param nb_figure total number of figure, split 50/50
     * @param wait_out_multiplier multiply the delay at the end of the test
     * @param step show all figures at once or wait @RATE between each figure
     * @param destruction enable the auto-destruction of a figure @LIFESPAN after the creation
     */
    private void macroTest(int nb_figure, int wait_out_multiplier, boolean step, boolean destruction){
        Task task = new Task<Void>(){
            @Override
            public Void call() throws Exception {
                generateFiguresToGenerate(nb_figure/2, nb_figure/2);
                for(int i = 0; i < nb_figure; i++){
                    Platform.runLater(() -> addFigure(destruction));
                    if(step) Thread.sleep(RATE);
                }
                Thread.sleep(WAIT_OUT*wait_out_multiplier);
                resWriter.printResults(teddyBearTime, squaresTime);
                Platform.runLater(() -> cleanUp());
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    /**
     * This method make sure there's no conflict between the differents tests
     */
    private void cleanUp() {
        figuresToGenerate.clear();
        figures.clear();
        index = 0;

        anchorP.getChildren().remove(0, anchorP.getChildren().size());

        testL.setText("Test over");
        testL.setOnMouseClicked(event -> {
        });
        anchorP.getChildren().add(testL);

        teddyBearTime.clear();
        squaresTime.clear();
    }

    private void generateFiguresToGenerate(int nb_teddy_bears, int nb_stars) {
        int i;
        for(i = 0; i < nb_teddy_bears; i++){
            figuresToGenerate.add(true);
        }
        for(i = 0; i < nb_stars; i++){
            figuresToGenerate.add(false);
        }
        Collections.shuffle(figuresToGenerate);
    }

    private void generateFigure(){
        boolean collision = false;
        double size;
        double location_x;
        double location_y;
        do{
            collision = false;
            size = figureSize();
            location_x = figureLocation_x();
            location_y = figureLocation_y();

            Figure placeholder = new Figure(size, location_x, location_y, "");


            if(figures.size() >= 1) {
                if(placeholder.collide(figures.get(figures.size()-1))){
                    collision = true;
                }

                if(figures.size() >= 2) {
                    if(placeholder.collide(figures.get(figures.size()-2))){
                        collision = true;
                    }

                    if(figures.size() >= 3) {
                        if(placeholder.collide(figures.get(figures.size()-3))){
                            collision = true;
                        }
                        if(figures.size() >= 4) {
                            if(placeholder.collide(figures.get(figures.size()-4))){
                                collision = true;
                            }
                            if(figures.size() >= 5) {
                                if(placeholder.collide(figures.get(figures.size()-5))){
                                    collision = true;
                                }
                            }
                        }
                    }
                }
            }

        } while (collision);

        if(figuresToGenerate.get(index)) {
            figures.add(new Figure(size, location_x, location_y, TEDDYBEAR));
        } else {
            figures.add(new Figure(size, location_x, location_y, SQUARE));
        }
    }

    /**
     * Adds a figure to the anchorpane
     */
    private void addFigure(boolean destruction) {
        generateFigure();
        int indexLocal = index;

        ImageView imageView = new ImageView(figures.get(indexLocal).getType());

        //definition of the size
        double size = figures.get(indexLocal).getSize();
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        imageView.setOnMouseClicked(event -> {

            figures.get(indexLocal).setDestruction(System.nanoTime());
            double time = figures.get(indexLocal).lifespan();
            time /= 1000000000;

            if (figures.get(indexLocal).getType().equals(SQUARE)) squaresTime.add(time);
            else if (figures.get(indexLocal).getType().equals(TEDDYBEAR)) teddyBearTime.add(time);

            anchorP.getChildren().remove(imageView);
        });

        figures.get(indexLocal).setCreation(System.nanoTime());


        //Setup destruction if the figure is not clicked
        if(destruction){
            Timeline timeline = new Timeline(new KeyFrame(
                    javafx.util.Duration.millis(LIFESPAN),
                    ae -> {
                        if (anchorP.getChildren().contains(imageView)) {
                            anchorP.getChildren().remove(imageView);
                        }
                    }));
            timeline.play();
        }

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
        double delta = random.nextGaussian() * DELTA_SIZE;
        return SIZE + delta;
    }
}
