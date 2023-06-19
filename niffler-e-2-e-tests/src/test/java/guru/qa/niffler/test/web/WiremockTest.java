package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.*;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.StartPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static guru.qa.niffler.config.Config.getConfig;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiremockTest extends BaseWebTest {
    private static final Config config = getConfig();
    private static final int USER_DATA_PORT = config.getUserDataPort();
    private static final int SPEND_PORT = config.getSpendPort();
    private static final int AUTH_PORT = config.getOauth2Port();
    private static String jsonUser;
    private static UserJson userJson;
    private static String invitations;
    private static List<UserJson> invitationsList;
    private static String categories;
    private static List<CategoryJson> categoriesList;
    private static String statistic;
    private static List<StatisticJson> statisticJsons;
    private static String spends;
    private static List<SpendJson> spendJsons;
    private static String currencies;
    private static List<CurrencyJson> currencyJsons;
    private static String friends;
    private static List<UserJson> friendJsons;
    private static String allUsers;
    private static List<UserJson> allUsersList;
    private static String loginResponseBody;

    @RegisterExtension
    WireMockExtension userDataMock = WireMockExtension.newInstance()
            .options(WireMockConfiguration.options().port(USER_DATA_PORT))
            .configureStaticDsl(true)
            .build();
    @RegisterExtension
    WireMockExtension spendMock = WireMockExtension.newInstance()
            .options(WireMockConfiguration.options().port(SPEND_PORT))
            .configureStaticDsl(true)
            .build();

    @Test
    void test() throws JsonProcessingException {
        userDataMock.stubFor(get(config.getCurrentUserPath() + "?username=" + userJson.getUsername())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(jsonUser)));

        userDataMock.stubFor(get(config.getInvitationsPath() + "?username=" + userJson.getUsername())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(invitations)));

        spendMock.stubFor(get(config.getCategoriesPath() + "?username=" + userJson.getUsername())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(categories)));

        spendMock.stubFor(get(config.getSpendPath() + "?username=" + userJson.getUsername())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(spends)));

//не проверить стандартными способами
        spendMock.stubFor(get(config.getStatisticPath() + "?username="
                + userJson.getUsername() + "&userCurrency=" + userJson.getCurrency())
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(statistic)));

        userDataMock.stubFor(get(config.getFriendsPath() + "?username="
                + userJson.getUsername() + "&includePending=false")
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody(friends)));

        StartPage startPage = Selenide.open(StartPage.URL, StartPage.class);
        startPage.checkThatPageLoaded();
        LoginPage loginPage = startPage.clickLoginButton();
        loginPage.checkThatPageLoaded();
        MainPage mainPage = loginPage.signInWithCredentials(userJson.getUsername(), userJson.getPassword());
        mainPage.checkThatPageLoaded();
        mainPage.getHeader().getFriendRedPoint().shouldBe(visible);
        mainPage.getCategoryInput().click();
        int categoriesInputSize = mainPage.getCategoryInputListBoxCategories().size();
        int spendingsTableSize = mainPage.getSpendingsTableRows().size();
        FriendsPage friendsPage = mainPage.getHeader().goToFriendsPage();
        friendsPage.checkThatPageLoaded();
        friendsPage.getPeopleTable().getRemoveFriendButton().shouldBe(visible);
        friendsPage.getPeopleTable().getSubmitInvitationButton().shouldBe(visible);
        assertAll(
                () -> assertEquals(categoriesInputSize, categoriesList.size()),
                () -> assertEquals(spendingsTableSize, spendJsons.size())
        );
    }

    @BeforeAll
    static void setTestData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonUser = "    {\n" +
                "        \"id\": \"87403f2a-17d7-41d4-9d09-8078a7338439\",\n" +
                "        \"username\": \"ELGATO\",\n" +
                "        \"firstname\": \"Jessi\",\n" +
                "        \"surname\": \"Collins\",\n" +
                "        \"currency\": \"KZT\",\n" +
                "        \"photo\": \"data:image/jpeg;base64,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\",\n" +
                "        \"friendState\": \"FRIEND\"\n" +
                "    }";
        userJson = objectMapper.readValue(jsonUser, UserJson.class);
        userJson.setPassword("12345");
        invitations = """
                [
                    {
                        "id": "8c56c4a3-5ea6-4ed3-9f68-800ac25dd13a",
                        "username": "Georgetta",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null,
                        "friendState": "INVITE_RECEIVED"
                    }
                ]""";
        invitationsList = objectMapper.readValue(invitations, new TypeReference<>() {
        });
        categories = """
                [
                    {
                        "id": "bcb35170-ef99-42f2-a442-e3de06ed3b31",
                        "category": "Electrode",
                        "username": "ELGATO"
                    },
                    {
                        "id": "8219c6a7-11c5-4d93-a48d-93370a3ed4a0",
                        "category": "Golduck",
                        "username": "ELGATO"
                    },
                    {
                        "id": "72994773-56d1-47a8-b56a-b506dc0e36d5",
                        "category": "Golem",
                        "username": "ELGATO"
                    },
                    {
                        "id": "3464b27e-9681-40ef-abbf-8b2141882cda",
                        "category": "Magnemite",
                        "username": "ELGATO"
                    },
                    {
                        "id": "47e3b754-1160-4723-aafc-7c4263f4c2c3",
                        "category": "Pidgeotto",
                        "username": "ELGATO"
                    },
                    {
                        "id": "6425ebb2-0bbb-4e3a-997f-72168c2c582a",
                        "category": "Weedle",
                        "username": "ELGATO"
                    },
                    {
                        "id": "389d0407-fa3c-4206-912e-f072748a1e7a",
                        "category": "looting3123",
                        "username": "ELGATO"
                    }
                ]""";
        categoriesList = objectMapper.readValue(categories, new TypeReference<>() {
        });
        statistic = """
                [
                    {
                        "dateFrom": "2023-06-03T21:00:00.000+00:00",
                        "dateTo": null,
                        "currency": "RUB",
                        "total": 104000.0,
                        "userDefaultCurrency": "KZT",
                        "totalInUserDefaultCurrency": 742857.14,
                        "categoryStatistics": [
                            {
                                "category": "Electrode",
                                "total": 104000.0,
                                "totalInUserDefaultCurrency": 742857.14,
                                "spends": [
                                    {
                                        "id": "69a12a1a-4545-42ee-9f68-2e1a63a49bd4",
                                        "spendDate": "2023-06-03T21:00:00.000+00:00",
                                        "category": "Electrode",
                                        "currency": "RUB",
                                        "amount": 52000.0,
                                        "description": "someDescription",
                                        "username": "ELGATO"
                                    },
                                    {
                                        "id": "e5582945-baba-4060-8d0b-a7ce585aa1ea",
                                        "spendDate": "2023-06-03T21:00:00.000+00:00",
                                        "category": "Electrode",
                                        "currency": "RUB",
                                        "amount": 52000.0,
                                        "description": "someDescription",
                                        "username": "ELGATO"
                                    }
                                ]
                            },
                            {
                                "category": "Golduck",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golem",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Magnemite",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Pidgeotto",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Weedle",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "looting3123",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            }
                        ]
                    },
                    {
                        "dateFrom": null,
                        "dateTo": null,
                        "currency": "USD",
                        "total": 0.0,
                        "userDefaultCurrency": "KZT",
                        "totalInUserDefaultCurrency": 0.0,
                        "categoryStatistics": [
                            {
                                "category": "Electrode",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golduck",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golem",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Magnemite",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Pidgeotto",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Weedle",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "looting3123",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            }
                        ]
                    },
                    {
                        "dateFrom": null,
                        "dateTo": null,
                        "currency": "EUR",
                        "total": 0.0,
                        "userDefaultCurrency": "KZT",
                        "totalInUserDefaultCurrency": 0.0,
                        "categoryStatistics": [
                            {
                                "category": "Electrode",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golduck",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golem",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Magnemite",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Pidgeotto",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Weedle",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "looting3123",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            }
                        ]
                    },
                    {
                        "dateFrom": "2023-06-12T21:00:00.000+00:00",
                        "dateTo": null,
                        "currency": "KZT",
                        "total": 223424.0,
                        "userDefaultCurrency": "KZT",
                        "totalInUserDefaultCurrency": 223424.0,
                        "categoryStatistics": [
                            {
                                "category": "Electrode",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golduck",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Golem",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Magnemite",
                                "total": 223424.0,
                                "totalInUserDefaultCurrency": 223424.0,
                                "spends": [
                                    {
                                        "id": "8736ae95-6ef0-4129-8b17-94cd465ad00f",
                                        "spendDate": "2023-06-12T21:00:00.000+00:00",
                                        "category": "Magnemite",
                                        "currency": "KZT",
                                        "amount": 223424.0,
                                        "description": "wre",
                                        "username": "ELGATO"
                                    }
                                ]
                            },
                            {
                                "category": "Pidgeotto",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "Weedle",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            },
                            {
                                "category": "looting3123",
                                "total": 0.0,
                                "totalInUserDefaultCurrency": 0.0,
                                "spends": []
                            }
                        ]
                    }
                ]""";
        statisticJsons = objectMapper.readValue(statistic, new TypeReference<>() {
        });
        spends = """
                [
                    {
                        "id": "69a12a1a-4545-42ee-9f68-2e1a63a49bd4",
                        "spendDate": "2023-06-03T21:00:00.000+00:00",
                        "category": "Electrode",
                        "currency": "RUB",
                        "amount": 52000.0,
                        "description": "someDescription",
                        "username": "ELGATO"
                    },
                    {
                        "id": "e5582945-baba-4060-8d0b-a7ce585aa1ea",
                        "spendDate": "2023-06-03T21:00:00.000+00:00",
                        "category": "Electrode",
                        "currency": "RUB",
                        "amount": 52000.0,
                        "description": "someDescription",
                        "username": "ELGATO"
                    },
                    {
                        "id": "8736ae95-6ef0-4129-8b17-94cd465ad00f",
                        "spendDate": "2023-06-12T21:00:00.000+00:00",
                        "category": "Magnemite",
                        "currency": "KZT",
                        "amount": 223424.0,
                        "description": "wre",
                        "username": "ELGATO"
                    }
                ]""";
        spendJsons = objectMapper.readValue(spends, new TypeReference<>() {
        });
        currencies = """
                [
                    {
                        "currency": "RUB",
                        "currencyRate": 0.015
                    },
                    {
                        "currency": "KZT",
                        "currencyRate": 0.0021
                    },
                    {
                        "currency": "EUR",
                        "currencyRate": 1.08
                    },
                    {
                        "currency": "USD",
                        "currencyRate": 1.0
                    }
                ]""";
        currencyJsons = objectMapper.readValue(currencies, new TypeReference<>() {
        });
        friends = """
                [
                    {
                        "id": "6442f34a-fe41-4236-a8e9-ec5569594b5a",
                        "username": "GEESEFETCHER",
                        "firstname": "Patrick",
                        "surname": "Bateman",
                        "currency": "RUB",
                        "photo": null,
                        "friendState": "FRIEND"
                    }
                ]""";
        friendJsons = objectMapper.readValue(friends, new TypeReference<>() {
        });
        allUsers = """
                [
                    {
                        "id": "6442f34a-fe41-4236-a8e9-ec5569594b5a",
                        "username": "GEESEFETCHER",
                        "firstname": "Patrick",
                        "surname": "Bateman",
                        "currency": "RUB",
                        "photo": null,
                        "friendState": "FRIEND"
                    },
                    {
                        "id": "f14b0028-d850-4fb0-93a4-2a07c40c12cc",
                        "username": "ZHABROSLI",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "3898621e-8564-4eb6-936d-673906133793",
                        "username": "Leonard",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "28f76063-2d8d-41eb-b10b-4cdea6f6b392",
                        "username": "Johnie",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "8c56c4a3-5ea6-4ed3-9f68-800ac25dd13a",
                        "username": "Georgetta",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null,
                        "friendState": "INVITE_RECEIVED"
                    },
                    {
                        "id": "30c47951-f574-40bc-8051-deca1cd084ff",
                        "username": "Rena",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "57595bf3-0e33-4950-b168-2f4fc9697a0d",
                        "username": "Orval",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "9f94d491-50a9-40ef-9202-68e1ebadd9e1",
                        "username": "Eli Ondefloor",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "804de630-4f3b-42ef-bf21-62e100e192c6",
                        "username": "Laurel",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "f47a472c-082e-460c-8073-f90b36644c58",
                        "username": "GEESECATCHER",
                        "firstname": "Rbk",
                        "surname": "Gossling",
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "0a409f3d-4ada-4415-a14b-a2ae1cc0cac2",
                        "username": "valentin0",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "d86606bf-adf5-48a0-8c78-aec3198a794a",
                        "username": "Eleonor",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "7096ae04-b6ab-450a-951d-bb749a6cf44e",
                        "username": "ALSS",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "cafde763-f054-485a-8166-5b9fc7e031d2",
                        "username": "valentin8",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "920b2fcb-63f3-4a4e-bc2d-52699b841aa0",
                        "username": "ZABOBON",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "aba51b42-8663-44cc-b6eb-1c590354deb1",
                        "username": "VAGABOND",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    },
                    {
                        "id": "9c3f4d43-0d55-42b9-af89-0b9d8ffdf266",
                        "username": "Kizzie",
                        "firstname": null,
                        "surname": null,
                        "currency": "RUB",
                        "photo": null
                    }
                ]""";
        allUsersList = objectMapper.readValue(allUsers, new TypeReference<>() {
        });
    }
}


