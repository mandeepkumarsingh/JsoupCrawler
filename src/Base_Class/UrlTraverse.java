package Base_Class;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.opencsv.CSVWriter;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UrlTraverse {




	public static String  getFailedUrl(String url){
		String result = null;
		String pageValue;
		try {
			if(url.contains("lenskart")) {
				pageValue=urlStatus(url);
				if(pageValue!=null) {
					if(pageValue.startsWith("4") || pageValue.startsWith("5")){
						System.out.println("Not working :- "+url);
						result= url;
					}
				}
			}

		}catch(Exception e){
			System.out.println("Exception occured while geeting the failure url"+url);
			result= url;
		}
		return result;

	}


	public static String urlStatus(String url){
		String result = null ;
		try{
			RestAssured.baseURI=url;
			Response res;
			res=RestAssured.given().header("X-Api-Client", "desktop").when().get().then().extract().response();
			result=Integer.toString(res.getStatusCode());
			return result;

		}catch(Exception e){

			System.out.println("Exception occured while getting the satus code of urls "+url+e);
		}
		return result;
	}


}







