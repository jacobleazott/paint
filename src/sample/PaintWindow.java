package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;

public class PaintWindow {

    public Canvas canvas;
    public GraphicsContext gc;
    public ScrollPane scrollpane;
    public Rectangle2D primaryScreenBounds;

    public Scene setup_scene(){


    Canvas canvas = new Canvas(0, 0);
    gc = canvas.getGraphicsContext2D();
    gc.setLineWidth(1);

    // Grabs the resolution that the window is being displayed on to better display the window on startup
    primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    int window_startup_width = (int) (primaryScreenBounds.getWidth()/1.5);
    int window_startup_height = (int) (primaryScreenBounds.getHeight()/1.5);

    //Wrapper for our layouts and special menu bars
    StackPane result = new StackPane(canvas);
    Region target = result;
    Group group = new Group(target);
    BorderPane borderpane = new BorderPane();
    borderpane.setCenter(group);
    //borderpane.setStyle("-fx-background-color:#FFFF00;");
    scrollpane = new ScrollPane(borderpane);
    //scrollpane.setStyle("-fx-background-color:#FF0000;");
    scrollpane.setFitToWidth(true);
    scrollpane.setFitToHeight(true);
    scrollpane.setPannable(true);

    PaintMenuBar menubar = new PaintMenuBar();

    VBox window = new VBox(menubar.setup_menubar(), scrollpane);


    // Uses the wrapper and given window starting sizes to create a scene which is yet another wrapper

    Scene main_scene = new Scene(window, window_startup_width, window_startup_height);

    scrollpane.setFitToHeight(true);
    scrollpane.setFitToWidth(true);
    borderpane.prefWidthProperty().bind(main_scene.widthProperty());
    borderpane.prefHeightProperty().bind(main_scene.heightProperty());

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

    return main_scene;

    }
}
