package seedu.address.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.testutil.EntryBuilder;

public class UniqueEntryListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueEntryList uniqueEntryList = new UniqueEntryList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.contains(null);
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueEntryList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueEntryList.add(ALICE);
        assertTrue(uniqueEntryList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniqueEntryList.add(ALICE);
        Entry editedAlice = new EntryBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SCIENCE)
                .build();
        assertTrue(uniqueEntryList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.add(null);
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueEntryList.add(ALICE);
        thrown.expect(DuplicateEntryException.class);
        uniqueEntryList.add(ALICE);
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.setEntry(null, ALICE);
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.setEntry(ALICE, null);
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        thrown.expect(EntryNotFoundException.class);
        uniqueEntryList.setEntry(ALICE, ALICE);
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniqueEntryList.add(ALICE);
        uniqueEntryList.setEntry(ALICE, ALICE);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        expectedUniqueEntryList.add(ALICE);
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniqueEntryList.add(ALICE);
        Entry editedAlice = new EntryBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SCIENCE)
                .build();
        uniqueEntryList.setEntry(ALICE, editedAlice);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        expectedUniqueEntryList.add(editedAlice);
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniqueEntryList.add(ALICE);
        uniqueEntryList.setEntry(ALICE, BOB);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        expectedUniqueEntryList.add(BOB);
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueEntryList.add(ALICE);
        uniqueEntryList.add(BOB);
        thrown.expect(DuplicateEntryException.class);
        uniqueEntryList.setEntry(ALICE, BOB);
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.remove(null);
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        thrown.expect(EntryNotFoundException.class);
        uniqueEntryList.remove(ALICE);
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueEntryList.add(ALICE);
        uniqueEntryList.remove(ALICE);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.setPersons((UniqueEntryList) null);
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniqueEntryList.add(ALICE);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        expectedUniqueEntryList.add(BOB);
        uniqueEntryList.setPersons(expectedUniqueEntryList);
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueEntryList.setPersons((List<Entry>) null);
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniqueEntryList.add(ALICE);
        List<Entry> entryList = Collections.singletonList(BOB);
        uniqueEntryList.setPersons(entryList);
        UniqueEntryList expectedUniqueEntryList = new UniqueEntryList();
        expectedUniqueEntryList.add(BOB);
        assertEquals(expectedUniqueEntryList, uniqueEntryList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Entry> listWithDuplicateEntries = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateEntryException.class);
        uniqueEntryList.setPersons(listWithDuplicateEntries);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueEntryList.asUnmodifiableObservableList().remove(0);
    }
}
