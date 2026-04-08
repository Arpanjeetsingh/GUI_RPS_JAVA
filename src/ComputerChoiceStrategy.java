public interface ComputerChoiceStrategy {
    ComputerTurn chooseTurn();
    void recordRound(Move humanMove, Move computerMove);
    void saveData();
    String getName();
}