import java.util.Random;

public class CleverPlayer implements Player{
    private static final int ROWSTART = 0;
    private static final int COLSTART = 0;
    private int[] lastTurn = new int[2];


    private boolean firstTurn(Board board, Mark mark){
        /**
            this function returns if it's the Player
            first turn
            @Params:  Board, Mark
            @Returns: boolean True if it's the first turn False otherwise
         */
        if(board.getMark(ROWSTART, COLSTART) != mark && board.getMark(ROWSTART+1,COLSTART) != mark) return true;
        return false;
    }


    private boolean placeMove(Mark mark, Board board, int row, int col){
        /**
            this methode place the move on the board
            if it's valid if so the methode will update
            the last placement of this player to the cur move
            @Params: Mark- mark, Board- board, int row, int col
            @Returns: boolean True if the move is valid False otherwise
         */
        boolean valid = board.putMark(mark, row, col);
        if(valid) {
            this.lastTurn[0] = row;
            this.lastTurn[1] = col;
        }
        return valid;
    }


    @Override
    public void playTurn(Board board, Mark mark) {
        /**
            this methode represent the strategy of
            this Player more is written in the README file
            @Params: Board- board, Mark- mark
            @Returns: none
         */
        boolean valid = true;
        if(this.firstTurn(board, mark)){
           valid = placeMove(mark, board, ROWSTART, COLSTART);
            if(!valid){
                placeMove(mark, board, ROWSTART+1, COLSTART);
            }

        }

        else {
            valid = placeMove(mark, board, this.lastTurn[0], this.lastTurn[1]+1);


            if(!valid){
                valid = placeMove(mark, board, this.lastTurn[0]+1, this.lastTurn[1]);
            }

            while (!valid){
                Random rnd = new Random();
                int col = rnd.nextInt(Board.SIZE);
                int row = rnd.nextInt(Board.SIZE);
                valid = board.putMark(mark, row,col);
                this.lastTurn[0] = row;
                this.lastTurn[1] = col;

            }
        }
    }
}
