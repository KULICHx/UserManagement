package web;

import model.User;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init() {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/update":
                    this.updateUser(request, response);
                    return;
                case "/new":
                    this.showNewForm(request, response);
                    return;
                case "/edit":
                    this.showEditForm(request, response);
                    return;
                case "/delete":
                    this.deleteUser(request, response);
                    return;
                case "/insert":
                    this.insertUser(request, response);
                    return;
            }

            this.listUser(request, response);
        } catch (SQLException var5) {
            throw new ServletException(var5);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<User> listUser = this.userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = this.userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User newUser = new User(name, email, country);
        this.userDAO.insertUser(newUser);
        response.sendRedirect("list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User book = new User(id, name, email, country);
        this.userDAO.updateUser(book);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        this.userDAO.deleteUser(id);
        response.sendRedirect("list");
    }
}
