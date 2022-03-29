package com.daiyu.service.bill;
import com.daiyu.pojo.Bill;
import com.daiyu.pojo.Provider;
import java.sql.SQLException;
import java.util.List;

public interface BillService {
    //获取订单列表
    public List<Bill> getBillList(Bill bill) throws Exception;
    //通过id查询订单
    public Bill getBillByID(int id) throws Exception;
    //通过订单id删除订单信息
    public boolean deleteBillById(int id) throws SQLException;
    //修改订单信息
    public boolean modify(Bill bill) throws SQLException;
    //获取供应商列表
    public List<Provider> getProviderList() throws SQLException;
    //增加订单信息
    public boolean add(Bill bill) throws SQLException;
}
