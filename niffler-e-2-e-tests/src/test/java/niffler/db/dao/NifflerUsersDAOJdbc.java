package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate = -1;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO users "
                    + "(id,username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?,?, ?, ?, ?, ?, ?)");
                 PreparedStatement st1 = conn.prepareStatement("INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
                conn.setAutoCommit(false);

                UUID uuid = randomUUID();
                st.setObject(1, uuid.toString(), Types.OTHER);
                st.setString(2, user.getUsername());
                st.setString(3, pe.encode(user.getPassword()));
                st.setBoolean(4, user.getEnabled());
                st.setBoolean(5, user.getAccountNonExpired());
                st.setBoolean(6, user.getAccountNonLocked());
                st.setBoolean(7, user.getCredentialsNonExpired());

                executeUpdate = st.executeUpdate();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    st1.setObject(1, uuid.toString(), Types.OTHER);
                    st1.setString(2, authority.getAuthority().name());
                    st1.executeUpdate();
                }
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return executeUpdate;
    }

    @Override
    public int updateUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement
                     ("update users set password=?,enabled=?" +
                             ",account_non_expired=?, account_non_locked=?, credentials_non_expired=?" +
                             " where username=?")) {

            st.setString(1, pe.encode(user.getPassword()));
            st.setBoolean(2, user.getEnabled());
            st.setBoolean(3, user.getAccountNonExpired());
            st.setBoolean(4, user.getAccountNonLocked());
            st.setBoolean(5, user.getCredentialsNonExpired());
            st.setString(6, user.getUsername());

            executeUpdate = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
    }

    @Override
    public int deleteUser(UserEntity user) {
        String deleteAuthoritiesSql =
                "delete from authorities where user_id in " +
                        "(select id from users where username='" + user.getUsername() + "');";

        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(deleteAuthoritiesSql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int executeDelete;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement
                     ("delete from users where username=?")) {

            st.setString(1, user.getUsername());

            executeDelete = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeDelete;
    }

    @Override
    public List<UserEntity> readUser(UserEntity user) {
        List<UserEntity> executeRead = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement
                     ("select * from users " +
                             " where username=?")) {

            st.setString(1, user.getUsername());
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                UserEntity readData = new UserEntity();
                readData.setId(UUID.fromString(resultSet.getString("id")));
                readData.setUsername(resultSet.getString("username"));
                //     password?
                readData.setEnabled(resultSet.getBoolean("enabled"));
                readData.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                readData.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                readData.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                executeRead.add(readData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeRead;
    }

    @Override
    public String getUserId(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            st.setString(1, userName);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
