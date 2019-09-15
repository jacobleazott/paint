package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;

public class Paint extends Application {

    public String version_number = "Pain(t) V.1.0.1";
    public Image img;
    public File filechooser_file;
    public Stage primaryStage;

    public Stage get_PrimaryStage(){
        return primaryStage;
    }

    public void start(Stage primaryStage) throws Exception{

        // Sets the title of the window
        primaryStage.setTitle(version_number);

        PaintWindow main_scene = new PaintWindow();

        // Applies our created scene to our default window
        primaryStage.setScene(main_scene.setup_scene());
        // Displays the window for the user to finally see
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
