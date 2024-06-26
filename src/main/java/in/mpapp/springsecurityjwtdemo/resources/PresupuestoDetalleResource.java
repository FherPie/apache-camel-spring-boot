package in.mpapp.springsecurityjwtdemo.resources;


import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
public class PresupuestoDetalleResource extends RouteBuilder {
	
	@Value("${server.port}")
	private String portPortal;
	
	@Value("${ownKey.portalCrmDir}")
	private String portalCrmDir;
	
	
	public void configure() throws Exception {
		
        JacksonDataFormat df = new JacksonDataFormat();
		restConfiguration().component("servlet").host("localhost").port(portPortal).contextPath("")
	    .bindingMode(RestBindingMode.auto);
		

		
		rest("").post("/addDetalle").to("direct:postaddDetalle").consumes("application/json")
		.bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:postaddDetalle").routeId("addDetalle")
        .process(new MyProcessorObjectBefore())
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to(portalCrmDir+"/api/addDetalle?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter())	.removeHeader("Access-Control-Allow-Origin");
		
	}

	public class MyProcessorList implements Processor {

	    public void process(Exchange exchange) throws Exception {
	    	 String author = exchange.getIn().getBody(String.class);
	    	 JsonParser parser = JsonParserFactory.getJsonParser();
	    	 List<Object> jsonMap = parser.parseList(author);
	    	 exchange.getIn().setBody(jsonMap);
	    }
	}
	
	
	
	public class MyProcessorObjectBefore implements Processor {

	    public void process(Exchange exchange) throws Exception {
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	String string = objectMapper.writeValueAsString(exchange.getIn().getBody());
	    	Map jsonMap= objectMapper.readValue(string, Map.class);
	    	 exchange.getIn().setBody(jsonMap);
	    }
	}
	
	
	public class MyProcessorObjectAfter implements Processor {

	    public void process(Exchange exchange) throws Exception {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        Message message=exchange.getIn();
	    	String string = (String) message.getBody(String.class);
	    	Map jsonMap= objectMapper.readValue(string, Map.class);
	    	 exchange.getIn().setBody(jsonMap);
	    	 if((Integer)jsonMap.get("codigoRespuestaValue")!=200) {
	    		 exchange.getIn().setHeader("Exchange.HTTP_RESPONSE_CODE", (Integer)jsonMap.get("codigoRespuestaValue"));
	    	 }
	    }
	}
	
	

}

