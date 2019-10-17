package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;

import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;



// Creates and initializes the menu bar with their associated actions
class PaintMenuBar{

    private File filechooser_file;
    private FileChooser filechooser;
    private Image img;
    volatile Canvas canvas;
    private GraphicsContext gc;
    private Stage primaryStage;
    private PaintSettingsWindow settings_window = new PaintSettingsWindow();
    private boolean image_saved = false;
    private MenuBar menubar;
    private MenuItem menu_file_save;
    private MenuItem menu_file_save_as;
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private PaintMain main = new PaintMain();
    private PaintWindow window = new PaintWindow();
    private PaintDrawActions drawactions;
    private final int HORIZONTAL_GAP = 10;
    private final int CANVAS_ORIGIN_X = 0;
    private final int CANVAS_ORIGIN_Y = 0;
    //private String background_string = "#EEEEEE";
    private String[] color_theme;
    private final int SETTINGS_WINDOW_RATIO_X = 5;
    private final int SETTINGS_WINDOW_RATIO_Y = 3;
    private Menu menu_file;
    private Menu menu_edit;
    private Menu menu_help;
    private Timer timer_a;

    // Constructors
    PaintMenuBar(Canvas canvas, GraphicsContext gc, Stage primaryStage, PaintDrawActions drawactions) {
        this.canvas = canvas;
        this.gc = gc;
        this.primaryStage = primaryStage;
        this.drawactions = drawactions;
        }

    PaintMenuBar() {}

    void setImageSaved(boolean save){
        image_saved = save;
    }

    Image getImage(){
        return img;
    }

    void saveAs(){
        filechooser.setTitle("Save Image");
        WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableimage);
        // Opens up the file explorer and stores your file name/ location in filechooser_file
        System.out.println(filechooser_file);

        String fileName_1 = filechooser_file.getName();
        String fileExtension_start = fileName_1.substring(fileName_1.lastIndexOf(".") + 1, filechooser_file.getName().length());
        //System.out.println(">> fileExtension" + fileExtension);

        filechooser_file = filechooser.showSaveDialog(primaryStage);

        String fileName_2 = filechooser_file.getName();
        String fileExtension_end = fileName_2.substring(fileName_2.lastIndexOf(".") + 1, filechooser_file.getName().length());

        if (!fileExtension_end.equals(fileExtension_start)){
            Alert alert_conv = new Alert(Alert.AlertType.WARNING);
            alert_conv.setTitle("File Conversion Warning");
            alert_conv.setHeaderText("Switching File Types May Result In Data Loss");
            ButtonType alert_save_cancel = new ButtonType("Cancel");
            ButtonType alert_save_save = new ButtonType("Save");
            alert_conv.getButtonTypes().clear();
            alert_conv.getButtonTypes().addAll(alert_save_cancel, alert_save_save);
            Optional<ButtonType> option = alert_conv.showAndWait();
            if (option.get() == null) {
                alert_conv.close();
            } else if (option.get() == alert_save_cancel){
                alert_conv.close();
            } else {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(writableimage, null), "png", filechooser_file);
                }
                // Catches if there is no selected location to save but no action necessary
                catch (IOException | IllegalArgumentException ignored) {
                    // System.out.println("Catch");
                }
            }
            //System.out.println("FILE CONVERSION");
        }
        // System.out.println(filechooser_file);
        // Saves the image to the desired location using the appropriate filters available

        // Since we have a file location we can just regularly save it
        menu_file_save.setDisable(false);
        image_saved = true;
    }

    void save(){
        try{
            WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            canvas.snapshot(null, writableimage);
            ImageIO.write(SwingFXUtils.fromFXImage(writableimage, null), "png", filechooser_file);
            System.out.println("Image Saved Successfully");
        }
        catch (IOException | IllegalArgumentException e){
            System.out.println("Error Saving Image");
        }
        image_saved = true;
    }

    void autoSave(){
        try{
            WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            canvas.snapshot(null, writableimage);
            File tmp = new File("tmp");
            ImageIO.write(SwingFXUtils.fromFXImage(writableimage, null), "png", tmp);
            System.out.println("Image Saved Successfully");
        }
        catch (IOException | IllegalArgumentException e){
            System.out.println("Error Saving Image");
        }
    }

    void closeAlert(){
        if (image_saved){
            Platform.exit();
        }
        else{
                Alert alert_save = new Alert(Alert.AlertType.CONFIRMATION);
                alert_save.setTitle("Exit Without Saving");
                alert_save.setHeaderText("Are You Sure You Want To Exit Without Saving?");
                ButtonType alert_save_close = new ButtonType("Close");
                ButtonType alert_save_save = new ButtonType("Save");
                alert_save.getButtonTypes().clear();
                alert_save.getButtonTypes().addAll(alert_save_close, alert_save_save);
                Optional<ButtonType> option = alert_save.showAndWait();
                if (option.get() == null) {
                    alert_save.close();
                } else if (option.get() == alert_save_close){
                    Platform.exit();
                } else if (option.get() == alert_save_save) {
                    if (menu_file_save.isDisable()) {
                        saveAs();
                    } else {save(); } }
                else{ Platform.exit(); }
        }
    }

    void open(){
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
            gc.drawImage(img, CANVAS_ORIGIN_X, CANVAS_ORIGIN_Y);
            // Makes sure the canvas is large enough to display our image properly but restricts to screen size
            /*
            if (primaryStage.getHeight() < canvas.getHeight() && primaryScreenBounds.getHeight() > canvas.getHeight()){
                primaryStage.setHeight(canvas.getHeight() + window.scrollpane.getViewportBounds().getWidth());
            }
            if (primaryStage.getWidth() < canvas.getWidth() && primaryScreenBounds.getWidth() > canvas.getWidth()){
                primaryStage.setWidth(canvas.getWidth() + window.scrollpane.getViewportBounds().getHeight());
            }
             */
            // We have opened an image therefore we can save use save as
            menu_file_save_as.setDisable(false);
            menu_file_save.setDisable(true);
            image_saved = false;
            drawactions.setImage(img);
            //PaintDrawActions tmp = new PaintDrawActions();
            //tmp.setImage(img);
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
    }

    PaintSettingsWindow get_settings(){
        return settings_window;
    }

    String[] get_color(){
        return settings_window.get_color();
    }

    void update_color(){
        menubar.getStylesheets().clear();
        menubar.getStylesheets().add(settings_window.getCSS());
        menubar.getStyleClass().add(".menu-bar");
    }

    /*
    void setTimer(){
        timer_a = new Timer();
        timer_a.schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run(){
                        try {
                            Platform.runLater(() -> autoSave());
                            System.out.println(settings_window.autosave_time);
                            setTimer();
                        }
                        catch(NullPointerException e) {
                            System.out.println("Auto Save Unsuccessful");
                            System.out.println(settings_window.autosave_time);
                            //Somehow bind this to the autosave time
                            // it is just using the original default value
                        }}}, settings_window.autosave_time);
    }


     */
    MenuBar setup_menubar() {
        // Use our custom filechooser class with our filters already applied
        PaintFileChooser chooser = new PaintFileChooser();
        filechooser = chooser.setup();
        // Creates the object for the menu bar at the top
        menubar = new MenuBar();
        // Creates the pull down tab "File" for the menu bar
        menu_file = new Menu("_File");
        menu_edit = new Menu("_Edit");
        menu_help = new Menu("_Help");
        // Creates 3 new options and adds them under the menu -> file tab in sequence
        MenuItem menu_file_open = new MenuItem("_Open");
        menu_file_save_as = new MenuItem("Save As");
        menu_file_save = new MenuItem("_Save");
        MenuItem menu_file_settings = new MenuItem("Settings");
        MenuItem menu_file_exit = new MenuItem("_Exit");
        menu_file.getItems().addAll(menu_file_open, menu_file_save_as, menu_file_save, menu_file_settings, menu_file_exit);
        // Creates sections under menu -> edit tab
        MenuItem menu_edit_size = new MenuItem("_Image Size");
        menu_edit.getItems().add(menu_edit_size);
        // Creates About section under menu -> help tab
        MenuItem menu_help_about = new MenuItem("_About");
        MenuItem menu_help_notes = new MenuItem("_Notes");
        menu_help.getItems().addAll(menu_help_about, menu_help_notes);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().addAll(menu_file, menu_edit, menu_help);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save_as.setDisable(true);
        menu_file_save.setDisable(true);





        ///////////////////////////////////////////////////////////////////////////
        ///////////////// Timer Stuff
        ////////////////////////////////////////////////////////////////////////////
        //setTimer();

        // Sets the action for the open button to open file chooser and select appropriate image
        menu_file_open.setOnAction(event -> {
            open();
        });

        // Sets the action for the save as button to open up the file chooser and save the image
        menu_file_save_as.setOnAction(event -> {
            saveAs();
        });

        // Sets the action for the save button to save where it was saved last
        menu_file_save.setOnAction(event -> {
            save();
        });

        // Sets the action to open up the settings menu
        menu_file_settings.setOnAction(event -> {
            settings_window.show();
            color_theme = settings_window.get_color();
            update_color();
        });

        // Sets the action for the exit button to close the window
        menu_file_exit.setOnAction(event -> {
            closeAlert();
        });

        // Sets the action if you press the red x on the top right of the window
        primaryStage.setOnCloseRequest(event-> {
            closeAlert();
        });

        menu_edit_size.setOnAction(event ->{
            Label label_x = new Label("X:");
            Label label_y = new Label("Y:");
            TextField textField_x = new TextField ();
            TextField textField_y = new TextField ();
            Button image_button = new Button("_Apply");
            FlowPane flowpane = new FlowPane();
            flowpane.getChildren().addAll(label_x, textField_x, label_y, textField_y, image_button);
            flowpane.setHgap(HORIZONTAL_GAP);

            Scene imagesize_scene = new Scene(flowpane);
            Stage imagesize_window = new Stage();
            imagesize_window.setTitle("Settings");
            imagesize_window.setScene(imagesize_scene);
            imagesize_window.show();

            image_button.setOnAction(events -> {
                try{
                    canvas.setWidth(Integer.parseInt(textField_x.getText()));
                    canvas.setHeight(Integer.parseInt(textField_y.getText()));
                    imagesize_window.close();
                    canvas.setStyle("-fx-background-color: " + "#FFFFFF" + ";");
                }
                catch(IllegalArgumentException e){
                    System.out.println("Error Changing Image Size");
                }
                if ((int)canvas.getHeight() == Integer.parseInt(textField_x.getText())){
                    System.out.println("Unit Test Success, canvas width = text field");
                } else {
                    System.out.println("Unit Test Failure, canvas width != text field");
                }
            });
        });

        // Sets the action for the help button to display "helpful" information
        menu_help_about.setOnAction(event -> {
            Alert alert_help = new Alert(Alert.AlertType.INFORMATION);
            alert_help.setContentText("Version Number: " + main.version_number + "\nAuthor: Jacob Leazott");
            alert_help.setTitle(main.version_number);
            alert_help.show();
        });

        menu_help_notes.setOnAction(event ->{
            File file = new File("src/Release_Notes_V.1.0.5.txt");
            try{
                Desktop.getDesktop().open(file);
            } catch (IOException e){
                System.out.println("Error opening file");
            }
        });

        update_color();
        // update the canvas and the gc with its changes
        window.set_Canvas(canvas);
        // window.set_gc(gc);
        return menubar;
}}
