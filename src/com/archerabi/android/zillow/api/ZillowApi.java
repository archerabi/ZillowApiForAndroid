/**
 * 
 */
package com.archerabi.android.zillow.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.archerabi.android.zillow.api.model.Demographics;
import com.archerabi.android.zillow.api.model.Neighborhood;
import com.archerabi.android.zillow.api.model.RegionChart;
import com.archerabi.android.zillow.api.model.RegionChildren;

/**
 * @author "Abhijith Reddy"
 * 
 */
public class ZillowApi {

	private static String ZILLOW_API_HOST = "http://www.zillow.com/webservice/";

	private static String GET_DEMOGRAPHICS_URI_STATE_CITY = "GetDemographics.htm?zws-id=%s&state=%s&city=";

	private static String GET_DEMOGRAPHICS_URI_REGION = "GetDemographics.htm?zws-id=%s&regionid=%s";

	private static String GET_NEIGHBORHOODS_URI = "GetRegionChildren.htm?zws-id=%s&state=%s&city=%s&childtype=neighborhood";

	private static String GET_REGION_CHART__CITY_URI = "GetRegionChart.htm?zws-id=%s&state=%s&city=%s&unit-type=dollar&width=%d&height=%d&chartDuration=%s";

	private static String GET_REGION_CHART__NEIGHBORHOOD_URI = "GetRegionChart.htm?zws-id=%s&neighborhood=%s&state=%s&city=%s&unit-type=dollar&width=%d&height=%d&chartDuration=%s";

	private String mApiKey;

	private Context context;

	public enum CHART_DURATION {
		ONE_YEAR, FIVE_YEARS, TEN_YEARS
	}

	/**
	 * @param apiKey
	 */
	public ZillowApi(String apiKey, Context context) {
		this.mApiKey = apiKey;
		this.context = context;
	}

	/**
	 * Starts a GET request to the demographics api and returns the results as a
	 * map of metrics. This is a synchronous method.
	 * 
	 * @param state
	 * @param cityName
	 * @return
	 */
	public Demographics getDemographicInformation(String state, String cityName) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_DEMOGRAPHICS_URI_STATE_CITY, mApiKey, URLEncoder.encode(state, "UTF-8"))
					+ URLEncoder.encode(cityName, "UTF-8");
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
			return dem;
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

	public Demographics getDemographicInformation(String regionId) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_DEMOGRAPHICS_URI_REGION, mApiKey, URLEncoder.encode(regionId, "UTF-8"));
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
			return dem;
		} catch (URISyntaxException e) {
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
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
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (Exception e) {
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
		}
		return null;
	}

	/**
	 * @param duration
	 * @param dur
	 * @return
	 */
	private String getDuration(CHART_DURATION duration) {
		String dur = "";
		switch (duration) {
		case FIVE_YEARS:
			dur = "5years";
			break;
		case ONE_YEAR:
			dur = "1year";
			break;
		case TEN_YEARS:
			dur = "10years";
			break;
		}
		return dur;
	}

	/**
	 * @param state
	 * @param city
	 * @param width
	 * @param height
	 * @param duration
	 * @return
	 */
	public URI getRegionChartUri(String regionId, String state, String city, int width, int height, CHART_DURATION duration) {
		String uri = null;
		try {
			String dur = "";
			dur = getDuration(duration);
			if (regionId != null) {
				uri = ZILLOW_API_HOST
						+ String.format(GET_REGION_CHART__NEIGHBORHOOD_URI, mApiKey, URLEncoder.encode(regionId, "UTF-8"), URLEncoder.encode(state, "UTF-8"),
								URLEncoder.encode(city, "UTF-8"), width, height, dur);
			} else {
				uri = ZILLOW_API_HOST
						+ String.format(GET_REGION_CHART__CITY_URI, mApiKey, URLEncoder.encode(state, "UTF-8"), URLEncoder.encode(city, "UTF-8"), width,
								height, dur);
			}
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
			RegionChart child = serializer.read(RegionChart.class, response.getEntity().getContent());
			return new URI(child.getUrl());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Log.e(context.getResources().getString(R.string.app_name), e.getMessage());
		} catch (Exception e) {
			Log.w(context.getResources().getString(R.string.app_name), e.getMessage());
		}
		return null;
	}
	
	/**
	 * Downloads the region chart for the specified parameters and returns in the form of a Drawable
	 * @param regionId
	 * @param state
	 * @param city
	 * @param width
	 * @param height
	 * @param duration
	 * @return
	 */
	public Drawable getRegionChart(String regionId, String state, String city, int width, int height, CHART_DURATION duration) {
		URI uri = getRegionChartUri(regionId, state, city, width, height, duration);
		InputStream is;
		try {
			is = (InputStream) uri.toURL().getContent();
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
