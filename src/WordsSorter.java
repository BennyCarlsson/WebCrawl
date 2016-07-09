import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WordsSorter {
	public HashMap<String, Word> countAllWords(String text){
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
	public String stripWords(String word){
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
	public boolean isAllowed(String word){
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
	public ArrayList<Word> hashMapToSortedArrayList(HashMap<String, Word> wordMap){
		ArrayList<Word> words = new ArrayList<Word>();
		for(Word word : wordMap.values()){
			words.add(word);
		}
		Collections.sort(words);
		return words;
	}
}
