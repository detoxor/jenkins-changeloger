package cz.derhaa.jenkins.changeloger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import cz.derhaa.jenkins.changeloger.domain.Build;
import cz.derhaa.jenkins.changeloger.sender.Sender;
import cz.derhaa.jenkins.changeloger.xml.ChangeLoggerException;
import cz.derhaa.jenkins.changeloger.xml.Parser;
import cz.derhaa.jenkins.changeloger.xml.Stylizer;

/**
 * @author derhaa
 * 
 */
public class App {
	public static void main(String[] args) {
		
		//load program properties
		final Properties props = new Properties();
		FileInputStream fis = null;
		try {
			String filePath = args[0];
			fis = new FileInputStream(new File(filePath));
			props.load(fis);
		} catch (IOException e) {
			throw new ChangeLoggerException("Fail load properties", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new ChangeLoggerException("Fail close properties file", e);
				}
			}
		}
		
		Parser parser = new Parser();
		Build build = new Build(null, props.getProperty("jenkins.job.url")+args[1], null, null, null, null);
		String xmlContent = parser.parse(build);
		Stylizer stylizer = new Stylizer();
		
		String message = stylizer.style(xmlContent, new File(props.getProperty("xsl.filepath")));
		new Sender(props).send(message);
	}
}
