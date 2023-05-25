import java.util.Scanner;
public class Chat {
    public static void main(String[] args) {
        ChatterBot[] bots = new ChatterBot[2];
        String[] bot1_replay_set_illegal = {"Don't <request>, he fell in love with Nalla",
                                    "say I should say <request>",
                                    "say I should say",
                                    "Don't fall in love with Nalla",
                                    "Listen kid, bad things happen, and there’s nothing you can do about it",
                                    "Listen kid, bad things happen, and there’s nothing you can do about it <request>",
                                    "No worries!!!",
                                    "No worries!!! <request>"};
        String[] bot1_replay_set_legal = {"Hakuna Matata <phrase>",
                                          "No worries for the rest of your days <phrase>",
                                           "<phrase> You uncle did WHAT?!",
                                           "When the world turns its back on you, you turn your back on the world"};
        bots[0] = new ChatterBot("Timon", bot1_replay_set_legal, bot1_replay_set_illegal);
        String[] bot2_replay_set_illegal = {"say say ",
                                            "say say <request>",
                                            "I farted…",
                                            "I farted… <request>",
                                            "No worries!!!",
                                            "No worries!!! <request>" };
        String[] bot2_replay_set_legal = {"Hakuna Matata <phrase>",
                                          "No worries for the rest of your days",
                                          "I ate like a pig <phrase>",
                                          "Here, eat this bug"};
        bots[1]  = new ChatterBot("Pumba", bot2_replay_set_legal, bot2_replay_set_illegal);
        String statement = "No worries!!!";
        Scanner scan = new Scanner(System.in);
        for(int i=0;;i++){
            statement = bots[i%bots.length].replyTo(statement);
            System.out.println(bots[i%bots.length].name + ": " +  statement);
            scan.nextLine();

        }



    }
}
