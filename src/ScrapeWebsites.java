import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScrapeWebsites {
	public static final String[] url = new String[]{"http://www.di.se/","http://www.svd.se/","http://nyheter24.se/"
		,"http://www.expressen.se/","http://www.dn.se/"
		,"http://www.aftonbladet.se/","http://www.svt.se/"};;
	public String scrapeWebsites(){
		String text = "";
		for(int i = 0; i < url.length;i++){
			try {
				Document doc = Jsoup.connect(url[i]).get();
				text += doc.body().text();
			} catch (IOException e) {
				System.out.println("Error! Failed to connect to: "+url[i]);
				e.printStackTrace();
			}
		}
		return text;
	}
}
