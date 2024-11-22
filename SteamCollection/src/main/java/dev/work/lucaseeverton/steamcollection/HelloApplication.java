package dev.work.lucaseeverton.steamcollection;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        // Header
        HBox topBox = new HBox();
        topBox.setPrefHeight(50);
        topBox.setMaxWidth(Double.MAX_VALUE);
        topBox.setSpacing(30);
        topBox.setAlignment(Pos.CENTER);

        // Header -> H1
        Label label = new Label("Busque na sua coleção");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 5px 10px; -fx-font-weight: bold");

        // Header -> SearchBar
        TextField searchField = new TextField();
        searchField.setPromptText("Digite aqui...");
        searchField.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: white; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px; " +
                        "-fx-text-fill: white; "
        );
        searchField.setPrefWidth(300); // Largura do campo de texto
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Header -> Adding Items Into Header
        topBox.getChildren().addAll(label, searchField, spacer);


        // Grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Grid -> Example Card
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);

        // Grid -> Creating Cards
        grid.add(createImageCard("Deathloop", "file:path/to/your/image1.jpg"), 0, 0);
        grid.add(createImageCard("Deathloop 2", "file:path/to/your/image2.jpg"), 1, 0);

        // Main Box as Root
        VBox root = new VBox(topBox, grid);
        Scene scene = new Scene(root, 920, 600);
        scene.getStylesheets().add(HelloApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("HBox com largura 100%");
        stage.show();
    }

    private VBox createImageCard(String name, String imagePath) {
        // Criando o VBox para o card
        VBox card = new VBox();
        card.setSpacing(10);

        // Carregando a imagem
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/deathloop.jpg")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(false);

        // Criando o label com o nome
        Label label = new Label(name);

        // Adicionando a imagem e o nome à VBox
        card.getChildren().addAll(imageView, label);

        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}
