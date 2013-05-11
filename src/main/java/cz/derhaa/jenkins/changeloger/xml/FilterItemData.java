/**
 * 
 */
package cz.derhaa.jenkins.changeloger.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import cz.derhaa.jenkins.changeloger.domain.Item;

/**
 * @author derhaa
 *
 */
public class FilterItemData implements FilterListener<List<Item>> {

	private static final String ITEM = "item";
	private static final String AUTHOR = "author";
	private static final String TIMESTAMP = "timestamp";
	private static final String COMMENT = "comment";
	private static final String FILE="affectedPath";	
	
	private DocumentBuilderFactory dbf;
	private DocumentBuilder docb;

	@Override
	public List<Item> getData(final String xmlContent) {
		final List<Item> items = new ArrayList<Item>();
		if(dbf == null) {
			dbf = DocumentBuilderFactory.newInstance();
			try {
				docb = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new ChangeLoggerException("Document building failed", e);
			}
		}		
		final ByteArrayInputStream bais = new ByteArrayInputStream(xmlContent.getBytes());
		NodeList nodes = null;
		try {
			final Document document = docb.parse(bais);
			nodes = document.getElementsByTagName(ITEM);
		} catch (SAXException e) {
			throw new ChangeLoggerException("Document parsing failed", e);
		} catch (IOException e) {
			throw new ChangeLoggerException("Document reading failed", e);
		}
		
		if(nodes == null) return items;
		
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
		return items;
	}

}
