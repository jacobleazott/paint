package sample;

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

    PaintSettingsWindow(){
        GridPane gridpane = new GridPane();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int window_startup_width = (int) (primaryScreenBounds.getWidth()/3);
        int window_startup_height = (int) (primaryScreenBounds.getHeight()/3);

        String[] display_theme_options = { "Dark Mode", "Light Mode" };
        display_theme_choicebox = new ChoiceBox<>(FXCollections.observableArrayList(display_theme_options));
        gridpane.add(new Label("Display-----------------------------------------------------------------------"), 0, 0);
        gridpane.add(display_theme_choicebox, 0, 1);

        display_theme_choicebox.setValue("Dark Mode");

        String[] settings_options = { "General", "Appearance", "Tools", "About", "Directory" };

        List<Button> settings_list = new ArrayList<>();

        GridPane gridpane_settings = new GridPane();

        for(int i = 0; i < settings_options.length; i++){
            settings_list.add(new Button(settings_options[i]));
            gridpane_settings.add(settings_list.get(i), 0, i);
        }

        SplitPane splitpane = new SplitPane();
        splitpane.getItems().add(gridpane_settings);
        splitpane.getItems().add(gridpane);

        settings_scene = new Scene(splitpane, window_startup_width, window_startup_height);
        settings_window = new Stage();
        settings_window.setTitle("Settings");
        settings_window.setScene(settings_scene);
    }
    void setup(){
        System.out.println("hey");
    }
    void show(){
        settings_window.show();
    }
    String get_check(){
        return display_theme_choicebox.getValue();
    }
}
