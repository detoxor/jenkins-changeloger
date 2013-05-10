/**
 * 
 */
package cz.derhaa.jenkins.changeloger.parser;


/**
 * @author derhaa
 *
 */
public class Item {

	private String author;
	private String comment;
	private String date;
	private String fileName;
	
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAuthor() {
		return author;
	}
	public String getComment() {
		return comment;
	}
	public String getDate() {
		return date;
	}
	public String getFileName() {
		return fileName;
	}

	@Override
	public String toString() {
		return "[Date: "+this.date+", Author: "+this.author+", File: "+this.fileName+", Message: "+this.comment+"]";
	}
	
}
