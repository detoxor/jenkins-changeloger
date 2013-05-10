package cz.derhaa.jenkins.changeloger.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.derhaa.jenkins.changeloger.build.Build;

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
		List<Item> items = new ArrayList<Item>(); 
		tested.parse(build, items);
		Assert.assertNotNull("Returned items cannot be null", items);
		Assert.assertFalse("Returned items cannot be empty", items.isEmpty());
		Assert.assertEquals(2, items.size());
	}

}
