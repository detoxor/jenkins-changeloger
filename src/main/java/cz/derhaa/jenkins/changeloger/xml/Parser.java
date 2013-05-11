package cz.derhaa.jenkins.changeloger.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import cz.derhaa.jenkins.changeloger.domain.Build;

/**
 * @author derhaa
 * 
 */
public class Parser {

	private final StringBuilder stringBuilder;

	public Parser() {
		this.stringBuilder = new StringBuilder();
	}
	
	public String parse(final Build build, final String filterUrl) {
		String retval;
		try {
			String location = build.getWebUrl()+"/"+build.getLastBuildLabel()+"/api/xml"+(filterUrl==null ? "" : filterUrl);
			URL url = new URL(location);
			URLConnection connection = url.openConnection();
			connection.connect();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			stringBuilder.setLength(0);
			String line = "";
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			retval = stringBuilder.toString().replaceAll("&nbsp;", " ");
			if(filterListener != null) filterListener.getData(retval);
		} catch (IOException e) {
			throw new ChangeLoggerException("Fail read xml file", e);
		}
		return retval;
	}
	
	public String parse(final Build build) {
		String retval = parse(build, null);
		return retval;
	}
	
	private FilterListener<?> filterListener;
	
	public void setFilterListener(FilterListener<?> dataMiner) {
		this.filterListener = dataMiner;
	}
}
