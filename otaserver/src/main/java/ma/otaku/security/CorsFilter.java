package ma.otaku.security;

import java.io.IOException;

import javax.servlet.annotation.WebFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@WebFilter("/CorsFilter")
public class CorsFilter implements ContainerResponseFilter {

	
	@Override
    public void filter(ContainerRequestContext request,
            ContainerResponseContext response) throws IOException {
		
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        if(request.getMethod().equals("OPTIONS"))
        {
	        response.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
	        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
	        response.setEntity("{}");
		}
    }
}
