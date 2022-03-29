package com.daiyu.service.provider;
import com.daiyu.dao.BaseDao;
import com.daiyu.dao.provider.ProviderDao;
import com.daiyu.dao.provider.ProviderDaoImpl;
import com.daiyu.pojo.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    ProviderDao providerDao = new ProviderDaoImpl();
    public List<Provider> getProviderList(String proName, String proCode) throws Exception {
        Connection connection = BaseDao.getConnection();
        List<Provider> providerList = providerDao.getProviderList(connection, proName, proCode);
        BaseDao.closeResources(connection,null,null);
        return providerList;
    }

    public Provider getProviderById(int id) throws SQLException {
        Connection connection = BaseDao.getConnection();
        Provider providerById = providerDao.getProviderById(connection, id);
        BaseDao.closeResources(connection,null,null);
        return providerById;
    }

    public boolean delProviderById(int id) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int i = providerDao.delProviderById(connection, id);
        boolean flag = false;
        if(i>0){
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

    public boolean updateProviderById(Provider provider) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int i = providerDao.updateProviderById(connection, provider);
        boolean flag = false;
        if(i>0){
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

    public Provider providerExistById(String proCode) {
        Connection connection = null;
        Provider provider = null;
        try {
            connection = BaseDao.getConnection();
            provider = providerDao.providerExistById(connection, proCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally{
            BaseDao.closeResources(connection,null,null);
        }
        return provider;
    }

    public boolean add(Provider provider) throws SQLException {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        int add = providerDao.add(connection, provider);
        if(add>0){
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

}
