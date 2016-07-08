import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.normalize.UpperCaseNormalizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class ReadWebsite {
	private String[] url;
	private String textFile;
	private String textFileWords;
	private static final int wordUsedLimitToShow = 5;
	
	 public static interface User32 extends Library {
	     User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class,W32APIOptions.DEFAULT_OPTIONS);        
	     boolean SystemParametersInfo (int one, int two, String s ,int three);         
	 }
	public static void main(String[] args) throws IOException {
		String wallpaper_file = "mini.jpg";
		ReadWebsite readWebsite = new ReadWebsite();
		readWebsite.textFile = "text.txt";
		readWebsite.textFileWords = "textWords.txt";
		String text;
		if(readWebsite.textIsOld()){
			text = readWebsite.scrapeWebsites();
			readWebsite.writeTextToFile(text);
		}else{
			text = readWebsite.getDataFromTextFile();
		}
		HashMap<String, Word> wordMap = readWebsite.countAllWords(text);
		ArrayList<Word> words = readWebsite.hashMapToSortedArrayList(wordMap);
		//readWebsite.printWords(words);
		readWebsite.printWordsToFile(words);
		readWebsite.createImage();
		
		User32.INSTANCE.SystemParametersInfo(0x0014, 0, "C:\\Users\\carls\\workspace\\WebCrawl\\img.png" , 1);
	}
	private void createImage() throws IOException{
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setNormalizer(new UpperCaseNormalizer());
		frequencyAnalyzer.setMinWordLength(wordUsedLimitToShow);
		frequencyAnalyzer.setWordFrequenciesToReturn(800);
		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream(textFileWords));
		final Dimension dimension = new Dimension(1440, 900);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(3);
		wordCloud.setKumoFont(new KumoFont("LICENSE PLATE", FontWeight.BOLD));
		wordCloud.setBackground(new RectangleBackground(dimension));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setFontScalar(new SqrtFontScalar(10, 80));
		wordCloud.build(wordFrequencies);
		wordCloud.writeToFile("img.png");
	}
	private InputStream getInputStream(String string) throws IOException {
		InputStream inputstream = new FileInputStream(string);
		return inputstream;
	}
	private String scrapeWebsites(){
		url = new String[]{"http://www.di.se/","http://www.svd.se/","http://nyheter24.se/"
				,"http://www.expressen.se/","http://www.dn.se/"
				,"http://www.aftonbladet.se/","http://www.svt.se/"};
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
	private String getDataFromTextFile(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(this.textFile));
			br.readLine();
			String text = br.readLine();
			br.close();
			return text;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	private boolean textIsOld(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.textFile));
			String dateString = br.readLine();
			br.close();
			long sixHours = 21600000;
			long oldTime = Long.parseLong(dateString);
			long thisTime = new Date().getTime();
			if(thisTime - oldTime < sixHours){
				System.out.println("Using old data from textfile.");
				return true;
			}
		} catch (IOException e) {
			System.out.println("Error! could not read textfile.txt");
			e.printStackTrace();
		}
		System.out.println("Scraping websites..");
		return true;
	}
	private void writeTextToFile(String text){
		try {
			PrintWriter out = new PrintWriter(textFile);
			Date date = new Date();
			out.println(date.getTime());
			out.print(text);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error! could not writeTextToFile");
			e.printStackTrace();
		}
	}

	private HashMap<String, Word> countAllWords(String text){
		HashMap<String, Word> wordMap = new HashMap<String, Word>();
		for (String word : text.split("\\s+")){
			//Checks and Trim words!
			word = word.toLowerCase();
			word = stripWords(word);
			if(isAllowed(word)){
				if(wordMap.get(word) == null){
					wordMap.put(word,new Word(word));
				}else{
					wordMap.get(word).increaseCount();
				}
			}
		}
		return wordMap;
	}
	private String stripWords(String word){
		word.trim();
		if (word != null && word.length() > 1) {
			char firstChar = word.charAt(0);
			char lastChar = word.charAt(word.length()-1);
			if("”’:',.-_<>|+?@£$!#¤%&/()=?´`´'*^¨\"".indexOf(firstChar) != -1){
				word = word.substring(1, word.length()-1);	
				word = stripWords(word);
			}
			if (word != null && word.length() > 1) {
				if("”’:',.-_<>|+?@£$!#¤%&/()=?´`´'*^¨\"".indexOf(lastChar) != -1){
					word = word.substring(0, word.length()-1);
					word = stripWords(word);
				}
			}else{
				return "";
			}
	    }else{
	    	return "";
	    }
	    return word;
	}
	private boolean isAllowed(String word){
		//Todo special words, stockholm -5
		ArrayList<String> blackList = new ArrayList<String>();
		try {
			String fileName = "blacklistedwords.txt";
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				blackList.add(line);
			}
			blackList.add("");
			blackList.add("foto:");
			br.close();
		} catch (IOException e) {
			System.out.println("Error! could not read blacklistedwords.txt");
			e.printStackTrace();
		}
		if(blackList.contains(word.toLowerCase()) || !word.matches(".*[a-zA-Z]+.*")
				|| word.toLowerCase().contains("foto:") || word.matches(".*\\d+.*")){
			return false;
		}
		return true;
	}
	private ArrayList<Word> hashMapToSortedArrayList(HashMap<String, Word> wordMap){
		ArrayList<Word> words = new ArrayList<Word>();
		for(Word word : wordMap.values()){
			words.add(word);
		}
		Collections.sort(words);
		return words;
	}
	private void printWordsToFile(ArrayList<Word> words) throws IOException {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFileWords), "UTF-8"));
			//PrintWriter out = new PrintWriter(textFileWords);
			for(Word word : words){
				if(word.getCount() > this.wordUsedLimitToShow){
					for(int i = 0; i < word.getCount();i++){
						out.write(word.getWord()+"\n");
					}
				}
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error! could not writeTextToFile");
			e.printStackTrace();
		}
	}
	private void printWords(ArrayList<Word> words){
		for(Word word : words){
			if(word.getCount() > this.wordUsedLimitToShow){
				System.out.println(word.getCount()+":"+word.getWord());
			}
		}
	}
}