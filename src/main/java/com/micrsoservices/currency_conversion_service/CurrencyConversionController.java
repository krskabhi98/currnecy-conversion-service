package com.micrsoservices.currency_conversion_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private  CurrencyExchangeProxy currencyExchangeProxy;


    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionValue calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {


        HashMap<String, String> uriValue = new HashMap<>();
        uriValue.put("from", from);
        uriValue.put("to", to);


        ResponseEntity<CurrencyConversion> entityResponse = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriValue);
        CurrencyConversion currencyConversion = entityResponse.getBody();

        return new CurrencyConversionValue(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(),
               quantity .multiply( currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());

    }

    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionValue calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {

        CurrencyConversion currencyConversion =currencyExchangeProxy.retriveExchangeValue(from,to);

        return new CurrencyConversionValue(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversionMultiple(),
                quantity .multiply( currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());

    }

}
