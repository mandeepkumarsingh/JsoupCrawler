package Base_Class;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.opencsv.CSVWriter;

public class UrlTraverse {


	public static String  getFailedUrl(String url){
		String result = null;
		try {

			String pageValue=urlStatus(url.trim());
			if(pageValue.contains("4") || pageValue.contains("5")){
				System.out.println("Page not working"+url);
				result= url ;
			}

		}catch(Exception e){
			System.out.println("Exception occured while geeting the failure url"+url);

		}
		return result;

	}
	public static String  urlStatus(String url){
		String result = null;
		try{
			HttpClient httpclient=HttpClientBuilder.create().build();
			HttpGet httpget=new HttpGet(url);
			httpget.addHeader("X-Api_Client", "desktop");
			HttpResponse response=httpclient.execute(httpget);
			String status=response.getStatusLine().toString();
			result=status;

		}catch(Exception e){

			System.out.println("Exception occured while getting the satus code of urls "+e +url);
		}
		return result;
	}

}







