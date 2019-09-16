package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class PaintMain extends Application {

    String version_number = "Pain(t) V.1.0.1";

    public void start(Stage primaryStage){
        // Sets the title of the window
        primaryStage.setTitle(version_number);
        // Create the window as a whole
        PaintWindow main_scene = new PaintWindow(primaryStage);
        // Applies our created scene to our default window
        primaryStage.setScene(main_scene.setup_Scene());
        // Displays the window for the user to finally see
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
