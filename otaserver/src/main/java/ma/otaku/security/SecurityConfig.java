package ma.otaku.security;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
 

public class SecurityConfig extends ResourceConfig {
    public SecurityConfig() {
        register(RolesAllowedDynamicFeature.class);
    }
}