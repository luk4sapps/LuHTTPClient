package com.luhttpclient.main;

import com.luhttpclient.net.HTTPClientWrapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lukas on 14.03.2017.
 */

@SuppressWarnings("deprecation")
public class Boot extends Application {
    private ExecutorService execService;
    private HTTPClientWrapper httpClientWrapper;

    private TextField inputField;
    private TextArea outputArea;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void start(Stage stage) {
        loadGUI(stage);
    }

    private void loadGUI(Stage stage) {
        execService = Executors.newSingleThreadExecutor();
        httpClientWrapper = new HTTPClientWrapper();

        Label headerLabel = new Label("Input URL and hit Start!");
        Label inputLabel = new Label("URL: ");
        Button startButton = new Button("Process");

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (httpClientWrapper.isProcessing()) {
                    httpClientWrapper.cancel();
                    startButton.setText(httpClientWrapper.isProcessing() ? "Cancel" : "Process");
                } else {
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Platform.runLater(() -> {
                                startButton.setText(httpClientWrapper.isProcessing() ? "Cancel" : "Process");
                            });

                            try {
                                String output = httpClientWrapper.process(inputField.getText());
                                Platform.runLater(() -> {
                                    outputArea.setText(output);
                                });
                            } catch (IOException | IllegalStateException e) {
                                httpClientWrapper.cancel();
                                e.printStackTrace();
                                showExceptionDialog(e);
                            } finally {
                                Platform.runLater(() -> {
                                    startButton.setText(httpClientWrapper.isProcessing() ? "Cancel" : "Process");
                                });
                            }

                            return null;
                        }
                    };

                    execService.submit(task);
                }
            }
        });

        inputField = new TextField();
        outputArea = new TextArea();
        outputArea.setWrapText(true);

        BorderPane root = new BorderPane();
        root.setTop(headerLabel);
        root.setRight(startButton);
        root.setBottom(outputArea);
        root.setLeft(inputLabel);
        root.setCenter(inputField);

        BorderPane.setAlignment(headerLabel, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(inputLabel, Pos.CENTER_LEFT);
        BorderPane.setAlignment(startButton, Pos.CENTER_RIGHT);

        root.setStyle("-fx-padding: 10;");
        root.setStyle("-fx-border-style: solid inside;");
        root.setStyle("-fx-border-width: 2;");
        root.setStyle("-fx-border-insets: 5;");
        root.setStyle("-fx-border-radius: 5;");
        root.setStyle("-fx-border-color: blue;");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Boot");
        stage.show();
    }

    private void showExceptionDialog(Exception e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An exception occurred!");
            alert.setContentText(e.toString());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.show();
        });
    }

}
