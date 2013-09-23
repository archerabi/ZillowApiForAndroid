package com.archerabi.android.zillow.api.model;


/**
 * 
 */

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * The object that represents a set of metrics. Each Metric carries a name,
 * national value and city value;
 * 
 * @author "Abhijith Reddy"
 */
@Root(strict = false, name = "attribute")
public class Metric {

	@Element(name = "name")
	private String mName;

	@Element(required = false)
	@Path("values")
	private NeighborhoodValue neighborhood;

	@Element(required = false)
	@Path("values")
	private CityValue city;

	@Element(required=false)
	private Float value;
	
	/**
	 * @return the city
	 */
	public CityValue getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(CityValue city) {
		this.city = city;
	}

	/**
	 * @return the neighborhoodValue
	 */
	public NeighborhoodValue getNeighborhoodValue() {
		return neighborhood;
	}

	/**
	 * @param neighborhoodValue
	 *            the neighborhoodValue to set
	 */
	public void setNeighborhoodValue(NeighborhoodValue neighborhoodValue) {
		this.neighborhood = neighborhoodValue;
	}

	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param mName
	 *            the mName to set
	 */
	public void setName(String mName) {
		this.mName = mName;
	}

	
	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
	}

	/**
	 * @param mName
	 * @param mNationalValue
	 * @param mCityValue
	 */
	public Metric(String mName, float mNationalValue, float mCityValue) {
		super();
		this.mName = mName;
	}

	public Metric() {
	}

}
