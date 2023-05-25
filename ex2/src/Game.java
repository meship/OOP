public class Game {
    /**
        this class represent a game run
        its responsible for all the physical
         operations of the current game
     */

    private static final int NUMOFPLAYERS = 2;
    private Player playerX;
    private Player playerO;
    private Renderer render;

    Game(Player player1, Player player2, Renderer render){
        this.playerX = player1;
        this.playerO = player2;
        this.render = render;

    }

    public Mark run(){
        /**
            this methode stimulate a game run
            of the current game
            @params: None
            @returns: Mark of this game winner
         */
        Board board = new Board();
        Player[] players = {this.playerX, this.playerO};
        int count = 0;
        while (!board.gameEnded()){
            if(count%NUMOFPLAYERS==0){
                players[0].playTurn(board, Mark.X);

            }
            else{
                players[1].playTurn(board, Mark.O);
            }
            this.render.renderBoard(board);
            ++count;


        }
        return board.getWinner();
    }
}
