import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GoHomeControl {

    @FXML
    private Label goHomeL;

    @FXML
    public void initialize() {
        goHomeL.setOnMouseClicked(event -> goHome());
    }

    @FXML
    public void goHome() {
        FxmlLoader loader = new FxmlLoader();
        Stage stage = (Stage) goHomeL.getScene().getWindow();
        try {
            loader.load(stage, Main.HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
