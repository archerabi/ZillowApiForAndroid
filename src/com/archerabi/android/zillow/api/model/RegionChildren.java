package com.archerabi.android.zillow.api.model;
/**
 * 
 */

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * @author "Abhijith Reddy"
 * 
 */
@Root(name = "response", strict = false)
public class RegionChildren {
	
	@Path("response/list")
	@ElementList(inline=true)
	private List<Neighborhood> list;

	@Path("response")
	@Element
	private String subregiontype;

	public RegionChildren() {

	}

	/**
	 * @return the subregiontype
	 */
	public String getSubregiontype() {
		return subregiontype;
	}

	/**
	 * @param subregiontype
	 *            the subregiontype to set
	 */
	public void setSubregiontype(String subregiontype) {
		this.subregiontype = subregiontype;
	}

	public List<Neighborhood> getList() {
		return list;
	}

}
