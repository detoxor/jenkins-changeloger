/**
 * 
 */
package cz.derhaa.jenkins.changeloger.sender;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import cz.derhaa.jenkins.changeloger.domain.Build;
import cz.derhaa.jenkins.changeloger.util.Tool;
import cz.derhaa.jenkins.changeloger.xml.Parser;
import cz.derhaa.jenkins.changeloger.xml.Stylizer;

/**
 * @author derhaa
 *
 */
public class SenderTest {

	private Sender tested;
	private Parser parser;
	private Stylizer stylizer;

	@Before
	public void setUp() throws Exception {
		tested = new Sender(Tool.getProperties("src/test/resources/config.properties"));
		parser = new Parser();
		stylizer = new Stylizer();
	}

	@Test
	public final void testSend() {
		Build build = new Build(null, "http://localhost:8080/jenkins/job/JMokos/", 18, null, null, null);
		String xmlContent = parser.parse(build);
		String message = stylizer.style(xmlContent, new File("src/test/resources/job.xsl"));
		tested.send(message);
	}

}
