package com.archerabi.android.zillow.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * 
 */

/**
 * @author "Abhijith Reddy"
 * 
 */
@Root(name = "response", strict = false)
public class Demographics {

	@ElementList
	@Path("response")
	List<DemographicPage> pages;

	@Element
	@Path("response/links")
	private String forSale;
	
	private Map<String, Metric> metricMap;

	public Demographics() {
		metricMap = new HashMap<String, Metric>();
	}

	/**
	 * @return the metrics
	 */
	public List<DemographicPage> getMetrics() {
		return pages;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public void setMetrics(List<DemographicPage> pages) {
		this.pages = pages;
	}

	/**
	 * @param metricName
	 * @return
	 */
	public Map<String, Metric> getMetricMap() {
		if (metricMap.isEmpty()) {
			for (DemographicPage page : pages) {
				for (Metric metric : page.getMetrics()) {
					metricMap.put(metric.getName(), metric);
				}
			}
		}
		return metricMap;
	}

	/**
	 * @return the forSale
	 */
	public String getForSale() {
		return forSale;
	}

	/**
	 * @param forSale the forSale to set
	 */
	public void setForSale(String forSale) {
		this.forSale = forSale;
	}
}
