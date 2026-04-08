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
    private Move lastPrediction = null;

    public MachineLearningChoiceStrategy(Random random) {
        this.random = random;
        this.frequencies = new HashMap<>();
        this.history = new ArrayDeque<>();
        loadData();
    }

    @Override
    public Move chooseMove() {
        if (history.size() < N - 1) {
            lastPrediction = null;
            return randomMove();
        }

        String prefix = historyToString();
        String rockSequence = prefix + Move.ROCK.getSymbol();
        String paperSequence = prefix + Move.PAPER.getSymbol();
        String scissorsSequence = prefix + Move.SCISSORS.getSymbol();

        int rockCount = frequencies.getOrDefault(rockSequence, 0);
        int paperCount = frequencies.getOrDefault(paperSequence, 0);
        int scissorsCount = frequencies.getOrDefault(scissorsSequence, 0);

        int max = Math.max(rockCount, Math.max(paperCount, scissorsCount));

        if (max == 0) {
            lastPrediction = null;
            return randomMove();
        }

        Move predictedHumanMove;
        if (scissorsCount == max) {
            predictedHumanMove = Move.SCISSORS;
        } else if (paperCount == max) {
            predictedHumanMove = Move.PAPER;
        } else {
            predictedHumanMove = Move.ROCK;
        }

        lastPrediction = predictedHumanMove;
        return predictedHumanMove.moveThatBeatsThis();
    }

    @Override
    public Move getPredictedHumanMove() {
        return lastPrediction;
    }

    @Override
    public void recordRound(Move humanMove, Move computerMove) {
        addToHistory(computerMove.getSymbol());
        addToHistory(humanMove.getSymbol());

        if (history.size() >= N) {
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
        return Move.fromInt(random.nextInt(3) + 1);
    }

    private void addToHistory(char choice) {
        history.addLast(choice);
        while (history.size() > N) {
            history.removeFirst();
        }
    }

    private String historyToString() {
        StringBuilder sb = new StringBuilder();
        for (char c : history) {
            sb.append(c);
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