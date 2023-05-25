public class Board {
    public static final int  SIZE = 6;
    public static final int WIN_STREAK = 4;

    private Mark[][] bord;
    private int NumOfMarkedSquares;
    private Mark winner;

    Board(){
        this.bord = new Mark[Board.SIZE][Board.SIZE];
        for(int i=0;i<Board.SIZE;++i){
            for(int j=0;j<Board.SIZE;++j){
                this.bord[i][j] = Mark.BLANK;
            }
        }
        this.NumOfMarkedSquares = 0;
        this.winner = Mark.BLANK;
    }
    private Mark checkSequence(int row, int col, int rowIncrement, int colIncrement){
        /**
            this method is a helper to the winnerOfCurRound methode
            it gets a row, col , rowIncrement and colIncrement
            and checks if there is a winning sequence of a mark in the
            row/ col/ diam/ reverse diam of the given square depends on the
            given row and col increments
            @params : int row, int col, int rowIncrement, int colIncrement
            @returns: Mark, mark of winning sequence blank if there is no winner
         */
        int count_x = 0;
        int count_o = 0;
        while (row<Board.SIZE && col<Board.SIZE && col>=0){
            if(this.bord[row][col].equals(Mark.X)){
                count_o = 0;
                ++count_x;
            }
            else if(this.bord[row][col].equals(Mark.O)){
                count_x = 0;
                ++count_o;
            }
            else {
                count_o = 0;
                count_x = 0;
            }
            row += rowIncrement;
            col += colIncrement;
            if(count_o == Board.WIN_STREAK) return Mark.O;
            if(count_x == Board.WIN_STREAK) return Mark.X;
        }
        return Mark.BLANK;
    }
    private Mark winnerOfCurRound(int row, int col){
        /**
            this method gets a row and a col
            it checks if there is a winning sequence of a mark in the
            row or col or diam or reverse diam of the given square
            @params : int row, int col
            @returns: Mark, mark of winning sequence blank if there is no winner
         */
        Mark checkRow = checkSequence(row, 0, 0, 1);
        if(!checkRow.equals(Mark.BLANK)) return checkRow;
        Mark checkCol = checkSequence(0, col,1,0);
        if (!checkCol.equals(Mark.BLANK)) return checkCol;
        int startDiam = Math.min(row,col);
        Mark checkDiam = checkSequence(row-startDiam,col-startDiam,1,1);
        if (!checkDiam.equals(Mark.BLANK)) return checkDiam;
        int startReverseDiam = Math.min(row,(Board.SIZE-1-col));
        Mark checkReverseDiam = checkSequence(row-startReverseDiam, col+startReverseDiam, 1, -1);
        if (!checkReverseDiam.equals(Mark.BLANK)) return checkReverseDiam;
        return Mark.BLANK;

    }
    public boolean putMark(Mark mark, int row, int col){
        /**
            this methode checks if a mark ac be placed at the
            given coordinates if so it will put the mark at the
            given square and return true else it does nothing and
            returns false
            @param: Mark ,mark, int row, int col
            @returns: boolean: true if successful and false otherwise
         */
        if(row>=Board.SIZE || col>=Board.SIZE || col<0 || row<0 || this.bord[row][col] != Mark.BLANK){
            return false;
        }
        this.bord[row][col] = mark;
        this.NumOfMarkedSquares++;
        this.winner = winnerOfCurRound(row,col);
        return true;
    }

    public Mark getMark(int row, int col){
        /**
            this methode returns the mark on the given square
            if the input is invalid it will return BLANK
            @params: int row, int col
            @returns: the mark of the given square
         */
        if(row>=Board.SIZE || col>=Board.SIZE || row<0 || col<0){
            return Mark.BLANK;
        }
        return bord[row][col];
    }

    public boolean gameEnded(){
        /**
            this methode checks if the game has ended
            @param: None
            @return: boolean true if ended false otherwise
         */
        if(this.NumOfMarkedSquares == Board.SIZE*Board.SIZE || !this.getWinner().equals(Mark.BLANK)){
            return true;
        }
        return false;
    }


    public Mark getWinner(){
        /**
            this function returns the winner
            of this board
            @Params: none
            @Returns: Mark, the mark of the winner Blank if no one won.
         */
        return this.winner;
    }




}
