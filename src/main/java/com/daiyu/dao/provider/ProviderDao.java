package com.daiyu.dao.provider;
import com.daiyu.pojo.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    //获得供应商列表
    public List<Provider> getProviderList(Connection connection, String proName, String proCode)throws Exception;

    //通过id获取供应商列表
    public Provider getProviderById(Connection connection,int id) throws SQLException;

    //通过id删除供应商信息
    public int delProviderById(Connection connection,int id) throws SQLException;

    //通过id修改供应商信息
    public int updateProviderById(Connection connection,Provider provider) throws SQLException;

    //验证供应商编码是否存在 没用但就是放这里
    public Provider providerExistById(Connection connection,String proCode) throws SQLException;

    //添加供应商信息
    public int add(Connection connection,Provider provider) throws SQLException;
}
