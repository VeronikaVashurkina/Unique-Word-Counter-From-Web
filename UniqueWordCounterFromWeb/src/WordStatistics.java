import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordStatistics {
    public WordStatistics() {
    }

    public void getStatistics(String url) throws IOException {


        StringBuilder sb = new StringBuilder();
        for (Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext(); )
            sb.append(sc.nextLine()).append('\n');
        String title = String.valueOf(sb);
        Document doc = Jsoup.parse(title);
        String text = doc.body().text();


        Map<String, Integer> words = new HashMap<String, Integer>();

        text = text.replaceAll("«", "");
        text = text.replaceAll("»", "");
        text = text.replaceAll("©", "");
        String[] arr = text.split("[^a-яА-Яa-zA-Z]+");

        for (int i = 0; i < arr.length; i++) {

            if (words.containsKey(arr[i])) {
                int count = words.get(arr[i]);
                words.put(arr[i], count + 1);
            } else {
                words.put(arr[i], 1);
            }

        }

        for (Map.Entry<String, Integer> item : words.entrySet()) {

            System.out.printf(" %s  -  %s \n", item.getKey(), item.getValue());
        }


    }
}


