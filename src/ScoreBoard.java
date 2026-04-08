public class ScoreBoard {
    private int humanScore;
    private int computerScore;
    private int draws;

    public void recordResult(RoundResult result) {
        switch (result) {
            case HUMAN_WIN:
                humanScore++;
                break;
            case COMPUTER_WIN:
                computerScore++;
                break;
            case DRAW:
                draws++;
                break;
            default:
                throw new IllegalStateException("Unexpected result");
        }
    }

    public int getHumanScore() {
        return humanScore;
    }

    public int getComputerScore() {
        return computerScore;
    }

    public int getDraws() {
        return draws;
    }

    public void reset() {
        humanScore = 0;
        computerScore = 0;
        draws = 0;
    }
}