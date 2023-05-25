import java.util.Random ;
public class WhateverPlayer implements Player {

    @Override
    public void playTurn(Board board, Mark mark){
        /**
            this methode represent the strategy of
            this Player more is written in the README file
            @Params: Board- board, Mark- mark
            @Returns: none
         */
        boolean valid = false;
        while (!valid){
            Random rnd = new Random();
            int col = rnd.nextInt(Board.SIZE);
            int row = rnd.nextInt(Board.SIZE);
            valid = board.putMark(mark, row, col);

        }


    }
}
