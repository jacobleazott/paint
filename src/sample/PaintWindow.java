package sample;

import javafx.geometry.*;
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

// Holds and initializes all of the main internal window aspects
class PaintWindow {

    private Canvas canvas;
    private GraphicsContext gc;
    private Stage primaryStage;
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
        canvas = new Canvas(0, 0);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);

        // Sets up the buttons for drawing and undoing
        PaintDrawActions drawactions = new PaintDrawActions();

        //Wrappers for our layouts to best optimize viewing
        Region target = new StackPane(canvas);
        Group group = new Group(target);
        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(group);
        borderpane.setLeft(drawactions.setup(canvas, gc));
        scrollpane = new ScrollPane(borderpane);
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);
        //scrollpane.setPannable(true);

        // Initialize our menubar and pass it our canvas, gc, and stage
        PaintMenuBar menubar = new PaintMenuBar(canvas, gc, primaryStage);
        // Wrap it all up into one nice VBox
        VBox window = new VBox(menubar.setup_menubar(), scrollpane);

        // Grabs the resolution that the window is being displayed on to better display the window on startup
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int window_startup_width = (int) (primaryScreenBounds.getWidth()/1.5);
        int window_startup_height = (int) (primaryScreenBounds.getHeight()/1.5);

        Scene main_scene = new Scene(window, window_startup_width, window_startup_height);

        // Make it so the scrollpane and the borderpane both fit to the window properly
        borderpane.prefWidthProperty().bind(main_scene.widthProperty());
        borderpane.prefHeightProperty().bind(main_scene.heightProperty());

        // Action event to allow zoom functionality
        borderpane.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();
                // These numbers need to be hardcoded for standard zoom factor
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
        }});

    return main_scene;
}}
