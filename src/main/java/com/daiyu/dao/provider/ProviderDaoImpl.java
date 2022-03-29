package com.daiyu.dao.provider;
import com.daiyu.dao.BaseDao;
import com.daiyu.pojo.Provider;
import com.mysql.jdbc.StringUtils;
import com.sun.javafx.runtime.async.BackgroundExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao{
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception {
        PreparedStatement pstm = null;
        List<Provider> providerList = new ArrayList<Provider>();
        ResultSet rs = null;
        Provider provider = null;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> list = new ArrayList<Object>();
            sql.append("select * from smbms_provider where 1=1");
            if(!StringUtils.isNullOrEmpty(proName)){
                sql.append(" and proName like ?");
                list.add("%"+proName+"%");
            }
            if(!StringUtils.isNullOrEmpty(proCode)){
                sql.append(" and proCode like ?");
                list.add("%"+proCode+"%");
            }
//            Object[] params = {proName,proCode}; //我去，这里写错！！！！！！引以为戒
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstm, sql.toString(), params, rs);
            while(rs.next()){
                provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreatedBy(rs.getInt("createdBy"));
                provider.setCreationDate(rs.getDate("creationDate"));
                provider.setModifyBy(rs.getInt("modifyBy"));
                provider.setModifyDate(rs.getDate("modifyDate"));
                providerList.add(provider);
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return providerList;
    }

    public Provider getProviderById(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Provider provider = null;
        if(connection!=null) {
            String sql = "select * from smbms_provider where id = ?";
            Object[] params = {id};
            rs = BaseDao.execute(connection, pstm, sql, params, rs);
            while(rs.next()){
                provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setModifyBy(rs.getInt("modifyBy"));
                provider.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return provider;
    }

    public int delProviderById(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        int delRows = 0;
        if(connection!=null){
            String sql = "delete from smbms_provider where id = ?";
            Object[] params = {id};
            delRows = BaseDao.execute(connection, pstm, sql, params, delRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return delRows;
    }

    public int updateProviderById(Connection connection, Provider provider) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(connection!=null) {
            String sql = "update smbms_provider set proCode = ?,proName = ?,proDesc = ?,\n" +
                    "                          proContact = ?,proPhone = ?,proAddress = ?,\n" +
                    "                          proFax = ?,\n" +
                    "                          modifyDate = ?,modifyBy = ?\n" +
                    "                            where id = ?;";
            Object[] params = {provider.getProCode(),provider.getProName(),provider.getProDesc(),
                                provider.getProContact(),provider.getProPhone(),provider.getProAddress(),
                                provider.getProFax(),
                                provider.getModifyDate(),provider.getModifyBy(),provider.getId()};//这里忘记写provider.getId()了！！！！！
                                //上面那一句 provider.getModifyDate(),provider.getModifyBy()位置错了
            updateRows = BaseDao.execute(connection, pstm, sql, params, updateRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return updateRows;
    }

    public Provider providerExistById(Connection connection, String proCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Provider provider = null;
        if(connection!=null) {
            String sql = "select * from smbms_provider where proCode = ?";
            Object[] params = {proCode};
            rs = BaseDao.execute(connection, pstm, sql, params, rs);
            while(rs.next()){
               provider = new Provider();
               provider.setId(rs.getInt("id"));
               provider.setProCode(rs.getString("proCode"));
               provider.setProName(rs.getString("proName"));
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return provider;
    }

    public int add(Connection connection, Provider provider) throws SQLException {
        PreparedStatement pstm = null;
        int addRows = 0;
        if(connection!=null) {
            String sql = "insert into smbms_provider(id, proCode, proName, proDesc, proContact, proPhone, proAddress, proFax, createdBy, creationDate, modifyDate, modifyBy)\n" +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?);";
            Object[] params = {provider.getId(),provider.getProCode()
                    ,provider.getProName(),provider.getProDesc()
                    ,provider.getProContact(),provider.getProPhone()
                    ,provider.getProAddress(),provider.getProFax()
                    ,provider.getCreatedBy(),provider.getCreationDate()
                    ,provider.getModifyDate(),provider.getModifyBy()};
            addRows = BaseDao.execute(connection, pstm, sql, params, addRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return addRows;
    }
}
