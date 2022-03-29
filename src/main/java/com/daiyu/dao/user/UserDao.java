package com.daiyu.dao.user;
import com.daiyu.pojo.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //获取登录的用户
    public User getLoginUsers(Connection connection,String userCode) throws SQLException;
    //修改用户密码
    public int updatePwd(Connection connection,int id,String userPassword) throws SQLException;
    //通过用户名和角色查询用户总数
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;
    //条件查询获得用户列表
    public List<User> getUserList (Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;
    //增加用户
    public int add(Connection connection,User user) throws SQLException;
    //通过用户id删除用户信息
    public int deleteUserById(Connection connection,Integer delid) throws SQLException;
    //修改用户信息
    public int modify(Connection connection,User user) throws SQLException;
    //通过userId查看当前用户信息
    public User getUserById(Connection connection,String id);
}
