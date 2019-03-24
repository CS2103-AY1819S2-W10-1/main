package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path listEntryBookFilePath = Paths.get("data" , "entrybook.json");
    private Path articleDataDirectoryPath = Paths.get("data", "articles");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setListEntryBookFilePath(newUserPrefs.getListEntryBookFilePath());
        setArticleDataDirectoryPath(newUserPrefs.getArticleDataDirectoryPath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getListEntryBookFilePath() {
        return listEntryBookFilePath;
    }

    public void setListEntryBookFilePath(Path listEntryBookFilePath) {
        requireNonNull(listEntryBookFilePath);
        this.listEntryBookFilePath = listEntryBookFilePath;
    }

    public Path getArticleDataDirectoryPath() {
        return articleDataDirectoryPath;
    }

    public void setArticleDataDirectoryPath(Path articleDataDirectoryPath) {
        requireNonNull(articleDataDirectoryPath);
        this.articleDataDirectoryPath = articleDataDirectoryPath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && listEntryBookFilePath.equals(o.listEntryBookFilePath)
                && articleDataDirectoryPath.equals(o.articleDataDirectoryPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, listEntryBookFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + listEntryBookFilePath);
        return sb.toString();
    }

}
