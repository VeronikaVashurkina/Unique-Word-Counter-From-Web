import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {


        System.out.println("Enter web-site:");
        Scanner in = new Scanner(System.in);
        String url = in.nextLine();
        System.out.println("Enter path:");
        String outputDirPath = in.nextLine();

        Downloader.getURL(url, outputDirPath);

        WordStatistics wordStatistics = new WordStatistics();
        wordStatistics.getStatistics(url);


    }
}
