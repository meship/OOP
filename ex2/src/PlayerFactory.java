public class PlayerFactory {

    public Player buildPlayer(String type){
        /**
            this methode build a Player
            according to its input and return
            a Player interface that holds the given player
            @Params: String type
            @Returns: Player interface that holds the given player
         */
        switch (type){
            case ("human"):
                Player player = new HumanPlayer();
                return player;
            case ("whatever"):
                return new WhateverPlayer();
            case ("clever"):
                return new CleverPlayer();
            case ("snartypamts"):
                return new SnartypamtsPlayer();
            case ("Snartypamts"):
                return new SnartypamtsPlayer();
            default:
                return null;
        }
    }
}
