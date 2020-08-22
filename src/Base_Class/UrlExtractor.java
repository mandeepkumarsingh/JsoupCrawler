package Base_Class;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlExtractor {
	public static long  totalextractedurls=0;
	static Set<String> failurls=new HashSet<String>();
	static Set<String> fetched=new HashSet<String>();
	static HashSet<String> globalurl=new HashSet<String>();
	static HashSet<String> globalurlImg=new HashSet<String>();
	public static void urlExtractors(String mainUrl) {

		try {
			if(fetched.contains(mainUrl) || mainUrl.contains("seo_sitemap")){
				System.out.println("Already extracted " +mainUrl);
			}
			else {
				Document doc = Jsoup.connect(mainUrl).get();
				fetched.add(mainUrl);
				Elements links = doc.select("a[href]");
				Elements linkImage = doc.select("img[src]");
				//			Elements linksscript=doc.select("script[src]");
				String url ;
				String linkHref;
				for (Element link : links) {
					url=null;
					linkHref=link.attr("href");
					if (linkHref.startsWith("/")) {
						url = "https://www.lenskart.com" + linkHref;
					} else if (linkHref.startsWith("http")&& linkHref.contains("lenskart")) {
						url = linkHref;

					}
					if(!failurls.contains(url) && url!=null) {
						if(assertOnTotalUrls(url.trim())) {
							failurls.add(url);
						}else {

							globalurl.add(url);
						}
					}
				}
				String src;
				for(Element link : linkImage) {
					src=link.attr("src");
					if(src.startsWith("//")) {
						src="https:"+src;
					}
					if(assertOnTotalUrls(src.trim())) {
						failurls.add(mainUrl+":-  "+src);
					}else {
						globalurlImg.add(mainUrl);
					}
				}
			}

			//			System.out.println(title + " current page title  " + urlList.size());
		}
		catch (Exception e) {

			System.out.println("Exception occured while extracting urls "+e);
		}
	}

	public static void crawlUrls(String url) {
		try {

			urlExtractors(url);

			final HashSet<String> localset=(HashSet)globalurl.clone();


			//			localset=(HashSet)globalurl.clone();
			totalextractedurls += globalurl.size()+globalurlImg.size();
			System.out.println("Total urls at first level is :- " +totalextractedurls);
			globalurl.clear();
			ForkJoinPool forkpool = new ForkJoinPool();
			ForkJoinPool forkpool1=new ForkJoinPool(Runtime.getRuntime().availableProcessors(),forkpool.getFactory(), forkpool.getUncaughtExceptionHandler(), false);
			forkpool1.submit(()->localset.parallelStream().forEach(link->urlExtractors(link))).get();
			forkpool1.shutdown();
			forkpool1.awaitTermination(2, TimeUnit.MINUTES);
			forkpool.shutdown();
			forkpool1.awaitTermination(1, TimeUnit.MINUTES);
			totalextractedurls+=globalurl.size()+failurls.size()+globalurlImg.size()+globalurlImg.size();
			System.out.println("Total urls at second level :-  "+totalextractedurls);
			final HashSet<String> localset1=(HashSet)globalurl.clone();
			globalurl.clear();
			ForkJoinPool forkpoolthird = new ForkJoinPool();
			ForkJoinPool forkpoolthird1=new ForkJoinPool(Runtime.getRuntime().availableProcessors(),forkpoolthird.getFactory(), forkpoolthird.getUncaughtExceptionHandler(), false);
			forkpoolthird1.submit(()->localset1.parallelStream().forEach(link->urlExtractors(link))).get();
			forkpoolthird1.shutdown();
			forkpoolthird1.awaitTermination(5, TimeUnit.MINUTES);
			forkpoolthird.shutdown();
			forkpoolthird.awaitTermination(2, TimeUnit.MINUTES);
			totalextractedurls+=globalurl.size()+failurls.size();
			System.out.println("Total urls at third level "+totalextractedurls);



		} catch (ExecutionException e) {
			System.out.println("got execution exception"+e);
		}
		catch (InterruptedException e) {
			System.out.println("got Interrupted exception"+e);
		}



		catch (Exception e) {
			System.out.println("Exception occured while getting url at second  level " + e);
		}


	}



	public static boolean assertOnTotalUrls(String baseurl) {

		boolean result=false;
		try {
			//			Set<String> globalurl=thirdlvel(baseurl);
			//			for (String urls : globalurl) {

			String wah[]=baseurl.split("https://www.lenskart.com/");	
			if(wah.length>1 && !wah[1].contains("/")&& UrlTraverse.getFailedUrl(baseurl)!=null) {
				failurls.add("Product_Url:-  "+baseurl);
				result=true;
			}else if(UrlTraverse.getFailedUrl(baseurl)!=null) {
				failurls.add(baseurl);
				result=true;
			}

			//			}
			//			totalfailedurls=failurls.size();
			//			GoogleSheet.writeTotalUrl(failurls);

		}catch(Exception e) {
			System.out.println(" Exception Occured while returning assert true or false val"+e);
		}
		return result;
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
	public static void asserandwrite(String url) {
		crawlUrls(url);
		GoogleSheet.writeTotalUrl(failurls);

	}

	public static void main(String args[]) {
		//		String action = args[0].trim();
		//		if (action.contains("dummyurl")) {
		//			assertOnTotalUrls("https://www.lenskart.com");
		//		}else {
		//			System.out.println("Invalid job name should have dummyurl at any position");
		//		}
		Instant starttime=Instant.now();
		asserandwrite("https://www.lenskart.com");
		int failedsize=failurls.size();
		Instant endTime=Instant.now();
		Duration totalTime=Duration.between(starttime, endTime);
		String executionTime=Long.toString(totalTime.toMinutes());
		System.out.println("Total Exection Time : "+executionTime +"  mins");
		System.out.println("Total Url Traversed is:-"+totalextractedurls);
		System.out.println("Total Failed Url is :- "+failedsize);
		updateProperties(totalextractedurls +failedsize,failedsize,executionTime);

	}
}
