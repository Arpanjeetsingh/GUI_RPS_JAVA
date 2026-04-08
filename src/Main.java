import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;

public class Main extends Application {

    private int totalRounds = 20;
    private int currentRound = 1;

    private ComputerPlayer computerPlayer;
    private RulesEngine rulesEngine;
    private ScoreBoard scoreBoard;

    private Label roundLabel;
    private Label humanChoiceLabel;
    private Label predictionLabel;
    private Label computerChoiceLabel;
    private Label winnerLabel;
    private Label humanWinsLabel;
    private Label computerWinsLabel;
    private Label tiesLabel;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        setupGame();

        MenuBar menuBar = makeMenu(stage);

        roundLabel = new Label("Round: 1");
        humanChoiceLabel = new Label("Human: ");
        predictionLabel = new Label("Prediction: ");
        computerChoiceLabel = new Label("Computer: ");
        winnerLabel = new Label("Winner: ");
        humanWinsLabel = new Label("Human wins: 0");
        computerWinsLabel = new Label("Computer wins: 0");
        tiesLabel = new Label("Ties: 0");
        statusLabel = new Label("Game started");

        Button rock = new Button("Rock");
        Button paper = new Button("Paper");
        Button scissors = new Button("Scissors");

        rock.setOnAction(e -> playRound(Move.ROCK));
        paper.setOnAction(e -> playRound(Move.PAPER));
        scissors.setOnAction(e -> playRound(Move.SCISSORS));

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        root.getChildren().addAll(
                menuBar,
                roundLabel,
                new Label("Choose move:"),
                rock, paper, scissors,
                humanChoiceLabel,
                predictionLabel,
                computerChoiceLabel,
                winnerLabel,
                new Label("Stats"),
                humanWinsLabel,
                computerWinsLabel,
                tiesLabel,
                statusLabel
        );

        Scene scene = new Scene(root, 350, 500);
        stage.setTitle("RPS Game");
        stage.setScene(scene);
        stage.show();
    }

    private void setupGame() {
        Random rand = new Random();
        ComputerChoiceStrategy strat = new MachineLearningChoiceStrategy(rand);

        computerPlayer = new ComputerPlayer(strat);
        rulesEngine = new RulesEngine();
        scoreBoard = new ScoreBoard();

        currentRound = 1;
    }

    private MenuBar makeMenu(Stage stage) {
        Menu menu = new Menu("Menu");

        MenuItem about = new MenuItem("About");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem changeRounds = new MenuItem("Change Rounds");
        MenuItem exit = new MenuItem("Exit");

        about.setOnAction(e -> showAbout());
        newGame.setOnAction(e -> resetGame(stage));
        changeRounds.setOnAction(e -> setRounds(stage));
        exit.setOnAction(e -> stage.close());

        menu.getItems().addAll(about, newGame, changeRounds, exit);

        MenuBar bar = new MenuBar();
        bar.getMenus().add(menu);
        return bar;
    }

    private void playRound(Move humanMove) {
        if (currentRound > totalRounds) {
            statusLabel.setText("Game over. Start a new one.");
            return;
        }

        humanChoiceLabel.setText("Human: " + humanMove.getDisplayName());

        Move computerMove = computerPlayer.chooseMove();
        Move predicted = computerPlayer.getPredictedHumanMove();
        if (predicted == null) {
            predictionLabel.setText("Prediction: none");
        } else {
            predictionLabel.setText("Prediction: " + predicted.getDisplayName());
        }

        computerChoiceLabel.setText("Computer: " + computerMove.getDisplayName());

        RoundResult result = rulesEngine.determineWinner(humanMove, computerMove);

        if (result == RoundResult.HUMAN_WIN) {
            winnerLabel.setText("Winner: Human");
        } else if (result == RoundResult.COMPUTER_WIN) {
            winnerLabel.setText("Winner: Computer");
        } else {
            winnerLabel.setText("Winner: Tie");
        }

        scoreBoard.recordResult(result);

        humanWinsLabel.setText("Human wins: " + scoreBoard.getHumanScore());
        computerWinsLabel.setText("Computer wins: " + scoreBoard.getComputerScore());
        tiesLabel.setText("Ties: " + scoreBoard.getDraws());

        computerPlayer.recordRound(humanMove, computerMove);
        computerPlayer.saveData();

        currentRound++;

        if (currentRound <= totalRounds) {
            roundLabel.setText("Round: " + currentRound);
        } else {
            statusLabel.setText("Finished all rounds.");
        }
    }

    private void resetGame(Stage stage) {
        setupGame();

        roundLabel.setText("Round: 1");
        humanChoiceLabel.setText("Human: ");
        predictionLabel.setText("Prediction: ");
        computerChoiceLabel.setText("Computer: ");
        winnerLabel.setText("Winner: ");
        humanWinsLabel.setText("Human wins: 0");
        computerWinsLabel.setText("Computer wins: 0");
        tiesLabel.setText("Ties: 0");
        statusLabel.setText("New game");

        stage.setTitle("RPS Game");
    }

    private void setRounds(Stage stage) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(totalRounds));
        dialog.setHeaderText("Enter number of rounds:");

        Optional<String> res = dialog.showAndWait();

        if (res.isPresent()) {
            try {
                int r = Integer.parseInt(res.get());
                if (r > 0) {
                    totalRounds = r;
                    statusLabel.setText("Rounds updated. Start new game.");
                } else {
                    statusLabel.setText("Must be > 0");
                }
            } catch (Exception e) {
                statusLabel.setText("Invalid input");
            }
        }
    }

    private void showAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("About");
        a.setContentText("Simple RPS game with ML strategy.");
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}