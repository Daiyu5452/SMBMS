package com.daiyu.servlet.provider;
import com.alibaba.fastjson.JSONArray;
import com.daiyu.pojo.Provider;
import com.daiyu.pojo.User;
import com.daiyu.service.provider.ProviderService;
import com.daiyu.service.provider.ProviderServiceImpl;
import com.daiyu.util.Constants;
import com.mysql.jdbc.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null && method.equals("query")){
            try {
                this.query(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(method!=null && method.equals("view")){
            try {
                this.getProviderById(req,resp,"providerview.jsp");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null && method.equals("delprovider")){
            try {
                this.delProviderById(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null && method.equals("modify")){
            try {
                this.getProviderById(req,resp,"providermodify.jsp");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null && method.equals("modifysave")){
            try {
                this.updateProviderById(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null && method.equals("add")){
            try {
                this.add(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //从前端获取参数
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");
        //判断条件
        if(StringUtils.isNullOrEmpty(queryProName)){//如果为空或不存在设置为""
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){//如果为空或不存在设置为""
            queryProCode = "";
        }
        //调用service层方法
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        //providerService.getProviderList(queryProName, queryProCode);引以为戒，没有为这个赋值
        providerList = providerService.getProviderList(queryProName, queryProCode);
        //返回前端数据
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("queryProCode", queryProCode);
        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }

    private void getProviderById(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException, SQLException {
        String id = req.getParameter("proid");
        System.out.println(req.getParameter("proid"));
        if(!StringUtils.isNullOrEmpty(id)){//如果不为空
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider providerById = providerService.getProviderById(Integer.parseInt(id));
            req.setAttribute("provider",providerById);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    private void delProviderById(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String id = req.getParameter("proid");
        HashMap<String,String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            boolean flag = providerService.delProviderById(Integer.parseInt(id));
            if(flag){//删除成功
                resultMap.put("delResult","true");
            }else{//删除失败
                resultMap.put("delResult","false");
            }
        }else{
            resultMap.put("delResult","notexist");
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    private void updateProviderById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
                //从前端获取参数
        String id = req.getParameter("id");
        System.out.println(req.getParameter("id"));
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String address = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        //将参数都封装到一个Provider类型的对象中
        Provider provider = new Provider();
        provider.setId(Integer.parseInt(id));
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(address);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        //调用service层方法
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean b = providerService.updateProviderById(provider);
        if(b){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");//我之前写成method=view
        }else{
            req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
        }
    }

    //private void providerExistById(HttpServletRequest req, HttpServletResponse resp){
        //从前端获取供应商编码参数
        //验证供应商编码，如果为空就提示存在
        //如果不为空，就调用service层方法判断这个供应商编码是否存在
//    } //js里面没有写验证的功能就不写了
    private void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //从前端获取参数
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        //将参数封装到Provider类型的对象中
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        //调用service层方法
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean add = providerService.add(provider);
        if(add){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("provideradd.jsp").forward(req,resp);
        }
    }
}
