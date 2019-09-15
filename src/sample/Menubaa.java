package sample;

import javafx.application.Application;
import javafx.stage.Stage;
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

public class Menubaa extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Creates the object for the menu bar at the top
        MenuBar menubar = new MenuBar();
        //
        // Creates the pull down tab "File" for the menu bar
        Menu menu_file = new Menu("File");
        // Creates 3 new options and adds them under the menu -> file tab in sequence
        MenuItem menu_file_open = new MenuItem("Open");
        MenuItem menu_file_save_as = new MenuItem("Save As");
        MenuItem menu_file_save = new MenuItem("Save");
        MenuItem menu_file_exit = new MenuItem("Exit");
        menu_file.getItems().add(menu_file_open);
        menu_file.getItems().add(menu_file_save_as);
        menu_file.getItems().add(menu_file_save);
        menu_file.getItems().add(menu_file_exit);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().add(menu_file);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save_as.setDisable(true);
        menu_file_save.setDisable(true);

    }
}
