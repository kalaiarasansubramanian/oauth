package com.sample.springrest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CountryController {
	
	/*
	 * http://localhost:8080/springrest/allState
	 */
	@RequestMapping(value="/allState")
	public String getAllStateInIndia(){		
		String url = "http://dhanasekarthirunavukkarasu-trial-test.apigee.net/indianstateAll";
		return processGetRequest(url, null);
	}
	

	/*
	 * http://localhost:8080/springrest/State?state=kerala
	 * http://localhost:8080/springrest/State?state=pradesh
	 * http://localhost:8080/springrest/State?state=nadu
	 */
	@RequestMapping(value="/State", params="state")
	public String getStateInIndia(@RequestParam(required=false) final String state){		
		String url = "http://dhanasekarthirunavukkarasu-trial-test.apigee.net/indianstate?text="+state;
		return processGetRequest(url, null);
	}
	
	@RequestMapping(value="/allCountryInWorld")
	public String getAllStateInWorld(){
		String accessToken = getAccessToken("https://dhanasekarthirunavukkarasu-trial-test.apigee.net/oauth2_clientcredentials/accesstoken?grant_type=client_credentials",
				"DJf7MLTkotF3ggu4qTMsgloA2FHG2j3g", "4dfCFS85OYsXxPnV");
		String serviceUrl = "http://dhanasekarthirunavukkarasu-trial-test.apigee.net/worldcountry/state_oAuth2";
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Bearer "+accessToken);
		
		return processGetRequest(serviceUrl, headerMap);
	}
	
	@RequestMapping(value="/allIndia")
	public String getAllStateIndia(){
		String accessToken = getAccessToken("https://dhanasekarthirunavukkarasu-trial-test.apigee.net/oauth2_clientcredentials/accesstoken?grant_type=client_credentials",
				"DJf7MLTkotF3ggu4qTMsgloA2FHG2j3g", "4dfCFS85OYsXxPnV");	
		String serviceUrl = "http://dhanasekarthirunavukkarasu-trial-test.apigee.net/worldcountry/state_oAuth2/allindia";
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Bearer "+accessToken);
		
		return processGetRequest(serviceUrl, headerMap);
	}
	
	@RequestMapping(value="/StateInIndia", params="state")
	public String getStateIndia(@RequestParam(required=false) final String state){
		String accessToken = getAccessToken("https://dhanasekarthirunavukkarasu-trial-test.apigee.net/oauth2_clientcredentials/accesstoken?grant_type=client_credentials",
				"DJf7MLTkotF3ggu4qTMsgloA2FHG2j3g", "4dfCFS85OYsXxPnV");
		
		String serviceUrl = "http://dhanasekarthirunavukkarasu-trial-test.apigee.net/worldcountry/state_oAuth2/india/state?text="+state;
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Bearer "+accessToken);
		
		return processGetRequest(serviceUrl, headerMap);
	}
	
	private String getAccessToken(final String url, final String client_id, final String client_secret){
		String oAuth2Url = url;
		
		String contentType = "application/x-www-form-urlencoded";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("client_id", client_id));
        nameValuePairs.add(new BasicNameValuePair("client_secret", client_secret));
				
		String accessTokenResponse = processPostRequest(oAuth2Url, contentType, nameValuePairs);
		
		String accessToken = "";
		try {
			JsonNode rootNode = new ObjectMapper().readTree(new StringReader(accessTokenResponse));
			accessToken = rootNode.get("access_token").asText();
			System.out.println("AccessToken : " + accessToken);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	private String processGetRequest(final String url, final Map<String, String> headerMap) {
		StringBuffer result = new StringBuffer();	
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		
		if(null != headerMap && !headerMap.isEmpty()) {
			for(String key : headerMap.keySet()){
				request.setHeader(key,headerMap.get(key));
			}
		}
		
		try {
			HttpResponse response = client.execute(request);
			
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " +
	                       response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
	                       new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}
	
	private String processPostRequest(final String url, final String contentType, final List<NameValuePair> nameValuePairs) {
		StringBuffer result = new StringBuffer();	
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		if(null != contentType) {
			request.setHeader("Content-Type", contentType);
		}
		
		try {
			
			if(null != nameValuePairs && !nameValuePairs.isEmpty()) {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}	        
	        
			HttpResponse response = client.execute(request);
			
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " +
	                       response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
	                       new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

}
