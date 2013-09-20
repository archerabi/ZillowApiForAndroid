package com.archerabi.android.zillow.api.model;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class CityValue {
	
	@Element(required=false)
	private Float value;

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
	
	
}
