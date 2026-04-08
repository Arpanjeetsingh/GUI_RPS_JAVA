public interface ComputerChoiceStrategy {
    Move chooseMove();
    void recordRound(Move humanMove, Move computerMove);
    void saveData();
    String getName();
    Move getPredictedHumanMove();
}