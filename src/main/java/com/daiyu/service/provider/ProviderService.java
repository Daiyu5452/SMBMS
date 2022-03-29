package com.daiyu.service.provider;
import com.daiyu.pojo.Provider;
import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    //获取供应商列表
    public List<Provider> getProviderList(String proName, String proCode)throws Exception;
    //通过id查询供应商
    public Provider getProviderById(int id) throws SQLException;
    //通过id删除供应商信息
    public boolean delProviderById(int id) throws SQLException;
    //通过id修改供应商信息
    public boolean updateProviderById(Provider provider) throws SQLException;
    //验证供应商编码是否存在 没用就放这里
    public Provider providerExistById(String proCode);
    //添加供应商信息
    public boolean add(Provider provider) throws SQLException;
}
