package seedu.address.model.task;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import seedu.address.commons.util.StringUtil;

/** Tests that a {@code Task}'s {@code Name} matches any of the keywords given. */
public class NameContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;
    private final int threshold = 1;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * score is -1 if keywords is empty else if edit distance calculated is larger than threshold,
     * score will be Integer.MAX_VALUE
     */
    @Override
    public boolean test(Task task) { // change test to return an int value as the edit distance
        int score = getEditDistance(task);
        return score != -1 && score != Integer.MAX_VALUE;
    }

    /**
     * Predicate has been enhanced to be return true based on three cases: 1.Complete match
     * this.score set to 0 2.Partial match from the start (i.e. Dist matches Distance search)
     * this.score set to 1 3.Any word with an edit disatnce of <= 2 [Used Levenshtein distance to
     * calculate this value] this.score set to result from levenshtein distance algorithm
     *
     * <p>For case 3, we calculate the distance by chunking the task name to segments of length ==
     * number of words in the search phrase. The score is then set to the minimum score of all task
     * name chunks.
     *
     * <p>Threshold of edit distance refers to the maximum edit distance allowed. It is set at 2 so
     * that phrases that are too dissimilar will not show up.
     *
     * <p>Order of Task search results will be based on the score. Tasks will be displayed in
     * ascending order of score.
     */
    public int getEditDistance(Task task) {
        if (keywords.size() == 0) {
            return -1;
        }

        int score = Integer.MAX_VALUE;
        String joinnedKeywords = String.join(" ", keywords).toLowerCase();
        String[] splitTaskName = task.getName().fullName.toLowerCase().split("\\s+");

        for (int i = 0; i < splitTaskName.length; i++) {
            if (joinnedKeywords.length() < 3) {
                break;
            }

            String joinnedPhrase =
                    String.join(" ", Arrays.copyOfRange(splitTaskName, i, i + keywords.size()));
            int currScore =
                    StringUtil.levenshteinDistanceCompare(
                            joinnedPhrase, joinnedKeywords, threshold);
            if (currScore >= 0) {
                score = Math.min(score, currScore);
            }
        }

        for (int i = 0; i < splitTaskName.length; i++) {
            String joinnedPhrase =
                    String.join(" ", Arrays.copyOfRange(splitTaskName, i, i + keywords.size()));
            if (StringUtil.keywordMatchStartOfPhrase(joinnedKeywords, joinnedPhrase)) {
                score = 1;
            }
            if (joinnedKeywords.equals(joinnedPhrase)) {
                score = 0;
            }
        }
        return score;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                        && keywords.equals(
                                ((NameContainsKeywordsPredicate) other).keywords)); // state check
    }
}
