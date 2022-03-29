package com.daiyu.service.role;
import com.daiyu.dao.BaseDao;
import com.daiyu.dao.role.RoleDao;
import com.daiyu.dao.role.RoleDaoImpl;
import com.daiyu.pojo.Role;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    RoleDao roleDao = new RoleDaoImpl();
    public List<Role> roleList() throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<Role> roleList = roleDao.getRoleList(connection);
        BaseDao.closeResources(connection,null,null);
        return roleList;
    }
}
