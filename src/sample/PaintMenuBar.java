package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

// Creates and initializes the menu bar with their associated actions
class PaintMenuBar{

    private File filechooser_file;
    private FileChooser filechooser;
    private Image img;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage primaryStage;
    private PaintSettingsWindow settings_window = new PaintSettingsWindow();

    // Constructors
    PaintMenuBar(Canvas canvas, GraphicsContext gc, Stage primaryStage) {
        this.canvas = canvas;
        this.gc = gc;
        this.primaryStage = primaryStage;
        }

    MenuBar setup_menubar() {
        // Creates instances so we can access specific data and methods
        Paint main = new Paint();
        PaintWindow window = new PaintWindow();
        // Grabs the screens resolution size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        // Use our custom filechooser class with our filters already applied
        PaintFileChooser chooser = new PaintFileChooser();
        filechooser = chooser.setup();

        // Creates the object for the menu bar at the top
        MenuBar menubar = new MenuBar();
        // Creates the pull down tab "File" for the menu bar
        Menu menu_file = new Menu("File");
        Menu menu_help = new Menu("Help");
        // Creates 3 new options and adds them under the menu -> file tab in sequence
        MenuItem menu_file_open = new MenuItem("Open");
        MenuItem menu_file_save_as = new MenuItem("Save As");
        MenuItem menu_file_save = new MenuItem("Save");
        MenuItem menu_file_settings = new MenuItem("Settings");
        MenuItem menu_file_exit = new MenuItem("Exit");
        menu_file.getItems().addAll(menu_file_open, menu_file_save_as, menu_file_save, menu_file_settings, menu_file_exit);
        // Creates About section under menu -> help tab
        MenuItem menu_help_about = new MenuItem("About");
        menu_help.getItems().add(menu_help_about);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().addAll(menu_file, menu_help);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save_as.setDisable(true);
        menu_file_save.setDisable(true);

        // Sets the action for the open button to open file chooser and select appropriate image
        menu_file_open.setOnAction(event -> {
            filechooser.setTitle("Open Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            filechooser_file = filechooser.showOpenDialog(null);
            // Tries to read and display the image selected
            try {
                InputStream io = new FileInputStream(filechooser_file);
                img = new Image(io);
                // Sets Canvas to image specs
                canvas.setHeight(img.getHeight());
                canvas.setWidth(img.getWidth());
                // Displays the image in the upper left corner of canvas
                gc.drawImage(img, 0, 0);
                // Makes sure the canvas is large enough to display our image properly but restricts to screen size
                if (primaryStage.getHeight() < canvas.getHeight() && primaryScreenBounds.getHeight() > canvas.getHeight()){
                    primaryStage.setHeight(canvas.getHeight() + window.scrollpane.getViewportBounds().getWidth());
                }
                if (primaryStage.getWidth() < canvas.getWidth() && primaryScreenBounds.getWidth() > canvas.getWidth()){
                    primaryStage.setWidth(canvas.getWidth() + window.scrollpane.getViewportBounds().getHeight());
                }
                // We have opened an image therefore we can save use save as
                menu_file_save_as.setDisable(false);
                menu_file_save.setDisable(true);
            }
            // Catches if there is no image selected but it is fine to just close the file explorer and move on
            catch (NullPointerException ignored) {}
            // Catches if the selected image is not one of the approved file types
            catch (IOException | IllegalArgumentException e){
                // Creates an alert window changes its displayed text and title
                Alert alert_filetype = new Alert(Alert.AlertType.ERROR);
                alert_filetype.setContentText("Selected File Is Not A Supported Image Type");
                alert_filetype.setTitle(main.version_number);
                alert_filetype.show();
            }
        });

        // Sets the action for the save as button to open up the file chooser and save the image
        menu_file_save_as.setOnAction(event -> {
            filechooser.setTitle("Save Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            filechooser_file = filechooser.showSaveDialog(null);
            // Saves the image to the desired location using the appropriate filters available
            try { ImageIO.write(SwingFXUtils.fromFXImage(img, null), "", filechooser_file); }
            // Catches if there is no selected location to save but no action necessary
            catch (IOException | IllegalArgumentException ignored) {}
            // Since we have a file location we can just regularly save it
            menu_file_save.setDisable(false);
        });

        // Sets the action for the save button to save where it was saved last
        menu_file_save.setOnAction(event -> {
            filechooser.setTitle("Save Image");
            // Saves the image to the desired location using the appropriate filters available
            try { ImageIO.write(SwingFXUtils.fromFXImage(img, null), "", filechooser_file); }
            // Catches if there is no selected location to save but no action necessary
            catch (IOException | IllegalArgumentException ignored) { }
        });

        // Sets the action to open up the settings menu
        menu_file_settings.setOnAction(event -> {
            settings_window.show();
            System.out.println(settings_window.get_check());
        });

        // Sets the action for the exit button to close the window
        menu_file_exit.setOnAction(event -> Platform.exit());

        // Sets the action for the help button to display "helpful" information
        menu_help_about.setOnAction(event -> {
            Alert alert_help = new Alert(Alert.AlertType.INFORMATION);
            alert_help.setContentText("Version Number: " + main.version_number + "\nAuthor: Jacob Leazott");
            alert_help.setTitle(main.version_number);
            alert_help.show();
        });

        // update the canvas and the gc with its changes
        window.set_Canvas(canvas);
        window.set_gc(gc);
        return menubar;
}}
