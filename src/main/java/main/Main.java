package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import servlets.ServletDefinition;
import servlets.ServletManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.Session;
import servlets.User;
import util.RegexId;
import util.RegexManager;

import javax.servlet.http.LoginRequiredDelete;
import javax.servlet.http.LoginRequiredGet;
import javax.servlet.http.LoginRequiredPost;

public class Main {

    public static void main(String[] args) throws Exception {
        try{
            final int port;
            try
            {
                port = Integer.parseInt(args[0]);
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                usage();
                return;
            }

            RegexManager.getInstance().put(RegexId.LOGIN_REGEX, "\\w{5,20}");
            RegexManager.getInstance().put(RegexId.EMAIL_REGEX, "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$");
            RegexManager.getInstance().put(RegexId.PASSWORD_REGEX, ".{5,20}");
            RegexManager.getInstance().put(RegexId.ID_REGEX, "\\d+");

            ServletManager.getManager().put(new ServletDefinition(
                    new LoginRequiredPost(
                            new LoginRequiredDelete(
                                    new LoginRequiredGet(
                                            new User()
                                    )
                            )
                    ), "/user"));

            ServletManager.getManager().put(new ServletDefinition(
                    new LoginRequiredDelete(
                            new LoginRequiredGet(
                                    new Session()
                            )
                    ), "/session"));

            final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

            for (ServletDefinition definition: ServletManager.getManager())
            {
                contextHandler.addServlet(new ServletHolder(definition.getServlet()), definition.getUrl());
            }

            final ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase("public_html");

            final HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});

            final Server server = new Server(port);
            server.setHandler(handlers);
            server.start();
            server.join();
        }
        catch (ExceptionInInitializerError e)
        {
            error();
        }
    }

    private static void usage()
    {
        System.out.println("Add valid port as parameter");
    }
    private static void error() {
        System.out.println("Data base connect error");
    }
}
