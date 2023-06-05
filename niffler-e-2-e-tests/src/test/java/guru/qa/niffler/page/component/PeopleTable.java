package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.BaseComponent;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@Getter
public class PeopleTable extends BaseComponent<PeopleTable> {

    private final SelenideElement peopleTable = $(".main-content__section").$(".table");
    private final SelenideElement peopleTableAvatar = peopleTable
            .$(byText("Avatar"));
    private final SelenideElement peopleTableUsername = peopleTable
            .$(byText("Username"));
    private final SelenideElement peopleTableName = peopleTable
            .$(byText("Name"));
    private final SelenideElement peopleTableActions = peopleTable
            .$(byText("Actions"));
    private final ElementsCollection people = $$x("//tbody/tr");
    private final SelenideElement peopleFriendButton = peopleTable
            .$(byAttribute("data-tooltip-id", "remove-friend"));
    public static final String youAreFriends = "You are friends";
    private final ElementsCollection peopleTableRows = peopleTable.$$("tbody>tr");


    public PeopleTable() {
        super($(".main-content__section").$(".table"));
    }

    @Override
    public PeopleTable checkThatComponentDisplayed() {
        peopleTableAvatar.shouldBe(visible);
        peopleTableUsername.shouldBe(visible);
        peopleTableName.shouldBe(visible);
        peopleTableActions.shouldBe(visible);
        return this;
    }

    public int addRandomFriend(String username) {
        peopleTable.$("td").shouldBe(visible);
        List<UserJson> userJsonList = parseTable(username);
        if (userJsonList == null) {
            refresh();
            userJsonList = parseTable(username);
        }
        peopleTable.$("td").shouldBe(visible);
        List<UserJson> noFriendList = userJsonList.stream().filter(u -> u.getFriendState() == null).toList();
        if (noFriendList.size() > 0) {
            Faker faker = new Faker();
            UserJson randomUserWithoutFriendship =
                    noFriendList.get(faker.number().numberBetween(0, noFriendList.size() - 1));
            SelenideElement addButton = peopleTableRows.get(userJsonList.indexOf(randomUserWithoutFriendship))
                    .$(byAttribute("data-tooltip-id", "add-friend")).$("button");
            if (!addButton.is(visible)) {
                addButton.scrollTo();
            }
            addButton.click();
            peopleTable.$("td").shouldBe(visible);
            userJsonList = parseTable(username);
            UserJson randomUserAfterSendingInvitation = userJsonList.stream()
                    .filter(u -> u.getUsername().equals(randomUserWithoutFriendship.getUsername())).toList().get(0);
            assertEquals(randomUserAfterSendingInvitation
                    .getFriendState(), FriendState.INVITE_SENT);
        }
        return noFriendList.size();
    }

    public List<UserJson> parseTable(String username) {
        if (peopleTableRows.size() > 0) {
            List<UserJson> usernameList = new ArrayList<>();
            for (SelenideElement peopleTableRow : peopleTableRows) {
                UserJson user = new UserJson();
                ElementsCollection cells = peopleTableRow.$$("td");
                user.setUsername(cells.get(1).getText());
                String nameTable = cells.get(2).getText();
                System.out.println(nameTable);
                List<String> namePlusSurname =
                        new ArrayList<>(Arrays.asList(nameTable.split(" ")));
                if (namePlusSurname.size() > 1) {
                    user.setFirstname(namePlusSurname.get(0));
                    user.setSurname(namePlusSurname.get(1));
                }
                //TODO(Think about logic)
//                else if (namePlusSurname.size() == 1) {
//                    if (namePlusSurname.get(0).length() > 0) {
//
//                    }
//                }

                //INVITE_SENT, INVITE_RECEIVED, FRIEND
                if (cells.get(3).getText().contains("You are friends")) {
                    user.setFriendState(FriendState.FRIEND);
                } else if (cells.get(3).getText().contains("Pending invitation")) {
                    user.setFriendState(FriendState.INVITE_SENT);
                } else if (cells.get(3)
                        .$(byAttribute("data-tooltip-id", "submit-invitation")).is(visible)) {
                    user.setFriendState(FriendState.INVITE_RECEIVED);
                }
                usernameList.add(user);
            }
            assertFalse(usernameList.stream().map(UserJson::getUsername).toList().contains(username));
            return usernameList;
        }
        return null;
    }

    public int confirmFriendRequest(String username) {
        peopleTable.$("td").shouldBe(visible);
        List<UserJson> userJsonList = parseTable(username);
        List<UserJson> requestFriendList = userJsonList.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_RECEIVED).toList();
        System.out.println(requestFriendList);
        if (requestFriendList.size() > 0) {
            Faker faker = new Faker();
            UserJson randomUserWithRequest =
                    requestFriendList.get(faker.number().numberBetween(0, requestFriendList.size() - 1));
            System.out.println(randomUserWithRequest);
            SelenideElement confirmButton =
                    peopleTableRows.get(userJsonList.indexOf(randomUserWithRequest))
                            .$(".button-icon_type_submit");
            if (!confirmButton.is(visible)) {
                confirmButton.scrollTo();
            }
            confirmButton.click();
            refresh();
            userJsonList = parseTable(username);
            System.out.println(userJsonList);
            UserJson randomUserAfterConfirmingInvitation = userJsonList.stream()
                    .filter(u -> u.getUsername().equals(randomUserWithRequest.getUsername())).toList().get(0);
            System.out.println(randomUserAfterConfirmingInvitation);
            assertEquals(randomUserAfterConfirmingInvitation
                    .getFriendState(), FriendState.FRIEND);
        }
        return requestFriendList.size();
    }

    public int declineFriendRequest(BasePage<?> basePage,String username) {
        peopleTable.$("td").shouldBe(visible);
        List<UserJson> userJsonList = parseTable(username);
        List<UserJson> requestFriendList = userJsonList.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_RECEIVED).toList();
        System.out.println(requestFriendList);
        if (requestFriendList.size() > 0) {
            Faker faker = new Faker();
            UserJson randomUserWithRequest =
                    requestFriendList.get(faker.number().numberBetween(0, requestFriendList.size() - 1));
            System.out.println(randomUserWithRequest);
            SelenideElement declineButton =
                    peopleTableRows.get(userJsonList.indexOf(randomUserWithRequest))
                            .$(".button-icon_type_close");
            if (!declineButton.is(visible)) {
                declineButton.scrollTo();

            }
            declineButton.click();

            userJsonList = parseTable(username);
            System.out.println(userJsonList);
            if (basePage instanceof PeoplePage) {
                UserJson randomUserAfterConfirmingInvitation = userJsonList.stream()
                        .filter(u -> u.getUsername().equals(randomUserWithRequest.getUsername())).toList().get(0);
                System.out.println(randomUserAfterConfirmingInvitation);
                assertNull(randomUserAfterConfirmingInvitation
                        .getFriendState());
            } else if (basePage instanceof FriendsPage) {
                List<String> userNamesListAfterDeclining = parseTable(username).stream().map(UserJson::getUsername).toList();
                System.out.println(userNamesListAfterDeclining);
                assertFalse(userNamesListAfterDeclining.contains(randomUserWithRequest.getUsername()));
            }
        }
        return requestFriendList.size();
    }
}
