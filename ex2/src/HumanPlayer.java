import java.util.Scanner;
public class HumanPlayer implements Player {
    private static final int DECIMAL = 10;
    private static final int STARTINDEX = 1;

    @Override
    public void playTurn(Board board, Mark mark){
        /**
            this methode responsible for
            placing the mark according to
            the human strategy
            @params: Board: board, Mark: mark
            @returns: None
         */
        boolean valid = false;
        Scanner scan = new Scanner(System.in);
        while (!valid){
            System.out.println("Player " + mark + ", type coordinates: ");
            int move = scan.nextInt();
            int row = move/DECIMAL-STARTINDEX;
            int col = move%DECIMAL-STARTINDEX;
            valid = board.putMark(mark, row, col);
            if(!valid) System.out.println("Invalid coordinates, type again: ");
        }
    }
}
