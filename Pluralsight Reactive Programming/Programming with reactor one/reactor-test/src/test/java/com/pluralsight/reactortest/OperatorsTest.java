package com.pluralsight.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class OperatorsTest {
    // Map , flatMap , zip , concat and merge

    @Test
    void mapTest() {
        Flux.range(1, 6)            // Map transforms the elements emitted by this flux by applying that synchronous function to each element
                .map(transform -> transform * 10)    // map transform each element of stream by taking a synchronous method in it in form of lambda
                .subscribe(System.out::println);

        // This means that inside the function passed as the argument of map
        // you can use imperative code and return an object of a different type from the one published for example
        // above flux is emitting 1 2 3 4 5 6 but map is returning 10 20 30 .....
    }

    @Test
    void testFlatMap() {
        // flatMap is a similar operator as map.
        // *** Like map, it also transforms the published elements , but in an asynchronous way ***
        // this means we have to return a flux or mono . In the end flatMap will flatten
        // all the publishers into a single one.

        Flux.range(1, 6)
                .flatMap(i -> Flux.range(i * 5, 2))
                .subscribe(System.out::println);

    }

    @Test
    void flatMapMany(){
        Mono.just(3)
                .flatMapMany(integer -> Flux.range(1,integer))
                .subscribe(System.out::println);
    }

    // Ways to combine the Publishers

    @Test
    void concat() throws InterruptedException {
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200));
        Flux<Integer> sixToTen = Flux.range(6, 5)
                .delayElements(Duration.ofMillis(400));



        // This process is happening in parallel that is why there is thread sleep
        // time delay to execute the thread on delay

//        Flux.concat(oneToFive,sixToTen)
//                .subscribe(System.out::println);

        // Another way
        oneToFive.concatWith(sixToTen)
                        .subscribe(System.out::println);


        Thread.sleep(4000);

    }

    @Test
    void merge() throws InterruptedException {

        // Merge does not sequentially combine the publisher . it interleaves the values
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200));
        Flux<Integer> sixToTen = Flux.range(6, 5)
                .delayElements(Duration.ofMillis(400));



        // This process is happening in parallel that is why there is thread sleep
        // time delay to execute the thread on delay

        Flux.merge(oneToFive,sixToTen)
                .subscribe(System.out::println);

//        oneToFive.mergeWith(sixToTen)
//                .subscribe(System.out::println);


        Thread.sleep(4000);

    }

    @Test
    void testZip(){
        Flux<Integer> oneToFive = Flux.range(1, 5);
        Flux<Integer> sixToTen = Flux.range(6, 5);

        // zip operator combines two or more publishers by waiting for all
        // the sources to emit on element and combine these elements into an
        // output value according to the provided function
        // zip can be use on mono or flux publisher

        Flux.zip(oneToFive, sixToTen,
                        (firstPublisherElement , secondPublisherElement)-> firstPublisherElement + ", " + secondPublisherElement)
                .subscribe(System.out::println);

        System.out.println();
        // Other way to zip is zipWith method


        // This method of zip does not take any function to format the output
        oneToFive.zipWith(sixToTen)
                .subscribe(System.out::println);


    }


}
