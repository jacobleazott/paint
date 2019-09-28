package sample;

import javafx.geometry.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.control.MenuBar;
import javafx.scene.shape.*;
import javafx.scene.image.WritableImage;


import java.lang.reflect.InvocationTargetException;

// Holds and initializes all of the main internal window aspects
class PaintWindow {

    private final int INITIAL_CANVAS_SIZE_X = 0;
    private final int INITIAL_CANVAS_SIZE_Y = 0;
    private final double INITIAL_WINDOW_RATIO_X = 1.5;
    private final double INITIAL_WINDOW_RATIO_Y = 1.5;
    private final int GRAPHICS_CONTEXT_LINE_WIDTH = 1;
    private final double ZOOM_SCALE = 1.2;
    private final double ZOOM_LOWER_BOUND = 0;
    private final double ZOOM_UPPER_BOUND = 1;
    private Canvas canvas;
    private GraphicsContext gc;
    private MenuBar menubar;
    private Stage primaryStage;
    private BorderPane borderpane = new BorderPane();
    private String background_string = "#EEEEEE";
    private PaintDrawActions drawactions;
    private PaintMenuBar paintmenubar;
    private String[] color_theme;
    private Group gr = new Group();
    private Group group;


    ScrollPane scrollpane;

    // Constructors
    PaintWindow (Stage primaryStage){
        this.primaryStage = primaryStage; }

    PaintWindow(){}

    // Setters
    void set_Canvas(Canvas canvas){
        this.canvas = canvas; }

    void set_gc(GraphicsContext gc){
        this.gc = gc; }

    void set_color(String background_string){
        this.background_string = background_string;
        System.out.println(background_string);
        update_color();
    }

    void set_group(Group gr){
        //this.gr = gr;
        try{
            //System.out.println(gr.getChildren());
            //group.getChildren().add(gr);
            //borderpane.setRight(gr);
            //System.out.println(group.getChildren());
        }
        catch(NullPointerException e){
            System.out.println("hey");
        }
    }

    void addToGroup(Shape shape){
        gr.getChildren().add(shape);
    }

    void removeToGroup(Shape shape){
        gr = new Group();
        //group.getChildren().remove(shape);
    }

    void update_color(){
        borderpane.setStyle("-fx-background-color: " + color_theme[1] + ";");
        /*
        menubar.getMenus().get(0).getItems().get(0).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(0).getItems().get(1).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(0).getItems().get(2).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(0).getItems().get(3).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");

        menubar.getMenus().get(0).getItems().get(0).setStyle("-fx-background-color: " + color_theme[2] + ";"+
                " -fx-text-fill: " + color_theme[3] + ";" + " -fx-color: " + color_theme[2]);
        menubar.getMenus().get(0).getItems().get(1).setStyle("-fx-background-color: " + color_theme[2] + ";"+
                " -fx-text-fill: " + color_theme[3] + ";" + " -fx-color: " + color_theme[2]);
        menubar.getMenus().get(0).getItems().get(2).setStyle("-fx-background-color: " + color_theme[2] + ";"+
                " -fx-text-fill: " + color_theme[3] + ";" + " -fx-color: " + color_theme[2]);
        menubar.getMenus().get(0).getItems().get(3).setStyle("-fx-background-color: " + color_theme[2] + ";"+
                " -fx-text-fill: " + color_theme[3] + ";" + " -fx-color: " + color_theme[2]);
        menubar.getMenus().get(0).getItems().get(4).setStyle("-fx-background-color: " + color_theme[2] + ";"+
                " -fx-text-fill: " + color_theme[3] + ";" + " -fx-color: " + color_theme[2]);

        menubar.getMenus().get(1).getItems().get(0).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(2).getItems().get(0).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(1).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(2).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        menubar.getMenus().get(0).setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
        //menubar.setStyle("-fx-background-color: " + color_theme[2] + ";");
        menubar.setStyle("-fx-background-color: " + color_theme[2] + ";"+ " -fx-text-fill: " + color_theme[3] + ";");
         */
    }

    Scene setup_Scene(){
        // Initializes our canvas to where we will draw and the gc which is the handler for the graphics
        canvas = new Canvas(INITIAL_CANVAS_SIZE_X, INITIAL_CANVAS_SIZE_Y);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(GRAPHICS_CONTEXT_LINE_WIDTH);

        // Sets up the buttons for drawing and undoing
        drawactions = new PaintDrawActions();

        // Initialize our menubar and pass it our canvas, gc, and stage
        paintmenubar = new PaintMenuBar(canvas, gc, primaryStage, drawactions);
        //background_string = paintmenubar.get_color();
        //update_color();
        // Wrap it all up into one nice VBox


        /*
        Line line = new Line();

        //Setting the properties to a line
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(500.0);
        line.setEndY(150.0);

        gr.getChildren().add(line);
        */

        //g.getChildren().addAll(gr, target);
        //Group g = new Group(group);
        //borderpane.setStyle("-fx-background-color: " + background_string + ";");

        menubar = paintmenubar.setup_menubar();
        //group.getChildren().add(canvas);
        //Wrappers for our layouts to best optimize viewing
        Region target = new StackPane(canvas);
        group = new Group(target);
        //Group g = new Group();
        borderpane.setCenter(group);
        //borderpane.setRight(gr);
        //borderpane.setLeft(drawactions.setup(canvas, gc));
        scrollpane = new ScrollPane(borderpane);
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);

        BorderPane outerpane = new BorderPane();
        outerpane.setCenter(scrollpane);
        outerpane.setLeft(drawactions.setup(canvas, gc));
        //scrollpane.setPannable(true);

        VBox window = new VBox(menubar, outerpane);

        drawactions.setImage(paintmenubar.getImage());

        // Grabs the resolution that the window is being displayed on to better display the window on startup
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int window_startup_width = (int) (primaryScreenBounds.getWidth()/INITIAL_WINDOW_RATIO_X);
        int window_startup_height = (int) (primaryScreenBounds.getHeight()/INITIAL_WINDOW_RATIO_Y);

        Scene main_scene = new Scene(window, window_startup_width, window_startup_height);

        // Make it so the scrollpane and the borderpane both fit to the window properly
        borderpane.prefWidthProperty().bind(main_scene.widthProperty());
        borderpane.prefHeightProperty().bind(main_scene.heightProperty());
        /*
        menubar.setOnMousePressed(event -> {
            background_string = paintmenubar.get_color();
            update_color();
        });

         */
        paintmenubar.get_settings().getStage().setOnCloseRequest(event ->{
            color_theme = paintmenubar.get_color();
            //background_string = paintmenubar.get_color();
            update_color();
        });
        /*
        menubar.getMenus().get(0).setOnShowing( event ->{
            background_string = paintmenubar.get_color();
            update_color();
        });
        */
        // Action event to allow zoom functionality
        borderpane.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();
                // These numbers need to be hardcoded for standard zoom factor
                final double zoomFactor = evt.getDeltaY() > ZOOM_LOWER_BOUND ? ZOOM_SCALE :
                        ZOOM_UPPER_BOUND / ZOOM_SCALE;
                Bounds groupBounds = group.getLayoutBounds();
                final Bounds viewportBounds = scrollpane.getViewportBounds();
                // calculate pixel offsets from [0, 1] range
                double valX = scrollpane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollpane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());
                // convert content coordinates to target coordinates
                Point2D posInZoomTarget = target.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(
                        zoomFactor - ZOOM_UPPER_BOUND));
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
                System.out.println(gr.getChildren());
        }});

        // Keyboard shortcuts
        KeyCombination kc_ctrl_s = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        Runnable rn_kc_ctrl_s = paintmenubar::saveAs;

        KeyCombination kc_ctrl_o = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        Runnable rn_kc_ctrl_o = paintmenubar::open;

        main_scene.getAccelerators().put(kc_ctrl_s, rn_kc_ctrl_s);
        main_scene.getAccelerators().put(kc_ctrl_o, rn_kc_ctrl_o);

    return main_scene;
}}
