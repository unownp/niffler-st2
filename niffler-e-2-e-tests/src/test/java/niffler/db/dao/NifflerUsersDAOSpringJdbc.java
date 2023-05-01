package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.UUID;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;

    public NifflerUsersDAOSpringJdbc() {
        DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(transactionManager.getDataSource()));
    }

    /**
     * @noinspection ConstantConditions
     */
    @Override
    public int createUser(UserEntity user) {
        return transactionTemplate.execute(ts -> {
            user.setId(UUID.randomUUID());
            jdbcTemplate.update("INSERT INTO users " +
                            " (id,username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            " VALUES (?, ?, ?, ?, ?, ?, ?)",
                    user.getId(), user.getUsername(), pe.encode(user.getPassword()), user.getEnabled()
                    , user.getAccountNonExpired(), user.getAccountNonLocked(), user.getCredentialsNonExpired());
            for (AuthorityEntity authority : user.getAuthorities()) {
                jdbcTemplate.update("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", user.getId(), authority.getAuthority().name());
            }
            return 1;
        });
    }

    @Override
    public int updateUser(UserEntity user) {
        return jdbcTemplate.update("update users set password=?,enabled=?" +
                        ",account_non_expired=?, account_non_locked=?, credentials_non_expired=? " +
                        " where username=?",
                pe.encode(user.getPassword()), user.getEnabled()
                , user.getAccountNonExpired(), user.getAccountNonLocked(), user.getCredentialsNonExpired(), user.getUsername());
    }

    @Override
    public String getUserId(String userName) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                rs -> {
                    return rs.getString(1);
                },
                userName
        );
    }

    @Override
    public int removeUser(UserEntity user) {
        return transactionTemplate.execute(st -> {
            jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
        });
    }
}
