package cz.derhaa.jenkins.changeloger.xml;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.derhaa.jenkins.changeloger.domain.Build;
import cz.derhaa.jenkins.changeloger.domain.Item;

/**
 * @author derhaa
 *
 */
public class ParserTest {

	private Parser tested;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tested = new Parser();
	}

	@Test
	public final void testParse() {
		Build build = new Build("JMokos 18", "http://localhost:8080/jenkins/job/JMokos", 18, new Date(), "Success", "someActivity");
		FilterItemData listener = new FilterItemData();
		tested.setFilterListener(listener);
		String xmlContent = tested.parse(build, null);//"?wrapper=changes&xpath=//changeSet"
		Assert.assertNotNull("Content of xml cannot be null", xmlContent);
		List<Item> data = listener.getData(xmlContent);
		Assert.assertNotNull("Returned items cannot be null", data);
		Assert.assertFalse("Returned items cannot be empty", data.isEmpty());
		Assert.assertEquals(2, data.size());
	}

}
