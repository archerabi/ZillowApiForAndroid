/**
 * 
 */
package com.archerabi.android.zillow.api;

/**
 * The object that represents a set of metrics. Each Metric  carries a name, national value and city value;
 * @author "Abhijith Reddy"
 */
public class Metric {

	private String mName;
	
	private float mNationalValue;
	
	private float mCityValue;

	private float mValue;
	
	
	/**
	 * @return the mValue
	 */
	public float getValue() {
		return mValue;
	}

	/**
	 * @param mValue the mValue to set
	 */
	public void setValue(float mValue) {
		this.mValue = mValue;
	}

	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param mName the mName to set
	 */
	public void setName(String mName) {
		this.mName = mName;
	}

	/**
	 * @return the mNationalValue
	 */
	public float getNationalValue() {
		return mNationalValue;
	}

	/**
	 * @param mNationalValue the mNationalValue to set
	 */
	public void setNationalValue(float mNationalValue) {
		this.mNationalValue = mNationalValue;
	}

	/**
	 * @return the mCityValue
	 */
	public float getCityValue() {
		return mCityValue;
	}

	/**
	 * @param mCityValue the mCityValue to set
	 */
	public void setCityValue(float mCityValue) {
		this.mCityValue = mCityValue;
	}

	/**
	 * @param mName
	 * @param mNationalValue
	 * @param mCityValue
	 */
	public Metric(String mName, float mNationalValue, float mCityValue) {
		super();
		this.mName = mName;
		this.mNationalValue = mNationalValue;
		this.mCityValue = mCityValue;
	}

	public Metric() {
	}
	
}
