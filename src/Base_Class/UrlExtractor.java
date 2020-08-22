package Base_Class;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.NoSuchElementException;

public class UrlExtractor {
	public static long  totalextractedurls;
	public static long  totalfailedurls;
	public static Set<String > extrated=new HashSet<String>();
	public static Set<String > failurls=new HashSet<String>();
	public static List<String> urlExtractors(String mainUrl) {
		List<String> urlList = null;
		try {
			urlList = new ArrayList<String>();
			if(mainUrl.contains("static")||extrated.contains(mainUrl) ) {
				System.out.println(" do Nothing");
			}else {
				Document doc = Jsoup.connect(mainUrl).get();
				extrated.add(mainUrl);
				String title = doc.title();
				Elements links = doc.select("a[href]");
				Elements linkImage = doc.select("img[src]");
				//			Elements linksscript=doc.select("script[src]");

				for (Element link : links) {
					if (link.attr("href").startsWith("/")&& link.attr("href").contains("lenskart") ) {
						String url = "https://www.lenskart.com" + link.attr("href");
						urlList.add(url);
					} else if (link.attr("href").startsWith("https")) {
						String url = link.attr("href");
						urlList.add(url);
					}
				}

				for (Element link : linkImage) {
					urlList.add(mainUrl+"@"+link.attr("src"));
				}
			}
			//			System.out.println(title + " current page title  " + urlList.size());
		}
		catch (Exception e) {

			System.out.println("Exception occured while extracting urls "+e);
		}
		return urlList;
	}


	public static Set <String> totalurl(String url) {
		Set<String> globalurl=new HashSet<String>();
		try {

			List<String> firslevel = urlExtractors(url);
			globalurl.addAll(firslevel);
			System.out.println("Total urls at firstlevel is :- "+globalurl.size());
			for (String list : firslevel) {
				if(!list.contains("static")) {
					List<String> secondlist = urlExtractors(list);
					globalurl.addAll(secondlist);
				}
			}
			totalextractedurls=globalurl.size();
			System.out.println("Total urls at second level is "+totalextractedurls);
		} catch (Exception e) {
			System.out.println("Exception occured while getting url at second  level " + e);
		}

		return globalurl;
	}
	public static void assertOnTotalUrls(String baseurl) {
		try {
			Set<String> globalurl=totalurl(baseurl);
			//			for (String urls : globalurl) {
			//				if(urls.contains("@")) {
			//					String [] imglink=	urls.split("@");
			//					if(UrlTraverse. getFailedUrl(imglink[1])!=null) {
			//						urls=imglink[0]+" :-- "+imglink[1];
			//						failurls.add(urls);
			//					}
			//				}else {
			//					failurls.add(UrlTraverse. getFailedUrl(urls));	
			//				}
			//			}

			ForkJoinPool forkpool=new ForkJoinPool(Runtime.getRuntime().availableProcessors());
			forkpool.submit(()-> globalurl.parallelStream().forEach(link->updatefailurelist(link))).get();
			forkpool.shutdown();
			totalfailedurls=failurls.size();
			GoogleSheet.writeTotalUrl(failurls);

		}catch(Exception e) {
			System.out.println("Exception Occered while writing the final fail urls "+e);
		}
	}
	public static void updatefailurelist(String urls) {
		try {
			if(urls.contains("@")) {
				String [] imglink=	urls.split("@");
				if(UrlTraverse. getFailedUrl(imglink[1])!=null) {
					urls=imglink[0]+" :-- "+imglink[1];
					failurls.add(urls);
				}
			}else {
				failurls.add(UrlTraverse. getFailedUrl(urls));	
			}
		}catch(Exception e) {
			System.out.println("Exception occured while updating the failure list"+e);
		}
	}
	public static void updateProperties(long totalUrls,long failedUrls,String totalExceutionTime){
		try {
			String filePath= "/Users/mandeep/Documents/MyProjects"+"/testResult.properties";
			Properties prop = new Properties();
			prop.setProperty("Total_Urls", String.valueOf(totalUrls));
			prop.setProperty("Total_Failed_Urls", String.valueOf(failedUrls));
			prop.setProperty("Total_Execution_Time", totalExceutionTime);
			OutputStream os = new FileOutputStream(new File(filePath));
			prop.store(os, "");
			os.close();
		}catch(Exception e) {
			System.out.println("Exception Occured while updating the Testresults"+e);
		}

	}

	public static void main(String args[]) {
		//		String action = args[0].trim();
		//		if (action.contains("dummyurl")) {
		//		assertOnTotalUrls("https://www.lenskart.com");
		//		}else {
		//			System.out.println("Invalid job name should have dummyurl at any position");
		//		}
		Instant starttime=Instant.now();
		assertOnTotalUrls("https://www.lenskart.com");
		Instant endTime=Instant.now();
		Duration totalTime=Duration.between(starttime, endTime);
		String executionTime=Long.toString(totalTime.toMinutes());
		System.out.println("Total Exection Time : "+executionTime +"  mins");
		System.out.println("Total Url Traversed is:-"+totalextractedurls);
		System.out.println("Total Failed Url is :- "+totalfailedurls);
		updateProperties(totalextractedurls,totalfailedurls,executionTime);


	}
}
