package launch;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.reflections.Reflections;

public class ResourceBinder extends AbstractBinder {

    @Override
    protected void configure() {
        var reflections = new Reflections("");
        var resources = reflections.getTypesAnnotatedWith(Resource.class);
        var singletons = reflections.getTypesAnnotatedWith(Singleton.class);
        for (var resource: resources){
            bind(resource).to(resource);
        }
        for (var resource: singletons){
            bind(resource).to(resource);
        }
    }
    
}
