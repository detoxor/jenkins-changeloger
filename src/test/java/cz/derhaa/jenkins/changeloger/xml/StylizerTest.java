package cz.derhaa.jenkins.changeloger.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author derhaa
 *
 */
public class StylizerTest {

	private Stylizer tested;
	private File xmlFile;
	private File xslFile;

	@Before
	public void setUp() throws Exception {
		tested = new Stylizer();
		xmlFile = new File("src/test/resources/job.xml");
		xslFile = new File("src/test/resources/job.xsl");
	}

	@Test
	public final void testStyle() {
		StringBuilder sb = new StringBuilder();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
			String line = "";
			while((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		String result = tested.style(sb.toString(), xslFile);
		Assert.assertNotNull(result);
	}

}
