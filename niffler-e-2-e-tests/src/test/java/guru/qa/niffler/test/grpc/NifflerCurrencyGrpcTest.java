package guru.qa.niffler.test.grpc;

import com.github.javafaker.Faker;
import guru.qa.grpc.niffler.grpc.*;
import guru.qa.niffler.db.dao.currency.NifflerCurrencyDAO;
import guru.qa.niffler.db.dao.currency.NifflerCurrencyDAOSpringJdbc;
import guru.qa.niffler.db.entity.CurrencyEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.*;
import static guru.qa.niffler.db.entity.CurrencyEntity.currencyGrpcsToCurrencyEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    private static final NifflerCurrencyDAO currencyDAO = new NifflerCurrencyDAOSpringJdbc();
    private static List<CurrencyEntity> currencyEntityList;

    @BeforeAll
    static void setUp() {
        currencyEntityList = currencyDAO.getAllCurrencies();
    }


    @Test
    void getAllCurrenciesTest() {
        List<CurrencyEntity> currencyEntityCopiedList = new ArrayList<>();
        for (CurrencyEntity currencyEntity : currencyEntityList) {
            currencyEntityCopiedList.add(new CurrencyEntity(currencyEntity));
        }
        for (CurrencyEntity currencyEntity : currencyEntityCopiedList) {
            currencyEntity.setId(null);
        }
        CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
        List<Currency> currenciesList = allCurrencies.getCurrenciesList();
        List<CurrencyEntity> currencyEntityListFromGrpc = currencyGrpcsToCurrencyEntities(currenciesList);
        Collections.sort(currencyEntityCopiedList);
        Collections.sort(currencyEntityListFromGrpc);

        assertEquals(currencyEntityCopiedList, currencyEntityListFromGrpc);
    }

    @ParameterizedTest
    @MethodSource("getTestDataForCalculateRate")
    void calculateRateTest(double amount, CurrencyValues spendCurrency, CurrencyValues desiredCurrency) {

        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse response = currencyStub.calculateRate(cr);

        String grpcSpendCurrency = spendCurrency.toString();
        guru.qa.niffler.model.CurrencyValues entityCurrencyValuesSpend = guru.qa.niffler.model.CurrencyValues.valueOf(grpcSpendCurrency);
        CurrencyEntity currencySpend = currencyEntityList.stream().filter(e -> e.getCurrency().equals(entityCurrencyValuesSpend)).findFirst().get();

        String grpcDesiredCurrency = desiredCurrency.toString();
        guru.qa.niffler.model.CurrencyValues entityCurrencyValuesDesired = guru.qa.niffler.model.CurrencyValues.valueOf(grpcDesiredCurrency);
        CurrencyEntity currencyDesired = currencyEntityList.stream().filter(e -> e.getCurrency().equals(entityCurrencyValuesDesired)).findFirst().get();
        double expectedAmount = BigDecimal
                .valueOf((amount *
                        (currencySpend.getCurrencyRate() / currencyDesired.getCurrencyRate())))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();

        assertEquals(expectedAmount, response.getCalculatedAmount());
    }

    static Stream<Arguments> getTestDataForCalculateRate() {
        HashMap<Currency, Double> testData = new HashMap<>();
        Faker faker = new Faker();
        //1 RUB->KZT +
        double amount1 = faker.number().randomDouble(2, 0, 1000);
        //2 USD->EUR -
        double amount2 = faker.number().randomDouble(2, -1000, -1);
        //3 EUR->EUR +
        double amount3 = 1.00;
        //4 RUB->USD 0
        double amount4 = 0;

        return Stream.of(
                Arguments.of(amount1, RUB, KZT),
                Arguments.of(amount2, USD, EUR),
                Arguments.of(amount3, EUR, EUR),
                Arguments.of(amount4, RUB, USD)
        );
    }
}