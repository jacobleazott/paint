package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    private String version_number = "Pain(t) V.1.0.1";
    private Image img;

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
        //
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
        // Image is displayed (even if null like on start) in the upper left corner of the window)

        //ImageView myImageView = new ImageView(img);
        //gridpane.add(myImageView, 0, 0);


        // Adds the menu bar and the grid layout to our window
        //window.getChildren().add(menubar);
        //window.getChildren().add(gridpane);


        ////////////////////////////////////////////
        Canvas canvas = new Canvas(0, 0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);



        //BorderPane borderpane = new BorderPane();
        //borderpane.setCenter(canvas);
        //System.out.println(primaryStage.getWidth());

        //ScrollPane scrollpane = new ScrollPane(borderpane);
        //scrollpane.set

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int window_startup_width = (int) (primaryScreenBounds.getWidth()/1.5);
        int window_startup_height = (int) (primaryScreenBounds.getHeight()/1.5);
        //////////////////////////////////////////////

        //Wrapper for our layouts and special menu bars

        StackPane result = new StackPane(canvas);
        Region target = result;
        Group group = new Group(target);
        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(group);
        borderpane.setStyle("-fx-background-color:#FFFF00;");
        ScrollPane scrollpane = new ScrollPane(borderpane);
        scrollpane.setStyle("-fx-background-color:#FF0000;");
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);
        scrollpane.setPannable(true);
        VBox window = new VBox(menubar, scrollpane);

        // Uses the wrapper and given window starting sizes to create a scene which is yet another wrapper

        Scene main_scene = new Scene(window, window_startup_width, window_startup_height);
        // Applies our created scene to our default window
        primaryStage.setScene(main_scene);
        // Displays the window for the user to finally see
        primaryStage.show();

        //Makes sure the border pane scales with the window and that the scrollbars scale properly as well
        scrollpane.setFitToHeight(true);
        scrollpane.setFitToWidth(true);
        borderpane.prefWidthProperty().bind(main_scene.widthProperty());
        borderpane.prefHeightProperty().bind(main_scene.heightProperty());

        final DoubleProperty zoomProperty = new SimpleDoubleProperty(10);
        ////////////////////////////////////////////////////////////////////////////////
        window.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });

        borderpane.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();
                final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;
                Bounds groupBounds = group.getLayoutBounds();
                final Bounds viewportBounds = scrollpane.getViewportBounds();
                // calculate pixel offsets from [0, 1] range
                double valX = scrollpane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollpane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());
                // convert content coordinates to target coordinates
                Point2D posInZoomTarget = target.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));
                // do the resizing
                target.setScaleX(zoomFactor * target.getScaleX());
                target.setScaleY(zoomFactor * target.getScaleY());
                // refresh ScrollPane scroll positions & content bounds
                scrollpane.layout();
                // convert back to [0, 1] range
                // (too large/small values are automatically corrected by ScrollPane)
                groupBounds = group.getLayoutBounds();
                scrollpane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollpane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });


        //img.preserveRatioProperty().set(true);
        ////////////////////////////////////////////////////////////////////////////////


        // Eventually move these into their own .java to organize menu bar actions if more persist (they will)
        // Sets the action for the open button to open up the file chooser and open a valid image
        menu_file_open.setOnAction(event -> {
            // Sets the title of the file explorer window
            filechooser.setTitle("Open Image");
            // Opens up the file explorer and stores your file name/ location in filechooser_file
            File filechooser_file = filechooser.showOpenDialog(null);
            // Tries to read and display the image selected
            try {
                //img = SwingFXUtils.toFXImage(ImageIO.read(filechooser_file), null);
                InputStream io = new FileInputStream(filechooser_file);
                img = new Image(io);
                canvas.setHeight(img.getHeight());
                canvas.setWidth(img.getWidth());
                gc.drawImage(img, 0, 0);
                if (primaryStage.getHeight() < canvas.getHeight() && primaryScreenBounds.getHeight() < canvas.getHeight()){
                    primaryStage.setHeight(canvas.getHeight());
                }
                if (primaryStage.getWidth() < canvas.getWidth() && primaryScreenBounds.getWidth() < canvas.getWidth()){
                    primaryStage.setWidth(canvas.getWidth());
                }


                //myImageView.setImage(open_image);
                ////////////////////////////////////////////////////
                System.out.println(primaryStage.getWidth());
                System.out.println(primaryStage.getHeight());
                /*
                if (primaryStage.getWidth() >= primaryStage.getHeight()){
                    if (myImageView.getFitWidth() >= myImageView.getFitHeight()){
                        myImageView.setFitHeight()
                    }
                }
                */
                // myImageView.setFitWidth(primaryStage.getWidth());
                //myImageView.setFitHeight(primaryStage.getHeight()*4/3);
                //myImageView.setFitWidth(primaryStage.getWidth());
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
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "", filechooser_file);
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

        canvas.setOnDragDetected(evt -> {
            Node targets = (Node) evt.getTarget();
            while (targets != canvas && targets != null) {
                targets = targets.getParent();
            }
            if (targets != null) {
                targets.startFullDrag();
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
