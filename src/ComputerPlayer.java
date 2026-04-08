public class ComputerPlayer implements Player {
    private final ComputerChoiceStrategy strategy;

    public ComputerPlayer(ComputerChoiceStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Move chooseMove() {
        return strategy.chooseMove();
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

    public Move getLastPredictedHumanMove() {
        if (strategy instanceof MachineLearningChoiceStrategy) {
            return ((MachineLearningChoiceStrategy) strategy).getLastPredictedHumanMove();
        }
        return null;
    }
}