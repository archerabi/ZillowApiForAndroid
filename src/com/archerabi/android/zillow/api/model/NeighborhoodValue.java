package com.archerabi.android.zillow.api.model;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class NeighborhoodValue {
	
	@Element(required=false)
	private Float value;

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}
}
