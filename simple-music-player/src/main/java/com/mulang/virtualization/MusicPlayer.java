package com.mulang.virtualization;

import java.io.File;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayer extends Application {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private int currentSongIndex = -1; // Index of currently playing song
    private ObservableList<String> playlist = FXCollections.observableArrayList();
    private ListView<String> playlistView = new ListView<>(playlist);
    private Slider volumeSlider = new Slider(0, 100, 50);
    private Slider progressSlider = new Slider();
    private Label progressLabel = new Label("00:00 / 00:00");
    private Button playPauseButton = new Button("Play");
    private Button stopButton = new Button("Stop");
    private Button previousButton = new Button("Previous");
    private Button nextButton = new Button("Next");
    private Canvas spectrumCanvas = new Canvas(600, 100);
    private GraphicsContext gc = spectrumCanvas.getGraphicsContext2D();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Music Player");

        // Media controls
        Button openButton = new Button("Open");
        Button addToPlaylistButton = new Button("Add to Playlist");

        // File chooser for selecting music files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
        fileChooser.setTitle("Select Music Files");

        openButton.setOnAction(e -> {
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    addToPlaylist(file);
                }
            }
        });

        addToPlaylistButton.setOnAction(e -> {
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    addToPlaylist(file);
                }
            }
        });

        playlistView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item ==null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(new File(item).getName()); // Display file name
                    if (getIndex() == currentSongIndex) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        playlistView.setOnMouseClicked(e -> {
            if (!playlistView.getSelectionModel().isEmpty()) {
                currentSongIndex = playlistView.getSelectionModel().getSelectedIndex();
                String selectedFile = playlist.get(currentSongIndex);
                playSelectedFile(selectedFile);
            }
        });

        playPauseButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                } else {
                    mediaPlayer.play();
                    playPauseButton.setText("Pause");
                }
                isPlaying = !isPlaying;
            }
        });

        stopButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                playPauseButton.setText("Play");
                isPlaying = false;
            }
        });

        previousButton.setOnAction(e -> playPreviousSong());

        nextButton.setOnAction(e -> playNextSong());

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        progressSlider.valueChangingProperty().addListener((observable, wasChanging, isChanging) -> {
            if (!isChanging && mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progressSlider.getValue() / 100.0));
            }
        });

        // Layout setup
        HBox controlButtons = new HBox(10, openButton, addToPlaylistButton, previousButton, playPauseButton, nextButton, stopButton);
        VBox playlistBox = new VBox(new Label("Playlist"), playlistView);
        VBox controls = new VBox(10, controlButtons, playlistBox);
        VBox root = new VBox(10, controls, spectrumCanvas, progressSlider, progressLabel, new Label("Volume"), volumeSlider);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        VBox.setVgrow(playlistView, Priority.ALWAYS);

        // Load CSS
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
    }

    private void resizeCanvas() {
        spectrumCanvas.setWidth(spectrumCanvas.getScene().getWidth());
        spectrumCanvas.setHeight(100); // Fixed height for spectrum display
    }

    private void addToPlaylist(File file) {
        String filePath = file.toURI().toString();
        playlist.add(filePath); // Add full file path to playlist
        updatePlaylistView(); // Update display list
    }

    private void updatePlaylistView() {
        ObservableList<String> displayList = FXCollections.observableArrayList();
        for (String filePath : playlist) {
            File file = new File(filePath);
            displayList.add(file.getName()); // Add file name to display list
        }
        playlistView.setItems(displayList);
    }

    private void playSelectedFile(String selectedFile) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Media media = new Media(selectedFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
        mediaPlayer.setOnReady(() -> {
            playPauseButton.setText("Pause");
            isPlaying = true;
            stopButton.setDisable(false);
            progressSlider.setDisable(false);
            progressSlider.setMax(100); // Set as percentage
            progressLabel.setText("00:00 / " + formatTime(mediaPlayer.getTotalDuration()));

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (!progressSlider.isValueChanging()) {
                    progressSlider.setValue(newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 100);
                }
                progressLabel.setText(formatTime(newValue) + " / " + formatTime(mediaPlayer.getTotalDuration()));
            });

            setupSpectrumAnalyzer();
        });

        mediaPlayer.setOnEndOfMedia(this::playNextSong);

        mediaPlayer.play();
        updatePlaylistView(); // Update playlist view
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            String previousFile = playlist.get(currentSongIndex);
            playSelectedFile(previousFile);
        }
    }

    private void playNextSong() {
        if (currentSongIndex < playlist.size() - 1) {
            currentSongIndex++;
            String nextFile = playlist.get(currentSongIndex);
            playSelectedFile(nextFile);
        }
    }

    private void setupSpectrumAnalyzer() {
        if (mediaPlayer != null) {
            mediaPlayer.setAudioSpectrumNumBands(40); // Set number of bands for spectrum analysis
            mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
                gc.clearRect(0, 0, spectrumCanvas.getWidth(), spectrumCanvas.getHeight());
                gc.setFill(javafx.scene.paint.Color.GREEN);
                double barWidth = spectrumCanvas.getWidth() / magnitudes.length;
                double centerY = spectrumCanvas.getHeight() / 2;

                for (int i = 0; i < magnitudes.length; i++) {
                    double height = magnitudes[i] + 60; // Adjust amplitude scaling
                    gc.fillRect(i * barWidth, centerY - height / 2, barWidth, height);
                }
            });
        }
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}

