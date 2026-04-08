import java.util.Random;

public class RandomChoiceStrategy implements ComputerChoiceStrategy {
    private final Random random;

    public RandomChoiceStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Move chooseMove() {
        int choice = random.nextInt(3) + 1;
        return Move.fromInt(choice);
    }

    @Override
    public void recordRound(Move humanMove, Move computerMove) {
        // No learning needed for random strategy
    }

    @Override
    public void saveData() {
        // Nothing to save
    }

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public Move getPredictedHumanMove() {
        return null;
    }
}