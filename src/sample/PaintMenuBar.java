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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class PaintMenuBar {

    private MenuItem menu_file_open;
    private MenuItem menu_file_save_as;
    private MenuItem menu_file_save;
    private MenuItem menu_file_exit;
    private File filechooser_file;
    private FileChooser filechooser;

    public MenuBar setup_menubar() {
        // Creates the object for the menu bar at the top
        MenuBar menubar = new MenuBar();
        // Creates the pull down tab "File" for the menu bar
        Menu menu_file = new Menu("File");
        // Creates 3 new options and adds them under the menu -> file tab in sequence
        menu_file_open = new MenuItem("Open");
        menu_file_save_as = new MenuItem("Save As");
        menu_file_save = new MenuItem("Save");
        menu_file_exit = new MenuItem("Exit");
        menu_file.getItems().add(menu_file_open);
        menu_file.getItems().add(menu_file_save_as);
        menu_file.getItems().add(menu_file_save);
        menu_file.getItems().add(menu_file_exit);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().add(menu_file);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save_as.setDisable(true);
        menu_file_save.setDisable(true);


        // Base file chooser object for interfacing with file explorer for opening and saving
        filechooser = new FileChooser();
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

        Paint main = new Paint();
        PaintWindow window = new PaintWindow();


        menu_file_open.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Open Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            filechooser_file = filechooser.showOpenDialog(null);
            // Tries to read and display the image selected
            try {
                //main.img = SwingFXUtils.toFXImage(ImageIO.read(filechooser_file), null);
                InputStream io = new FileInputStream(filechooser_file);
                main.img = new Image(io);
                window.canvas.setHeight(main.img.getHeight());
                window.canvas.setWidth(main.img.getWidth());
                window.gc.drawImage(main.img, 0, 0);
                if (main.primaryStage.getHeight() < window.canvas.getHeight() && window.primaryScreenBounds.getHeight() > window.canvas.getHeight()){
                    main.primaryStage.setHeight(window.canvas.getHeight() + window.scrollpane.getViewportBounds().getWidth());
                }
                if (main.primaryStage.getWidth() < window.canvas.getWidth() && window.primaryScreenBounds.getWidth() > window.canvas.getWidth()){
                    main.primaryStage.setWidth(window.canvas.getWidth() + window.scrollpane.getViewportBounds().getHeight());
                }
                // We have opened an image therefore we can save it
                menu_file_save_as.setDisable(false);
                // We must save as before we can just save
                menu_file_save.setDisable(true);
            }
            // Catches if there is no image selected but it is fine to just close the file explorer and move on
            //catch (IOException | IllegalArgumentException e) {}
            // Catches if the selected image is not one of the approved file types
            catch (IOException e){
                // Creates an alert window changes its displayed text and title
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selected File Is Not A Supported Image Type");
                a.setTitle(main.version_number);
                // Displays the alert in a new window for the user
                a.show();
            }
        });

        //Sets the action for the save as button to open up the file chooser and save the image
        menu_file_save_as.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Save Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            File filechooser_file = filechooser.showSaveDialog(null);
            try {
                // Saves the image to the desired location using the appropriate filters available
                ImageIO.write(SwingFXUtils.fromFXImage(main.img, null), "", filechooser_file);
                // Possibly have this be displayed in the lower left corner of the program
                System.out.println("Image Saved Successfully");
            }
            // Catches if there is no selected location to save but no action necessary
            catch (IOException | IllegalArgumentException e) {}
            // Since we have a file location we can just regularly save it
            menu_file_save.setDisable(false);
        });

        //Sets the action for the save button to save where it was saved last
        menu_file_save.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Save Image");
            try {
                // Saves the image to the desired location using the appropriate filters available
                ImageIO.write(SwingFXUtils.fromFXImage(main.img, null), "", filechooser_file);
                // Possibly have this be displayed in the lower left corner of the program
                System.out.println("Image Saved Successfully");
            }
            // Catches if there is no selected location to save but no action necessary
            catch (IOException | IllegalArgumentException e) {
                System.out.println("Error Saving Image");
            }
        });

        //Sets the action for the exit button to close the window
        menu_file_exit.setOnAction(event -> {
            Platform.exit();
        });


        return menubar;
    }

}
