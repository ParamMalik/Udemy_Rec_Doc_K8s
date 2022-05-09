package com.coffeecafe.webclientproductapi;

import com.coffeecafe.webclientproductapi.service.WebClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebclientProductApiApplication {

    private static WebClientService webClientService;

    public static void main(String[] args) {
        SpringApplication.run(WebclientProductApiApplication.class, args);

        // We can put this code in main method to run our webClient api calss internally

//        webClientService.postProduct()
//                .thenMany(webClientService.getAllProduct())
//                .take(1)
//                .flatMap(product -> webClientService.updateProduct(product.getId(), "Coffee",3.05))
//                .flatMap(product -> webClientService.deleteProduct(product.getId()))
//                .thenMany(webClientService.getAllProduct())
//                .thenMany(webClientService.getEvents())
//                .subscribeOn(Schedulers.newSingle("myThread"))         here we are creating new thread to execute our
//                                                                          flow now our app will not terminate until the
//                                                                          complete thread pool(means our main thread and new thread) gets executed
//                .subscribe(System.out::println);
//


    }

}
