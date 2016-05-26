package servlets;

import javax.servlet.http.HttpServlet;

/*
 * Created by kvukolov on 18.02.16.
 */
public class ServletDefinition {
    private HttpServlet servlet;
    private String url;

    public ServletDefinition(HttpServlet servlet, String url) {
        this.servlet = servlet;
        this.url = url;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public String getUrl() {
        return url;
    }

    public void setServlet(HttpServlet servlet) {
        this.servlet = servlet;
    }
}
