package org.whuims.easynlp.parserwrapper;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceModelAbbSensitive extends SentenceModel {

	public SentenceModelAbbSensitive(InputStream in) throws IOException,
			InvalidFormatException {
		super(in);
	}
	

}
