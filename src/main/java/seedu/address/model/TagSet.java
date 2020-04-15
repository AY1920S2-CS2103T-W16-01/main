package seedu.address.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * HashMap that maps tag to tag frequency Tags are considered to be equivalent when names match,
 * ignoring case
 */
public class TagSet {
    private final HashMap<Tag, Integer> tagCount = new HashMap<>();

    public TagSet(ReadOnlyTaskList taskList) {
        tagCount.clear();
        populateTag(taskList);
    }

    /** creates frequency map from Tag to Tag count */
    public void populateTag(ReadOnlyTaskList taskList) {
        tagCount.clear();
        for (Task t : taskList.getTaskList()) {
            addTask(t);
        }
    }

    public boolean contains(Tag t) {
        return this.tagCount.containsKey(t);
    }

    public Set<Tag> getTags() {
        return tagCount.keySet();
    }

    /** Adds new entry initialized to a count of 1 if task is not already in TagSet */
    public void addTask(Task task) {
        Set<Tag> tags = task.getTags();
        for (Tag t : tags) {
            tagCount.computeIfAbsent(t, (tag) -> 0);
            tagCount.compute(
                    t,
                    (tag, count) -> {
                        return count + 1;
                    });
        }
    }

    /** Decrements count of tag by 1 and removes tag from TagSet if count is 0 */
    public void removeTask(Task task) {
        Set<Tag> tags = task.getTags();
        for (Tag t : tags) {
            tagCount.computeIfAbsent(t, (tag) -> 1); // just in case
            tagCount.compute(
                    t,
                    (tag, count) -> {
                        if (count - 1 == 0) {
                            return null;
                        }
                        return count - 1;
                    });
        }
    }

    /** Array of tag names */
    public String[] getTagNames() {
        ArrayList<String> tagNames = new ArrayList<>();
        for (Tag t : tagCount.keySet().toArray(new Tag[0])) {
            tagNames.add(t.tagName);
        }
        return tagNames.toArray(new String[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tag t : tagCount.keySet()) {
            sb.append(t.toString().toLowerCase());
            sb.append("\n");
        }
        return sb.toString();
    }
}
