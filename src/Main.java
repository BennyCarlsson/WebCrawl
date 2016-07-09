import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class Main {
	
	public static final int wordUsedLimitToShow = 5;
	FileHandler fileHandler;
	ScrapeWebsites scrapeWebsites;
	WordsSorter wordsSorter;
	ImageCreator imageCreator;
	public static interface User32 extends Library {
	     User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class,W32APIOptions.DEFAULT_OPTIONS);        
	     boolean SystemParametersInfo (int one, int two, String s ,int three);         
	}
	public static void main(String[] args) throws IOException {
		Main main = new Main();
		main.options(args);
		
		User32.INSTANCE.SystemParametersInfo(0x0014, 0, "C:\\Users\\carls\\workspace\\WebCrawl\\img.png" , 1);
	}
	private void options(String[] args){
		fileHandler = new FileHandler();
		scrapeWebsites = new ScrapeWebsites();
		wordsSorter = new WordsSorter();
		imageCreator = new ImageCreator();
		if(args.length == 0){
			doProgram();
		}
		if(args.length >= 2){
			if(args[1].equals("-refresh")){
				if(args.length == 3){
					if(args[2].equals("soft")){
						softRefresh();
					}else if(args[2].equals("hard")){
						hardRefresh();
					}
				}
			}
			if(args[1].equals("-timestamp")){
				
			}
			if(args[1].equals("-add")){
				
			}
			
			
			switch(args[0]){
				case "-refresh":break;
				case "-add":break;
				case "-timestamp":break;
			}
		}
		
	}
	private void doProgram(){
		String text = getText();
		HashMap<String, Word> wordMap = wordsSorter.countAllWords(text);
		ArrayList<Word> words = wordsSorter.hashMapToSortedArrayList(wordMap);
		try {
			fileHandler.printWordsToFile(words);
			//imageCreator.createImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWords(words);
	}
	private String getText(){
		String text = "";
		if(fileHandler.textIsOld()){
			text = scrapeWebsites.scrapeWebsites();
			fileHandler.writeTextToFile(text);
		}else{
			text = fileHandler.getDataFromTextFile();
		}
		return text;
	}
	private void hardRefresh(){
		String text = scrapeWebsites.scrapeWebsites();
		fileHandler.writeTextToFile(text);
		HashMap<String, Word> wordMap = wordsSorter.countAllWords(text);
		ArrayList<Word> words = wordsSorter.hashMapToSortedArrayList(wordMap);
		try {
			fileHandler.printWordsToFile(words);
			//imageCreator.createImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWords(words);
	}
	private void softRefresh(){
		String text = fileHandler.getDataFromTextFile();
		HashMap<String, Word> wordMap = wordsSorter.countAllWords(text);
		ArrayList<Word> words = wordsSorter.hashMapToSortedArrayList(wordMap);
		try {
			fileHandler.printWordsToFile(words);
			//imageCreator.createImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWords(words);
	}
	private void printWords(ArrayList<Word> words){
		for(Word word : words){
			if(word.getCount() > this.wordUsedLimitToShow){
				System.out.println(word.getCount()+":"+word.getWord());
			}
		}
	}
}