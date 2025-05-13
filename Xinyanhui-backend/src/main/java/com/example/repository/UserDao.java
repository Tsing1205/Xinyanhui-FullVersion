package com.example.repository;

import com.example.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM Users WHERE username = #{username}")
    User findByUsername(String username); // 通过用户名查找用户

    @Select("SELECT * FROM Users WHERE username = #{username} AND password_HashwithSalt = #{password}")
    User findByUsernameAndPassword(String username, String password); // 通过用户名和密码查找用户

    @Select("SELECT * FROM Users WHERE phone = #{phone} AND password_HashwithSalt = #{password}")
    User findByPhoneAndPassword(String phone, String password); // 通过电话和密码查找用户

    @Select("SELECT * FROM Users WHERE email = #{email} AND password_HashwithSalt = #{password}")
    User findByEmailAndPassword(String email, String password); // 通过邮箱和密码查找用户

    @Select("SELECT * FROM Users WHERE phone = #{phone}")
    User findByPhone(String phone); // 通过电话查找用户

    @Select("SELECT * FROM Users WHERE email = #{email}")
    User findByEmail(String email); // 通过邮箱查找用户

    @Insert("INSERT INTO Users (username, password_HashwithSalt, salt, email, phone, register_date) VALUES (#{username}, #{password}, #{salt}, #{email}, #{phone}, NOW())")
    void save(User user);

    @Select("SELECT username FROM Users WHERE user_id = #{id}")
    String findNameById(Integer id);
}
