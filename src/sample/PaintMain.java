/**
 * This Paint project as a whole creates a Microsoft Paint like program with different, and possibly better
 * functionality. This is in collaboration with my CS-250 class, Object Oriented Programming
 *
 * This particular file is responsible for calling all of the necessary setup commands to initialize our window.
 * The rest of the program is separated into the other classes for better file management and readability
 *
 * @author  Jacob Leazott
 * @version 1.0.4
 * @since   2019-10-06
 */
package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class PaintMain extends Application {

    String version_number = "Pain(t) V.1.0.4";

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
