import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        ComputerChoiceStrategy strategy = chooseStrategy(args, scanner, random);

        Player humanPlayer = new HumanPlayer(scanner);
        ComputerPlayer computerPlayer = new ComputerPlayer(strategy);
        RulesEngine rulesEngine = new RulesEngine();

        Game game = new Game(humanPlayer, computerPlayer, rulesEngine);
        game.play();

        scanner.close();
    }

    private static ComputerChoiceStrategy chooseStrategy(String[] args, Scanner scanner, Random random) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-r")) {
                return new RandomChoiceStrategy(random);
            }
            if (args[0].equalsIgnoreCase("-m")) {
                return new MachineLearningChoiceStrategy(random);
            }
        }

        while (true) {
            System.out.print("Choose computer algorithm (r = random, m = machine learning): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("r")) {
                return new RandomChoiceStrategy(random);
            }
            if (input.equals("m")) {
                return new MachineLearningChoiceStrategy(random);
            }

            System.out.println("Invalid choice. Please enter r or m.");
        }
    }
}