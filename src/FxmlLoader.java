import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlLoader {

    public void load(Stage stage, String fxml_file) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxml_file));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
