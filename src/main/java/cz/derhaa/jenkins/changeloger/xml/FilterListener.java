/**
 * 
 */
package cz.derhaa.jenkins.changeloger.xml;

/**
 * @author derhaa
 *
 */
public interface FilterListener<E> {

	 E getData(final String xmlContent);
}
