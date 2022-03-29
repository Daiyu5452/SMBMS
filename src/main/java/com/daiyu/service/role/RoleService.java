package com.daiyu.service.role;
import com.daiyu.pojo.Role;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<Role> roleList() throws SQLException;
}
