package Base_Class;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheet {
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/sheets.googleapis.com-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize()  {
		Credential credential =null;
		try{
			String a=System.getProperty("user.dir")+"/client_secret.json";
			// Load client secrets.
			//        InputStream in =
			//        		AuthorizationCodeFlow.class.getResourceAsStream("/client_secret.json");
			File file= new File(a);
			InputStream inpu=new DataInputStream(new FileInputStream(file));
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inpu));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(DATA_STORE_FACTORY)
					.setAccessType("offline")
					.build();
			credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		}catch(Exception e){
			System.out.println("exception occured while getting the credentials");
		}
		return credential;
	}
	/**  getting sheet service to use further for writing and reading purpose*/

	public static Sheets getSheetsService() throws IOException {
		Credential credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
          /** getting data from google sheet*/
	public static void getSpreadSheetRecords(String sheetId,String range){
		try{
			Sheets sheet=getSheetsService();
			ValueRange response=sheet.spreadsheets().values().get(sheetId, range).execute();
			List<List<Object>> resultData=response.getValues();
		}catch(Exception e){
			System.out.println("Exception occured while getting the value"+e);
		}
	}
	/**   Write in to the google sheet*/
	public static void writeTotalUrl(Set<String> globalUrl){
		try{
			String sheetid="1fwA5uelz_qpESVk-DcE4LeJq3_pWL5GXhqXQqaE4VxI";
			Sheets sheet=getSheetsService();
			ValueRange valrange=new ValueRange();
			List<ValueRange>data=new ArrayList<ValueRange>();
			int counter =1; 
			for ( Object url:globalUrl){
				data.add(new ValueRange().setRange("A"+counter).setValues(Arrays.asList(Arrays.asList(url)))
						.setMajorDimension("ROWS"));
				counter++;

			}
			BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
					.setValueInputOption("USER_ENTERED")
					.setData(data);

			BatchUpdateValuesResponse batchResult = sheet.spreadsheets().values()
					.batchUpdate(sheetid, batchBody)
					.execute();

			System.out.println("Written in google sheet");
		}catch(Exception e){


		}
	}
	/** Read From the google sheet*/
	public static void readAllUrls(){
		String sheetid="1fwA5uelz_qpESVk-DcE4LeJq3_pWL5GXhqXQqaE4VxI";
		String sheetRange="A:C!";
	     
		
		
	}

	public static void main(String args[]){
		//		getSpreadSheetRecords("1fwA5uelz_qpESVk-DcE4LeJq3_pWL5GXhqXQqaE4VxI","A:C");
//		Set<String>set1=new HashSet<String>();
//		set1.add("https://ww.lenskart.com");
//		set1.add("Http://javatpoint.com");
//		writeTotalUrl(set1);
	}
}
