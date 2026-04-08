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

    public void printScore() {
        System.out.println("Score: Human:" + humanScore
                + " Computer:" + computerScore
                + " Draws=" + draws);
    }
}