package com.daiyu.service.user;
import com.daiyu.dao.BaseDao;
import com.daiyu.dao.user.UserDao;
import com.daiyu.dao.user.UserDaoImpl;
import com.daiyu.pojo.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    UserDao userDao = new UserDaoImpl();
    public User login(String userCode, String userPassword) throws SQLException {
        Connection connection = BaseDao.getConnection();
        User user = userDao.getLoginUsers(connection, userCode);
        BaseDao.closeResources(connection,null,null);
        return user;
    }

    public boolean updatePwd(int id, String userPassword) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,userPassword)>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public int userCount(String userName, int userRole) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int count = userDao.getUserCount(connection, userName, userRole);
        BaseDao.closeResources(connection,null,null);
        return count;
    }

    public List<User> userList(String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<User> userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        BaseDao.closeResources(connection, null, null);
        return userList;
    }

    public boolean add(User user){
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            int add = userDao.add(connection, user);
            connection.commit();
            if(add>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();//失败就回滚
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUsers(connection,userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return user;
    }

    public boolean deleteUserById(Integer delid) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            int i = userDao.deleteUserById(connection,delid);
            if(i>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public boolean modifyUser(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int modify = userDao.modify(connection, user);
            connection.commit();
            if(modify>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    public User getUserById(String id) {
        Connection connection = BaseDao.getConnection();
        User user = userDao.getUserById(connection,id);
        BaseDao.closeResources(connection,null,null);
        return user;
    }
}
