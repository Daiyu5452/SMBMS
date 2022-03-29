package com.daiyu.service.user;
import com.daiyu.pojo.User;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode,String userPassword) throws SQLException;
    //调用Dao层的修改密码方法
    public boolean updatePwd(int id,String userPassword);

    //通过用户名和角色查询用户总数        分页要求之一
    public int userCount(String userName,int userRole) throws SQLException;
    //条件查询获得用户列表
    public List<User> userList (String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    //增加用户                        用户列表增删改操作
    public boolean add(User user) throws SQLException;
    //根据编码判断当前用户是否存在
    public User selectUserCodeExist(String userCode);
    //根据用户id删除用户信息
    public boolean deleteUserById(Integer delid);
    //修改用户信息
    public boolean modifyUser(User user);
    //根据用户id得到当前用户
    public User getUserById(String id);
}
