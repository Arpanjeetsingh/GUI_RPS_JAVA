public class Game {
    private static final int TOTAL_ROUNDS = 20;

    private final Player humanPlayer;
    private final ComputerPlayer computerPlayer;
    private final RulesEngine rulesEngine;
    private final ScoreBoard scoreBoard;

    public Game(Player humanPlayer, ComputerPlayer computerPlayer, RulesEngine rulesEngine) {
        this.humanPlayer = humanPlayer;
        this.computerPlayer = computerPlayer;
        this.rulesEngine = rulesEngine;
        this.scoreBoard = new ScoreBoard();
    }

    public void play() {
        System.out.println("Computer strategy: " + computerPlayer.getStrategyName());
        System.out.println();

        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            System.out.println("Round " + round);

            Move humanMove = humanPlayer.chooseMove();

            Move computerMove = computerPlayer.chooseMove();
            Move predictedMove = computerPlayer.getPredictedHumanMove();

            if (predictedMove == null) {
                System.out.println("Predicted human choice: Unknown");
            } else {
                 System.out.println("Predicted human choice: " + predictedMove.getDisplayName());
            }       

            System.out.println("Computer chooses: " + computerMove.getDisplayName());  


            System.out.println("You chose " + humanMove.getDisplayName() + ".");
            System.out.println("The computer chose " + computerMove.getDisplayName() + ".");

            RoundResult result = rulesEngine.determineWinner(humanMove, computerMove);
            printRoundMessage(result);

            scoreBoard.recordResult(result);
            System.out.println();

            computerPlayer.recordRound(humanMove, computerMove);
        }

        computerPlayer.saveData();
    }

    private void printRoundMessage(RoundResult result) {
        switch (result) {
            case HUMAN_WIN:
                System.out.println("Human Wins!");
                break;
            case COMPUTER_WIN:
                System.out.println("Computer Wins!");
                break;
            case DRAW:
                System.out.println("Draw!");
                break;
            default:
                throw new IllegalStateException("Unexpected result");
        }
    }
}