package cz.derhaa.jenkins.changeloger.sender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.derhaa.jenkins.changeloger.util.Tool;
import cz.derhaa.jenkins.changeloger.xml.ChangeLoggerException;

/**
 * @author derhaa
 * parser.parse(build, "?wrapper=changes&xpath=//changeSet")
 */
public class Sender {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
	private final Properties properties;
	private InternetAddress[] addresses;
	private MimeMessage mimeMsg;
	
	public Sender(final Properties properties) {
		this.properties = properties;
	}

	public void send(String message) {
		// init addresses
		if(addresses == null) {
			try {
				addresses = InternetAddress.parse(properties.getProperty(Tool.CONTACTS));
			} catch (AddressException e) {
				LOGGER.error("Error during parsing emails", e);
				throw new ChangeLoggerException(e);
			}			
		}

		// init mail
		if (mimeMsg == null) {
			try {
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");				
				Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(properties.getProperty(Tool.SENDER_MAIL),properties.getProperty(Tool.SENDER_EMAIL_PASS));//""
						}
					}
				);
				mimeMsg = new MimeMessage(session);
				mimeMsg.setFrom(new InternetAddress(properties.getProperty(Tool.SENDER_MAIL)));
				mimeMsg.setRecipients(Message.RecipientType.TO,	addresses);
				mimeMsg.setSubject("Jenkins - build statuses");
			} catch (AddressException e) {
				LOGGER.error("Error during parsing emails", e);
				throw new ChangeLoggerException(e);
			} catch (MessagingException e) {
				LOGGER.error("Settings of mail failed", e);
				throw new ChangeLoggerException(e);
			}
		}
		
		try {
			mimeMsg.setContent(message, "text/html");
			Transport.send(mimeMsg);
			LOGGER.info("Message has been sended to "+addresses.toString()+". Content of message: "+message);
		} catch (MessagingException e) {
			LOGGER.error("Send of mail failed", e);
			throw new ChangeLoggerException(e);
		}		
	}
   }
