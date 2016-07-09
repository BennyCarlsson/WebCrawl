import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

public class FileHandler {
	public static final String textFile = "text.txt";
	public static final String textFileWords = "textWords.txt";
	public FileHandler(){
		
	}
	public String getDataFromTextFile(){
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
	public boolean textIsOld(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.textFile));
			String dateString = br.readLine();
			br.close();
			long sixHours = 21600000;
			long oldTime = Long.parseLong(dateString);
			long thisTime = new Date().getTime();
			if(thisTime - oldTime < sixHours){
				System.out.println("Using old data from textfile.");
				return false;
			}
		} catch (IOException e) {
			System.out.println("Error! could not read textfile.txt");
			e.printStackTrace();
		}
		System.out.println("Scraping websites..");
		return true;
	}
	public void writeTextToFile(String text){
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
	public void printWordsToFile(ArrayList<Word> words) throws IOException {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFileWords), "UTF-8"));
			//PrintWriter out = new PrintWriter(textFileWords);
			for(Word word : words){
				if(word.getCount() > Main.wordUsedLimitToShow){
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
}
