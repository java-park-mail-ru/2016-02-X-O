package servlets;

import account.*;
import account.User;
import database.DBService;
import database.datasets.UserDataset;
import util.Context;
import util.ResponseJson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by kvukolov on 03.06.16.
 */
public class TopUsers extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final DBService dbService = Context.getInstance().get(DBService.class);
        final List<UserDataset> users = dbService.readTopNUsers(10);
        final ResponseJson responseJson = new ResponseJson();
        for (UserDataset user: users)
        {
            final ResponseJson userJSON = new ResponseJson();
            userJSON.put("id", user.getId());
            userJSON.put("login", user.getName());
            userJSON.put("email", user.getEmail());
            userJSON.put("score", user.getScore());
            responseJson.append("topUsers", userJSON);
        }
        resp.getWriter().println(responseJson);
    }
}
