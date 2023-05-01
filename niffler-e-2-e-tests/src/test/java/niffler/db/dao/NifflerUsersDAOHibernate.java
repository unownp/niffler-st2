package niffler.db.dao;

import niffler.db.ServiceDB;
import niffler.db.entity.UserEntity;
import niffler.db.jpa.EmfProvider;
import niffler.db.jpa.JpaTransactionManager;

import java.util.UUID;

public class NifflerUsersDAOHibernate extends JpaTransactionManager implements NifflerUsersDAO {

    public NifflerUsersDAOHibernate() {
        super(EmfProvider.INSTANCE.getEmf(ServiceDB.NIFFLER_AUTH).createEntityManager());
    }

    @Override
    public int createUser(UserEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        persist(user);
        return 0;
    }

    @Override
    public int updateUser(UserEntity user) {
        UserEntity userDb = em.find(UserEntity.class, UUID.fromString(getUserId(user.getUsername())));
        userDb.setPassword(pe.encode(user.getPassword()));
        userDb.setEnabled(user.getEnabled());
        userDb.setAccountNonExpired(user.getAccountNonExpired());
        userDb.setAccountNonLocked(user.getAccountNonLocked());
        userDb.setCredentialsNonExpired(user.getCredentialsNonExpired());
        merge(userDb);
        return 0;
    }

    @Override
    public String getUserId(String userName) {
        return em.createQuery("select u from UserEntity u where username=:username", UserEntity.class)
                .setParameter("username", userName)
                .getSingleResult()
                .getId()
                .toString();
    }

    @Override
    public int removeUser(UserEntity user) {
        remove(user);
        return 0;
    }
}
