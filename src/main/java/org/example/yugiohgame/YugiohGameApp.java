package org.example.yugiohgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.List;

public class YugiohGameApp extends Application {
    private Game game;
    private Player player1, player2;
    private VBox player1UI, player2UI;
    private Label playerTurnLabel, player1PhaseLabel, player2PhaseLabel;

    @Override
    public void start(Stage primaryStage) {
        // Initialize game
        player1 = createPlayer("676ad312488357720829bf84");
        player2 = createPlayer("676ad33884741913cd0ece8c");
        game = new Game(player1, player2);
        game.setUI(this); // Set the UI reference

        // Create UI
        player1UI = createPlayerUI(player1, "Player 1", Color.LIGHTBLUE);
        player2UI = createPlayerUI(player2, "Player 2", Color.LIGHTGREEN);

        // Control Buttons
        Button drawButton = new Button("Draw Phase");
        drawButton.setOnAction(e -> game.drawCard());

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> game.playCard());

        Button attackButton = new Button("Attack Phase");
        attackButton.setOnAction(e -> game.attack());

        Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(e -> game.endTurn());

        playerTurnLabel = new Label("Player 1's Turn");
        playerTurnLabel.setFont(new Font("Arial", 16));
        playerTurnLabel.setTextFill(Color.WHITE);

        HBox controls = new HBox(10, drawButton, playButton, attackButton, endTurnButton, playerTurnLabel);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, player1UI, controls, player2UI);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: #444;");

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Yu-Gi-Oh! Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        updateUI(); // Initial update
    }

    private VBox createPlayerUI(Player player, String name, Color bgColor) {
        Text playerName = new Text(name);
        playerName.setFill(Color.WHITE);

        Label lifePointsLabel = new Label("Life Points: " + player.getLifePoints());
        lifePointsLabel.setTextFill(Color.WHITE);

        Label phaseLabel = new Label("Phase: " + (game.getCurrentPhase().getClass().getSimpleName()));
        phaseLabel.setTextFill(Color.WHITE);

        HBox monsterAndBonus = new HBox(10, createCardSlot(player), createBonusControls(player));
        monsterAndBonus.setAlignment(Pos.CENTER);

        Label attackLabel = new Label("Attack: N/A");
        attackLabel.setTextFill(Color.WHITE);

        Label defenseLabel = new Label("Defense: N/A");
        defenseLabel.setTextFill(Color.WHITE);

        if (name.equals("Player 1")) {
            player1PhaseLabel = phaseLabel;
        } else {
            player2PhaseLabel = phaseLabel;
        }

        VBox playerUI = new VBox(10, playerName, lifePointsLabel, monsterAndBonus, attackLabel, defenseLabel, phaseLabel);
        playerUI.setAlignment(Pos.CENTER);
        playerUI.setStyle("-fx-padding: 10; -fx-background-color: " + toHex(bgColor) + ";");

        return playerUI;
    }

    private HBox createBonusControls(Player player) {
        Button bonus100Button = new Button("+100 ATK");
        Button bonus500Button = new Button("+500 ATK");
        Button bonus1000Button = new Button("+1000 ATK");

        bonus100Button.setOnAction(
                e -> { player.applyBonus(100);
                    updateUI();
                });

        bonus500Button.setOnAction(e -> {player.applyBonus(500);  updateUI();});
        bonus1000Button.setOnAction(e -> {player.applyBonus(1000);  updateUI();});

        HBox bonusControls = new HBox(10, bonus100Button, bonus500Button, bonus1000Button);
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

    private void applyAttackBonus(Player player, int bonus) {
        if (player.getMonsterSlot() != null) {
            player.setMonsterSlot(new BuffedCard(player.getMonsterSlot(), bonus));
            updateUI();
        } else {
            System.out.println("No monster on the field to apply bonus.");
        }
    }

    public void updateUI() {
        updatePlayerUI(player1UI, player1);
        updatePlayerUI(player2UI, player2);
        updatePhaseLabel();
        updateTurnLabel();
    }

    private void updatePlayerUI(VBox playerUI, Player player) {
        Label lifePointsLabel = (Label) playerUI.getChildren().get(1);
        HBox monsterAndBonus = (HBox) playerUI.getChildren().get(2);
        VBox monsterSlot = (VBox) monsterAndBonus.getChildren().get(0);
        Label attackLabel = (Label) playerUI.getChildren().get(3);
        Label defenseLabel = (Label) playerUI.getChildren().get(4);
        ImageView monsterImage = (ImageView) monsterSlot.getChildren().get(1);

        lifePointsLabel.setText("Life Points: " + player.getLifePoints());

        if (player.getMonsterSlot() != null) {
            Text slotLabel = (Text) monsterSlot.getChildren().get(0);
            slotLabel.setText(player.getMonsterSlot().getName());
            monsterImage.setImage(new Image(player.getMonsterSlot().getImageUrl()));
            if (player.isMonsterInAttackPosition()) {
                monsterImage.setRotate(0);
                attackLabel.setText("Attack: " + player.getMonsterSlot().getAttack());
                attackLabel.setTextFill(player.getMonsterSlot() instanceof BuffedCard ? Color.RED : Color.WHITE);
                defenseLabel.setText("Defense: " + player.getMonsterSlot().getDefense());
            } else {
                monsterImage.setRotate(90);
                attackLabel.setText("Attack: "+ player.getMonsterSlot().getAttack());
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
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == player1) {
            player1PhaseLabel.setText("Phase: " + phaseName);
            player2PhaseLabel.setText("Phase: ");
        } else {
            player1PhaseLabel.setText("Phase: ");
            player2PhaseLabel.setText("Phase: " + phaseName);
        }
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

        Scene scene = new Scene(vbox, 300, 150);
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

    public static void main(String[] args) {
        launch(args);
    }
}
