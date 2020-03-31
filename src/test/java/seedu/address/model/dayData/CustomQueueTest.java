package seedu.address.model.dayData;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CustomQueueTest {

    private final CustomQueue customQueue = new CustomQueue();

    @Test
    public void contains_nullTask_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customQueue.contains(null));
    }
    /*
       @Test
       public void contains_taskNotInList_returnsFalse() {
           assertFalse(uniqueTaskList.contains(HOMEWORK10));
       }

       @Test
       public void contains_taskInList_returnsTrue() {
           uniqueTaskList.add(HOMEWORK10);
           assertTrue(uniqueTaskList.contains(HOMEWORK10));
       }

       @Test
       public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
           uniqueTaskList.add(HOMEWORK10);
           Task editedAlice =
                   new TaskBuilder(HOMEWORK10)
                           .withDescription(VALID_DESCRIPTION_TASK2)
                           .withTags(VALID_TAG_MA1521)
                           .build();
           assertTrue(uniqueTaskList.contains(editedAlice));
       }

       @Test
       public void add_nullPerson_throwsNullPointerException() {
           assertThrows(NullPointerException.class, () -> uniqueTaskList.add(null));
       }

       @Test
       public void add_duplicatePerson_throwsDuplicatePersonException() {
           uniqueTaskList.add(HOMEWORK10);
           assertThrows(DuplicateTaskException.class, () -> uniqueTaskList.add(HOMEWORK10));
       }

       @Test
       public void setPerson_nullTargetPerson_throwsNullPointerException() {
           assertThrows(NullPointerException.class, () -> uniqueTaskList.setTask(null, HOMEWORK10));
       }

       @Test
       public void setPerson_nullEditedPerson_throwsNullPointerException() {
           assertThrows(NullPointerException.class, () -> uniqueTaskList.setTask(HOMEWORK10, null));
       }

       @Test
       public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
           assertThrows(
                   TaskNotFoundException.class, () -> uniqueTaskList.setTask(HOMEWORK10, HOMEWORK10));
       }

       @Test
       public void setPerson_editedPersonIsSamePerson_success() {
           uniqueTaskList.add(HOMEWORK10);
           uniqueTaskList.setTask(HOMEWORK10, HOMEWORK10);
           UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
           expectedUniqueTaskList.add(HOMEWORK10);
           assertEquals(expectedUniqueTaskList, uniqueTaskList);
       }

       @Test
       public void setPerson_editedPersonHasSameIdentity_success() {
           uniqueTaskList.add(HOMEWORK10);
           Task editedAlice =
                   new TaskBuilder(HOMEWORK10)
                           .withDescription(VALID_DESCRIPTION_TASK2)
                           .withTags(VALID_TAG_MA1521)
                           .build();
           uniqueTaskList.setTask(HOMEWORK10, editedAlice);
           UniqueTaskList expectedUniquePersonList = new UniqueTaskList();
           expectedUniquePersonList.add(editedAlice);
           assertEquals(expectedUniquePersonList, uniqueTaskList);
       }

       @Test
       public void setPerson_editedPersonHasDifferentIdentity_success() {
           uniqueTaskList.add(HOMEWORK10);
           uniqueTaskList.setTask(HOMEWORK10, TASK2);
           UniqueTaskList expectedUniquePersonList = new UniqueTaskList();
           expectedUniquePersonList.add(TASK2);
           assertEquals(expectedUniquePersonList, uniqueTaskList);
       }

       @Test
       public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
           uniqueTaskList.add(HOMEWORK10);
           uniqueTaskList.add(TASK2);
           assertThrows(DuplicateTaskException.class, () -> uniqueTaskList.setTask(HOMEWORK10, TASK2));
       }

       @Test
       public void remove_nullPerson_throwsNullPointerException() {
           assertThrows(NullPointerException.class, () -> uniqueTaskList.remove(null));
       }

       @Test
       public void remove_personDoesNotExist_throwsPersonNotFoundException() {
           assertThrows(TaskNotFoundException.class, () -> uniqueTaskList.remove(HOMEWORK10));
       }

       @Test
       public void remove_existingPerson_removesPerson() {
           uniqueTaskList.add(HOMEWORK10);
           uniqueTaskList.remove(HOMEWORK10);
           UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
           assertEquals(expectedUniqueTaskList, uniqueTaskList);
       }

       @Test
       public void setPersons_nullUniquePersonList_throwsNullPointerException() {
           assertThrows(
                   NullPointerException.class, () -> uniqueTaskList.setTasks((UniqueTaskList) null));
       }

       @Test
       public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
           uniqueTaskList.add(HOMEWORK10);
           UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
           expectedUniqueTaskList.add(TASK2);
           uniqueTaskList.setTasks(expectedUniqueTaskList);
           assertEquals(expectedUniqueTaskList, uniqueTaskList);
       }

       @Test
       public void setPersons_nullList_throwsNullPointerException() {
           assertThrows(NullPointerException.class, () -> uniqueTaskList.setTasks((List<Task>) null));
       }

       @Test
       public void setPersons_list_replacesOwnListWithProvidedList() {
           uniqueTaskList.add(HOMEWORK10);
           List<Task> taskList = Collections.singletonList(TASK2);
           uniqueTaskList.setTasks(taskList);
           UniqueTaskList expectedUniqueTaskList = new UniqueTaskList();
           expectedUniqueTaskList.add(TASK2);
           assertEquals(expectedUniqueTaskList, uniqueTaskList);
       }

       @Test
       public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
           List<Task> listWithDuplicateTask = Arrays.asList(HOMEWORK10, HOMEWORK10);
           assertThrows(
                   DuplicateTaskException.class, () -> uniqueTaskList.setTasks(listWithDuplicateTask));
       }

       @Test
       public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
           assertThrows(
                   UnsupportedOperationException.class,
                   () -> uniqueTaskList.asUnmodifiableObservableList().remove(0));
       }

    */
}
