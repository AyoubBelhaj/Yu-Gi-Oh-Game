package org.example.yugiohgame;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YugiohGameApp extends Application {
    private Game game;
    private Player player1, player2;
    private VBox player1UI, player2UI;
    private Label playerTurnLabel,phaseLabel;
    private Map<String, Image> imageCache = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Initialize game
        player1 = createPlayer("676ad312488357720829bf84");
        player2 = createPlayer("676ad33884741913cd0ece8c");
        game = new Game(player1, player2);
        game.setUI(this);

        preloadImages();

        // Create UI
        player1UI = createPlayerUI(player1, "Player 1", Color.LIGHTBLUE,"red");
        player2UI = createPlayerUI(player2, "Player 2", Color.LIGHTGREEN,"blue");

        // Control Buttons
        Button drawButton = createPhaseButton("Draw Phase");
        drawButton.setOnAction(e -> game.drawCard());

        Button playButton = createPhaseButton("Main Phase");
        playButton.setOnAction(e -> game.playCard());

        Button attackButton = createPhaseButton("Attack Phase");
        attackButton.setOnAction(e -> game.attack());

        Button endTurnButton = createPhaseButton("End Turn");
        endTurnButton.setOnAction(e -> game.endTurn());

        playerTurnLabel = new Label("Player 1's Turn");
        playerTurnLabel.setFont(new Font("Arial", 16));
        playerTurnLabel.setTextFill(Color.WHITE);

        phaseLabel = new Label("Phase: " + game.getCurrentPhase().getClass().getSimpleName());
        phaseLabel.setFont(new Font("Arial", 16));
        phaseLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox(10, drawButton, playButton, attackButton, endTurnButton, playerTurnLabel, phaseLabel);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, player1UI, controls, player2UI);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 5px; -fx-background-color: #444;");

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Yu-Gi-Oh! Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");

        updateUI();
    }

    private void preloadImages() {
        for (Card card : player1.getDeck()) {
            String imageUrl = card.getImageUrl();
            if (!imageCache.containsKey(imageUrl)) {
                System.out.println("Loading image for player 1: " + imageUrl);
                Image img = new Image(imageUrl, true);
                imageCache.put(imageUrl, img);
            }
        }
        for (Card card : player1.getHand()) {
            String imageUrl = card.getImageUrl();
            if (!imageCache.containsKey(imageUrl)) {
                System.out.println("Loading image for player 1 (hand): " + imageUrl);
                Image img = new Image(imageUrl, true);
                imageCache.put(imageUrl, img);
            }
        }
        for (Card card : player2.getDeck()) {
            String imageUrl = card.getImageUrl();
            if (!imageCache.containsKey(imageUrl)) {
                System.out.println("Loading image for player 2: " + imageUrl);
                Image img = new Image(imageUrl, true);
                imageCache.put(imageUrl, img);
            }
        }
        for (Card card : player2.getHand()) {
            String imageUrl = card.getImageUrl();
            if (!imageCache.containsKey(imageUrl)) {
                System.out.println("Loading image for player 2 (hand): " + imageUrl);
                Image img = new Image(imageUrl, true);
                imageCache.put(imageUrl, img);
            }
        }
    }



    private VBox createPlayerUI(Player player, String name, Color bgColor, String lifeColor) {
        Text playerName = new Text(name);
        playerName.setFont(new Font("Arial", 18));
        playerName.setFill(Color.WHITE);

        Label lifePointsLabel = new Label("Life Points: " + player.getLifePoints());
        lifePointsLabel.setFont(new Font("Arial", 16));
        lifePointsLabel.setTextFill(Color.WHITE);
        lifePointsLabel.setStyle("-fx-background-color: " + lifeColor + "; -fx-text-fill: white; " +
                "-fx-min-width: 90px; -fx-min-height: 90px; -fx-max-width: 120px; " +
                "-fx-max-height: 120px; -fx-alignment: center; -fx-background-radius: 50%;");
        HBox lifeAndPhase = new HBox(10, lifePointsLabel);
        lifeAndPhase.setAlignment(Pos.CENTER);

        HBox handAndField = new HBox(10, createHandUI(player), createCardSlot(player), createBonusControls(player),lifeAndPhase);
        handAndField.setAlignment(Pos.CENTER);

        Label attackLabel = new Label("Attack: N/A");
        attackLabel.setFont(new Font("Arial", 16));
        attackLabel.setTextFill(Color.WHITE);

        Label defenseLabel = new Label("Defense: N/A");
        defenseLabel.setFont(new Font("Arial", 16));
        defenseLabel.setTextFill(Color.WHITE);

        VBox playerUI = new VBox(10, playerName, handAndField, attackLabel, defenseLabel);
        playerUI.setAlignment(Pos.CENTER);
        playerUI.setPadding(new Insets(5));
        applyBackgroundImage(playerUI, name.equals("Player 1") ? "player1_bg.png" : "player2_bg.png");

        return playerUI;
    }


    private VBox createBonusControls(Player player) {
        Button bonus100Button = createCircularButton("+100 ATK");
        Button bonus500Button = createCircularButton("+500 ATK");
        Button bonus1000Button = createCircularButton("+1000 ATK");

        bonus100Button.setOnAction(
                e -> { player.applyBonus(100);
                    updateUI();
                });

        bonus500Button.setOnAction(e -> {player.applyBonus(500);  updateUI();});
        bonus1000Button.setOnAction(e -> {player.applyBonus(1000);  updateUI();});

        VBox bonusControls = new VBox(10, bonus100Button, bonus500Button, bonus1000Button);
        bonusControls.setAlignment(Pos.CENTER_LEFT);
        return bonusControls;
    }

    private VBox createCardSlot(Player player) {
        Text slotLabel = new Text("Monster Slot");
        slotLabel.setFill(Color.WHITE);

        ImageView cardImage = new ImageView();
        cardImage.setFitWidth(100);
        cardImage.setFitHeight(150);
        cardImage.setOnMouseClicked(e -> {
            if (!(game.getCurrentPhase() instanceof AttackPhase) && player.getMonsterSlot() != null) {
                player.changeMonsterPosition();
                updateUI();
            }
        });

        VBox slot = new VBox(5, slotLabel, cardImage);
        slot.setAlignment(Pos.CENTER);
        slot.setStyle("-fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");
        return slot;
    }

    private Player createPlayer(String deckId) {
        List<Card> deck = CardLoader.loadDeckFromDatabase(deckId);
        return new Player(deck);
    }

    private HBox createHandUI(Player player) {
        HBox handUI = new HBox(5);
        handUI.setPadding(new Insets(10));
        handUI.setStyle("-fx-border-color: white; -fx-border-width: 2;");
        handUI.setAlignment(Pos.CENTER_LEFT);
        updateHandUI(handUI, player);
        return handUI;
    }

    private void updateHandUI(HBox handUI, Player player) {
        handUI.getChildren().clear();

        int maxCardsDisplayed = Math.min(3, player.getHand().size());
        for (int i = 0; i < maxCardsDisplayed; i++) {
            Card card = player.getHand().get(i);
            String imageUrl = card.getImageUrl();

            ImageView cardImage = new ImageView(imageCache.get(imageUrl));
            cardImage.setFitWidth(75);
            cardImage.setFitHeight(100);

            VBox tooltipContent = new VBox(5);
            Button playButton = new Button("Play");
            Button removeButton = new Button("Remove");

            playButton.setOnAction(ev -> {
                runInBackground(() -> {
                    player.playCardFromHand(player.getHand().indexOf(card));
                    Platform.runLater(() -> {
                        updateHandUI(handUI, player);
                        updateUI();
                    });
                });
            });

            removeButton.setOnAction(ev -> {
                runInBackground(() -> {
                    player.removeCardFromHand(player.getHand().indexOf(card));
                    Platform.runLater(() -> updateHandUI(handUI, player));
                });
            });

            tooltipContent.getChildren().addAll(playButton, removeButton);
            CustomTooltip tooltip = new CustomTooltip(cardImage, tooltipContent);

            Tooltip.install(cardImage, tooltip);
            handUI.getChildren().add(cardImage);
        }
    }



    private void applyAttackBonus(Player player, int bonus) {
        if (player.getMonsterSlot() != null) {
            player.setMonsterSlot(new BuffedCard(player.getMonsterSlot(), bonus));
            updateUI();
        } else {
            System.out.println("No monster on the field to apply bonus.");
        }
    }

    public void updateUI() {
        Platform.runLater(() -> {
            updatePlayerUI(player1UI, player1);
            updatePlayerUI(player2UI, player2);
            updatePhaseLabel();
            updateTurnLabel();
        });
    }

    private void updatePlayerUI(VBox playerUI, Player player) {

        HBox handAndField = (HBox) playerUI.getChildren().get(1);
        HBox handUI = (HBox) handAndField.getChildren().get(0);
        VBox monsterSlot = (VBox) handAndField.getChildren().get(1);
        HBox lifeAndPhase = (HBox) handAndField.getChildren().get(3);
        Label lifePointsLabel = (Label) lifeAndPhase.getChildren().get(0);
        Label attackLabel = (Label) playerUI.getChildren().get(2);
        Label defenseLabel = (Label) playerUI.getChildren().get(3);
        ImageView monsterImage = (ImageView) monsterSlot.getChildren().get(1);

        updateHandUI(handUI, player);

        lifePointsLabel.setText("Life Points: " + player.getLifePoints());

        if (game.getCurrentPlayer() == player) {
            handUI.setVisible(true);
        } else {
            handUI.setVisible(false);
        }

        if (player.getMonsterSlot() != null) {
            Text slotLabel = (Text) monsterSlot.getChildren().get(0);
            slotLabel.setText(player.getMonsterSlot().getName());
            monsterImage.setImage(imageCache.get(player.getMonsterSlot().getImageUrl()));
            if (player.isMonsterInAttackPosition()) {
                monsterImage.setRotate(0);
                attackLabel.setText("Attack: " + player.getMonsterSlot().getAttack());
                attackLabel.setTextFill(player.getMonsterSlot() instanceof BuffedCard ? Color.RED : Color.WHITE);
                defenseLabel.setText("Defense: " + player.getMonsterSlot().getDefense());
            } else {
                monsterImage.setRotate(90);
                attackLabel.setText("Attack: " + player.getMonsterSlot().getAttack());
                attackLabel.setTextFill(player.getMonsterSlot() instanceof BuffedCard ? Color.RED : Color.WHITE);
                defenseLabel.setText("Defense: " + player.getMonsterSlot().getDefense());
            }
        } else {
            monsterImage.setImage(null);
            attackLabel.setText("Attack: N/A");
            defenseLabel.setText("Defense: N/A");
        }
    }

  private void updatePhaseLabel() {
       String phaseName = game.getCurrentPhase().getClass().getSimpleName();
       phaseLabel.setText(phaseName);
    }

    private void updateTurnLabel() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == player1) {
            playerTurnLabel.setText("Player 1's Turn");
        } else {
            playerTurnLabel.setText("Player 2's Turn");
        }
    }

    public void displayGameOver(String winner) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Game Over");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        vbox.setPrefSize(400, 300);
        applyBackgroundImage(vbox, "yugioh-win.png");

        Text message = new Text("Game Over! " + winner + " wins!");
        message.setFont(new Font("Arial", 20));

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {System.exit(0);});

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            resetGame();
            dialogStage.close();
        });

        HBox buttons = new HBox(10, exitButton, newGameButton);
        buttons.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(message, buttons);

        Scene scene = new Scene(vbox, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void resetGame() {
        player1 = createPlayer("676ad312488357720829bf84");
        player2 = createPlayer("676ad33884741913cd0ece8c");
        game = new Game(player1, player2);
        game.setUI(this);
        updateUI();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void applyBackgroundImage(Pane pane, String imagePath) {
        try {
            Image backgroundImage = new Image(getClass().getResource("/images/" + imagePath).toExternalForm());
            BackgroundImage background = new BackgroundImage(backgroundImage,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, false)); // Using COVER method
            pane.setBackground(new Background(background));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + imagePath + " - " + e.getMessage());
        }
    }

    private void runInBackground(Runnable task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public class CustomTooltip extends Tooltip {
        public CustomTooltip(Node owner, VBox tooltipContent) {
            owner.setOnMouseEntered(e -> {
                this.setGraphic(tooltipContent);
                this.show(owner, e.getScreenX(), e.getScreenY() + 10); });
            owner.setOnMouseExited(e -> {
                PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
                delay.setOnFinished(event -> {
                    if (!tooltipContent.isHover() && !owner.isHover()) { this.hide();
                    }
                });
                delay.play();
            });
            tooltipContent.setOnMouseExited(e -> {
                PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
                delay.setOnFinished(event -> {
                    if (!tooltipContent.isHover() && !owner.isHover()) {
                        this.hide();
                    }
                });
                delay.play();
            });
        }
    }

    private Button createPhaseButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 2px; -fx-background-radius: 0; ");

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 2px; -fx-background-radius: 0;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 2px; -fx-background-radius: 0; "));

        return button;
    }

    private Button createCircularButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 0px; -fx-background-radius: 50%; -fx-padding: 10px; " +
                "-fx-min-width: 70px; -fx-min-height: 70px; -fx-max-width: 90px; -fx-max-height: 90px;");

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 0px; -fx-background-radius: 50%; -fx-padding: 10px; " +
                "-fx-min-width: 70px; -fx-min-height: 70px; -fx-max-width: 90px; -fx-max-height: 90px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-border-color: white; " +
                "-fx-border-width: 0px; -fx-background-radius: 50%; -fx-padding: 10px; " +
                "-fx-min-width: 70px; -fx-min-height: 70px; -fx-max-width: 90px; -fx-max-height: 90px;"));

        return button;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
