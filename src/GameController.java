import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameController {

    private VBox root;

    private Label roundLabel;
    private int currentRound = 1;

    public GameController() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        roundLabel = new Label("Round: " + currentRound);

        Button rockBtn = new Button("Rock");
        Button paperBtn = new Button("Paper");
        Button scissorsBtn = new Button("Scissors");

        rockBtn.setOnAction(e -> handleMove("Rock"));
        paperBtn.setOnAction(e -> handleMove("Paper"));
        scissorsBtn.setOnAction(e -> handleMove("Scissors"));

        root.getChildren().addAll(
                roundLabel,
                rockBtn,
                paperBtn,
                scissorsBtn
        );
    }

    private void handleMove(String move) {
        System.out.println("User chose: " + move);

        currentRound++;
        roundLabel.setText("Round: " + currentRound);
    }

    public VBox getView() {
        return root;
    }
}import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameController {

    private VBox root;

    private Label roundLabel;
    private int currentRound = 1;

    public GameController() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        roundLabel = new Label("Round: " + currentRound);

        Button rockBtn = new Button("Rock");
        Button paperBtn = new Button("Paper");
        Button scissorsBtn = new Button("Scissors");

        rockBtn.setOnAction(e -> handleMove("Rock"));
        paperBtn.setOnAction(e -> handleMove("Paper"));
        scissorsBtn.setOnAction(e -> handleMove("Scissors"));

        root.getChildren().addAll(
                roundLabel,
                rockBtn,
                paperBtn,
                scissorsBtn
        );
    }

    private void handleMove(String move) {
        System.out.println("User chose: " + move);

        currentRound++;
        roundLabel.setText("Round: " + currentRound);
    }

    public VBox getView() {
        return root;
    }
}