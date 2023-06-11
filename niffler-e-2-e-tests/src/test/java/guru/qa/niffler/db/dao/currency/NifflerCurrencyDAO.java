package guru.qa.niffler.db.dao.currency;

import guru.qa.niffler.db.entity.CurrencyEntity;

import java.util.List;

public interface NifflerCurrencyDAO {
    List<CurrencyEntity> getAllCurrencies();
}
