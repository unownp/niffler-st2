package niffler.test;

import com.github.javafaker.Faker;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static niffler.db.dao.NifflerUsersDAO.pe;

public class LoginNewUserTest extends BaseWebTest {

    private static Faker faker = new Faker();
    //    private NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
    // private NifflerUsersDAO usersDAO = new NifflerUsersDAOSpringJdbc();
    private NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();
    private UserEntity ue;

    private static final String TEST_PWD = "1234567";

    @BeforeEach
    void updateUserForTest() {

        ue = usersDAO.getUser("muchogusto2");
        ue.setPassword(pe.encode(TEST_PWD));
        ue.setEnabled(true);
        ue.setAccountNonExpired(true);
        ue.setAccountNonLocked(true);
        ue.setCredentialsNonExpired(true);
        List<Authority> newAuthorities = new ArrayList<>();
        newAuthorities.add(Authority.read);
        newAuthorities.add(Authority.write);
//        newAuthorities.add(Authority.admin);
//        newAuthorities.add(Authority.premiumUser);
        List<AuthorityEntity> listAuthorityEntity = ue.getAuthorities();
        List<Authority> currentAuthorities = listAuthorityEntity.stream().map(AuthorityEntity::getAuthority).toList();
        List<Authority> authoritiesToWrite = new ArrayList<>(newAuthorities);
        List<Authority> authoritiesToDelete = new ArrayList<>(currentAuthorities);
        authoritiesToWrite.removeAll(currentAuthorities);
        System.out.println(authoritiesToWrite.size());
        authoritiesToDelete.removeAll(newAuthorities);
        System.out.println(authoritiesToDelete.size());
        for (Authority authority : authoritiesToDelete) {
            listAuthorityEntity.removeIf(authorityEntity -> authorityEntity.getAuthority().equals(authority));
        }
        for (Authority authority : authoritiesToWrite) {
            AuthorityEntity authorityEntity = new AuthorityEntity();
            authorityEntity.setAuthority(authority);
            authorityEntity.setUser(ue);
            listAuthorityEntity.add(authorityEntity);
        }

        ue.setAuthorities(listAuthorityEntity);
        usersDAO.updateUser(ue);

    }

    @AfterEach
    void cleanUp() {
        //  usersDAO.removeUser(ue);
    }

    @Test
    void loginTest() {

    }

}
