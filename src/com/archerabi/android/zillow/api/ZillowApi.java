/**
 * 
 */
package com.archerabi.android.zillow.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.archerabi.android.zillow.api.model.Demographics;
import com.archerabi.android.zillow.api.model.Metric;
import com.archerabi.android.zillow.api.model.Neighborhood;
import com.archerabi.android.zillow.api.model.RegionChildren;

/**
 * @author "Abhijith Reddy"
 * 
 */
public class ZillowApi {

	private static String ZILLOW_API_HOST = "http://www.zillow.com/webservice/";

	private static String GET_DEMOGRAPHICS_URI = "GetDemographics.htm?zws-id=%s&state=%s&city=";

	private static String GET_NEIGHBORHOODS_URI = "GetRegionChildren.htm?zws-id=%s&state=%s&city=%s&childtype=neighborhood";

	private String mApiKey;

	/**
	 * @param apiKey
	 */
	public ZillowApi(String apiKey) {
		this.mApiKey = apiKey;
	}

	/**
	 * Starts a GET request to the demographics api and returns the results as a
	 * map of metrics. This is a synchronous method.
	 * 
	 * @param state
	 * @param cityName
	 * @return
	 */
	public Map<String, Metric> getDemographicInformation(String state, String cityName) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_DEMOGRAPHICS_URI, mApiKey, URLEncoder.encode(state, "UTF-8")) + URLEncoder.encode(cityName, "UTF-8");
			Log.d(getClass().getName(), "URI = " + uri);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (uri == null) {
			return null;
		}
		try {
			HttpResponse response = makeHttpRequest(uri);
			Serializer parser = new Persister();
			Demographics dem = parser.read(Demographics.class, response.getEntity().getContent());
			Map<String, Metric> map = new HashMap<String, Metric>();
			for (Metric metric : dem.getMetrics()) {
				map.put(metric.getName(), metric);
			}
			return map;
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param cityName
	 * @param state
	 * @return
	 */
	public List<Neighborhood> getNeighborhoods(String cityName, String state) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_NEIGHBORHOODS_URI, mApiKey, URLEncoder.encode(state, "UTF-8"), URLEncoder.encode(cityName, "UTF-8"));
			Log.d(getClass().getName(), "URI = " + uri);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (uri == null) {
			return null;
		}
		try {
			HttpResponse response = makeHttpRequest(uri);
			Serializer serializer = new Persister();
			RegionChildren child = serializer.read(RegionChildren.class, response.getEntity().getContent());
			return child.getList();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param uri
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private HttpResponse makeHttpRequest(String uri) throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		HttpResponse response;
		request.setURI(new URI(uri));
		response = client.execute(request);
		return response;
	}

}
