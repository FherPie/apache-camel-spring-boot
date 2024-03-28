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
public class DetalleResource extends RouteBuilder {
	
	
	@Value("${server.port}")
	private String portPortal;
	

	public void configure() throws Exception {
		
        JacksonDataFormat df = new JacksonDataFormat();
		// TODO Auto-generated method stub
		restConfiguration().component("servlet").host("localhost").port(portPortal).contextPath("")
	    .bindingMode(RestBindingMode.auto);

		rest("").get("/detalle").to("direct:gellalldetails").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:gellalldetails").routeId("getDetails")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
		.to("http://localhost:8080/api/detalle?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessor()).removeHeaders("*");
		
		rest("").post("/detalle").to("direct:postdetails").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:postdetails").routeId("postdetails")
        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to("http://localhost:8080/api/detalle?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");
		
		
		
		rest("").get("/detalle/{id}").to("direct:gettdetailsid").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:gettdetailsid").routeId("gettdetailsid")
//        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
//        .marshal(df)
		.to("http://localhost:8080/api?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");
		
		
		rest("").put("/detalle").to("direct:putdetails").consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:putdetails").routeId("putdetails")
        .process(new MyProcessorObject()) //Move directoryLocation property to an exchange property  named WriteTargetDirectory
		.setHeader(Exchange.HTTP_METHOD, simple("PUT"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to("http://localhost:8080/api/detalle?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");

		
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

