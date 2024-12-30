package org.example.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Config;
import org.example.PasswordCracker;
import javafx.scene.layout.Priority;

import java.io.File;

public class FXApplication extends Application {
    private Button startButton;
    private Button pauseButton;
    private TextField cpuField;
    private File selectedFile;
    private Thread crackThread;
    private PasswordCracker currentCracker;
    private Label[] threadLabels;
    private Label initLogLabel;
    private Label resultLogLabel;
    private VBox processBox;
    private Label[] processLabels;

    @Override
    public void start(Stage stage) {
        // Root container
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Tự động điều chỉnh chiều rộng
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-background-color: #2b2b2b;");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b;");

        // Tiêu đề
        Label titleLabel = new Label("CHƯƠNG TRÌNH GIẢI MÃ FILE ZIP");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Sử dụng kỹ thuật đa luồng và phân chia CPU");
        subtitleLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14;");

        // Grid cho controls
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // CPU Input
        Label cpuLabel = new Label("CPU:");
        cpuLabel.setStyle("-fx-text-fill: white;");
        cpuField = new TextField();
        cpuField.setPromptText("Nhập CPU (vd: 0,1,2)");
        cpuField.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white;");

        // File Selection
        Button fileButton = new Button("Chọn File ZIP");
        fileButton.setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white;");
        Label fileLabel = new Label("Chưa chọn file");
        fileLabel.setStyle("-fx-text-fill: white;");

        // Buttons Container
        HBox buttonBox = new HBox(10);

        // Start Button
        startButton = new Button("Bắt đầu");
        startButton.setStyle("-fx-background-color: #365880; -fx-text-fill: white;");
        startButton.setDisable(true);

        // Pause Button
        pauseButton = new Button("Tạm dừng");
        pauseButton.setStyle("-fx-background-color: #365880; -fx-text-fill: white;");
        pauseButton.setDisable(true);

        buttonBox.getChildren().addAll(startButton, pauseButton);

        // Log Area với layout mới
        VBox logContainer = new VBox(10);
        logContainer.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 10;");

        // Tạo HBox để chứa 2 box bên trái và phải
        HBox mainBoxContainer = new HBox(10);
        mainBoxContainer.setStyle("-fx-background-color: #2b2b2b;");
        mainBoxContainer.setFillHeight(true);
        HBox.setHgrow(mainBoxContainer, Priority.ALWAYS);

        // 1. Process Status Box (Bên trái)
        VBox processBoxContainer = new VBox(5);
        processBoxContainer.setStyle("-fx-background-color: #2b2b2b;");
        HBox.setHgrow(processBoxContainer, Priority.ALWAYS);
        Label processTitle = new Label("Trạng thái xử lý");
        processTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        ScrollPane processScrollPane = new ScrollPane();
        processScrollPane.setStyle("-fx-background: #3c3f41; -fx-background-color: #3c3f41;");
        processScrollPane.setPrefViewportWidth(280);
        processScrollPane.setPrefViewportHeight(200);
        processScrollPane.setFitToWidth(true);
        processScrollPane.setFitToHeight(true);
        VBox.setVgrow(processScrollPane, Priority.ALWAYS);

        processBox = new VBox(5);
        processBox.setStyle("-fx-background-color: #3c3f41; -fx-padding: 10;");
        initLogLabel = new Label();
        initLogLabel.setStyle("-fx-text-fill: white;");
        processBox.getChildren().add(initLogLabel);

        processLabels = new Label[16];
        for (int i = 0; i < processLabels.length; i++) {
            processLabels[i] = new Label();
            processLabels[i].setStyle("-fx-text-fill: white;");
            processLabels[i].setVisible(false);
            processBox.getChildren().add(processLabels[i]);
        }

        processScrollPane.setContent(processBox);
        processScrollPane.prefWidthProperty().bind(
                mainBoxContainer.widthProperty().multiply(0.5).subtract(5));
        processBoxContainer.getChildren().addAll(processTitle, processScrollPane);

        // 2. Thread Status Box (Bên phải)
        VBox threadBoxContainer = new VBox(5);
        threadBoxContainer.setStyle("-fx-background-color: #2b2b2b;");
        HBox.setHgrow(threadBoxContainer, Priority.ALWAYS);
        Label threadTitle = new Label("Phân bổ CPU");
        threadTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        ScrollPane threadScrollPane = new ScrollPane();
        threadScrollPane.setStyle("-fx-background: #3c3f41; -fx-background-color: #3c3f41;");
        threadScrollPane.setPrefViewportWidth(280);
        threadScrollPane.setPrefViewportHeight(200);
        threadScrollPane.setFitToWidth(true);
        threadScrollPane.setFitToHeight(true);
        VBox.setVgrow(threadScrollPane, Priority.ALWAYS);

        VBox threadBox = new VBox(5);
        threadBox.setStyle("-fx-background-color: #3c3f41; -fx-padding: 10;");
        threadLabels = new Label[16];
        for (int i = 0; i < threadLabels.length; i++) {
            threadLabels[i] = new Label();
            threadLabels[i].setStyle("-fx-text-fill: white;");
            threadLabels[i].setVisible(false);
            threadBox.getChildren().add(threadLabels[i]);
        }

        threadScrollPane.setContent(threadBox);
        threadScrollPane.prefWidthProperty().bind(
                mainBoxContainer.widthProperty().multiply(0.5).subtract(5));
        threadBoxContainer.getChildren().addAll(threadTitle, threadScrollPane);

        // Thêm 2 box vào container chính
        mainBoxContainer.getChildren().addAll(processBoxContainer, threadBoxContainer);

        // 3. Result Box (Dưới cùng)
        VBox resultBoxContainer = new VBox(5);
        Label resultTitle = new Label("Kết quả");
        resultTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        VBox resultBox = new VBox(5);
        resultBox.setStyle("-fx-background-color: #3c3f41; -fx-padding: 10;");
        resultBox.setPrefHeight(70);
        resultLogLabel = new Label();
        resultLogLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-wrap-text: true;");
        resultBox.getChildren().add(resultLogLabel);

        resultBoxContainer.getChildren().addAll(resultTitle, resultBox);

        // Thêm tất cả vào container chính
        logContainer.getChildren().addAll(mainBoxContainer, resultBoxContainer);

        // Layout
        grid.add(cpuLabel, 0, 0);
        grid.add(cpuField, 1, 0);
        grid.add(fileButton, 0, 1);
        grid.add(fileLabel, 1, 1);
        grid.add(buttonBox, 0, 2, 2, 1);

        root.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                new Separator(),
                grid,
                new Separator(),
                logContainer);

        // Đặt root vào ScrollPane
        scrollPane.setContent(root);

        // Event Handlers
        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("ZIP files", "*.zip"));
            selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                fileLabel.setText(selectedFile.getName());
                startButton.setDisable(false);
                initLogLabel.setText("File đã chọn: " + selectedFile.getName());
            }
        });

        startButton.setOnAction(e -> {
            if (crackThread != null && crackThread.isAlive()) {
                currentCracker.setPaused(false);
                pauseButton.setDisable(false);
                startButton.setDisable(true);
                initLogLabel.setText("Tiếp tục tìm kiếm...");
                return;
            }

            String cpuInput = cpuField.getText().trim();
            if (cpuInput.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập CPU");
                return;
            }

            // Parse CPU input
            String[] cpuStrings = cpuInput.split(",");
            int[] selectedCores = new int[cpuStrings.length];
            try {
                for (int i = 0; i < cpuStrings.length; i++) {
                    selectedCores[i] = Integer.parseInt(cpuStrings[i].trim());
                }
            } catch (NumberFormatException ex) {
                showAlert("Lỗi", "CPU không hợp lệ");
                return;
            }

            // Disable/Enable controls
            startButton.setText("Tiếp tục");
            cpuField.setDisable(true);
            fileButton.setDisable(true);
            pauseButton.setDisable(false);

            // Reset và hiển thị log labels
            initLogLabel.setText("Đang xử lý file...");
            resultLogLabel.setText("");

            // Reset process labels
            for (Label label : processLabels) {
                label.setVisible(false);
            }

            // Hiển thị thông tin thread và CPU
            for (int i = 0; i < threadLabels.length; i++) {
                if (i < selectedCores.length) {
                    // Thread status box
                    threadLabels[i].setText("Thread " + i + " đã được gán vào CPU " + selectedCores[i]);
                    threadLabels[i].setVisible(true);

                    // Process status box - tạo label cho mỗi thread
                    processLabels[i].setText("Thread " + i + " thử mật khẩu: ");
                    processLabels[i].setVisible(true);
                } else {
                    threadLabels[i].setVisible(false);
                    processLabels[i].setVisible(false);
                }
            }

            // Start cracking in background
            crackThread = new Thread(() -> {
                try {
                    Config.SELECTED_CORES = selectedCores;
                    Config.THREAD_COUNT = selectedCores.length;
                    currentCracker = new PasswordCracker(selectedFile, Config.THREAD_COUNT) {
                        @Override
                        protected void log(int threadIndex, String message) {
                            Platform.runLater(() -> {
                                // Cập nhật label trong process box
                                processLabels[threadIndex].setText("Thread " + threadIndex + ": " + message);
                            });
                        }

                        @Override
                        protected void logResult(String message) {
                            Platform.runLater(() -> {
                                resultLogLabel.setText(message);
                            });
                        }
                    };
                    currentCracker.crackPassword();
                } catch (Exception ex) {
                    initLogLabel.setText("Lỗi: " + ex.getMessage());
                } finally {
                    Platform.runLater(() -> {
                        startButton.setText("Bắt đầu");
                        startButton.setDisable(false);
                        cpuField.setDisable(false);
                        fileButton.setDisable(false);
                        pauseButton.setDisable(true);
                    });
                }
            });
            crackThread.start();
        });

        pauseButton.setOnAction(e -> {
            if (currentCracker != null) {
                currentCracker.setPaused(true);
                initLogLabel.setText("Đã tạm dừng");
                pauseButton.setDisable(true);
                startButton.setDisable(false);
            }
        });

        Scene scene = new Scene(scrollPane, 600, 500);
        stage.setTitle("ZIP Password Cracker");
        stage.setScene(scene);
        stage.show();

        // Set kích thước tối thiểu cho cửa sổ
        stage.setMinWidth(400);
        stage.setMinHeight(300);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}