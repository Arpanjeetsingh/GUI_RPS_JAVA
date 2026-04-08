public class RulesEngine {
    public RoundResult determineWinner(Move humanMove, Move computerMove) {
        if (humanMove == computerMove) {
            return RoundResult.DRAW;
        }

        if ((humanMove == Move.ROCK && computerMove == Move.SCISSORS)
                || (humanMove == Move.PAPER && computerMove == Move.ROCK)
                || (humanMove == Move.SCISSORS && computerMove == Move.PAPER)) {
            return RoundResult.HUMAN_WIN;
        }

        return RoundResult.COMPUTER_WIN;
    }
}