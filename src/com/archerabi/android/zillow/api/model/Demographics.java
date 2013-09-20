package com.archerabi.android.zillow.api.model;
import java.util.List;

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

	@ElementList(inline=true)
	@Path("response/pages/page/tables/table/data")
	List<Metric> metrics;

	/**
	 * @return the metrics
	 */
	public List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}
	
}
