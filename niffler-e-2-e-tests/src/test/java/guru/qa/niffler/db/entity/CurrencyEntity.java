package guru.qa.niffler.db.entity;

import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "currency")
public class CurrencyEntity implements Comparable<CurrencyEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CurrencyValues currency;

    @Column(name = "currency_rate", nullable = false)
    private Double currencyRate;

    private CurrencyEntity(UUID id, CurrencyValues currency, Double currencyRate) {
        this.id = id;
        this.currency = currency;
        this.currencyRate = currencyRate;
    }

    public CurrencyEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyEntity that = (CurrencyEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(currency, that.currency) && Objects.equals(currencyRate, that.currencyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, currencyRate);
    }

    @Override
    public String toString() {
        return "CurrencyEntity{" +
                "id=" + id +
                ", currency=" + currency +
                ", currencyRate=" + currencyRate +
                '}';
    }

    public static List<CurrencyEntity> currencyGrpcsToCurrencyEntities(List<Currency> currencyList) {
        List<CurrencyEntity> currencyEntityList = new ArrayList<>();
        for (Currency currency : currencyList) {
            CurrencyEntity currencyEntity = new CurrencyEntity();
            currencyEntity.setCurrency(CurrencyValues.valueOf(currency.getCurrency().name()));
            currencyEntity.setCurrencyRate(currency.getCurrencyRate());
            currencyEntityList.add(currencyEntity);
        }
        return currencyEntityList;
    }

    public CurrencyEntity(CurrencyEntity other) {
        this(other.id, other.currency, other.currencyRate);
    }

    @Override
    public int compareTo(CurrencyEntity o) {
        if (this.currency.compareTo(o.currency) > 0) {
            return 1;
        } else return -1;
    }
}
