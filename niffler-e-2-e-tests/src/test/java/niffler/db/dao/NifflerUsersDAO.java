package niffler.db.dao;

import niffler.db.entity.UserEntity;

import java.util.List;

public interface NifflerUsersDAO {

    int createUser(UserEntity user);

    int updateUser(UserEntity user);

    int deleteUser(UserEntity user);

    List<UserEntity> readUser(UserEntity user);

    String getUserId(String userName);

}
