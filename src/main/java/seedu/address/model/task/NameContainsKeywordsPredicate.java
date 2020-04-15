package seedu.address.model.task;

import java.util.Arrays;
import java.util.Comparator;
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

    public NameContainsKeywordsPredicate(List<String> keywords, Set<Tag> tags) {
        this.keywords = keywords;
        this.tags = tags;
    }

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
        this.tags = new HashSet();
    }

    /**
     * Predicate has been enhanced to return true if the final score < 2. final score is calculated
     * by nameScore - tagCount nameScore is given by getEditDistacne tagCount is given by countTag
     * function
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
        return score <= threshold || hasTag;
    }

    /**
     * returns a comparator by getting the score of a task. Comaprator sorts tasks in ascending
     * order of task score.
     */
    public Comparator<Task> getSearchOrderComparator() {
        return new Comparator<>() {
            @Override
            public int compare(Task task1, Task task2) {
                int score1 = getEditDistance(task1) - countTag(task1);
                int score2 = getEditDistance(task2) - countTag(task2);
                if (score1 == score2) {
                    return 0;
                }
                return score1 < score2 ? -1 : 1;
            }
        };
    }

    /**
     * Counts the number of tags in the task that match a tag given by the user in the find
     * parameters
     */
    private int countTag(Task task) {
        int count = 0;
        for (Tag t : tags) {
            if (task.hasTag(t)) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * A chunk is a subsequence of the taskname that has as many words as in the search phrase. We
     * go through all chunks of the task name and calculate a score for each chunk. The minimum of
     * all chunk scores is then taken as the name score
     *
     * <p>Chunk score is calculated by: 1. A partial name match where input matches start of chunk
     * => score set to 1 2. A match where chunk and input have edit distance < 2 => score set to 1
     * 3. A full chunk between input and task name => score set to 0
     *
     * <p>The edit distance threshold is set at 2 so that phrases that are too dissimilar will not
     * show up.
     */
    private int getEditDistance(Task task) {
        if (keywords.size() == 0) {
            return threshold + 1;
        }

        int score = threshold + 1;
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
