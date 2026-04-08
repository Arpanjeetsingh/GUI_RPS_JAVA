public enum Move {
    ROCK('R', "Rock"),
    PAPER('P', "Paper"),
    SCISSORS('S', "Scissors");

    private final char symbol;
    private final String displayName;

    Move(char symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Move fromInt(int choice) {
        switch (choice) {
            case 1:
                return ROCK;
            case 2:
                return PAPER;
            case 3:
                return SCISSORS;
            default:
                throw new IllegalArgumentException("Invalid choice: " + choice);
        }
    }

    public static Move fromSymbol(char symbol) {
        switch (Character.toUpperCase(symbol)) {
            case 'R':
                return ROCK;
            case 'P':
                return PAPER;
            case 'S':
                return SCISSORS;
            default:
                throw new IllegalArgumentException("Invalid move symbol: " + symbol);
        }
    }

    public Move moveThatBeatsThis() {
        switch (this) {
            case ROCK:
                return PAPER;
            case PAPER:
                return SCISSORS;
            case SCISSORS:
                return ROCK;
            default:
                throw new IllegalStateException("Unexpected move");
        }
    }
}