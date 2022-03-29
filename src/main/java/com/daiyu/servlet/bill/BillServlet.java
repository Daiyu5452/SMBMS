package com.daiyu.servlet.bill;
import com.alibaba.fastjson.JSONArray;
import com.daiyu.pojo.Bill;
import com.daiyu.pojo.Provider;
import com.daiyu.pojo.User;
import com.daiyu.service.bill.BillService;
import com.daiyu.service.bill.BillServiceImpl;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("query")) {
            try {
                this.query(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(method != null && method.equals("view")){
            try {
                this.getBillById(req,resp,"billview.jsp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(method != null && method.equals("delbill")){
            try {
                this.deleteBillById(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method !=null && method.equals("modify")){//先通过这个方法,在修改页面获得相应的字段 1
            try {
                this.getBillById(req,resp,"billmodify.jsp");
            } catch (Exception e) {                              //1和2通过bill.providerId = provider.id连接，所以2不能没有id这个字段
                e.printStackTrace();
            }
        }else if(method !=null && method.equals("getproviderlist")){//获得修改页面的供应商字段 2
            try {
                this.getProviderList(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(method !=null && method.equals("modifysave")){
            try {
                this.modify(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method !=null && method.equals("add")){
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

    //订单列表展示
    private void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("","");
        req.setAttribute("providerList", providerList);

        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");


        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            bill.setProductName("");
        } else {
            bill.setProductName(queryProductName);
        }
        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        } else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        BillServiceImpl billService = new BillServiceImpl();
        List<Bill> billList = billService.getBillList(bill);

        req.setAttribute("billList", billList);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);

        req.getRequestDispatcher("billlist.jsp").forward(req, resp);


        //    //从前端获取参数
//    String queryProductName = req.getParameter("queryProductName");
//    String queryProviderId = req.getParameter("queryProviderId");
//    String queryIsPayment = req.getParameter("queryIsPayment");
////判断条件
//        if(StringUtils.isNullOrEmpty(queryProductName)){
//                queryProductName = "";
//                }
//                List<Bill> billList = new ArrayList<Bill>();
//        BillService billService = new BillServiceImpl();
//        Bill bill = new Bill();
//        if(StringUtils.isNullOrEmpty(queryIsPayment)){
//        bill.setIsPayment(0);
//        }else{
//        bill.setIsPayment(Integer.parseInt(queryIsPayment));
//        }
//
//        if(StringUtils.isNullOrEmpty(queryProviderId)){
//        bill.setProviderId(0);
//        }else{
//        bill.setProviderId(Integer.parseInt(queryProviderId));
//        }
//
//        bill.setProductName(queryProductName);
//        billList = billService.getBillList(bill);
//        //让数据在前端显示
//        req.setAttribute("billList", billList);
//        req.setAttribute("queryProductName", queryProductName);
//        req.setAttribute("queryProviderId", queryProviderId);
//        req.setAttribute("queryIsPayment", queryIsPayment);
//        //最后重定向到这个页面
//        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    //通过id获取订单
    private void getBillById(HttpServletRequest req, HttpServletResponse resp,String url) throws Exception {
        String id = req.getParameter("billid");
        if(!StringUtils.isNullOrEmpty(id)){
            BillServiceImpl billService = new BillServiceImpl();
            Bill billByID = billService.getBillByID(Integer.parseInt(id));
            req.setAttribute("bill",billByID);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    //通过id删除订单信息
    private void deleteBillById(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String id = req.getParameter("billid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            BillService billService = new BillServiceImpl();
            boolean flag = billService.deleteBillById(Integer.parseInt(id));
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //修改订单信息
    private void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //从前端获取参数
        String id = req.getParameter("id");//我就加了这个就可以了？？？？
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");
        //将对应的参数封装到Bill类型的对象中
        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));//我就加了这个就可以了？？？？
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount));
        bill.setTotalPrice(new BigDecimal(totalPrice));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        //调用service层方法
        BillServiceImpl billService = new BillServiceImpl();
        boolean flag = billService.modify(bill);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billmodify.jsp").forward(req,resp);
        }
    }

    //获取供应商列表
    private void getProviderList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //调用service层
        List<Provider> providerList = new ArrayList<Provider>();
        BillServiceImpl billService = new BillServiceImpl();
        try {
            providerList = billService.getProviderList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(providerList));
        writer.flush();
        writer.close();
    }

    //添加订单信息
    private void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //从前端获取参数
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        //将参数封装到Bill类型的对象中
        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount));
        bill.setTotalPrice(new BigDecimal(totalPrice));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        //调用service层方法
        BillServiceImpl billService = new BillServiceImpl();
        boolean add = billService.add(bill);
        if(add){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billadd.jsp").forward(req,resp);
        }
    }
}

