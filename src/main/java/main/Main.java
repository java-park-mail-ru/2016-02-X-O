package main;


import account.AccountManager;
import account.DataBaseAccountManager;
import database.DBService;
import mechanics.GameMecahnics;
import mechanics.WebSocketService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.hibernate.HibernateException;
import resource.Builder;
import resource.handlers.DBServiceHandler;
import resource.handlers.RegexManagerHandler;
import resource.handlers.ServerConfigHandler;
import resource.handlers.ServletManagerHandler;
import servlets.GameWebSocketServlet;
import servlets.ServletDefinition;
import servlets.ServletManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import util.Context;
import util.RegexManager;

public class Main {

    private static final String REGEX_CONFIG_FILE = "config/regexconf.xml";
    private static final String SERVER_CONFIG_FILE = "config/server.xml";
    private static final String SERVLET_MANAGER_CONFIG_FILE = "config/servletsconf.xml";

    public static void main(String[] args) throws Exception {
        final Context context = Context.getInstance();
        context.add(GameMecahnics.class, new GameMecahnics());
        context.add(WebSocketService.class, new WebSocketService());

        try{
            final DBService dbService = (DBService) new Builder("config/database.xml", new DBServiceHandler()).build();
            context.add(DBService.class, dbService);
        }
        catch (HibernateException e)
        {
            System.out.println("DB connect error");
            System.exit(1);
        }

        context.add(AccountManager.class, new DataBaseAccountManager());

        final RegexManager regexManager = (RegexManager) new Builder(REGEX_CONFIG_FILE, new RegexManagerHandler()).build();
        context.add(RegexManager.class, regexManager);

        final ServerConfig serverConfig = (ServerConfig) new Builder(SERVER_CONFIG_FILE, new ServerConfigHandler()).build();

        final ServletManager servletManager = (ServletManager) new Builder(SERVLET_MANAGER_CONFIG_FILE, new ServletManagerHandler()).build();

        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        for (ServletDefinition definition: servletManager)
        {
            contextHandler.addServlet(new ServletHolder(definition.getServlet()), definition.getUrl());
        }

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});

        final Server server = new Server(serverConfig.getPort());
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
