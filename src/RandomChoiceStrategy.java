import java.util.Random;

public class RandomChoiceStrategy implements ComputerChoiceStrategy {
    private final Random random;

    public RandomChoiceStrategy(Random random) {
        this.random = random;
    }

    @Override
    public ComputerTurn chooseTurn() {
        Move computerMove = Move.fromInt(random.nextInt(3) + 1);

        // Random strategy has no real prediction, so use null
        return new ComputerTurn(null, computerMove);
    }

    @Override
    public void recordRound(Move humanMove, Move computerMove) {
        // No learning needed
    }

    @Override
    public void saveData() {
        // Nothing to save
    }

    @Override
    public String getName() {
        return "Random";
    }
}