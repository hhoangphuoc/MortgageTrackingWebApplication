package launch;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class MyResourceConfig extends ResourceConfig {
    public MyResourceConfig() {
        register(MultiPartFeature.class);
        register(new ResourceBinder());
        packages(true, "servlets");
    }
}
