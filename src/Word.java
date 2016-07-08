
public class Word implements Comparable<Word>{
	private String word;
	private int count;
	
	public Word(String word){
		count = 1;
		this.word = word;
	}
	public int getCount(){
		return this.count;
	}
	public void increaseCount(){
		this.count++;
	}
	public String getWord(){
		return this.word;
	}
	public int compareTo(Word compareWord){
		int compareCount = ((Word) compareWord).getCount();
		return compareCount - this.getCount();
	}
}
