package dev.lucasm.pratice.demo1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;

public class HelloApplication extends Application {

    private Connection connection;

    @Override
    public void start(Stage stage) {
        // Conectar ao banco de dados
        connectToDatabase();

        // Inicia com a cena principal
        Scene mainScene = createMainScene(stage);
        stage.setScene(mainScene);
        stage.setTitle("Sua Coleção De Jogos");

        // Força a tela a ficar em tela cheia
        stage.setMaximized(true);

        // Exibe a janela em tela cheia
        stage.show();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/collection";
            String username = "root";
            String password = "eeugdhs@0312";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conectado ao banco de dados.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Scene createMainScene(Stage stage) {
        VBox root = new VBox();
        root.setSpacing(30);
        root.setAlignment(Pos.TOP_LEFT);

        HBox header = createHeader(stage);
        GridPane grid = createGameGrid(stage);

        root.getChildren().addAll(header, grid);

        Scene scene = new Scene(root, 920, 600);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        return scene;
    }

    private HBox createHeader(Stage stage) {
        HBox topBox = new HBox();
        topBox.setPrefHeight(50);
        topBox.setMaxWidth(Double.MAX_VALUE);
        topBox.setSpacing(10);
        topBox.setAlignment(Pos.CENTER);

        Label label = new Label("Sua Coleção De Jogos !");
        label.setId("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addButton = new Button("Adicionar Jogo");
        addButton.setId("add-button");
        addButton.setOnAction(e -> stage.setScene(createAddGameScene(stage, null)));

        topBox.getChildren().addAll(label, spacer, addButton);
        return topBox;
    }

    private GridPane createGameGrid(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_LEFT);

        try {
            String sql = "SELECT id, game_name, company, release_date, image_path, game_status FROM games";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            int row = 0; // linha no grid
            int col = 0; // coluna no grid

            while (resultSet.next()) {
                int gameId = resultSet.getInt("id");
                String gameName = resultSet.getString("game_name");
                String company = resultSet.getString("company");
                String releaseDate = resultSet.getString("release_date");
                String imagePath = resultSet.getString("image_path");
                String status = resultSet.getString("game_status");

                VBox card = createGameCard(stage, gameId, gameName, company, releaseDate, imagePath, status);

                grid.add(card, col, row);
                col++;
                if (col == 11) { // Limite de 11 colunas
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return grid;
    }

    private VBox createGameCard(Stage stage, int gameId, String gameName, String company,
                                String releaseDate, String imagePath, String status) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-border-color: gray; -fx-padding: 10;");

        ImageView imageView = new ImageView();
        if (imagePath != null && !imagePath.isEmpty()) {
            imageView.setImage(new Image("file:" + imagePath));
        }
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);

        Label nameLabel = new Label(gameName);
        nameLabel.setStyle("-fx-font-size: 14px;");

        Button deleteButton = new Button("Excluir");
        deleteButton.setOnAction(e -> handleDeleteGame(stage, gameId));

        card.getChildren().addAll(imageView, nameLabel, deleteButton);

        card.setOnMouseClicked(e -> {
            if (e.getTarget() != deleteButton) {
                stage.setScene(createAddGameScene(stage, new Game(gameId, gameName, company, releaseDate, imagePath, status)));
            }
        });

        return card;
    }

    private Scene createAddGameScene(Stage stage, Game game) {
        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label(game == null ? "Adicionar Novo Jogo" : "Editar Jogo");
        title.setId("form-title");

        TextField gameNameField = createTextField("Nome do Jogo");
        TextField companyNameField = createTextField("Nome da Empresa");
        DatePicker releaseDatePicker = new DatePicker();
        releaseDatePicker.setPromptText("Data de Lançamento");

        FileChooser fileChooser = new FileChooser();
        Label selectedImageLabel = new Label("Nenhuma imagem selecionada");

        Button selectImageButton = new Button("Selecionar Imagem");
        selectImageButton.setOnAction(e -> handleImageSelection(stage, fileChooser, selectedImageLabel));

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Jogo Zerado", "Ainda Jogando");
        statusComboBox.setPromptText("Selecione o status do jogo");

        if (game != null) {
            gameNameField.setText(game.getName());
            companyNameField.setText(game.getCompany());
            if (game.getReleaseDate() != null) {
                releaseDatePicker.setValue(java.time.LocalDate.parse(game.getReleaseDate()));
            }
            selectedImageLabel.setText(game.getImagePath());
            statusComboBox.setValue(game.getStatus());
        }

        Button saveButton = createSaveButton(stage, gameNameField, companyNameField, releaseDatePicker, selectedImageLabel, statusComboBox, game);
        Button backButton = createBackButton(stage);

        root.getChildren().addAll(title, gameNameField, companyNameField, releaseDatePicker, selectImageButton, selectedImageLabel, statusComboBox, saveButton, backButton);

        Scene scene = new Scene(root, 920, 600);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());

        // Maximizar a tela para a cena de adicionar/editar
        stage.setMaximized(true);

        return scene;
    }


    private Button createSaveButton(Stage stage, TextField gameNameField, TextField companyNameField,
                                    DatePicker releaseDatePicker, Label selectedImageLabel, ComboBox<String> statusComboBox, Game game) {
        Button saveButton = new Button("Salvar");
        saveButton.setOnAction(e -> {
            String gameName = gameNameField.getText().trim();
            String companyName = companyNameField.getText().trim();
            String releaseDate = releaseDatePicker.getValue() != null ? releaseDatePicker.getValue().toString() : null;
            String imagePath = selectedImageLabel.getText();
            String status = statusComboBox.getValue();

            if (gameName.isEmpty() || status == null || status.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Preencha todos os campos obrigatórios!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            try {
                if (game == null) {
                    String sql = "INSERT INTO games (game_name, company, release_date, image_path, game_status) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, gameName);
                    stmt.setString(2, companyName);
                    stmt.setString(3, releaseDate);
                    stmt.setString(4, imagePath);
                    stmt.setString(5, status);
                    stmt.executeUpdate();
                } else {
                    String sql = "UPDATE games SET game_name = ?, company = ?, release_date = ?, image_path = ?, game_status = ? WHERE id = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, gameName);
                    stmt.setString(2, companyName);
                    stmt.setString(3, releaseDate);
                    stmt.setString(4, imagePath);
                    stmt.setString(5, status);
                    stmt.setInt(6, game.getId());
                    stmt.executeUpdate();
                }
                stage.setScene(createMainScene(stage));
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar o jogo no banco de dados.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        return saveButton;
    }

    private void handleDeleteGame(Stage stage, int gameId) {
        try {
            String sql = "DELETE FROM games WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, gameId);
            stmt.executeUpdate();
            stage.setScene(createMainScene(stage));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao excluir o jogo.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    private void handleImageSelection(Stage stage, FileChooser fileChooser, Label selectedImageLabel) {
        fileChooser.setTitle("Selecione uma Imagem");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            selectedImageLabel.setText(selectedFile.getAbsolutePath());
        }
    }

    private Button createBackButton(Stage stage) {
        Button backButton = new Button("Voltar");
        backButton.setOnAction(e -> stage.setScene(createMainScene(stage)));
        return backButton;
    }

    public static void main(String[] args) {
        launch();
    }
}

class Game {
    private final int id;
    private final String name;
    private final String company;
    private final String releaseDate;
    private final String imagePath;
    private final String status;

    public Game(int id, String name, String company, String releaseDate, String imagePath, String status) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.releaseDate = releaseDate;
        this.imagePath = imagePath;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }
}
