package com.daiyu.dao.user;
import com.daiyu.dao.BaseDao;
import com.daiyu.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public User getLoginUsers(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, sql, params, rs);
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResources(null, pstm, rs);
        }
        return user;
    }

    public int updatePwd(Connection connection, int id, String userPassword) throws SQLException {
        PreparedStatement pstm = null;
        int ur = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {userPassword, id};
            ur = BaseDao.execute(connection, pstm, sql, params, ur);
            BaseDao.closeResources(null, pstm, null);
        }
        return ur;
    }

    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
        List<Object> list = new ArrayList<Object>();
        if (userName != null) {
            sql.append(" and userName like ?");
            list.add("%" + userName + "%");
        }
        if (userRole > 0) {
            sql.append(" and userRole = ?");
            list.add(userRole);
        }
        Object[] params = list.toArray();
        rs = BaseDao.execute(connection, pstm, sql.toString(), params, rs);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        BaseDao.closeResources(null, pstm, rs);
        return count;
    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if (userName != null) {
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if (userRole > 0) {
                sql.append(" and userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,? ");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstm, sql.toString(), params, rs);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("roleName"));
                userList.add(_user);
            }
            BaseDao.closeResources(null, pstm, rs);
        }
        return userList;
    }

    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(connection!=null) {
            String sql = "insert into smbms_user(id, userCode, userName, userPassword, gender, birthday, phone, address, userRole, createdBy, creationDate, modifyBy, modifyDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getId(), user.getUserCode(), user.getUserName(),
                    user.getUserPassword(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getUserRole(),
                    user.getCreatedBy(), user.getCreationDate(),
                    user.getModifyBy(), user.getModifyDate()};
            updateRows = BaseDao.execute(connection, pstm, sql, params, updateRows);
            BaseDao.closeResources(null, pstm, null);
        }
        return updateRows;
    }

    public int deleteUserById(Connection connection, Integer delid) throws SQLException {
        PreparedStatement pstm = null;
        int deleteRows = 0;
        if(connection!=null) {
            String sql = "delete from smbms_user where id = ?";
            Object[] params = {delid};
            deleteRows = BaseDao.execute(connection, pstm, sql, params, deleteRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return deleteRows;
    }

    public int modify(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(connection!=null) {
            String sql = "update smbms_user set userCode=?,userName=?,userPassword=?,gender=?,birthday=?,\n" +
                    "           phone=?,address=?,userRole=?,createdBy=?,creationDate=?,\n" +
                    "                      modifyBy=?,modifyDate=? where id = ?";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword()
                    , user.getGender(), user.getBirthday(),user.getPhone(), user.getAddress(), user.getUserRole(), user.getCreatedBy(),
                    user.getCreationDate(), user.getModifyBy(), user.getModifyDate(),user.getId()};
            updateRows = BaseDao.execute(connection, pstm, sql, params, updateRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return updateRows;
    }

    public User getUserById(Connection connection, String id) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if(connection!=null) {
            String sql = "select u.*,r.roleName from smbms_user u,smbms_role r where u.userRole = r.id and u.id = ?";
            Object[] params = {id};
            try {
                rs = BaseDao.execute(connection, pstm, sql, params, rs);
                while(rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                    user.setUserRoleName(rs.getString("roleName"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.closeResources(null,pstm,rs);
            }
        }
        return user;
    }

}
