import java.text.Format;

public class Tournament {
    public static final int NUMOFARGUMENTS = 4;
    private static final String INVALIDINPUT = "invalid arguments, " +
                                                "the run arguments should be: \njava Tournament [round count]" +
                                                " [renderer target: console/none] [player1: human/clever/whatever]" +
                                                " [player2: human/clever/whatever]\nplease try again";
    private int numOfRounds;
    private Game curGame;
    private Player player1;
    private Player player2;
    private Renderer renderer;

    Tournament(int rounds, Renderer renderer,  Player[] players){
        this.numOfRounds = rounds;
        this.player1 = players[0];
        this.player2 = players[1];
        this.renderer = renderer;
    }

    public void playTournament() {
        /**
            this methode stimulate a tournament it
            builds the players and runs the number of rounds
            the tournament got from its constructor,
            at the end the function will print the tournament
            results
            @Params: none
            @returns: none
         */
        int[] gamesRecord = new int[3];
        Mark winner = Mark.BLANK;
        int playerX, playerO;
        for (int i = 0; i < this.numOfRounds; ++i) {
            if (i % 2 == 0) {
                this.curGame = new Game(this.player1, this.player2, this.renderer);
                winner = this.curGame.run();
                if (winner == Mark.X) gamesRecord[0]++;
                if (winner == Mark.O) gamesRecord[1]++;
            }

            else {
                this.curGame = new Game(this.player2, this.player1, this.renderer);
                winner = this.curGame.run();
                if (winner == Mark.X) gamesRecord[1]++;
                if (winner == Mark.O) gamesRecord[0]++;
            }


            if (winner == Mark.BLANK) gamesRecord[2]++;



        }
        System.out.format("player 1: %d | player 2: %d | Draws: %d\n",gamesRecord[0], gamesRecord[1],
                gamesRecord[2]);
    }



    private static boolean isValidNumber(String strNum){
        /**
            checks if the given number is a none negative number
            @Params: a string that should represent a number
            @Returns: boolean True if the string is a none negative
            number False otherwise
         */
        for(int i=0;i<strNum.length();++i){
            if(!Character.isDigit(strNum.charAt(i))){
                return false;
            }
        }
        return true;
    }
    private static boolean isValidInput(String[] args){
        /**
            this function checks the validity of
            the command line arguments
            @Params: String[] args, represents the command line arguments
            @Returns: boolean True if the argument are valid false otherwise
         */
        if(args.length != NUMOFARGUMENTS){
            return false;
        }
        if(!isValidNumber(args[0])){
            return false;
        }

        if(!args[1].equals("console") && !args[1].equals("none")){
            return false;
        }

        if(!args[2].equals("human") && !args[2].equals("clever") && !args[2].equals("whatever") &&
                !args[2].equals("snartypamts") && !args[2].equals("Snartypamts")){
            return false;
        }

        if(!args[3].equals("human") && !args[3].equals("clever") && !args[3].equals("whatever") &&
                !args[3].equals("snartypamts") && !args[3].equals("Snartypamts")){
            return false;
        }


        return true;
    }

    public static void main(String[] args){
        /**
            the main function runs the program
            this function gets the command line arguments
            and builds the players, renderer, and Tournament
            accordingly
            @Params: String[] args, represents the command line arguments
            @Returns: none
         */
        if(!isValidInput(args)){
            System.err.println(INVALIDINPUT);
            return;

        }
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();
        Player player1 = playerFactory.buildPlayer(args[2]);
        Player player2 = playerFactory.buildPlayer(args[3]);
        Renderer renderer = rendererFactory.buildRenderer(args[1]);
        int roundsNumber = Integer.parseInt(args[0]);
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        Tournament tournament = new Tournament(roundsNumber, renderer, players);
        tournament.playTournament();

    }



}


