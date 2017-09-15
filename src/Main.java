import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String HOME = "fxml/home.fxml";
    private static final String TITLE = "Linguistique";

    public static final double WIDTH = 1024;
    public static final double HEIGHT = 576;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        FxmlLoader loader = new FxmlLoader();
        loader.load(primaryStage, HOME);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
