package parallel;

import sequental.SequentialPinger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Perhaps this you go into a separate file
class PingURL implements Callable<String> {
    String url;

    PingURL(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        return sequental.SequentialPinger.getStatus(url);
    }
}

public class ParallelPinger {

    /*
    Create and executor service
    Get the list of URLs to contact from the static method in SequentalPinger
    Make a List of <Future<String>>
    Create your Callables, and start them via a method on the executor and add the returned future to the list
    Call a "relevant" method on all your futures to get the response, and to the List you eventually will return
    */
    public static List<String> getStatusFromAllServers() throws Exception {
        String[] urls = SequentialPinger.getHostList();
        List<String> results = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();

        // We make a list of futures, since they fetch on their own thread.
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            Future<String> fut = es.submit(new PingURL(urls[i]));
            futures.add(fut);
        }

        // Loops through all fetched futures.
        // Each are blocking the loop until they finish fecthing,
        // so some futures later down the loop CAN finish before the others
        for(Future<String> future : futures) {
            String response = future.get();
            results.add(response);
        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        long timeStart = System.nanoTime();
        List<String> results = getStatusFromAllServers();
        for (String r : results) {
            System.out.println(r);
        }
        long timeEnd = System.nanoTime();
        long total = (timeEnd - timeStart) / 1_000_000;
        System.out.println("Time to check URLS: " + total + "ms.");
    }
}
