public class ComputerPlayer {
    private final ComputerChoiceStrategy strategy;

    public ComputerPlayer(ComputerChoiceStrategy strategy) {
        this.strategy = strategy;
    }

    public ComputerTurn chooseTurn() {
        return strategy.chooseTurn();
    }

    public void recordRound(Move humanMove, Move computerMove) {
        strategy.recordRound(humanMove, computerMove);
    }

    public void saveData() {
        strategy.saveData();
    }

    public String getStrategyName() {
        return strategy.getName();
    }
}