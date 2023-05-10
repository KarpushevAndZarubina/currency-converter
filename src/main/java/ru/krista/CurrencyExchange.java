package ru.krista;


import java.util.HashMap;

public interface CurrencyExchange {
    HashMap<Currency, Double> getCurrencyRates(Currency base, Currency... symbols);
}

enum Currency{
    USD,
    EUR,
    RUB
}
