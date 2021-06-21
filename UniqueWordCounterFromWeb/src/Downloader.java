import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Downloader {

    private static Logger log = Logger.getLogger(Downloader.class.getName());
    FileHandler fh;
    public Downloader(FileHandler fh) {
        this.fh=fh;
        log.addHandler(fh);
        log.log(Level.INFO, "Instance of the class is created");
    }


    public static void getURL(String url, String outputDirPath) throws Exception {


        if (url == null || outputDirPath == null) {
            System.out.println("output directory path is null or url is null ");
           Exception e =  new Exception("Invalid input parameters");
            log.log(Level.SEVERE, "Exception: ",e);
            throw e;
        }


        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
            log.log(Level.INFO, "Added a prefix to the site address");
        }

        URL obj = new URL(url);


        File outputDir = new File(outputDirPath);
        if (outputDir.exists() && outputDir.isFile()) {
            log.log(Level.INFO, "output directory path is wrong");
            return;
        } else if (!outputDir.exists()) {
            outputDir.mkdirs();
            log.log(Level.INFO, "Directory created");
        }
        Downloader.getWebPage(obj, outputDir);
        log.log(Level.INFO, "First Page grabbed successfully");

    }

    public static void getWebPage(URL obj, File outputDir) {
        FileOutputStream fop = null;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        File outputFile = null;
        InputStream is = null;
        try {
            String filename = "default.html";
            System.out.println(filename);


            outputFile = new File(outputDir, filename);

            conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);


            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER) {

                } else {
                    log.log(Level.INFO, "Unable to get resource mostly 404 " + status);
                    return;
                }
            }

            log.log(Level.INFO, "Response Code ... " + status);

            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            if (!outputFile.canWrite()) {
                log.log(Level.INFO, "Cannot write to file - " + outputFile.getAbsolutePath());
                return;
            }

            if (filename.endsWith("html") || filename.endsWith("htm")
                    || filename.endsWith("asp") || filename.endsWith("aspx")
                    || filename.endsWith("php") || filename.endsWith("php")
                    || filename.endsWith("net")) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                StringBuffer strResponse = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    strResponse.append(inputLine + "\r\n");
                }
                String htmlContent = strResponse.toString();

                try {
                    fop = new FileOutputStream(outputFile);
                    fop.write(htmlContent.getBytes());
                    fop.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.log(Level.SEVERE, "Exception: ", e);
                }


            } else {

                try {
                    fop = new FileOutputStream(outputFile);
                    is = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 16];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        fop.write(buffer, 0, length);
                    }
                    fop.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.log(Level.SEVERE, "Exception: ", e);
                }
            }


        } catch (Exception e) {

            log.log(Level.SEVERE, "Excpetion in getting webpage - " + obj);
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.log(Level.SEVERE, "Exception: ", e);
            }
        }
    }
}


