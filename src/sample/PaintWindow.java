/**
 * This file is responsible for housing everything for our program by creating the window itself
 * It is also responsible for
 * Threading for the autosave timer
 * Keyboard shortcuts
 * As well as wrapping everything together
 *
 * @author  Jacob Leazott
 * @version 1.0.6
 * @since   2019-10-06
 **/
package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.Label;
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
import javafx.util.Duration;
import java.util.Timer;

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
    private VBox draw_buttons;
    private Timer timer_a;
    private AnimationTimer timer_b;
    private Integer frame = 0;
    private Integer start_time = 15;
    private Timeline timeline;
    private Label time_label = new Label();
    private Integer time_seconds = start_time;
    private PaintToolTimer tooltimer = new PaintToolTimer();;


    ScrollPane scrollpane;

    // Constructors
    PaintWindow (Stage primaryStage){
        this.primaryStage = primaryStage; }

    PaintWindow(){}

    // Setters
    void set_Canvas(Canvas canvas){
        this.canvas = canvas; }

    //Updates the color theme of the window
    void update_color(){
        borderpane.getStylesheets().clear();
        borderpane.getStylesheets().add(paintmenubar.get_settings().getCSS());
        borderpane.getStyleClass().add(".borderpane");
        borderpane.setId("custom-label");
        draw_buttons.getStylesheets().clear();
        draw_buttons.getStylesheets().add(paintmenubar.get_settings().getCSS());
        draw_buttons.setId("custom-label");
        for (int i = 0; i < draw_buttons.getChildren().size(); i++){
            draw_buttons.getChildren().get(i).getStyleClass().add(".button");
            if (draw_buttons.getChildren().get(i) instanceof Label){
                draw_buttons.getChildren().get(i).getStyleClass().clear();
                draw_buttons.getChildren().get(i).setId("custom-label");
            }
        }
    }

    // Resets the timer for autosave
    void resetTimer(){
        start_time = paintmenubar.get_settings().autosave_time;
        setupTimer();
        timeline.playFromStart();
        paintmenubar.autoSave();
        if (paintmenubar.get_settings().display_auto_save.isSelected()){
            borderpane.setTop(time_label);
            BorderPane.setAlignment(time_label, Pos.TOP_LEFT);
        } else {
            borderpane.getChildren().remove(time_label);
        }
    }

    // Gives current tool to be used for displaying in the bottom left
    String setTool() {
        String tmp = drawactions.getSelected();
        Label tool = new Label(tmp);
        borderpane.setBottom(tool);
        return tmp;
    }

    // Sets up the timer for autosave
    void setupTimer(){
        if (timeline != null){
            timeline.stop();
        }
        time_seconds = start_time;
        time_label.setText(time_seconds.toString());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                time_seconds --;
                                time_label.setText(time_seconds.toString());
                                if (time_seconds <= 0){
                                    timeline.stop();
                                    resetTimer();
                                }
                            }}));
    }


    Scene setup_Scene(){

        tooltimer.beginTimer("Standby");

        // Initializes our canvas to where we will draw and the gc which is the handler for the graphics
        canvas = new Canvas(INITIAL_CANVAS_SIZE_X, INITIAL_CANVAS_SIZE_Y);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(GRAPHICS_CONTEXT_LINE_WIDTH);

        // Sets up the buttons for drawing and undoing
        drawactions = new PaintDrawActions();

        // Initialize our menubar and pass it our canvas, gc, and stage
        paintmenubar = new PaintMenuBar(canvas, gc, primaryStage, drawactions);

        menubar = paintmenubar.setup_menubar();
        //Wrappers for our layouts to best optimize viewing
        Region target = new StackPane(canvas);
        group = new Group(target);
        borderpane.setCenter(group);
        scrollpane = new ScrollPane(borderpane);
        scrollpane.setFitToWidth(true);
        scrollpane.setFitToHeight(true);

        BorderPane outerpane = new BorderPane();
        outerpane.setCenter(scrollpane);
        draw_buttons = drawactions.setup(canvas, gc);
        outerpane.setLeft(draw_buttons);

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

        time_label.setText(time_seconds.toString());
        resetTimer();

        borderpane.setOnMousePressed(event ->{
            //setTool();
            tooltimer.switchTool(setTool());
        });

        draw_buttons.setOnMousePressed(event ->{
            tooltimer.switchTool(setTool());
           //setTool();
        });

        paintmenubar.get_settings().getStage().setOnCloseRequest(event ->{
            update_color();
            paintmenubar.update_color();
            resetTimer();
        });

        menubar.getMenus().get(0).getItems().get(1).setOnAction(event ->{
            resetTimer();
            paintmenubar.saveAs();
        });

        menubar.getMenus().get(0).getItems().get(2).setOnAction(event ->{
            resetTimer();
            paintmenubar.save();
        });

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
                //System.out.println(gr.getChildren());
        }});

        primaryStage.setOnCloseRequest(event ->{
            tooltimer.end();
        });

        // Keyboard shortcuts
        KeyCombination kc_ctrl_s = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        Runnable rn_kc_ctrl_s = paintmenubar::saveAs;

        KeyCombination kc_ctrl_o = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        Runnable rn_kc_ctrl_o = paintmenubar::open;

        main_scene.getAccelerators().put(kc_ctrl_s, rn_kc_ctrl_s);
        main_scene.getAccelerators().put(kc_ctrl_o, rn_kc_ctrl_o);

    return main_scene;
}}
