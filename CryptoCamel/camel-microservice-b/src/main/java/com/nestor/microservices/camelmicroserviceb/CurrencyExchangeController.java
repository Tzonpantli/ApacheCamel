package com.nestor.microservices.camelmicroserviceb;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange findConversionValue(
            @PathVariable String from,
            @PathVariable String to
            ) {
        return  new CurrencyExchange(1000L,from, to, BigDecimal.TEN);
    }
}
