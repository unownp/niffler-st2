package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyJson {
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("currencyRate")
    private Double currencyRate;

    public CurrencyJson(CurrencyValues currency, Double currencyRate) {
        this.currency = currency;
        this.currencyRate = currencyRate;
    }

    public CurrencyJson() {
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }
}
