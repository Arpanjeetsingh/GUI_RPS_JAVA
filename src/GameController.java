import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.Random;

public class GameController {

    private static final int DEFAULT_ROUNDS = 20;

    private final Stage stage;
    private final BorderPane root;
    private final RulesEngine rulesEngine = new RulesEngine();
    private final Random random = new Random();

    private int totalRounds;
    private int currentRound;
    private int humanWins;
    private int computerWins;
    private int ties;

    private ComputerPlayer computerPlayer;

    private Label roundLabel;
    private Label humanChoiceLabel;
    private Label predictionLabel;
    private Label computerChoiceLabel;
    private Label winnerLabel;
    private Label humanWinsLabel;
    private Label computerWinsLabel;
    private Label tiesLabel;
    private Label statusLabel;
    private Button rockBtn;
    private Button paperBtn;
    private Button scissorsBtn;

    public GameController(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        buildUI();
        startNewGame(DEFAULT_ROUNDS);
    }

    private void buildUI() {
        root.setTop(buildMenuBar());
        root.setCenter(buildCenter());

        statusLabel = new Label("Welcome to the Rock Paper Scissors game!");
        statusLabel.setPadding(new Insets(4, 10, 4, 10));
        root.setBottom(statusLabel);
    }

    private HBox buildMenuBar() {
        Button newGameBtn = new Button("New Game");
        Button aboutBtn   = new Button("About");
        Button exitBtn    = new Button("Exit");

        newGameBtn.setOnAction(e -> promptNewGame());
        aboutBtn.setOnAction(e -> showAbout());
        exitBtn.setOnAction(e -> Platform.exit());

        String normal = "-fx-background-color: transparent; -fx-padding: 4 12 4 12;";
        String hovered = "-fx-background-color: #d6d6d6; -fx-padding: 4 12 4 12;";

        for (Button btn : new Button[]{newGameBtn, aboutBtn, exitBtn}) {
            btn.setStyle(normal);
            btn.setOnMouseEntered(e -> btn.setStyle(hovered));
            btn.setOnMouseExited(e -> btn.setStyle(normal));
        }

        HBox bar = new HBox(newGameBtn, aboutBtn, exitBtn);
        bar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private VBox buildCenter() {
        VBox center = new VBox(8);
        center.setPadding(new Insets(15));
        center.setAlignment(Pos.CENTER);

        roundLabel = boldLabel("Round: 1", 16);

        Label humanHeader = boldLabel("Human", 14);

        HBox chooseRow = new HBox(8);
        chooseRow.setAlignment(Pos.CENTER);
        rockBtn     = new Button("Rock");
        paperBtn    = new Button("Paper");
        scissorsBtn = new Button("Scissors");
        rockBtn.setOnAction(e -> handleMove(Move.ROCK));
        paperBtn.setOnAction(e -> handleMove(Move.PAPER));
        scissorsBtn.setOnAction(e -> handleMove(Move.SCISSORS));
        chooseRow.getChildren().addAll(new Label("Choose:"), rockBtn, paperBtn, scissorsBtn);

        humanChoiceLabel = new Label("Human chooses:");

        Label computerHeader = boldLabel("Computer", 14);
        predictionLabel     = new Label("Predicted human choice:");
        computerChoiceLabel = boldLabel("Therefore computer chooses:", 13);

        winnerLabel = boldLabel("The winner:", 14);

        Label statsHeader = boldLabel("Statistics", 14);
        humanWinsLabel    = new Label("Human wins: 0");
        computerWinsLabel = new Label("Computer wins: 0");
        tiesLabel         = new Label("Ties: 0");

        center.getChildren().addAll(
            roundLabel,
            new Separator(),
            humanHeader,
            chooseRow,
            humanChoiceLabel,
            new Separator(),
            computerHeader,
            predictionLabel,
            computerChoiceLabel,
            new Separator(),
            winnerLabel,
            new Separator(),
            statsHeader,
            humanWinsLabel,
            computerWinsLabel,
            tiesLabel
        );
        return center;
    }

    private Label boldLabel(String text, int size) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, size));
        return label;
    }

    private void startNewGame(int rounds) {
        totalRounds  = rounds;
        currentRound = 1;
        humanWins = computerWins = ties = 0;

        computerPlayer = new ComputerPlayer(new MachineLearningChoiceStrategy(random));

        stage.setTitle("Rock Paper Scissors: " + totalRounds + " rounds/game");
        roundLabel.setText("Round: 1");
        humanChoiceLabel.setText("Human chooses:");
        predictionLabel.setText("Predicted human choice:");
        computerChoiceLabel.setText("Therefore computer chooses:");
        winnerLabel.setText("The winner:");
        humanWinsLabel.setText("Human wins: 0");
        computerWinsLabel.setText("Computer wins: 0");
        tiesLabel.setText("Ties: 0");
        setButtonsEnabled(true);
        statusLabel.setText("New game started! " + totalRounds + " rounds. Make your choice.");
    }

    private void handleMove(Move humanMove) {
        Move computerMove = computerPlayer.chooseMove();
        Move prediction   = computerPlayer.getPredictedHumanMove();

        RoundResult result = rulesEngine.determineWinner(humanMove, computerMove);
        computerPlayer.recordRound(humanMove, computerMove);

        switch (result) {
            case HUMAN_WIN:    humanWins++;    break;
            case COMPUTER_WIN: computerWins++; break;
            case DRAW:         ties++;         break;
            default: break;
        }

        humanChoiceLabel.setText("Human chooses:  " + humanMove.getDisplayName());

        if (prediction != null) {
            predictionLabel.setText("Predicted human choice:  " + prediction.getDisplayName());
        } else {
            predictionLabel.setText("Predicted human choice:  None (random)");
        }

        computerChoiceLabel.setText("Therefore computer chooses:  " + computerMove.getDisplayName());

        String winnerText;
        switch (result) {
            case HUMAN_WIN:    winnerText = "Human";    break;
            case COMPUTER_WIN: winnerText = "Computer"; break;
            default:           winnerText = "Tie";      break;
        }
        winnerLabel.setText("The winner:  " + winnerText);

        humanWinsLabel.setText("Human wins: " + humanWins);
        computerWinsLabel.setText("Computer wins: " + computerWins);
        tiesLabel.setText("Ties: " + ties);

        if (currentRound >= totalRounds) {
            computerPlayer.saveData();
            setButtonsEnabled(false);
            String gameWinner = humanWins > computerWins ? "Human"
                              : computerWins > humanWins ? "Computer"
                              : "Nobody — it's a tie";
            roundLabel.setText("Round: " + currentRound + "  (Game Over)");
            statusLabel.setText("Game over! Final winner: " + gameWinner
                + ". Use Game \u2192 New Game to play again.");
        } else {
            currentRound++;
            roundLabel.setText("Round: " + currentRound);
            statusLabel.setText("Round result: " + winnerText
                + ". Now choose for round " + currentRound + ".");
        }
    }

    private void promptNewGame() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(DEFAULT_ROUNDS));
        dialog.setTitle("New Game");
        dialog.setHeaderText("Start a New Game");
        dialog.setContentText("Number of rounds:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                int n = Integer.parseInt(input.trim());
                if (n > 0) {
                    startNewGame(n);
                } else {
                    showError("Number of rounds must be a positive integer.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid input. Starting with default " + DEFAULT_ROUNDS + " rounds.");
                startNewGame(DEFAULT_ROUNDS);
            }
        });
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Rock Paper Scissors");
        alert.setContentText(
            "A GUI-based Rock Paper Scissors game with machine learning.\n\n"
            + "CS 151 \u2014 Object-Oriented Analysis and Design\n"
            + "San Jos\u00e9 State University"
        );
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setButtonsEnabled(boolean enabled) {
        rockBtn.setDisable(!enabled);
        paperBtn.setDisable(!enabled);
        scissorsBtn.setDisable(!enabled);
    }

    public BorderPane getView() {
        return root;
    }
}
