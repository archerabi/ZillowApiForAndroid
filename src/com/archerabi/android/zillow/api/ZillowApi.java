/**
 * 
 */
package com.archerabi.android.zillow.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * @author "Abhijith Reddy"
 * 
 */
public class ZillowApi {

	private static String ZILLOW_API_HOST = "http://www.zillow.com/";

	private static String GET_DEMOGRAPHICS_URI = "webservice/GetDemographics.htm?zws-id=%s&state=%s&city=";

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
	public Map<String,Metric> getDemographicInformation(String state, String cityName) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_DEMOGRAPHICS_URI, mApiKey, state) + URLEncoder.encode(cityName, "UTF-8");
			Log.d(getClass().getName(), "URI = "+uri);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (uri == null) {
			return null;
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		HttpResponse response;
		try {
			request.setURI(new URI(uri));
			response = client.execute(request);
			Map<String,Metric> metrics = parseDemographics(response.getEntity().getContent());
			Log.d(getClass().getName(), "Metric List size is " + metrics.size());
			return metrics;
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse the results from getDemographicInformation
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private Map<String, Metric> parseDemographics(InputStream in) throws IOException {
		XmlPullParser parser = Xml.newPullParser();
		Map<String, Metric> entries = new HashMap<String, Metric>();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "Demographics:demographics");
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				if (name != null && name.compareTo("attribute") == 0) {
					parser.next();
					Metric metric = parseAttribute(parser);
					entries.put(metric.getName(), metric);
				}
			}
			return entries;
		} catch (XmlPullParserException e) {
			if (parser.getName() != null) {
				Log.d(getClass().getName(), "Current tag is " + parser.getName());
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return null;
	}

	/**
	 * Parse {@code attribute} tags. Since each attribute is modeled as a
	 * {@link Metric} , this method returns a Metric object.
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Metric parseAttribute(XmlPullParser parser) throws XmlPullParserException, IOException {
		Metric metric = new Metric();
		metric.setName(readText(parser, "name"));
		Log.d(getClass().getName(), "Constructing metric for " + metric.getName());
		if (parser.getName().compareTo("values") == 0) {
			parser.require(XmlPullParser.START_TAG, null, "values");
			parser.next();
			if (parser.getName().compareTo("city") == 0) {
				parser.next();
				try {
					float value = Float.parseFloat(readText(parser, "value"));
					metric.setCityValue(value);
					metric.setValue(value);
				} catch (NumberFormatException e) {
				}
				parser.next();
			}
			if (parser.getName().compareTo("nation") == 0) {
				parser.next();
				try {
					metric.setNationalValue(Float.parseFloat(readText(parser, "value")));
				} catch (NumberFormatException e) {
				}
				parser.next();
			}
			parser.require(XmlPullParser.END_TAG, null, "values");
			parser.next();
		} else if (parser.getName().compareTo("value") == 0) {
			metric.setValue(Float.parseFloat(readText(parser, "value")));
		}
		Log.d(getClass().getName(), "Built metric for attribute " + metric.getName());
		return metric;
	}

	/**
	 * Reads text correspoding to {@code tag} and moves the parser to the next
	 * tag. <br/>
	 * For Example <br/>
	 * <br/>
	 * The following xml will be successfully parsed <br>
	 * {@code <name>Zillow Home Value Index</name>}. <br>
	 * The string "Zillow Home Value Index" will be returned.
	 * 
	 * @param parser
	 * @param tag
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readText(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
		String result = "";
		parser.require(XmlPullParser.START_TAG, null, tag);
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		parser.require(XmlPullParser.END_TAG, null, tag);
		parser.next();
		return result;
	}

}
