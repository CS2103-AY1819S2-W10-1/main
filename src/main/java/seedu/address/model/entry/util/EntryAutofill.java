package seedu.address.model.entry.util;

import java.net.URL;
import java.util.Optional;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.io.Files;
import com.rometools.rome.feed.synd.SyndFeed;

import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import seedu.address.commons.util.Candidate;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;

/**
 * Attempts to autofill an Entry's missing Title or Description by parsing its URL or HTML.
 */
public class EntryAutofill {

    private static final String FALLBACK_TITLE = "Untitled";
    private static final String FALLBACK_DESCRIPTION = "No description";
    private static final int MAX_WORDS = 32;

    private final Entry originalEntry;
    private final boolean noTitleOrNoDescription;
    private final Candidate<String, Title> titleCandidate;
    private final Candidate<String, Description> descriptionCandidate;

    public EntryAutofill(Entry originalEntry) {
        this.originalEntry = originalEntry;
        this.noTitleOrNoDescription = originalEntry.getTitle().isEmpty() || originalEntry.getDescription().isEmpty();
        this.titleCandidate = new Candidate<>(new Title(FALLBACK_TITLE), string -> {
            try {
                return Optional.of(ParserUtil.parseTitle(Optional.of(string)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
        this.descriptionCandidate = new Candidate<>(new Description(FALLBACK_DESCRIPTION), string -> {
            try {
                return Optional.of(ParserUtil.parseDescription(Optional.of(string)));
            } catch (ParseException pe) {
                return Optional.empty();
            }
        });
    }

    /**
     * Extract candidates by parsing URL.
     * @param url URL to parse
     */
    public void extractFromUrl(URL url) {
        if (noTitleOrNoDescription) {
            String baseName = Files.getNameWithoutExtension(url.getPath())
                    .replaceAll("\n", "") // remove newline chars
                    .replaceAll("\r", "") // remove carriage return chars
                    .replaceAll("[^a-zA-Z0-9]+", " ") // replace special chars with spaces
                    .trim();
            titleCandidate.tryout(WordUtils.capitalizeFully(baseName)); // title - cleaned up base name
            descriptionCandidate.tryout(url.getHost()); // description - host name
        }
    }

    /**
     * Extract candidates by parsing URL pointing to a feed.
     * Differs from {@code extractFromUrl} because we want to autofill the Title with hostname and description
     * with the base name of the file.
     * @param url URL to parse as a link to RSS/Atom feed
     */
    public void extractFromFeedUrl(URL url) {
        if (noTitleOrNoDescription) {
            String baseName = Files.getNameWithoutExtension(url.getPath())
                    .replaceAll("\n", "") // remove newline chars
                    .replaceAll("\r", "") // remove carriage return chars
                    .replaceAll("[^a-zA-Z0-9]+", " ") // replace special chars with spaces
                    .trim();
            titleCandidate.tryout(url.getHost()); // title - host name
            descriptionCandidate.tryout(WordUtils.capitalizeFully(baseName)); // description - cleaned up base name
        }
    }
    /**
     * Extract candidates by parsing HTML.
     * @param html raw HTML to parse
     */
    public void extractFromHtml(String html) {
        if (noTitleOrNoDescription) {

            // Process through Jsoup
            Document document = Jsoup.parse(html);
            titleCandidate // title 2nd choice - document title element
                    .tryout(StringUtil.utfSafeOf(document.title().trim()));
            descriptionCandidate // desc 3rd choice - first N words of raw document body text
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                        StringUtil.utfSafeOf(document.body().text().trim()), MAX_WORDS));


            // Process through Readability4J
            Readability4J readability4J = new Readability4J("", document);
            Article article = readability4J.parse();
            titleCandidate // title 1st choice - extract title
                    .tryout(StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getTitle())).trim());
            descriptionCandidate
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                            StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getTextContent())).trim(), MAX_WORDS
                    )) // desc 2nd choice - first N words of cleaned-up document body text
                    .tryout(StringUtil.getFirstNWordsWithEllipsis(
                            StringUtil.utfSafeOf(StringUtil.nullSafeOf(article.getExcerpt())).trim(), MAX_WORDS
                    )); // desc 1st choice - extract description

        }
    }

    /** Extract candidate by parsing RSS/Atom feed metadata. */
    public void extractFromFeed(SyndFeed feed) {
        if (noTitleOrNoDescription) {
            titleCandidate.tryout(feed.getTitle());
            descriptionCandidate.tryout(feed.getDescription());
        }
    }

    /** get the filled up Entry. */
    public Entry getFilledEntry() {
        return new Entry(
                // best title
                originalEntry.getTitle().isEmpty() ? titleCandidate.get() : originalEntry.getTitle(),
                // best description
                originalEntry.getDescription().isEmpty() ? descriptionCandidate.get() : originalEntry.getDescription(),
                originalEntry.getLink(),
                originalEntry.getTags()
        );
    }
}
