package com.pluralsight.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    // Reactive Streams have 4 interfaces :
    // Publisher
    // Subscriber
    // Subscription
    // Processor

    // When the Subscriber subscribes to the publisher , a subscription is created
    // and it is passed to the onSubscribe() method. The subscriber requests a number of elements
    // elements and the publisher starts sending them. When the Publisher has no more
    // elements to send , onComplete is called
    // If there's an error , the subscription is canceled and onError is called


    // In Project Reactor we learn how to create and work with Flux and Mono
    // the implementations of the Publisher interface that reactor provides
    // Nothing happens until we subscribe to a publisher and many of the options the subscribe method provides and
    // and how to use operators like map and flatMap.

    // @Controller - model and view rendering
    // @RestController - @Controller and @ResponseBody
    
    @Test
    void firstMono(){
        Mono.just("A");   // With mono we can publish at most 1 element

    }

    @Test
    void monoWithConsumerWithoutSubscribing(){
        Mono.just("A")
                .log();   // Nothing happens until we subscribe to the publisher
    }

    @Test
    void monoWithConsumer(){
        Mono.just("A")
                .log().subscribe();   // Nothing happens until we subscribe to the publisher
    }

    @Test
    void doSomethingWithMonoElement(){
        Mono.just("A")
                .log().subscribe(s -> System.out.println(s));   // to do something with the publisher element we can define lambda expression in subscribe
                                                                 // we can pass more than one consumer to the subscribe
                                                                //    method to do something when error happens or the stream of element completes
                                                                // But we can also use doOn methods

    }

    @Test
    void monoWithDoOnMethods(){
        Mono.just("B")
                .log()    // It will log for every element
                .doOnSubscribe(subs -> System.out.println("Subscribed : " + subs))
                .doOnRequest(req -> System.out.println("Request : " + req))
                .doOnSuccess(complete -> System.out.println("Completed :" + complete))
                .subscribe(System.out::println);
    }

    @Test
    void emptyMono(){
        Mono.empty()    // empty mono do not publish any value
                .log()   // empty mono does not return a value but it emits a completion signal to let us know the processing is done
                .subscribe(System.out::println);     // Here onNext is never called

    }

    // Now Let's pass consumer to the subscribe method

    @Test
    void emptyCompleteConsumerMono(){
        Mono.empty()
                .log()                // below line will do something with the element received after subscribing to the publisher here it is printing
                                        // and the next line is for errors
                .subscribe(System.out::println ,
                        null,
                        ()-> System.out.println("DONE!"));   // This line will print the message when the sequence is complete
    }


    @Test
    void errorRuntimeExceptionMono(){
        Mono.error(new RuntimeException())     // here we are simulating an error
                .log()
                .subscribe(null,
                        throwable -> System.out.println("Error Occur : " + throwable),
                        () -> System.out.println("COMPLETED"));
    }

    @Test
    void errorRuntimeExceptionMonoWitDoOnError(){      // Almost same as the above method
        Mono.error(new RuntimeException())     // here we are simulating an error
                .doOnError(s->
                        System.out.println("Error Occured : " + s))
                .log()
                .subscribe(null,
                        null,
                        () -> System.out.println("COMPLETED"));
    }

    @Test
    void errorDoOnErrorMono(){
        // If we want to simulate catching the exception , swalloing the exception and
      // returning a new Mono we can use method onErrorResume()
        Mono.error(new Exception())
                // Here we are handling the error
                .onErrorResume(e -> {             // we return a mono here
                    System.out.println("caught the error " + e);   // No stackTrace will be printed here only our custom msg will get printed here (Handling)
                    return Mono.just("Error! And Here is the solution.");
                })
                .log()
                .subscribe(System.out::println);

    }

    @Test
    void errorOnErrorReturnMono(){
        Mono.error(new Exception())   // we use onErrorReturn when we don't want to return a mono but an object
                .onErrorReturn("Returning Object Not Mono")
                .log()
                .subscribe(System.out::println);
    }






}
