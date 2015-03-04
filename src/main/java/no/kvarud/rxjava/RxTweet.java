package no.kvarud.rxjava;

import rx.Observable;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class RxTweet {


    /**
     * Wrap a sample twitter stream in a RxJava Observable
     */
    public static Observable<Status> observableStatusStream() {
        return Observable.create(subscriber -> {
            
            final TwitterStream twitterStream =
                    new TwitterStreamFactory()
                            .getInstance();

            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

            });
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    twitterStream.cleanUp();
                    twitterStream.shutdown();
                    System.out.println("Closed twitter connection.");
                }
            });
            twitterStream.sample();
        });
    }
}
