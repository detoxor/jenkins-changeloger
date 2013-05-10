/**
 * 
 */
package cz.derhaa.jenkins.changeloger.parser;

/**
 * @author derhaa
 *
 */
@SuppressWarnings("serial")
public class ChangeLoggerException extends RuntimeException {

	public ChangeLoggerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChangeLoggerException(String message) {
		super(message);
	}

	public ChangeLoggerException(Throwable cause) {
		super(cause);
	}

}
