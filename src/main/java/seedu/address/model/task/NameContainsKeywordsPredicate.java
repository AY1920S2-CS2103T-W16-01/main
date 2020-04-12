package seedu.address.model.task;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import seedu.address.commons.util.StringUtil;

/** Tests that a {@code Task}'s {@code Name} matches any of the keywords given. */
public class NameContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;
    private final int threshold = 3;
    private int score = Integer.MAX_VALUE;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public boolean test(Task task) { // change test to return an int value as the edit distance
        if (keywords.size() == 0) {
            return false;
        }

        this.score = Integer.MAX_VALUE;
        String joinnedKeywords = String.join(" ", keywords);
        String[] splitTaskName = task.getName().fullName.split("\\s+");
        for (int i = 0; i < splitTaskName.length; i++) {
            String joinnedPhrase =
                    String.join(" ", Arrays.copyOfRange(splitTaskName, i, i + keywords.size()));
            int currScore = StringUtil.limitedCompare(joinnedPhrase, joinnedKeywords, threshold);
            if (currScore >= 0) {
                this.score = Math.min(this.score, currScore);
            }
        }

        for (String key : keywords) {
            for (String name : splitTaskName) {
                if (StringUtil.keywordMatchStartOfPhrase(key, name)) {
                    this.score = 1;
                }
                if (StringUtil.keywordMatchPhrase(key, name)) {
                    this.score = 0;
                }
            }
        }

        return this.score != -1 && this.score != Integer.MAX_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                        && keywords.equals(
                                ((NameContainsKeywordsPredicate) other).keywords)); // state check
    }
}
