package com.archerabi.android.zillow.api.model;
/**
 * 
 */


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author "Abhijith Reddy"
 *
 */

@Root(name="region",strict=false)
public class Neighborhood {

	@Element
	private String id;
	
	@Element
	private String name;
	
	/**
	 * 
	 */
	public Neighborhood() {
	}
	
	public Neighborhood(String neighborhoodName,String regionId){
		this.name = neighborhoodName;
		this.id = regionId;
	}

	public void setId(String id){
		this.id = id;
	}
	/**
	 * @return the regionId
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(String regionId) {
		this.id = regionId;
	}

	/**
	 * @return the neighborhoodName
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	/**
	 * @param neighborhoodName the neighborhoodName to set
	 */
	public void setNeighborhoodName(String neighborhoodName) {
		this.name = neighborhoodName;
	}
	
	public String toString(){
		return name;
	}
}
