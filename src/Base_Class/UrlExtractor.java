package Base_Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlExtractor {
	public static List<String> urlExtractors(String mainUrl) {
		List<String> urlList = null;
		try {
			urlList = new ArrayList<String>();
			Document doc = Jsoup.connect(mainUrl).get();
			String title = doc.title();
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				if (link.attr("href").startsWith("/")) {
					String url = "https://www.lenskart.com" + link.attr("href");
					urlList.add(url);
				} else if (link.attr("href").startsWith("https")) {
					String url = link.attr("href");
					urlList.add(url);
				}
			}

			//			System.out.println(title + " current page title  " + urlList.size());
		} catch (Exception e) {
		}
		return urlList;
	}

	public static Set <String> totalurl(String url) {
		Set<String> globalurl=new HashSet<String>();
		try {

			List<String> firslevel = urlExtractors(url);
			globalurl.addAll(firslevel);
			for (String list : firslevel) {
				List<String> secondlist = urlExtractors(list);
				globalurl.addAll(secondlist);
			}
		} catch (Exception e) {
			System.out.println("Exception occured while getting url at  level " + e);
		}

		return globalurl;
	}

	public static void main(String args[]) {
		Set<String> globalurl=totalurl("https://www.lenskart.com");
		System.out.println("Total url is "+globalurl.size());
		GoogleSheet.writeTotalUrl(globalurl);
	}
}
