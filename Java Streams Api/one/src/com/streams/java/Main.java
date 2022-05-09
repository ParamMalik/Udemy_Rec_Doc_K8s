package com.streams.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static class Transactions {

        private Integer amount;
        private String type;
        private Integer id;

        public Transactions(Integer amount, Type credit, Integer id) {
            this.amount = amount;
            this.type = String.valueOf(credit);
            this.id = id;
        }

        public Integer getAmount() {
            return amount;
        }


        public Integer getId() {
            return id;
        }


    }


    public static void main(String[] args) {

        Transactions transactions = new Transactions(12000, Type.CREDIT, 1);
        Transactions transactions1 = new Transactions(13000, Type.DEBIT, 2);
        Transactions transactions2 = new Transactions(14000, Type.DEBIT, 3);
        Transactions transactions3 = new Transactions(15000, Type.CREDIT, 4);

        ArrayList<Transactions> listOfTrans = new ArrayList<>();
        listOfTrans.add(transactions);
        listOfTrans.add(transactions1);
        listOfTrans.add(transactions2);
        listOfTrans.add(transactions3);

        Stream<Transactions> streamOfTrans = listOfTrans.stream();     // step 1 - creating stream of lists
        streamOfTrans.forEach(trans -> System.err.println(trans.getId()));     // step 2 - traversing and printing values of lists stream using forEach

        // Creating Stream from array

        Transactions[] arrOfTrans = {transactions, transactions1, transactions2, transactions3};
        Stream<Transactions> stream = Arrays.stream(arrOfTrans);
        stream.forEach(transaction -> System.out.println(transaction.getAmount()));


        Stream<Transactions> streamOfArrayUsingIndexSlicing = Arrays.stream(arrOfTrans, 0, 2);
        streamOfArrayUsingIndexSlicing.forEach(t -> System.err.println(t.getId()));

        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 10, 32);
//        System.out.println(integerStream);
        System.out.println();

        integerStream.forEach(System.out::println);


        // Generating infinite (unbounded) stream but limiting it using limit method
        System.out.println();
        Stream<Double> generateStramOfRandomValues = Stream.generate(Math::random).limit(4);
        generateStramOfRandomValues.forEach(System.out::println);

        // infinite stream using iterate
        System.out.println();
        Stream<Integer> limitInifiniteStream = Stream.iterate(30, integer -> integer + 5).limit(5);
        limitInifiniteStream.forEach(System.out::println);

        // breaks a String into sub-strings according to specified RegEx:
        System.out.println();
        Stream<String> stringStream = Pattern.compile("#").splitAsStream("MON#TUS#THRU#FRI");
        stringStream.forEach(System.out::println);


        // We Can perform
        // Intermediate Operations
        // Terminal Operations
        // Stream Pipeline = Intermediate + Terminal

        // Intermediate operations are the operations between source or stream creation and terminal operation
        // Returns a new stream
        // Intermediate operations are lazy
        // Terminal Operations need to be defined in order to execute  means
        // if we won't define terminal operation intermediate operation will not execute

        // Intermediate  Operations are of two types -
        // Stateful  -> May retain state from past elements : sorted, distinct
        // Stateless  -> Don't retain state : Filter and map

        // Intermediate Operations : Short circuit operaitons
        // used for short-circuiting infinite streams
        // infinite input , finite output
        // Limit


        // Filter
        // it is like where clause in sql
        // filter condition is passed in the form of lambda expression

        Stream<Transactions> transactionsStream = listOfTrans.stream().filter(trans -> trans.amount == 12000);
        transactionsStream.forEach(trans -> System.out.println(trans.id));

        // filter other way

        System.out.println();

        double count = listOfTrans.stream()
                .filter(t -> {
                    System.out.println(t.amount == 14000);
                    return t.amount.equals(14000);
                }).count();  // terminal operation

        System.out.println(count);

        // map method is used to transform the object acc to our requirement
        // for example from a stream of object we need one attribute of each object there we use map to trasform that

        System.out.println();

        Stream<Integer> transformObjectUsingMap = listOfTrans.stream()
                .filter(t -> t.type.equals("CREDIT"))
                .map(t -> t.id);// terminal operation

        transformObjectUsingMap.forEach(System.out::println);


        System.out.println();

        listOfTrans.stream()
                .sorted(Comparator.comparing(Transactions::getAmount).reversed())
                .forEach(t -> System.out.println(t.amount));



        // Terminal Operations
        // Traverse the stream to produce a result or a side effect
        // After terminal operation is performed, the stream pipeline is considered consumed
        // Eager operations
        // ex - forEach , toArray, min, max , count, anyMatch(), allMatch, findFirst, findAny()

        // short circuit operations
        // Used for short-circuiting infinite streams
        // infinite input, terminate in finite time
        // anyMatch, allMatch, noneMatch, findFirst and findAny

        // collect terminal operation


        System.out.println(listOfTrans.stream()
                .collect(Collectors.groupingBy(Transactions::getId))
                );



    }



}
