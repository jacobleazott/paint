package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.RadioButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PaintSettingsWindow {

    private Scene settings_scene;
    private Stage settings_window;
    private RadioButton darkmode_button;
    private RadioButton lightmode_button;
    private ChoiceBox<String> display_theme_choicebox;
    private String background_string = "#2D2D2D";
    private String font_string = "#EFEFEF";
    private GridPane gridpane_settings;
    private GridPane gridpane;
    private List<Button> settings_list;
    private SplitPane splitpane;

    PaintSettingsWindow() {

        String colors[][] = {{"#3C3F41", "#313335", "2B2B2B", "#BBBBBB", "#606366"}
        };
        //background_value.set(Paint.valueOf("3D3D3D"));
        //String background_string = "#2D2D2D";
        gridpane = new GridPane();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int window_startup_width = (int) (primaryScreenBounds.getWidth() / 5);
        int window_startup_height = (int) (primaryScreenBounds.getHeight() / 3);

        String[] display_theme_options = {"Dark Mode", "Light Mode"};
        display_theme_choicebox = new ChoiceBox<String>(FXCollections.observableArrayList(display_theme_options));
        gridpane.add(new Label("Display-----------------------------------------------------------------------"), 0, 0);
        gridpane.add(display_theme_choicebox, 0, 1);

        display_theme_choicebox.setValue("Dark Mode");

        String[] settings_options = {"General", "Appearance", "Tools", "About", "Directory"};

        settings_list = new ArrayList<>();

        gridpane_settings = new GridPane();
        gridpane_settings.setVgap(5);

        for (int i = 0; i < settings_options.length; i++) {
            Button tmp = new Button(settings_options[i]);
            tmp.setStyle("-fx-background-color: " + background_string + ";");
            tmp.setStyle("-fx-text-fill: " + font_string + ";");
            settings_list.add(new Button(settings_options[i]));
            gridpane_settings.add(settings_list.get(i), 0, i);
        }

        splitpane = new SplitPane();
        splitpane.getItems().add(gridpane_settings);
        splitpane.getItems().add(gridpane);
        System.out.println(window_startup_width);
        // Locks Settings display into two parts
        gridpane.maxWidthProperty().bind(splitpane.widthProperty().multiply(0.7));
        gridpane.minWidthProperty().bind(splitpane.widthProperty().multiply(0.7));
        gridpane_settings.setStyle("-fx-background-color: " + background_string + ";");
        //gridpane_settings.setStyle("-fx-text-fill: " + font_string + ";");


        settings_scene = new Scene(splitpane, window_startup_width, window_startup_height);
        settings_window = new Stage();
        settings_window.setTitle("Settings");
        settings_window.setScene(settings_scene);

        display_theme_choicebox.setOnAction(event -> {
            if (display_theme_choicebox.getValue().equals("Dark Mode")) {
                background_string = "#2D2D2D";
                font_string = "#EFEFEF";
            } else {
                background_string = "#EFEFEF";
                font_string = "#2D2D2D";
            }
            update_color();
        });
    }

    void setup() {
        System.out.println("hey");
    }

    void show() {
        settings_window.show();
    }

    String get_check() {
        gridpane_settings.setStyle("-fx-background-color: " + background_string + ";");
        return display_theme_choicebox.getValue();
    }

    void update_color() {
        Node divider = splitpane.lookup(".split-pane-divider");
        if (divider != null) {
            divider.setStyle("-fx-background-color: " + background_string + ";");
        }
        splitpane.setStyle("-fx-background-color: " + background_string + ";");
        gridpane_settings.setStyle("-fx-background-color: " + background_string + ";");
        //gridpane_settings.setStyle("-fx-text-fill: " + font_string + ";");
        gridpane.setStyle("-fx-background-color: " + background_string + ";");
        //gridpane.setStyle("-fx-text-fill: " + font_string + ";");
        for (Button button : settings_list) {
            button.setStyle("-fx-background-color: " + background_string + ";" + " -fx-text-fill: " + font_string + ";");
            //button.setStyle("-fx-text-fill: " + font_string + ";");
        }
    }

}
