package com.daiyu.servlet.user;
import com.alibaba.fastjson.JSONArray;
import com.daiyu.pojo.Role;
import com.daiyu.pojo.User;
import com.daiyu.service.role.RoleServiceImpl;
import com.daiyu.service.user.UserServiceImpl;
import com.daiyu.util.Constants;
import com.daiyu.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method!=null && method.equals("add")){
            try {
                this.add(req,resp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(method!=null && method.equals("getrolelist")){
            try {
                this.getRoleList(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null && method.equals("view")){
            this.getUserById(req,resp,"userview.jsp");
        }else if(method!=null && method.equals("modify")){
            this.getUserById(req,resp,"usermodify.jsp");
        }else if(method!=null && method.equals("modifyexe")){
            this.modify(req,resp);
        }else if(method!=null && method.equals("deluser")){
            this.delUser(req,resp);
        }else if(method!=null && method.equals("ucexist")){
            this.userCodeExist(req,resp);
        }else if(method!=null && method.equals("savepwd")){
            this.updatePwd(req,resp);
        }else if(method!=null && method.equals("pwdmodify")){
            this.PwdModify(req,resp);
        }else if(method!=null && method.equals("query")){
            try {
                this.query(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp){
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        String rnewpassword = req.getParameter("rnewpassword");
        boolean flag = false;
        if(user!=null && !StringUtils.isNullOrEmpty(rnewpassword)){
            UserServiceImpl userService = new UserServiceImpl();
            flag = userService.updatePwd(user.getId(), rnewpassword);
            if(flag){
                req.getSession().setAttribute("message","密码修改成功！");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.getSession().setAttribute("message","密码修改失败！");
            }
        }else{
            req.getSession().setAttribute("message","新密码有问题！");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码
    private void PwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        //万能的map
        Map<String,String> resultSet = new HashMap<String, String>();
        if(attribute==null){//session过期
            resultSet.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultSet.put("result","error");
        }else{
            String userPassword = ((User) attribute).getUserPassword();
            if(userPassword.equals(oldpassword)){
                resultSet.put("result","true");
            }else{
                resultSet.put("result","false");
            }
        }
        //{key,value}JSON类似是这种
        //集合转换为JSON格式，这样js才能用
        resp.setContentType("application/json");
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultSet));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //分页展示页面
    private void query(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //从前端获取参数
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");//分页用,说实话不是很懂
        int queryUserRole = 0;
        if(queryUserName==null){
            queryUserName="";
        }
        if(temp!=null && !temp.equals("")){//判断角色下拉框是否为空
            queryUserRole = Integer.parseInt(temp);//给角色下拉框复制，给查询赋值！0,1,2,3
        }
        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;
        //第一次走这个请求一定是第一页，页面大小也固定
        int pageSize = 5;    //可以写进配置文件
        int currentPageNo = 1; //不可能一直都是1，你一旦进入下一页,就变了
        if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);//比如第一页用户列表是1到5,第二页用户列表是6到10,这里的currentPageNo就是1和6
        }
        //查询用户的总数(分页：存在上一页，下一页的情况)
        int totalCount = userService.userCount(queryUserName,queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = (int)(totalCount/pageSize)+1;
        //控制首页和尾页
        //如果页面小于1了，就显示第一页的东西
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){//当前页面大于最后一页
            currentPageNo = totalPageCount;
        }

        //获取用户列表显示
        userList = userService.userList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);
        //角色列表显示
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.roleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);
        //返回前端
        req.getRequestDispatcher("userlist.jsp").forward(req,resp);
    }

    //添加用户
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ParseException, ServletException, IOException {
        //从前端获取参数
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        //把前端获取的参数封装到一个User对象中，其他有必要的也一起封装
        User user = new User();//注意我这边就是忘记实例化，报了500错误
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.valueOf(gender));
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        UserServiceImpl userService = new UserServiceImpl();
        boolean add = userService.add(user);
        if(add){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }

    //获取用户角色列表
    private List<Role> getRoleList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        List<Role> roleList = null;
        RoleServiceImpl roleService = new RoleServiceImpl();
        roleList = roleService.roleList();
        //把roleList集合转换为JSON对象输出
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
        return roleList;
    }

    //判断当前输入的用户编码是否可用
    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userCode = req.getParameter("userCode");
        HashMap<String,String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else{
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if(user!=null){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode","notexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    //根据用户id删除用户信息
    private void delUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("uid");
        UserServiceImpl userService = new UserServiceImpl();
        Integer delId = Integer.parseInt(id);
        //判断是否能删除成功
        HashMap<String,String> resultMap = new HashMap<String, String>();
        if(delId<=0){
            resultMap.put("delResult","notexist");
        }else{
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult","true");
            }else{
                resultMap.put("delResult","false");
            }
        }
        //最后再把集合转化为json对象输出
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    //修改用户信息
    private void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //从前端获取参数
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        //把要修改的都封装到一个User对象中
        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        //调用service层方法
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag = userService.modifyUser(user);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");//跳转用户列表
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
        }
    }

    //通过id得到用户信息
    private void getUserById(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException {
        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user",user);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }
}

