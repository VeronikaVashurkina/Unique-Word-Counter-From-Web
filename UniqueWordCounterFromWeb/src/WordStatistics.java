import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordStatistics {

    private static Logger log = Logger.getLogger(WordStatistics.class.getName());
    FileHandler fh;

    public WordStatistics(FileHandler fh) {
        this.fh = fh;
        log.addHandler(fh);
        log.log(Level.INFO, "Instance of the class is created");
    }


    public void getStatistics(String url) throws IOException {


        StringBuilder sb = new StringBuilder();
        for (Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext(); )
            sb.append(sc.nextLine()).append('\n');
        String title = String.valueOf(sb);
        log.log(Level.INFO, "Page read");
        Document doc = Jsoup.parse(title);
        String text = doc.body().text();
        log.log(Level.INFO, "Text from the page received");


        Map<String, Integer> words = new HashMap<String, Integer>();

        text = text.replaceAll("«", "");
        text = text.replaceAll("»", "");
        text = text.replaceAll("©", "");
        String[] arr = text.split("[^a-яА-Яa-zA-Z]+");

        log.log(Level.INFO, "Text divided into words");

        for (int i = 0; i < arr.length; i++) {

            if (words.containsKey(arr[i])) {
                int count = words.get(arr[i]);
                words.put(arr[i], count + 1);
            } else {
                words.put(arr[i], 1);
            }

        }
        log.log(Level.INFO, "Word statistics calculated");



        for (Map.Entry<String, Integer> item : words.entrySet()) {

            System.out.printf(" %s  -  %s \n", item.getKey(), item.getValue());
        }

        log.log(Level.INFO, "Word statistics displayed");
    }
}


