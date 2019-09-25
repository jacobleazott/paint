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
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;


// Creates and initializes the menu bar with their associated actions
class PaintMenuBar{

    private File filechooser_file;
    private FileChooser filechooser;
    private Image img;
    private Canvas canvas;
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
        System.out.println("Start");
        System.out.println((int)canvas.getHeight() + " " + (int)canvas.getWidth());
        System.out.println((int)canvas.getHeight() + " " + (int)canvas.getWidth());
        WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableimage);
        System.out.println("End");
        // Opens up the file explorer and stores your file name/ location in filechooser_file
        filechooser_file = filechooser.showSaveDialog(primaryStage);
        // Saves the image to the desired location using the appropriate filters available
        try {
            System.out.println("1");
            ImageIO.write(SwingFXUtils.fromFXImage(writableimage, null), "png", filechooser_file);
            System.out.println("2");
        }
        // Catches if there is no selected location to save but no action necessary
        catch (IOException | IllegalArgumentException ignored) {
            System.out.println("Catch");
        }
        // Since we have a file location we can just regularly save it
        menu_file_save.setDisable(false);
        image_saved = true;
    }

    void save(){
        filechooser.setTitle("Save Image");
        // Saves the image to the desired location using the appropriate filters available
        try { ImageIO.write(SwingFXUtils.fromFXImage(img, null), "", filechooser_file); }
        // Catches if there is no selected location to save but no action necessary
        catch (IOException | IllegalArgumentException ignored) { }
        image_saved = true;
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
        menubar.setStyle("-fx-background-color: " + settings_window.get_color()[0] + ";"+
                " -fx-text-fill: " + settings_window.get_color()[3] + ";");
    }

    MenuBar setup_menubar() {
        // Use our custom filechooser class with our filters already applied
        PaintFileChooser chooser = new PaintFileChooser();
        filechooser = chooser.setup();
        // Creates the object for the menu bar at the top
        menubar = new MenuBar();
        // Creates the pull down tab "File" for the menu bar
        Menu menu_file = new Menu("_File");
        Menu menu_edit = new Menu("_Edit");
        Menu menu_help = new Menu("_Help");
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
        menu_help.getItems().add(menu_help_about);
        // Applies our "File" tab to our menu bar
        menubar.getMenus().addAll(menu_file, menu_edit, menu_help);
        // This disables our save function since when we launch program there is nothing to save
        menu_file_save_as.setDisable(true);
        menu_file_save.setDisable(true);

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
            //System.out.println(settings_window.get_color());
            //window.set_color(settings_window.get_color());
            //System.out.println(settings_window.get_check());
        });
        //test

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
            });
        });

        // Sets the action for the help button to display "helpful" information
        menu_help_about.setOnAction(event -> {
            Alert alert_help = new Alert(Alert.AlertType.INFORMATION);
            alert_help.setContentText("Version Number: " + main.version_number + "\nAuthor: Jacob Leazott");
            alert_help.setTitle(main.version_number);
            alert_help.show();
        });

        // update the canvas and the gc with its changes
        window.set_Canvas(canvas);
       // window.set_gc(gc);
        return menubar;
}}
