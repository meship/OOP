package ascii_art;

import java.util.HashMap;
import java.util.HashSet;

public class Algorithms {
    /**
     * this is a static class which
     * have tow static functions that
     * solves algorithmic problems
     */
    private static final String [] MORSE_LIST = {".-", "-...", "-.-." , "-..",".","..-.","--.","....","..",".---","-.-"
            ,".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-"
            ,".--","-..-","-.--","--.."};
    private static final int A_IN_ASCII = 97;

    public static int findDuplicate(int[] numList) {
        /**
         * this function solves the find Duplicate
         * problem. its find the duplicate element in
         * the given array more info can be found in the README file
         *
         */
        int slow = numList[0];
        int fast = numList[numList[0]];
        while (slow!=fast){
            slow = numList[slow];
            fast = numList[numList[fast]];
        }
        slow = 0;
        while (slow!=fast){
            slow = numList[slow];
            fast = numList[fast];
        }
        return slow;

    }

    public static int uniqueMorseRepresentations(String[] words){
        String morseCode;
        HashSet<String> moresCodeSet = new HashSet<>();
        for(String word : words){
            morseCode = "";
            for (int i = 0; i < word.length(); i++) {
                morseCode += MORSE_LIST[word.charAt(i)-A_IN_ASCII];
            }
            moresCodeSet.add(morseCode);
        }
        return moresCodeSet.size();
    }

}

