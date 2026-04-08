import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner scanner;

    public HumanPlayer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move chooseMove() throws NumberFormatException {
        while (true) {
            System.out.print("Choose (1=rock, 2=paper, 3=scissors): ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                return Move.fromInt(choice);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
    }
}