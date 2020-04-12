package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Helper functions for handling strings. */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}. Ignores case, but a full word
     * match is required. <br>
     * examples:
     *
     * <pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     *
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(
                preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence).anyMatch(preppedWord::equalsIgnoreCase);
    }

    /** Returns a detailed message of the t, including the stack trace. */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer e.g. 1, 2, 3, ..., {@code
     * Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input e.g. empty string, "-1", "0", "+1", and
     * " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     *
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0
                    && !s.startsWith(
                            "+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean keywordMatchStartOfPhrase(String keyword, String phrase) {
        Pattern pattern = Pattern.compile(String.format("^%s", phrase.toLowerCase()));
        Matcher matcher = pattern.matcher(keyword.toLowerCase());
        return matcher.matches() || matcher.hitEnd();
    }

    public static boolean keywordMatchPhrase(String keyword, String phrase) {
        Pattern pattern = Pattern.compile(String.format("^%s", phrase.toLowerCase()));
        Matcher matcher = pattern.matcher(keyword.toLowerCase());
        return matcher.matches();
    }

    public static int limitedCompare(CharSequence left, CharSequence right, final int threshold) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }

        int n = left.length(); // length of left
        int m = right.length(); // length of right

        // if one string is empty, the edit distance is necessarily the length
        // of the other
        if (n == 0) {
            return m <= threshold ? m : -1;
        } else if (m == 0) {
            return n <= threshold ? n : -1;
        }

        if (n > m) {
            // swap the two strings to consume less memory
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        int[] p = new int[n + 1]; // 'previous' cost array, horizontally
        int[] d = new int[n + 1]; // cost array, horizontally
        int[] tempD; // placeholder to assist in swapping p and d

        // fill in starting table values
        final int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }
        // these fills ensure that the value above the rightmost entry of our
        // stripe will be ignored in following loop iterations
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);

        // iterates through t
        for (int j = 1; j <= m; j++) {
            final char rightJ = right.charAt(j - 1); // jth character of right
            d[0] = j;

            // compute stripe indices, constrain to array size
            final int min = Math.max(1, j - threshold);
            final int max = j > Integer.MAX_VALUE - threshold ? n : Math.min(n, j + threshold);

            // the stripe may lead off of the table if s and t are of different
            // sizes
            if (min > max) {
                return -1;
            }

            // ignore entry left of leftmost
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }

            // iterates through [min, max] in s
            for (int i = min; i <= max; i++) {
                if (left.charAt(i - 1) == rightJ) {
                    // diagonally left and up
                    d[i] = p[i - 1];
                } else {
                    // 1 + minimum of cell to the left, to the top, diagonally
                    // left and up
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
            }

            // copy current distance counts to 'previous row' distance counts
            tempD = p;
            p = d;
            d = tempD;
        }

        // if p[n] is greater than the threshold, there's no guarantee on it
        // being the correct
        // distance
        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }
}
