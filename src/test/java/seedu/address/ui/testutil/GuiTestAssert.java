package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.address.model.entry.Entry;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {

    private static final String LABEL_BASE_STYLE = "label";

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(PersonCardHandle expectedCard, PersonCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getLink(), actualCard.getLink());
        assertEquals(expectedCard.getTitle(), actualCard.getTitle());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
        expectedCard.getTags().forEach(tag ->
                assertEquals(expectedCard.getTagStyleClasses(tag), actualCard.getTagStyleClasses(tag))
        );
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedEntry}.
     */
    public static void assertCardDisplaysPerson(Entry expectedEntry, PersonCardHandle actualCard) {
        assertEquals(expectedEntry.getTitle().fullName, actualCard.getTitle());
        assertEquals(expectedEntry.getComment().value, actualCard.getPhone());
        assertEquals(expectedEntry.getLink().value, actualCard.getLink());
        assertEquals(expectedEntry.getAddress().value, actualCard.getAddress());
        assertTagsAndTagColorStylesEqual(expectedEntry, actualCard);
    }

    /**
     * Asserts that the list in {@code personListPanelHandle} displays the details of {@code entries} correctly and
     * in the correct order.
     */
    public static void assertListMatching(PersonListPanelHandle personListPanelHandle, Entry... entries) {
        for (int i = 0; i < entries.length; i++) {
            personListPanelHandle.navigateToCard(i);
            assertCardDisplaysPerson(entries[i], personListPanelHandle.getPersonCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code personListPanelHandle} displays the details of {@code entries} correctly and
     * in the correct order.
     */
    public static void assertListMatching(PersonListPanelHandle personListPanelHandle, List<Entry> entries) {
        assertListMatching(personListPanelHandle, entries.toArray(new Entry[0]));
    }

    /**
     * Asserts the size of the list in {@code personListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(PersonListPanelHandle personListPanelHandle, int size) {
        int numberOfPeople = personListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }

    /**
     * Returns the color style for {@code tagName}'s label. The tag's color is determined by looking up the color
     * in {@code PersonCard#TAG_COLOR_STYLES}, using an index generated by the hash code of the tag's content.
     */
    private static String getTagColorStyleFor(String tagName) {
        switch (tagName) {

        case "classmates":
            return "pink";

        case "owesMoney":
            return "lightBlue";

        case "colleagues":
            return "deepOrange";

        case "neighbours":
            return "gray";

        case "family":
            return "red";

        case "friend":
            return "amber";

        case "friends":
            return "lightBlue";

        case "husband":
            return "gray";

        default:
            throw new AssertionError(tagName + " does not have a color assigned.");
        }
    }

    /**
     * Asserts that the tags in {@code actualCard} matches all the tags in {@code expectedEntry} with the correct
     * color.
     */
    private static void assertTagsAndTagColorStylesEqual(Entry expectedEntry, PersonCardHandle actualCard) {

        // assert tags equal
        List<String> expectedTags = expectedEntry
                .getTags()
                .stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toList());
        List<String> actualTags = actualCard.getTags();
        assertEquals(expectedTags, actualTags);

        // assert tag color styles correct
        expectedTags.forEach(tag -> {
            List<String> expectedTagStyleClasses = Arrays.asList(LABEL_BASE_STYLE, getTagColorStyleFor(tag));
            List<String> actualTagStyleClasses = actualCard.getTagStyleClasses(tag);
            assertEquals(expectedTagStyleClasses, actualTagStyleClasses);
        });

    }

}
