package seedu.address.model.task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

/** Tests that a {@code Task}'s {@code Name} matches any of the keywords given. */
public class NameContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;
    private final Set<Tag> tags;
    private final int threshold = 1;

    public NameContainsKeywordsPredicate(
            List<String> keywords, Set<Tag> tags) { // if second list is available
        this.keywords = keywords;
        this.tags = tags;
    }

    public NameContainsKeywordsPredicate(List<String> keywords) { // if second list is available
        this.keywords = keywords;
        this.tags = new HashSet();
    }

    /**
     * score is -1 if keywords is empty else if edit distance calculated is larger than threshold,
     * score will be Integer.MAX_VALUE
     */
    @Override
    public boolean test(Task task) { // change test to return an int value as the edit distance
        int score = getEditDistance(task);
        boolean hasTag = false;
        for (Tag t : tags) {
            if (task.hasTag(t)) {
                hasTag = true;
                break;
            }
        }
        return (score != -1 && score != Integer.MAX_VALUE) || hasTag;
    }

    public int countTag(Task task) {
        int count = 0;
        for (Tag t : tags) {
            if (task.hasTag(t)) {
                count += 1;
            }
        }
        return count;
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
            return -1; // TODO maybe throw error
        }

        int score = Integer.MAX_VALUE;
        String joinnedKeywords = String.join(" ", keywords).toLowerCase();
        String taskName = task.getName().fullName.toLowerCase();
        String[] splitTaskName = taskName.split("\\s+");

        for (int i = 0; i < splitTaskName.length; i++) {
            String joinnedPhrase =
                    String.join(" ", Arrays.copyOfRange(splitTaskName, i, i + keywords.size()));

            if (StringUtil.keywordMatchStartOfPhrase(joinnedKeywords, joinnedPhrase)) {
                score = Math.min(1, score);
            }

            if (joinnedKeywords.equals(joinnedPhrase)) {
                score = Math.min(0, score);
            }

            if (joinnedKeywords.length() > 2) {
                int currScore =
                        StringUtil.levenshteinDistanceCompare(
                                joinnedPhrase, joinnedKeywords, threshold);
                if (currScore >= 0) {
                    score = Math.min(score, currScore);
                }
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
