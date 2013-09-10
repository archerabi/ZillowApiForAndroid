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
import java.util.ArrayList;
import java.util.List;

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

	public ZillowApi(String apiKey) {
		this.mApiKey = apiKey;
	}

	public List<Metric> getDemographicInformation(String state, String cityName) {
		String uri = null;
		try {
			uri = ZILLOW_API_HOST + String.format(GET_DEMOGRAPHICS_URI, mApiKey, state) + URLEncoder.encode(cityName, "UTF-8");
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
			List<Metric> metrics = parse(response.getEntity().getContent());
			Log.d(getClass().getName(), "Metric List size is " + metrics.size());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Metric> parse(InputStream in) throws IOException {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readResults(parser);
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

	private List<Metric> readResults(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Metric> entries = new ArrayList<Metric>();

		parser.require(XmlPullParser.START_TAG, null, "Demographics:demographics");
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			String name = parser.getName();
			if (name != null && name.compareTo("attribute") == 0) {
				parser.next();
				entries.add(parseAttribute(parser));
			}
		}
		return entries;
	}

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
					metric.setCityValue(Float.parseFloat(readText(parser, "value")));
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
