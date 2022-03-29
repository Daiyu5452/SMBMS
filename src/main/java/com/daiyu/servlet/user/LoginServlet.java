package com.daiyu.servlet.user;
import com.daiyu.pojo.User;
import com.daiyu.service.user.UserServiceImpl;
import com.daiyu.util.Constants;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        UserServiceImpl userService = new UserServiceImpl();
        User user = null;
        try {
            user = userService.login(userCode,userPassword);
            if(user.getUserCode().equals(userCode) && user.getUserPassword().equals(userPassword)){//查有此人
                req.getSession().setAttribute(Constants.USER_SESSION,user);
                resp.sendRedirect("jsp/frame.jsp");
            }else{//查无此人
                req.getSession().setAttribute("error","对不起，您的用户名或密码有误！");
                req.getRequestDispatcher("login.jsp").forward(req,resp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
