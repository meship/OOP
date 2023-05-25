public class SnartypamtsPlayer implements Player {
    private static final int ROWSTART = Board.SIZE/2 - 1;
    private static final int COLSTART = Board.SIZE/2 - 1;

    private Mark getOtherPlayerMark(Mark mark){
        /**
            this function returns the
            mark of the other player
            @Params: Mark of cur Player
            @Returns: Mark of the other Player
         */
        if(mark == Mark.O) return Mark.X;
        return Mark.O;
    }

    private boolean firstTurn(Board board, Mark mark){
        /**
            this function returns if it's the Player
            first turn
            @Params:  Board, Mark
            @Returns: boolean True if it's the first turn False otherwise
         */
        if(board.getMark(ROWSTART, COLSTART) != mark && board.getMark(ROWSTART,COLSTART+1) != mark) return true;
        return false;
    }

    private int scanSequence(int row, int col, int rowIncrement ,int colIncrement, Mark mark, Board board){
        /**
            this method is a helper to the findBestPlacementForSquare methode
            it gets a row, col , rowIncrement and colIncrement
            mark and bord, and find the max sequence of the given mark in the
            row/ col/ diam/ reverse diam of the given square depends on the
            given row and col increments
            @params : int row, int col, int rowIncrement, int colIncrement, Mark- mark, Board- board
            @returns: int the length of the longest possible sequence
         */
        int count = 0;
        for(int i=0;i<Board.WIN_STREAK;++i){
            if(board.getMark(row,col) == getOtherPlayerMark(mark)){
                count = 0;
            }

            else if (board.getMark(row,col) == mark) {
                ++count;
            }

            if(count == Board.WIN_STREAK-1) return count;
            row += rowIncrement;
            col += colIncrement;
            if(row>=Board.SIZE || col>=Board.SIZE || col<0) break;

        }
        return count;
    }
    private int findBestPlacementForSquare(int row, int col, Mark mark, Board bord){
        /**
            this method gets a row, a col, a Mark and a board
            it checks the maximum sequence that can be formed in the
            row or col or diam or reverse diam of the given square
            @params : int row, int col, Mark- mark, Board- board
            @returns: int the length of the longest possible sequence
         */
        int max =0, result;
        // this check the possible sequence that starts above the cur square
        int rowStart = Math.max(0, row-(Board.WIN_STREAK-1));
        result = scanSequence(rowStart, col, 1,0,mark,bord);
        max = Math.max(max,result);
        // this check the possible sequence that starts squares below the cur square
        rowStart = Math.min(row, Board.SIZE - Board.WIN_STREAK);
        result = scanSequence(rowStart, col, 1,0,mark,bord);
        max = Math.max(max,result);
        // this check the possible sequence that starts left to the cur square
        int colStart = Math.max(0, col-(Board.WIN_STREAK-1));
        result = scanSequence(row, colStart, 0,1,mark,bord);
        max = Math.max(max,result);
        // this check the possible sequence that starts right to the cur square
        colStart = Math.min(col, Board.SIZE - Board.WIN_STREAK);
        result = scanSequence(row, colStart, 0,1,mark,bord);
        max = Math.max(max,result);
        // this check the possible sequence that starts in the diam above the cur square
        int startDiam = Math.min(Math.min(row, col), Board.WIN_STREAK-1);
        result = scanSequence(row-startDiam, col-startDiam, 1, 1, mark, bord);
        max = Math.max(max,result);
        // this check the possible sequence that starts in the diam below the cur square
        startDiam = Math.min(row,col);
        if(startDiam+Board.WIN_STREAK >= Board.SIZE) startDiam = Board.SIZE - Board.WIN_STREAK;
        else startDiam = 0;
        result = scanSequence(row-startDiam, col-startDiam, 1, 1, mark, bord);
        max = Math.max(max, result);
        // this check the possible sequence that starts in the revers diam above the cur square
        int startReverseDiam = Math.min(Math.min(row, (Board.SIZE-1)-col), Board.WIN_STREAK-1);
        result = scanSequence(row-startReverseDiam, col+startReverseDiam, 1, -1, mark, bord);
        max = Math.max(max, result);
        // this check the possible sequence that starts in the reverse diam below the cur square
        startReverseDiam = Math.min(row,(Board.SIZE-1)-col);
        if(startReverseDiam+Board.WIN_STREAK >= Board.SIZE) startReverseDiam = Board.SIZE - Board.WIN_STREAK;
        else startReverseDiam = 0;
        result = scanSequence(row-startReverseDiam, col-startReverseDiam, 1, 1, mark, bord);
        max = Math.max(max, result);

        return max;



    }

    private int[] findBestPlacement(Board board, Mark mark){
        /**
            this function find the square on the cur board
            which have the potential of the longest sequence
            can be formed using the findBestPlacementForSquare methode
            @Params: Board- board, Mark- mark
            @Returns: int[3] were the first two cells represent the coordinate
            and the third cell represent the length of found sequence
         */
        int[] bestPlacement = new int[3];
        int max = -1, result;
        for(int row = 0; row<Board.SIZE; ++row) {
            for (int col = 0; col < Board.SIZE; ++col) {
                if(board.getMark(row, col) == Mark.BLANK){
                    result = findBestPlacementForSquare(row,col,mark,board);
                    if(max<result){
                        max = result;
                        bestPlacement[0] = row;
                        bestPlacement[1] = col;
                        bestPlacement[2] = max;
                    }
                    if(max == Board.WIN_STREAK-1){
                        return bestPlacement;
                    }
                }
            }
        }

        return bestPlacement;
    }

    private int[] blockTheOtherPlayer(Board board, Mark mark){
        /**
            this methode checks if the other Player can win
            by using the findBestPlacementForSquare methode
            and check if there is a Board.WIN_STREAK-1 sequence
            @Params: Board- board, Mark- mark
            @Returns: int[2] which represent the winning coordinate
            [-1,-1] if the other player can't win
         */
        int[] winStreak = new int[2];
        winStreak[0] = -1;
        winStreak[1] = -1;
        int result;
        for(int row = 0; row<Board.SIZE; ++row) {
            for (int col = 0; col < Board.SIZE; ++col) {
                if(board.getMark(row, col) == Mark.BLANK){
                    result = findBestPlacementForSquare(row,col,mark,board);
                    if(result == Board.WIN_STREAK-1){
                        winStreak[0] = row;
                        winStreak[1] = col;
                        return winStreak;
                    }
                }
            }
        }
        return winStreak;
    }

    @Override
    public void playTurn(Board board, Mark mark) {
        /**
            this methode represent the strategy of
            this Player more is written in the README file
            @Params: Board- board, Mark- mark
            @Returns: none
         */
        boolean valid;
        int[] toPlace, toBlock;
        if(this.firstTurn(board, mark)){
            valid = board.putMark(mark, ROWSTART, COLSTART);
            if(!valid) board.putMark(mark,ROWSTART,COLSTART+1);
        }

        else {
            toBlock = blockTheOtherPlayer(board, getOtherPlayerMark(mark));
            toPlace = findBestPlacement(board, mark);
            if(toBlock[0]== -1 || toPlace[2] == Board.WIN_STREAK-1){
                board.putMark(mark, toPlace[0], toPlace[1]);
            }
            else {
                board.putMark(mark, toBlock[0], toBlock[1]);
            }


        }
    }

}
