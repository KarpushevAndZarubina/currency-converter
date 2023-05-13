package ru.krista;


import java.util.Map;

public interface CurrencyExchange {
    Map<Currency, Double> getCurrencyRates(Currency base, Currency... symbols);
}

enum Currency{
    USD,
    EUR,
    RUB
}
