package com.luhttpclient.main;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("deprecation")
public class LuHTTPClient extends Application {
	private TextField inputField;
	private TextArea outputArea;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) {
		Label headerLabel = new Label("Input URL and hit GO!");
		Label inputLabel = new Label("URL: ");
		Button okButton = new Button("GO");

		okButton.setOnAction(new EventHandler() {
			@Override
			public void handle(Event event) {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(inputField.getText());

				getRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
				getRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				getRequest.addHeader("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
				getRequest.addHeader("DNT", "1");
				getRequest.addHeader("Connection", "keep-alive");
				
				try {
					HttpResponse response = httpClient.execute(getRequest);
					BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
					
					String output;
					StringBuilder data = new StringBuilder();
					while ((output = br.readLine()) != null) {
						data.append(output);
					}

					outputArea.setText(data.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		inputField = new TextField();
		outputArea = new TextArea();

		BorderPane root = new BorderPane();
		root.setTop(headerLabel);
		root.setRight(okButton);
		root.setBottom(outputArea);
		root.setLeft(inputLabel);
		root.setCenter(inputField);

		BorderPane.setAlignment(headerLabel, Pos.BOTTOM_CENTER);
		BorderPane.setAlignment(inputLabel, Pos.CENTER_LEFT);
		BorderPane.setAlignment(okButton, Pos.CENTER_RIGHT);

		root.setStyle("-fx-padding: 10;");
		root.setStyle("-fx-border-style: solid inside;");
		root.setStyle("-fx-border-width: 2;");
		root.setStyle("-fx-border-insets: 5;");
		root.setStyle("-fx-border-radius: 5;");
		root.setStyle("-fx-border-color: blue;");

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("LuHTTPClient");
		stage.show();
	}

}
