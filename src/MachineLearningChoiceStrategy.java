import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MachineLearningChoiceStrategy implements ComputerChoiceStrategy {
    private static final int N = 5;
    private static final String DATA_FILE = "ml_data.txt";

    private final Random random;
    private final Map<String, Integer> frequencies;
    private final Deque<Character> history;

    public MachineLearningChoiceStrategy(Random random) {
        this.random = random;
        this.frequencies = new HashMap<>();
        this.history = new ArrayDeque<>();
        loadData();
    }

    @Override
    public ComputerTurn chooseTurn() {
        // Need at least N-1 previous choices to predict the next human move
        if (history.size() < N - 1) {
            Move computerMove = randomMove();
            return new ComputerTurn(null, computerMove);
        }

        // Use only the last N-1 choices as the prefix
        String prefix = getLastNMinusOneChoices();

        String rockSequence = prefix + Move.ROCK.getSymbol();
        String paperSequence = prefix + Move.PAPER.getSymbol();
        String scissorsSequence = prefix + Move.SCISSORS.getSymbol();

        int rockCount = frequencies.getOrDefault(rockSequence, 0);
        int paperCount = frequencies.getOrDefault(paperSequence, 0);
        int scissorsCount = frequencies.getOrDefault(scissorsSequence, 0);

        int max = Math.max(rockCount, Math.max(paperCount, scissorsCount));

        // No matching pattern found yet
        if (max == 0) {
            Move computerMove = randomMove();
            return new ComputerTurn(null, computerMove);
        }

        Move predictedHumanMove;

        if (rockCount == max) {
            predictedHumanMove = Move.ROCK;
        } else if (paperCount == max) {
            predictedHumanMove = Move.PAPER;
        } else {
            predictedHumanMove = Move.SCISSORS;
        }

        Move computerMove = predictedHumanMove.moveThatBeatsThis();
        return new ComputerTurn(predictedHumanMove, computerMove);
    }

    @Override
    public void recordRound(Move humanMove, Move computerMove) {
        addToHistory(computerMove.getSymbol());
        addToHistory(humanMove.getSymbol());

        if (history.size() == N) {
            String sequence = lastNChoicesAsString();
            frequencies.put(sequence, frequencies.getOrDefault(sequence, 0) + 1);
        }
    }

    @Override
    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save ML data: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Machine Learning";
    }

    private Move randomMove() {
        int choice = random.nextInt(3) + 1;
        return Move.fromInt(choice);
    }

    private void addToHistory(char moveSymbol) {
        history.addLast(moveSymbol);

        while (history.size() > N) {
            history.removeFirst();
        }
    }

    private String getLastNMinusOneChoices() {
        StringBuilder sb = new StringBuilder();

        int skip = history.size() - (N - 1);
        int index = 0;

        for (char c : history) {
            if (index >= skip) {
                sb.append(c);
            }
            index++;
        }

        return sb.toString();
    }

    private String lastNChoicesAsString() {
        StringBuilder sb = new StringBuilder();
        for (char c : history) {
            sb.append(c);
        }
        return sb.toString();
    }

    private void loadData() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String sequence = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    frequencies.put(sequence, count);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load ML data: " + e.getMessage());
        }
    }
}