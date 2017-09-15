import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeControl {

    private static final String INHIBITION = "inhibition/fxml/inhibition.fxml";

    @FXML
    private Label languageL;
    @FXML
    private Label inhibitionL;
    @FXML
    private Label exitL;

    @FXML
    public void initialize() {
        languageL.setOnMouseClicked(event -> System.out.println("Clicked on the language test"));

        inhibitionL.setOnMouseClicked(event -> goInhibition());

        exitL.setOnMouseClicked(event -> System.exit(0));
    }

    public void goInhibition() {
        FxmlLoader fxmlLoader = new FxmlLoader();
        try {
            fxmlLoader.load((Stage) inhibitionL.getScene().getWindow(), INHIBITION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
