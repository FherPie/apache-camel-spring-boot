package in.mpapp.springsecurityjwtdemo.controllers;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ApplicationResource extends RouteBuilder {

	public void configure() throws Exception {
		// TODO Auto-generated method stub
		restConfiguration().component("servlet").host("localhost").port(8092).contextPath("")
	    .bindingMode(RestBindingMode.auto);
		
		rest("").get("/hello").produces(MediaType.APPLICATION_JSON_VALUE).to("direct:start");
		
		from("direct:start").routeId("someRouteGet").setBody(constant("Welcome to java techie"));

		//.setBody(constant("Welcome to java techie")).endRest();
	}

}
