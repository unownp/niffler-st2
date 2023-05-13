package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement insertUserSt = conn.prepareStatement("INSERT INTO users "
                    + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertAuthoritySt = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
                insertUserSt.setString(1, user.getUsername());
                insertUserSt.setString(2, pe.encode(user.getPassword()));
                insertUserSt.setBoolean(3, user.getEnabled());
                insertUserSt.setBoolean(4, user.getAccountNonExpired());
                insertUserSt.setBoolean(5, user.getAccountNonLocked());
                insertUserSt.setBoolean(6, user.getCredentialsNonExpired());
                executeUpdate = insertUserSt.executeUpdate();

                final UUID finalUserId;

                try (ResultSet generatedKeys = insertUserSt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        finalUserId = UUID.fromString(generatedKeys.getString(1));
                        user.setId(finalUserId);
                    } else {
                        throw new SQLException("Creating user failed, no ID present");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    insertAuthoritySt.setObject(1, finalUserId);
                    insertAuthoritySt.setString(2, authority.getAuthority().name());
                    insertAuthoritySt.addBatch();
                    insertAuthoritySt.clearParameters();
                }
                insertAuthoritySt.executeBatch();
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public int updateUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement
                    ("update users set password=?,enabled=?" +
                            ",account_non_expired=?, account_non_locked=?, credentials_non_expired=?" +
                            " where username=?");
                 PreparedStatement deleteAuthorities = conn.prepareStatement("delete from authorities where user_id = ?");
                 PreparedStatement createNewAuthorities = conn.prepareStatement("INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {

                st.setString(1, pe.encode(user.getPassword()));
                st.setBoolean(2, user.getEnabled());
                st.setBoolean(3, user.getAccountNonExpired());
                st.setBoolean(4, user.getAccountNonLocked());
                st.setBoolean(5, user.getCredentialsNonExpired());
                st.setString(6, user.getUsername());

                executeUpdate = st.executeUpdate();

                deleteAuthorities.setObject(1, user.getId());
                deleteAuthorities.execute();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    createNewAuthorities.setObject(1, user.getId());
                    createNewAuthorities.setString(2, authority.getAuthority().name());
                    createNewAuthorities.addBatch();
                }
                createNewAuthorities.executeBatch();
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
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

    @Override
    public int removeUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement deleteUserSt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement deleteAuthoritySt = conn.prepareStatement(
                         "DELETE FROM authorities WHERE user_id = ?")) {
                deleteUserSt.setObject(1, user.getId());
                deleteAuthoritySt.setObject(1, user.getId());

                deleteAuthoritySt.executeUpdate();
                executeUpdate = deleteUserSt.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public UserEntity getUser(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement userSt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
             PreparedStatement authoritiesSt = conn.prepareStatement("Select * from authorities where user_id=?")) {
            userSt.setString(1, userName);
            UserEntity userEntity = new UserEntity();
            ResultSet userResultSet = userSt.executeQuery();
            if (userResultSet.next()) {
                userEntity.setId((UUID) userResultSet.getObject(1));
                userEntity.setUsername(userName);
                userEntity.setPassword(userResultSet.getString(3));
                userEntity.setEnabled(userResultSet.getBoolean(4));
                userEntity.setAccountNonExpired(userResultSet.getBoolean(5));
                userEntity.setAccountNonLocked(userResultSet.getBoolean(6));
                userEntity.setCredentialsNonExpired(userResultSet.getBoolean(7));
                System.out.println(userEntity);

            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
            List<AuthorityEntity> authorityEntityList = new ArrayList<>();
            authoritiesSt.setObject(1, userEntity.getId());
            ResultSet authoritiesResultSet = authoritiesSt.executeQuery();
            while (authoritiesResultSet.next()) {
                AuthorityEntity authorityEntity = new AuthorityEntity();
//                authorityEntity.setId((UUID) authoritiesResultSet.getObject(1));
//                authorityEntity.setUser(userEntity);
                authorityEntity.setAuthority(Authority.valueOf(authoritiesResultSet.getString(3)));
                authorityEntityList.add(authorityEntity);
            }
            userEntity.setAuthorities(authorityEntityList);
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
