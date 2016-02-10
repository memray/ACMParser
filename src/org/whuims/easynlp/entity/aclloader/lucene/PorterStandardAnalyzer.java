package org.whuims.easynlp.entity.aclloader.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;

public final class PorterStandardAnalyzer extends StopwordAnalyzerBase {

	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	/**
	 * An unmodifiable set containing some common English words that are usually
	 * not useful for searching.
	 */
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	/**
	 * Builds an analyzer with the given stop words.
	 * 
	 * @param stopWords
	 *            stop words
	 */
	public PorterStandardAnalyzer(CharArraySet stopWords) {
		super(stopWords);
	}

	/**
	 * @deprecated Use {@link #StandardAnalyzer(CharArraySet)}
	 */
	@Deprecated
	public PorterStandardAnalyzer(Version matchVersion, CharArraySet stopWords) {
		super(matchVersion, stopWords);
	}

	/**
	 * Builds an analyzer with the default stop words ({@link #STOP_WORDS_SET}).
	 */
	public PorterStandardAnalyzer() {
		this(STOP_WORDS_SET);
	}

	/**
	 * @deprecated Use {@link #StandardAnalyzer()}
	 */
	@Deprecated
	public PorterStandardAnalyzer(Version matchVersion) {
		this(matchVersion, STOP_WORDS_SET);
	}

	/**
	 * Builds an analyzer with the stop words from the given reader.
	 * 
	 * @see WordlistLoader#getWordSet(Reader)
	 * @param stopwords
	 *            Reader to read stop words from
	 */
	public PorterStandardAnalyzer(Reader stopwords) throws IOException {
		this(loadStopwordSet(stopwords));
	}

	/**
	 * @deprecated Use {@link #StandardAnalyzer()}
	 */
	@Deprecated
	public PorterStandardAnalyzer(Version matchVersion, Reader stopwords)
			throws IOException {
		this(matchVersion, loadStopwordSet(stopwords, matchVersion));
	}

	/**
	 * Set maximum allowed token length. If a token is seen that exceeds this
	 * length then it is discarded. This setting only takes effect the next time
	 * tokenStream or tokenStream is called.
	 */
	public void setMaxTokenLength(int length) {
		maxTokenLength = length;
	}

	/**
	 * @see #setMaxTokenLength
	 */
	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName,
			final Reader reader) {
		final StandardTokenizer src = new StandardTokenizer(getVersion(),
				reader);
		src.setMaxTokenLength(maxTokenLength);
		TokenStream tok = new StandardFilter(getVersion(), src);
		tok = new LowerCaseFilter(getVersion(), tok);
		tok = new StopFilter(getVersion(), tok, stopwords);
		tok = new PorterStemFilter(tok);
		return new TokenStreamComponents(src, tok) {
			@Override
			protected void setReader(final Reader reader) throws IOException {
				src.setMaxTokenLength(PorterStandardAnalyzer.this.maxTokenLength);
				super.setReader(reader);
			}
		};
	}
}
