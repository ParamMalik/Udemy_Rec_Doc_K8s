package com.pluralsight.reactortest;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class FluxTest {
    // Create a flux
    // Subscribe to it
    // And Request an element

    // NOTE: handling errors on Mono And Flux are same

    @Test
    void firstFlux() {
        Flux.just("A", "B", "C")
                .log()
                .subscribe();
    }

    @Test
    void fluxFromIterable() {

        // We  have method like this --  fromArray , fromIterable, fromStream
        Flux.fromIterable(Arrays.asList("A", "B", "C", "D"))
                .log()
                .subscribe();
    }

    @Test
    void fluxFromRange() {
        Flux.range(5, 5)
                .log()
                .subscribe();
    }

    // We can also create a Flux that emits values
    // starting from 0 and incremented then at specified time
    // intervals with the method interval

    @Test
    void fluxFromInterval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))   // Here After every one second a value will be published starting form 0
                .log()
                .subscribe();

        Thread.sleep(10000);   // we are putting our main thread in sleep so that our other threads gets executed
        // here we are putting our main thread in sleep for 5 secs to give time to other threads to publish values
        // This flux will never complete this only stops because the thread was killed when the program finished.
    }

    @Test
    void fluxFromIntervalUsingTake() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))   // Here After every one second a value will be published starting form 0
                .log()
                .take(3)                  // Here it will take only three elements and then cancel the subscription
                .subscribe();               // This take operator doesn't work as backpressure mechanism
        // It just takes N elements from the stream and just cancel the subscription once they have been published
        Thread.sleep(10000);
    }

    // To work with backpressure we need to work with request method on the subscription object

    @Test
    void fluxFromRangeWithBackPressureUsingRequestMethod() {
        Flux.range(5, 5)
                .log()

                // This method is depricated and will be removed in future
                // The alternative is in next Test method()
                .subscribe(null,
                        null,
                        null,
                        subscription -> subscription.request(2));       // Here making request of 2 elements
        // In some version of reactor we can do this way
        // The subscribe method can take 4th parameter

    }

    @Test
    void fluxFromRangeWithBackPressure() {
        Flux.range(5, 5)
                .log()   // This is the alternative is to use the version of subscribe method that takes an implementation of subscriber interface i.e BaseSubscriber
                .subscribe(new BaseSubscriber<Integer>() {         // which has some special hooks such as hookOnSubscribe which we can use to make an initial request
                               @Override
                               protected void hookOnSubscribe(Subscription subscription) {
                                   subscription.request(3l);
                               }
                           }
                );

    }


    // If we want to request a fixed no of elements we can use limitRate()
    @Test
    void fluxFromRangeWithRequest() {
        Flux.range(1, 6)
                .log()
                .limitRate(3)  // This method is making request until the flux is completed
                .subscribe();
    }

}
