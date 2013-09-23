/**
 * 
 */
package com.archerabi.android.zillow.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * @author "Abhijith Reddy"
 * 
 */
@Root(strict = false)
public class RegionChart {

	@Element
	@Path("response")
	private String url;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
