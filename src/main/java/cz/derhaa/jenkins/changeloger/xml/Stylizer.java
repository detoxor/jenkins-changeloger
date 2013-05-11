package cz.derhaa.jenkins.changeloger.xml;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author derhaa
 */
public class Stylizer {

	private TransformerFactory tFactory;
	private Transformer transformer;

	public String style(String xml, File xslFile) {
		final StringReader reader = new StringReader(xml);
	    final StringWriter writer = new StringWriter();
	    
	    if(tFactory == null) { 
		    tFactory = TransformerFactory.newInstance();
		    try {
				transformer = tFactory.newTransformer(new StreamSource(xslFile));
			} catch (TransformerConfigurationException e) {
				throw new ChangeLoggerException("Problem loading xsl stylesheet", e);
			}
	    }
	    
	    try {
			transformer.transform(new StreamSource(reader), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new ChangeLoggerException("Problem transforming xsl stylesheet", e);
		}
	    
	    return writer.toString();
	}
}
