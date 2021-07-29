package launch;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletRegistration;

import com.sun.tools.javac.Main;

import database.MyPool;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import io.swagger.jaxrs.config.BeanConfig;

// https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/getting-started.html#new-from-archetype
// https://stackoverflow.com/questions/15769415/grizzly-and-servletcontainercontext
public class Grizz {
    static final String apiPath = "/api";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(final URI BASE_URI) {

        final HttpServer server = GrizzlyHttpServerFactory
            .createHttpServer(BASE_URI);
        
        final ServerConfiguration cfg = server.getServerConfiguration();

        final ResourceConfig rc = new MyResourceConfig();
//#region register swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath(apiPath);
        beanConfig.setResourcePackage("servlets");
        beanConfig.setScan(true);

        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        ClassLoader loader = Main.class.getClassLoader();
        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        docsHandler.setFileCacheEnabled(false);

        cfg.addHttpHandler(docsHandler, "/docs/");
//#endregion
//#region register statics server
        var staticHandler = new StaticHttpHandler("target/ROOT");
        staticHandler.setFileCacheEnabled(false);
        cfg.addHttpHandler(staticHandler, "/");
//#endregion
        
//#region deploy servlets in webapp context
        WebappContext context = new WebappContext("TrackTraceApi", apiPath);
        ServletRegistration registration = context
            .addServlet("ServletContainer", new ServletContainer(rc));    
        registration.addMapping("/*");
        context.deploy(server);
//#endregion
        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (doUseTestDb()){
            MyPool.setTesting();
        }

        int port = getPort();
        var uri = URI.create("http://localhost:" + port + "/");
        final HttpServer server = startServer(uri);
        

        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", uri));
        if (doUseTestDb()){
            System.out.println("WARNING: server is running on a test db the db might be reset at any point");
        }
        System.in.read();
        server.shutdown();
    }
    public static int getPort(){
        int port = 8080;
        var portString = System.getProperty("port");
        if (portString != null) {
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return port;
    }

    public static boolean doUseTestDb(){
        return System.getProperty("use-testdb") != null;
    }
}
