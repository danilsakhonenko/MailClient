package pks.mailclient;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage){
        try {
            Scene scene = new Scene(new FXMLLoader(getClass().getResource("StartForm.fxml")).load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            new DialogMessage("Во время выполнения программы возникла ошибка").ShowConsoleMessage();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
