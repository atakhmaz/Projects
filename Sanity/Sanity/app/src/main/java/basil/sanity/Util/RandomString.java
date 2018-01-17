package basil.sanity.Util;

import android.util.Log;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Andre Takhmazyan on 10/27/2017.
 */

public class RandomString {

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomString() {
        this(21);
    }
    public String  getPhrase(int phraseLength){

        // Hundred most random words.
         String[] wordList ={"a","about","all","also","and","as","at","be","because","but"
                 ,"by","can","come","could","day","do","even","find","first","for","from","get"
                 ,"give","go","have","he","her","here","him","his","how","I","if","in","into",
                 "it","its","just","know","like","look","make","man","many","me","more","my",
                 "new","no","not","now","of","on","one","only","or","other","our","out","people",
                 "say","see","she","so","some","take","tell","than","that","the","their","them",
                 "then","there","these","they","thing","think","this","those","time","to","two",
                 "up","use","very","want","way","we","well","what","when","which","who","will",
                 "with","would","year","you","your"};

        String word = "";

        for(int i = 0; i< phraseLength ; i++){
            int randomNumber = (int) (Math.random() *wordList.length-1);

           word+=wordList[randomNumber];
            word+= " ";


        }
    return word;
    }

}