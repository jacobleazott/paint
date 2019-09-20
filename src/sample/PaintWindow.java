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

// Holds and initializes all of the main internal window aspects
class PaintWindow {

    private Canvas canvas;
    private GraphicsContext gc;
    private Stage primaryStage;
    private final int INITIAL_CANVAS_SIZE_X = 0;
    private final int INITIAL_CANVAS_SIZE_Y = 0;
    private final double INITIAL_WINDOW_RATIO_X = 1.5;
    private final double INITIAL_WINDOW_RATIO_Y = 1.5;
    private final int GRAPHICS_CONTEXT_LINE_WIDTH = 1;
    private final double ZOOM_SCALE = 1.2;
    private final double ZOOM_LOWER_BOUND = 0;
    private final double ZOOM_UPPER_BOUND = 1;

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

    Scene setup_Scene(){
        // Initializes our canvas to where we will draw and the gc which is the handler for the graphics
        canvas = new Canvas(INITIAL_CANVAS_SIZE_X, INITIAL_CANVAS_SIZE_Y);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(GRAPHICS_CONTEXT_LINE_WIDTH);

        // Sets up the buttons for drawing and undoing
        PaintDrawActions drawactions = new PaintDrawActions();

        // Initialize our menubar and pass it our canvas, gc, and stage
        PaintMenuBar paintmenubar = new PaintMenuBar(canvas, gc, primaryStage, drawactions);
        // Wrap it all up into one nice VBox
        MenuBar menubar = paintmenubar.setup_menubar();

        //Wrappers for our layouts to best optimize viewing
        Region target = new StackPane(canvas);
        Group group = new Group(target);
        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(group);
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
