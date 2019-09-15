package sample;

import javafx.stage.FileChooser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaintFileChooser {
    public FileChooser setup(){
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

        return filechooser;
    }
}
