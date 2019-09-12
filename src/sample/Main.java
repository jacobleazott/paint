package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

public class Main extends Application {

    private String version_number = "Pain(t) V.1.0.0";

    public void start(Stage primaryStage) throws Exception{
        // Sets the title of the window
        primaryStage.setTitle(version_number);

        // Base file chooser object for interfacing with file explorer for opening and saving
        FileChooser filechooser = new FileChooser();
        // The different valid image types that the user is allowed to open and their associated file extensions
        String[][] file_filter_data = {{"All Files (*.*)", "*.*"},
                {"Bitmap Files Files (*.bmp;*.dib)", "*.bmp", "*.dib"},
                {"GIF Files (*.gif)", "*.gif"},
                {"HEIC Files (*.heic)", "*.heic"},
                {"ICO Files (*.ico)", "*.ico"},
                {"JPEG Files (*.jpg;*.jpeg;*.jpe;*.jfif))", "*.jpg", "*.jpeg", "*.jpe", "*.jfif"},
                {"PNG Files (*.png)", "*.png"},
                {"TIFF Files (*.tif;*.tiff)", "*.tif", "*.tiff"},
                {"WEBP Files (*.webp)", "*.webp"}};
        // A list we can append to so that if file_filter_data is changed this can stay the same
        List<FileChooser.ExtensionFilter> file_filters = new ArrayList<>();
        // Creates a list of filters for file types based off of file_filter_data
        for(int i = 0; i < file_filter_data.length; i++){
            String[] tmp_filter = Arrays.copyOfRange(file_filter_data[i], 1, file_filter_data[i].length);
            file_filters.add(i, new FileChooser.ExtensionFilter(file_filter_data[i][0], tmp_filter));
        }
        // Applies the filters we have created to our filechooser object
        filechooser.getExtensionFilters().addAll(file_filters);

        // Creates the object for the menu bar at the top
        MenuBar menubar = new MenuBar();
        // Creates the pull down tab "File" for the menu bar
        Menu menu_file = new Menu("File");
        // Creates 3 new options and adds them under the menu -> file tab in sequence
        MenuItem menu_file_open = new MenuItem("Open");
        MenuItem menu_file_save = new MenuItem("Save As");
        MenuItem menu_file_exit = new MenuItem("Exit");
        menu_file.getItems().add(menu_file_open);
        menu_file.getItems().add(menu_file_save);
        menu_file.getItems().add(menu_file_exit);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().add(menu_file);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save.setDisable(true);

        // Creates the grid layout where we can place images, buttons, and labels using grid functions
        GridPane gridpane = new GridPane();
        // Image is displayed (even if null like on start) in the upper left corner of the window)
        ImageView myImageView = new ImageView();
        gridpane.add(myImageView, 0, 0);

        //Wrapper for our layouts and special menu bars
        VBox window = new VBox(menubar, gridpane);
        // Adds the menu bar and the grid layout to our window
        //window.getChildren().add(menubar);
        //window.getChildren().add(gridpane);

        // Uses the wrapper and given window starting sizes to create a scene which is yet another wrapper
        Scene main_scene = new Scene(window, 500, 500);
        // Applies our created scene to our default window
        primaryStage.setScene(main_scene);
        // Displays the window for the user to finally see
        primaryStage.show();


        // Eventually move these into their own .java to organize menu bar actions if more persist (they will)
        // Sets the action for the open button to open up the file chooser and open a valid image
        menu_file_open.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Open Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            File filechooser_file = filechooser.showOpenDialog(null);
            // Tries to read and display the image selected
            try {
                Image open_image = SwingFXUtils.toFXImage(ImageIO.read(filechooser_file), null);
                myImageView.setImage(open_image);
                // We have opened an image therefore we can save it
                menu_file_save.setDisable(false);
            }
            // Catches if there is no image selected but it is fine to just close the file explorer and move on
            catch (IOException | IllegalArgumentException e) {}
            // Catches if the selected image is not one of the approved file types
            catch (NullPointerException e){
                // Creates an alert window changes its displayed text and title
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selected File Is Not A Supported Image Type");
                a.setTitle(version_number);
                // Displays the alert in a new window for the user
                a.show();
            }
        });

        //Sets the action for the save button to open up the file chooser and save the image
        menu_file_save.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Save Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            File filechooser_file = filechooser.showSaveDialog(null);
            try {
                // Saves the image to the desired location using the appropriate filters available
                ImageIO.write(SwingFXUtils.fromFXImage(myImageView.getImage(), null), "", filechooser_file);
                // Possibly have this be displayed in the lower left corner of the program
                System.out.println("Image Saved Successfully");
            }
            // Catches if there is no selected location to save but no action necessary
            catch (IOException | IllegalArgumentException e) {}
        });

        //Sets the action for the exit button to close the window
        menu_file_exit.setOnAction(event -> {
            Platform.exit();
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
