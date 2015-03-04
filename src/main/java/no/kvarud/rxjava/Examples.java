package no.kvarud.rxjava;


import rx.Observable;

import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * A series of basic samples using RxJava
 */
public class Examples {

    public static void main(String[] args) {
        window();
        //As subscriptions do not block the main thread we must prevent
        //the program for terminating before we se the emits from the observer
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}
    }

    private static void basics() {
        Observable
                .just("A", "B", "C")
                .subscribe(System.out::print);
    }
    private static void basicsAsStream() {
        Stream
                .of("A", "B", "C")
                .forEach(System.out::print);
    }

    private static void basicsAsStreamAsync() {
        Stream
                .of("A", "B", "C")
                .forEach(s -> CompletableFuture.runAsync(() -> System.out.println(s)));
    }

    private static void zip() {
        Observable
            .just("A", "B", "C")
            .zipWith(Observable.just("1", "2", "3"), (s1, s2) -> s1 + s2)
            .subscribe(System.out::print);
    }

    private static void zipWithRepeat() {
        Observable
                .just("A", "B", "C", "D")
                .zipWith(Observable.just("1", "2", "3").repeat(), (s1, s2) -> s1 + s2)
                .subscribe(s -> System.out.print(s + " "));
    }

    private static void mergeWith() {
        Observable
            .just("A", "B", "C", "D")
            .mergeWith(Observable.just("1", "2", "3"))
            .subscribe(System.out::print);
    }

    private static void interval() {
        Observable
                .interval(150, TimeUnit.MILLISECONDS)
                .subscribe(System.out::print);
    }

    private static void sample() {
        Observable.interval(150, TimeUnit.MILLISECONDS)
                .sample(400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::print);
    }

    private static void map() {
        Observable
            .just("A", "B", "C")
            .map(s -> s.toLowerCase())
            .subscribe(System.out::print);
    }

    private static void filter() {
        Observable
            .just("A", "B", "C")
            .filter(s -> s.matches("[A|C]"))
            .subscribe(System.out::print);
    }

    private static void buffer() {
        Observable.interval(150, TimeUnit.MILLISECONDS)
                .buffer(400, TimeUnit.MILLISECONDS)
                .subscribe(System.out::print);
    }

    private static void window() {
        Observable.interval(150, TimeUnit.MILLISECONDS)
                .window(400, TimeUnit.MILLISECONDS)
                .subscribe(o -> o.subscribe(System.out::print));
    }

}
