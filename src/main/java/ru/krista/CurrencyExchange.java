package ru.krista;


import java.util.HashMap;

public interface CurrencyExchange {
    //TODO: Вместо HashMap нужно использовать интерфейс Map. Т.к. HashMap это лишь реализация этого интерфейса
    HashMap<Currency, Double> getCurrencyRates(Currency base, Currency... symbols);
}

//TODO: Вынести в отдельный файл. Не бойтесь создавать дополнительные классы и файлы.
enum Currency{
    USD,
    EUR,
    RUB
}
