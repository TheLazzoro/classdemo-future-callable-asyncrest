package sequental;

/*
 * Code taken from 
 * http://crunchify.com/how-to-get-ping-status-of-any-http-end-point-in-java/
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SequentialPinger {

  private static String[] hostList = {
    "https://crunchify.com",
    "https://yahoo.com",
    "https://www.ebay.com",
    "https://google.com",
    "https://www.example.com",
    "https://paypal.com",
    "https://bing.com/",
    "http://techcrunch.com/",
    "https://mashable.com/",
    "https://thenextweb.com/",
    "https://wordpress.com/",
    "https://cphbusiness.dk/",
    //"http://sjsu.edu/",
    "http://ebay.co.uk/", "http://google.co.uk/",
    "https://www.wikipedia.org/",
    "https://dr.dk", "https://pol.dk", "https://www.google.dk",
    "https://phoronix.com", "http://www.i-dont-exist-sorry.com/",
    "https://studypoint-plaul.rhcloud.com/", "http://stackoverflow.com",
    "http://docs.oracle.com",
    "http://imgur.com/", "http://www.imagemagick.org"
  };
  
  //Public so URL's can be reused in the parallel part
  public static String[] getHostList(){
    return hostList;
  }
  
  //Public so it can be reused in the  parallel part
  public static String getStatus(String url) throws IOException {

    String result = "Error";
    try {
      URL siteURL = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) siteURL
              .openConnection();
      connection.setRequestMethod("GET");
      connection.connect();

      int code = connection.getResponseCode();
      if (code == 200) {
        result = "GREEN";
      }
      if (code == 301) {
        result = "REDIRECT";
      }
    } catch (Exception e) {
      result = "RED";
    }
    return result;
  }

  public static void main(String args[]) throws Exception {

    long timeStart = System.nanoTime();
    for (String url : hostList) {
      String status = getStatus(url);
      System.out.println(url + "\t\tStatus:" + status);
    }
    long timeEnd = System.nanoTime();
    long total = (timeEnd - timeStart) / 1_000_000;
    System.out.println("Time to check URLS: " + total + "ms.");

  }
}
