package ascii_art.img_to_char;

import image.Image;
import java.awt.*;
import java.util.HashMap;

public class BrightnessImgCharMatcher {
    /**
     * this class responsible for translating an image file
     * to an array of characters each character will be chosen
     * by the similarity by his brightness and the image brightness
     */
    private Image image;
    private String font;
    private final HashMap<Image, Double> cache = new HashMap<>();

    public BrightnessImgCharMatcher(Image img, String font) {
        /**
         * a constructor for BrightnessImgCharMatcher object
         * @param img - the image file the instance work on
         * @param font - the font of the character
         */
        this.image = img;
        this.font = font;
    }

    private double calcBrightnessForChar(boolean[][]arr){
        /**
         * this is a helper methode for the charBrightnessArray method
         * it calculates the brightness for a single char
         * @param arr a boolean array that represent the amount of black and
         *            white in a character representation as a picture
         * @returns a float represent the brightness of the fitting char
         */
        int count =0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j]){
                    ++count;
                }
            }
        }
        return ((double) count) / (arr.length*arr[0].length);
    }

    private double[] charBrightnessArray(Character[] setCharacter){
        /**
         * this methode is a helper methode to the chooseChars
         * method it calculates the brightness array of the
         * given set of character
         * @param setCharacter a set of character to translate
         * @returns float[] represent the char brightness array
         */
        double[] charSetByBrightness = new double[setCharacter.length];
        for (int i = 0; i < charSetByBrightness.length; i++) {
            charSetByBrightness[i] = calcBrightnessForChar(CharRenderer.getImg(setCharacter[i], 16,
                    this.font));
        }
        return charSetByBrightness;

    }

    private double[] findMinMax(double[] charSetByBrightness){
        /**
         * find both the minimal and maximal members
         * in the given array
         * @param charSetByBrightness the char brightness array
         * @returns float[] an array so that float[0] = max, float[1] = min
         */
        double max = 0;
        double min =1;
        double[] maxMin = new double[2];
        for (int i = 0; i < charSetByBrightness.length; i++) {
            if(charSetByBrightness[i]<min){
                min = charSetByBrightness[i];
            }
            if(charSetByBrightness[i]>max){
                max = charSetByBrightness[i];
            }
        }
        maxMin[0] = max;
        maxMin[1] = min;
        return maxMin;
    }

     private double[] linearStretch(double[] charSetByBrightness){
         /**
          * this methode is a helper methode to the chooseChars
          * method it stretches the char brightness array value
          * by the given formula.
          * @param charSetByBrightness the char brightness array
          * @returns float[] represent the stretched char brightness array
          */
        double[] maxMin = findMinMax(charSetByBrightness);
        for (int i = 0; i < charSetByBrightness.length; i++) {
            charSetByBrightness[i] = (charSetByBrightness[i]-maxMin[1])/
                    (maxMin[0]- maxMin[1]);
        }
        return charSetByBrightness;
    }

    private double imageBrightness(Image image){
        /**
         * gets an image and calculate its brightness
         * by a given formula
         * @param image the image to work on
         * @returns float that represent the image brightness
         */
        double greyPixels=0;
        int numOfPixels = 0;
        for (Color pixel: image.pixels()){
            greyPixels += (pixel.getRed() * 0.2126 + pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722);
            ++numOfPixels;
        }
        return ((greyPixels/255)/numOfPixels);
    }

    private int findMinimalDistance(double[] stretchedChars, double imageBrightness){
        /**
         * finds the member in the array that have the minimal
         * distance from the image brightness
         * @param stretchedChars the stretched char brightness array
         * @param  imageBrightness the image brightness
         * @returns the index of the member with minimal distance from the image brightness
         */
        double min = imageBrightness + 1;
        int index = 0;
        for (int i = 0; i < stretchedChars.length; i++) {
            double dist = Math.abs(imageBrightness-stretchedChars[i]);
            if(dist<min){
                min = dist;
                index = i;
            }
        }
        return index;
    }

    private char[][] convertImageToAscii(int numCharsInRow, double[] stretchedChars, Character[] charSet){
        /**
         * finds the fitting character for any square of the
         * given image
         * @param numCharsInRow the number of squares in a row
         * @param stretchedChars the stretched char brightness array
         * @param charSet a set of character to work with
         * @returns  char[][] representing the ascii art
         */
        int pixels = this.image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[this.image.getHeight()/pixels][this.image.getWidth()/pixels];
        if(charSet.length == 0){
            return asciiArt;
        }
        float curBrightness;
        int index;
        int row = 0, col =0;
        for(Image square: this.image.squareSubImagesOfSize(pixels)){
            if (!cache.containsKey(square)) {
                cache.put(square, (Double) imageBrightness(square));
            }
            index = findMinimalDistance(stretchedChars, (double) cache.get(square));
            asciiArt[row][col] = charSet[index];
            col+=1;
            if(col == this.image.getWidth()/pixels){
                col = 0;
                row +=1;
            }
        }
        return asciiArt;
    }

    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        /**
         * this is the main methode of this class
         * it translates the image to ascii art
         * @param numCharsInRow the number of squares in a row
         * @param charSet a set of character to work with
         * @returns  char[][] representing the ascii art
         */
        double[] stretchedChars = linearStretch(charBrightnessArray(charSet));
        return convertImageToAscii(numCharsInRow, stretchedChars, charSet);

    }

}
