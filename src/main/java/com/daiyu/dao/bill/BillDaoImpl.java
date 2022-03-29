package com.daiyu.dao.bill;
import com.daiyu.dao.BaseDao;
import com.daiyu.pojo.Bill;
import com.daiyu.pojo.Provider;
import com.mysql.jdbc.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao{
    public List<Bill> getBillList(Connection connection, Bill bill) throws SQLException {
        List<Bill> billList = new ArrayList<Bill>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(connection!=null){
            StringBuilder sql = new StringBuilder();
            sql.append("select b.*,p.proName from smbms_bill b,smbms_provider p where b.providerId = p.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(bill.getProductName())){//判断产品名是否为空
                sql.append(" and b.productName like ?");
                list.add(bill.getProductName());
            }
            if(bill.getProviderId()>0){//判断是否有选择供应商
                sql.append(" and b.providerId = ?");
                list.add(bill.getProviderId());
            }
            if(bill.getIsPayment()>0) {//判断是否付款
                sql.append(" and b.isPayment = ?");
                list.add(bill.getIsPayment());
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstm, sql.toString(), params, rs);
            while(rs.next()){
                Bill _bill = new Bill();
                _bill.setId(rs.getInt("id"));
                _bill.setBillCode(rs.getString("billCode"));
                _bill.setProductName(rs.getString("productName"));
                _bill.setProductDesc(rs.getString("productDesc"));
                _bill.setProductCount(rs.getBigDecimal("productCount"));
                _bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                _bill.setIsPayment(rs.getInt("isPayment"));
                _bill.setCreatedBy(rs.getInt("createdBy"));
                _bill.setCreationDate(rs.getDate("creationDate"));
                _bill.setModifyBy(rs.getInt("modifyBy"));
                _bill.setModifyDate(rs.getDate("modifyDate"));
                _bill.setProviderId(rs.getInt("providerId"));
                _bill.setProviderName(rs.getString("proName"));
                billList.add(_bill);
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return billList;
    }

    public Bill getBillById(Connection connection, int id) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Bill bill = null;
        if(connection!=null) {
            StringBuilder sql = new StringBuilder();
            sql.append("select b.*,p.proName from smbms_bill b, smbms_provider p where b.providerId = p.id and b.id = ?");
            Object[] params = {id};
            rs = BaseDao.execute(connection, pstm, sql.toString(), params, rs);
            while (rs.next()) {
                bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setCreatedBy(rs.getInt("createdBy"));
                bill.setCreationDate(rs.getDate("creationDate"));
                bill.setModifyBy(rs.getInt("modifyBy"));
                bill.setModifyDate(rs.getDate("modifyDate"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("proName"));
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return bill;
    }

    public int deleteBillById(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        int delRows = 0;
        if(connection!=null) {
            String sql = "delete from smbms_bill where id=?";
            Object[] params = {id};
            delRows = BaseDao.execute(connection, pstm, sql, params, delRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return delRows;
    }

    public int modify(Connection connection, Bill bill) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(connection!=null){
            String sql = "update smbms_bill set billCode = ?,productName = ?,\n" +
                    "                      productUnit = ?,productCount = ?,\n" +
                    "                      totalPrice = ?,providerId = ?,\n" +
                    "                      modifyBy = ?,modifyDate = ? where id = ?";
            Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductUnit(),
                                bill.getProductCount(),bill.getTotalPrice(),bill.getProviderId(),
                                bill.getModifyBy(),bill.getModifyDate(),bill.getId()};
            updateRows = BaseDao.execute(connection, pstm, sql, params, updateRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return updateRows;
    }

    public List<Provider> getProviderList(Connection connection) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Provider> providerList = new ArrayList<Provider>();
        if(connection!=null) {
            String sql = "select * from smbms_provider";
            Object[] params = {};
            rs = BaseDao.execute(connection, pstm, sql, params, rs);
            while(rs.next()){
                Provider provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProName(rs.getString("proName"));
                providerList.add(provider);
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return providerList;
    }

    public int add(Connection connection, Bill bill) throws SQLException {
        PreparedStatement pstm = null;
        int addRows = 0;
        if (connection != null) {
            String sql = "insert into smbms_bill(id, billCode, productName, productDesc, productUnit, productCount, totalPrice, isPayment, createdBy, creationDate, modifyBy, modifyDate, providerId)\n" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?);";
            Object[] params = {bill.getId(),bill.getBillCode()
                    ,bill.getProductName(),bill.getProductDesc()
                    ,bill.getProductUnit(),bill.getProductCount()
                    ,bill.getTotalPrice(),bill.getIsPayment()
                    ,bill.getCreatedBy(),bill.getCreationDate()
                    ,bill.getModifyBy(),bill.getModifyDate()
                    ,bill.getProviderId()};
            addRows = BaseDao.execute(connection, pstm, sql, params, addRows);
            BaseDao.closeResources(null,pstm,null);
        }
        return addRows;
    }
}
