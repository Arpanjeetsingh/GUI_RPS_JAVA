public class ComputerTurn {
    private final Move predictedHumanMove;
    private final Move computerMove;

    public ComputerTurn(Move predictedHumanMove, Move computerMove) {
        this.predictedHumanMove = predictedHumanMove;
        this.computerMove = computerMove;
    }

    public Move getPredictedHumanMove() {
        return predictedHumanMove;
    }

    public Move getComputerMove() {
        return computerMove;
    }
}