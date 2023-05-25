package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Shell {
    /**
     * this class responsible for operating
     * thr Shell command it gets an input from the
     * user and acting accordingly
     */

    //constants declaration
    private static final String CMD_WRONG_OUTPUT_ERR = "invalid command the valid commands are:\n  " +
            "chars, add, remove, res, console and render please try again";
    private static final String INVALID_ADD_COMMAND ="invalid command the valid add commands are:\n " +
            "a (one character), a-p (range of characters), space and all, please try again";
    private static final String INVALID_REMOVE_COMMAND ="invalid command the valid remove commands are:\n " +
            "a (one character), a-p (range of characters), space and all, please try again";
    private static final String HIGHEST_RESOLUTION_ERR = "this is the highest resolution";
    private static final String LOWEST_RESOLUTION_ERR = "this is the lowest resolution";
    private static final String INVALID_RES_COMMAND = "invalid res command  the valid commands are:\n  " +
            "up and down, please try again";
    private static final String INVALID_CHARS_COMMAND = "chars command should have no arguments, pleas try again";
    private static final String INVALID_CONSOLE_COMMAND = "console command should have no arguments, pleas try again";
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private Set<Character> charSet = new HashSet<>();
    private static final String CMD_EXIT = "exit";
    private static final String CMD_CHARS = "chars";
    private static final String CMD_SPACE = "space";
    private static final String CMD_ALL = "all";
    private static final String CMD_ADD = "add";
    private static final String CMD_REMOVE = "remove";
    private static final String CMD_DOWN = "down";
    private static final String CMD_UP = "up";
    private static final String CMD_RES = "res";
    private static final String CMD_RENDER = "render";
    private static final String INITIALIZE_SET_VALUE = "0-9";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final String FONT_NAME = "Courier New";
    private static final String CMD_CONSOLE = "console";
    private static final String OUTPUT_FILENAME = "out.html";
    private final int MAX_WORDS_IN_COMMAND = 2;
    //end of constant declaration

    //field declaration
    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private int charsInRow;
    private Image image;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    //end of field declaration

    public Shell(Image img) {
        /**
         * this is the constructor of the shell
         * class it's responsible for initializing
         * the class fields
         * @param img an image file to render
         */
        this.image = img;
        addChars(INITIALIZE_SET_VALUE);
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME); //sets the output to Html file

    }
    public void run(){
        /**
         * this is the main methode of this class
         * it's responsible for operating the commands the
         * user has entered and displaying an error message
         * in case of invalid input
         */
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.nextLine().trim();
        String[] words = cmd.split("\\s+");
        while(!words[0].toLowerCase().equals(CMD_EXIT)) {
            if(!words[0].equals("")) {
                String param = "";
                if(words.length > 1) {
                    param = words[1];
                }
                if(words.length > MAX_WORDS_IN_COMMAND){
                    System.out.println(CMD_WRONG_OUTPUT_ERR);
                }
                switch (words[0]) {
                    case CMD_CHARS:
                       if(words.length>1){
                           System.out.println(INVALID_CHARS_COMMAND);
                           break;
                       }
                       showChars();
                       break;
                    case CMD_ADD:
                        addChars(param);
                        break;
                    case CMD_REMOVE:
                        removeChars(param);
                        break;
                    case CMD_RES:
                        resChange(param);
                        break;
                    case CMD_CONSOLE:
                        if(words.length>1){
                            System.out.println(INVALID_CONSOLE_COMMAND);
                            break;
                        }
                        output = new ConsoleAsciiOutput();
                        break;
                    case CMD_RENDER:
                        render();
                        break;
                    default:
                        System.out.println(CMD_WRONG_OUTPUT_ERR);
                        break;
                }

            }
            System.out.print(">>> ");
            cmd = scanner.nextLine().trim();
            words = cmd.split("\\s+");
        }
    }

    private void showChars(){
        /**
         * this methode displays all the
         * chars in the cur set of chars to the console
         */
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();

    }

    private static char[] parseCharRange(String param){
        /**
         * this methode gets the range of characters
         * the user wants to add to the set or remove from the set
         * and returns an array that represent the range such that
         * the first argument is the first letter and the last argument
         * is the second
         * @param param an input parameter from the user
         *              representing the rang of chars the user
         *              wants to add/remove
         */
        char[] returnValue = new char[2];
        if(param.equals(CMD_SPACE)){
            return new char[]{' ', ' '};
        }
        if(param.equals(CMD_ALL)){
            return new char[]{' ', '~'};
        }
        if(param.length() == 1){
            returnValue[0] = param.charAt(0);
            returnValue[1] = param.charAt(0);
            return returnValue;
        }

        if (param.length() == 3 && param.charAt(1) == '-'){
            returnValue[0] = (char) Math.min(param.charAt(0), param.charAt(2));
            returnValue[1] = (char) Math.max(param.charAt(0), param.charAt(2));
            return returnValue;
        }
        return null;
    }

    private void addChars(String s) {
        /**
         * this methode add characters to
         * the character set at the given range
         * @param s string input from the user
         */
        char[] range = parseCharRange(s);
        if(range != null){
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::add);
        }
        else {
            System.out.println(INVALID_ADD_COMMAND);
        }
    }

    private void removeChars(String s){
        /**
         * this methode remove characters from
         * the character set at the given range
         * @param s string input from the user
         */
        char[] range = parseCharRange(s);
        if(range != null){
            Stream.iterate(range[0], c -> c <= range[1], c -> (char)((int)c+1)).forEach(charSet::remove);
        }
        else {
            System.out.println(INVALID_REMOVE_COMMAND);
        }
    }

    private void resChange(String s){
        /**
         * this methode set the resolution
         * of the AsciiArt output according to the
         * user input if the input was up it will
         * multiply the resolution, and if the input is down
         * it will divide it by tow.
         * @param s command string from user
         */
        if (s.equals(CMD_UP)){
            if(charsInRow*2>maxCharsInRow){
                System.out.println(HIGHEST_RESOLUTION_ERR);
                return;
            }
            charsInRow *=2;
            System.out.println("Width set to "+ charsInRow +"\n");
        }

        else if (s.equals(CMD_DOWN)){
            if(charsInRow/2 < minCharsInRow){
                System.out.println(LOWEST_RESOLUTION_ERR);
                return;
            }
            charsInRow /=2;
            System.out.println("Width set to "+ charsInRow +"\n");
        }

        else {
            System.out.println(INVALID_RES_COMMAND);
        }


    }

    private void render(){
        /**
         * this methode display the AsciiArt
         * of the given image according to the cur
         * char set, the cur res and the renderer type (Html/console)
         *
         */
        Character[] characters = new Character[charSet.size()];
        int index = 0;
        for (Character character : charSet){
            characters[index] = character;
            ++index;
        }
        output.output(charMatcher.chooseChars(charsInRow, characters));

    }
}
