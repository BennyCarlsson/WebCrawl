import java.awt.Color;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

public class ImageCreator {
	public void createImage() throws IOException{
		final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
		frequencyAnalyzer.setNormalizer(new UpperCaseNormalizer());
		frequencyAnalyzer.setMinWordLength(Main.wordUsedLimitToShow);
		frequencyAnalyzer.setWordFrequenciesToReturn(800);
		final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream(FileHandler.textFileWords));
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
}
