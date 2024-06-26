package in.mpapp.springsecurityjwtdemo.resources;


import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class MaestroResource extends RouteBuilder {
	
	
	@Value("${server.port}")
	private String portPortal;
	

	public void configure() throws Exception {
		
        JacksonDataFormat df = new JacksonDataFormat();
		// TODO Auto-generated method stub
		restConfiguration().component("servlet").host("localhost").port(portPortal).contextPath("")
	    .bindingMode(RestBindingMode.auto);
		
//		rest("").post("/authenticate2").consumes(MediaType.APPLICATION_JSON_VALUE).type(AuthenticationRequest.class)
//		.produces(MediaType.APPLICATION_JSON_VALUE).outType(ResponseEntity.class).to("direct:start");
//		
//		from("direct:start").routeId("someRouteGet").setBody(constant("Welcome to java techie"));
		
		//Maestros
		rest("").get("/maestro").to("direct:gellallmasters").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:gellallmasters").routeId("getMasters")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
		.to("http://localhost:8080/api/maestro?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessor()).removeHeaders("*");
		
		rest("").post("/maestro").to("direct:postmasters").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:postmasters").routeId("postmasters")
        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to("http://localhost:8080/api/maestro?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");
		
		
		
		rest("").get("/maestro/{id}").to("direct:gettmastersid").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:gettmastersid").routeId("gettmastersid")
//        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
//        .marshal(df)
		.to("http://localhost:8080/api?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");
		
		
		rest("").put("/maestro").to("direct:putmasters").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:putmasters").routeId("putmasters")
        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("PUT"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to("http://localhost:8080/api/maestro?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");

		
		
		//Maestros
		//.setBody(constant("Welcome to java techie")).endRest();
	}

	public class MyProcessor implements Processor {

	    public void process(Exchange exchange) throws Exception {
	    	 String author = exchange.getIn().getBody(String.class);
	    	 JsonParser parser = JsonParserFactory.getJsonParser();
	    	 //Map<String, Object> jsonMap = parser.parseMap(author);
	    	 List<Object> jsonMap = parser.parseList(author);
	    	 exchange.getIn().setBody(jsonMap);
	        //System.out.println(exchange.getIn().getBody(String.class));
	    }
	}
	
	
	public class MyProcessorObject implements Processor {

	    public void process(Exchange exchange) throws Exception {
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	String string = objectMapper.writeValueAsString(exchange.getIn().getBody());
	    	// String author = exchange.getIn().getBody(String.class);
	    	 //JsonParser parser = JsonParserFactory.getJsonParser();
	    	Map jsonMap= objectMapper.readValue(string, Map.class);
	    	//Map<String, Object> jsonMap = parser.parseMap(author);
	    	 exchange.getIn().setBody(jsonMap);
	        //System.out.println(exchange.getIn().getBody(String.class));
	    }
	}
	
	

}

