package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;
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

    public static String capitalizeWord(String word) {
        if (word.length() == 0) {
            return word;
        }
        StringBuilder toCapitalize = new StringBuilder(word);
        toCapitalize.setCharAt(0, Character.toUpperCase(toCapitalize.charAt(0)));
        return toCapitalize.toString();
    }

    /** Returns a detailed message of the t, including the stack trace. */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    public static String getTitleCase(String s) {
        String result = s.substring(0, 1) + s.substring(1).toLowerCase();
        return result;
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

    /**
     * keyword = dist, phrase = distance here is long => true keyword = distance, phrase = distance
     * here is long => true keyword = did, phrase = distance here is long => false
     *
     * @param keyword used in checking
     * @param phrase checks if part/entire keyword is in start of phrase
     * @return true if part of or all of keyword is at the start of phrase
     */
    public static boolean keywordMatchStartOfPhrase(String keyword, String phrase) {
        Pattern pattern = Pattern.compile(String.format("^%s", phrase.toLowerCase()));
        Matcher matcher = pattern.matcher(keyword.toLowerCase());
        return matcher.matches() || matcher.hitEnd();
    }

    /** Returns complete command if given partial command */
    public static Optional<String> getCompletedWord(String word, String[] possibilities) {
        for (String matcher : possibilities) {
            if (StringUtil.keywordMatchStartOfPhrase(word, matcher) || word.contains(matcher)) {
                return Optional.of(matcher);
            }
            if (StringUtil.levenshteinDistanceCompare(word, matcher, 1) != -1) {
                return Optional.of(matcher);
            }
        }
        return Optional.empty();
    }

    public static int levenshteinDistanceCompare(
            CharSequence left, CharSequence right, final int threshold) {
        return LevenshteinDistance.levenshteinDistanceCompare(left, right, threshold);
    }
}
