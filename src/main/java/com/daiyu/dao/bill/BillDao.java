package com.daiyu.dao.bill;
import com.daiyu.pojo.Bill;
import com.daiyu.pojo.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BillDao {
    //查询订单表
    public List<Bill> getBillList(Connection connection,Bill bill) throws SQLException;
    //通过id获取订单表
    public Bill getBillById(Connection connection, int id)throws Exception;
    //通过订单id删除订单信息
    public int deleteBillById(Connection connection,int id) throws SQLException;
    //修改订单信息
    public int modify(Connection connection, Bill bill) throws SQLException;
    //获取供应商列表
    public List<Provider> getProviderList(Connection connection)throws Exception;
    //增加订单信息
    public int add(Connection connection,Bill bill) throws SQLException;
}
