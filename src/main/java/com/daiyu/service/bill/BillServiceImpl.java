package com.daiyu.service.bill;
import com.daiyu.dao.BaseDao;
import com.daiyu.dao.bill.BillDao;
import com.daiyu.dao.bill.BillDaoImpl;
import com.daiyu.pojo.Bill;
import com.daiyu.pojo.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements BillService{
    BillDao billDao = new BillDaoImpl();
    public List<Bill> getBillList(Bill bill) throws Exception {
        Connection connection = BaseDao.getConnection();
        List<Bill> billList = billDao.getBillList(connection, bill);
        BaseDao.closeResources(connection,null,null);
        return billList;
    }

    public Bill getBillByID(int id) throws Exception {
        Connection connection = BaseDao.getConnection();
        Bill billById = billDao.getBillById(connection, id);
        BaseDao.closeResources(connection,null,null);
        return billById;
    }

    public boolean deleteBillById(int id) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int i = billDao.deleteBillById(connection,id);
        boolean flag = false;
        if(i>0) {
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

    public boolean modify(Bill bill) throws SQLException {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        int modify = billDao.modify(connection, bill);
        if(modify>0){
             flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

    public List<Provider> getProviderList() throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<Provider> providerList = new ArrayList<Provider>();
        try {
            providerList = billDao.getProviderList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection,null,null);
        }
        return providerList;
    }

    public boolean add(Bill bill) throws SQLException {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        int add = billDao.add(connection, bill);
        if(add>0){
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }
}
