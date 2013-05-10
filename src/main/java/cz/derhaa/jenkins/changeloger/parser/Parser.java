package cz.derhaa.jenkins.changeloger.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cz.derhaa.jenkins.changeloger.build.Build;
import cz.derhaa.jenkins.changeloger.parser.ChangeLoggerException;
import cz.derhaa.jenkins.changeloger.parser.Item;

/**
 * @author derhaa
 * 
 */
public class Parser {

	private final StringBuilder stringBuilder;
	private DocumentBuilderFactory dbf;
	private DocumentBuilder docb;
	private static final String ITEM = "item";
	private static final String AUTHOR = "author";
	private static final String TIMESTAMP = "timestamp";
	private static final String COMMENT = "comment";
	private static final String FILE="affectedPath";

	public Parser() {
		this.stringBuilder = new StringBuilder();
	}

	public void parse(Build build, List<Item> items) {
		if(dbf == null) {
			dbf = DocumentBuilderFactory.newInstance();
			try {
				docb = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new ChangeLoggerException("Document building failed", e);
			}
		}
		
		try {
			URL url = new URL(build.getWebUrl()+"/"+build.getLastBuildLabel()+"/api/xml?wrapper=changes&xpath=//changeSet");
			URLConnection connection = url.openConnection();
			connection.connect();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			stringBuilder.setLength(0);
			String line = "";
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			final String xml = stringBuilder.toString().replaceAll("&nbsp;", " ");
			final ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
			final Document document = docb.parse(bais);
			final NodeList nodes = document.getElementsByTagName(ITEM);
			final int count = nodes.getLength();
			for (int x = 0; x < count; x++) {
				final Node item = nodes.item(x);
				final NodeList children = item.getChildNodes();
				Item itemx = new Item();
				for (int i = 0; i < children.getLength(); i++) {
					final Node child = children.item(i);
					if(child.getNodeName().equals(AUTHOR)) {
						final NodeList a = child.getChildNodes();
						itemx.setAuthor(a.item(1).getTextContent());
					}
					if(child.getNodeName().equals(TIMESTAMP)) {
						try {
							String dateString = child.getTextContent();
							Date date = new Date(Long.valueOf(dateString));
							final String foo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH).format(date);
							itemx.setDate(foo);
						} catch (IndexOutOfBoundsException e) {
							throw new ChangeLoggerException("Invalid length", e);
						}
					}
					if(child.getNodeName().equals(COMMENT)) itemx.setComment(child.getTextContent());
					if(child.getNodeName().equals(FILE)) itemx.setFileName(child.getTextContent());
				}
				items.add(itemx);
			}
		} catch (IOException e) {
			throw new ChangeLoggerException("Fail read xml file", e);
		} catch (SAXException e) {
			throw new ChangeLoggerException("Fail parse xml file", e);
		}
	}
}
