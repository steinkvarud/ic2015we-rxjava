package no.kvarud.rxjava;

import com.google.gson.Gson;
import rx.Observable;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Simple WebSocket implementation sending @see no.kvarud.rxjava.Location objects
 * to the client
 *
 * {name: "Stockholm", lat: 56.133333, lng: 13.416667, color: "red"}
 *
 */
@ClientEndpoint
@ServerEndpoint(value="/events/")
public class TwitterRxSocket
{
    private final Gson gson = new Gson();

    private final List<Location> cities = Arrays.asList(
            new Location("Paris", 48.866667,2.333333),
            new Location("Oslo", 59.916667, 10.75),
            new Location("London", 51.514125,-.093689),
            new Location("New York", 53.083333,-.15),
            new Location("Sydney", 27.9630556,-82.2075000),
            new Location("Rome", 41.9,12.483333),
            new Location("Stockholm", 56.133333,13.416667)
    );

    //Configures the Observables before the WebSocket connects just to illustrate that the
    //Observable is "lazy and will not do anything until subscribed to

    final Observable<Location> locationStream =
            Observable
                    .from(cities)
                    .zipWith(Observable.interval(2, TimeUnit.SECONDS), (location, i) -> location)
                    .repeat();

    final Observable<String> twitterStream =
            RxTweet.observableStatusStream()
                    .cache()
                    .filter(status -> status.getGeoLocation() != null)
                    .sample(200, TimeUnit.MILLISECONDS)
                    .map(Location::new)
                    .mergeWith(locationStream)
                    .map(gson::toJson);

    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        twitterStream.subscribe(
                sess.getAsyncRemote()::sendText
        );
    }
}
