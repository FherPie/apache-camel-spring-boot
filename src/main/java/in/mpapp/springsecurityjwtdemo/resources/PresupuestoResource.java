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
public class PresupuestoResource extends RouteBuilder {
	
	@Value("${server.port}")
	private String portPortal;
	
	@Value("${ownKey.portalCrmDir}")
	private String portalCrmDir;
	
	
	public void configure() throws Exception {
		
        JacksonDataFormat df = new JacksonDataFormat();
		restConfiguration().component("servlet").host("localhost").port(portPortal).contextPath("")
	    .bindingMode(RestBindingMode.auto);
		
		rest("").get("/listarVenta").to("direct:gellalllistarVenta")
		.bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:gellalllistarVenta").routeId("getlistarVenta")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
		.to(portalCrmDir+"/api/listarVenta?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter()).removeHeaders("*");
		
		rest("").post("/guardarVenta").to("direct:postguardarVenta").consumes("application/json")
		.bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:postguardarVenta").routeId("guardarVenta")
        .process(new MyProcessorObjectBefore())
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to(portalCrmDir+"/api/guardarVenta?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter())	.removeHeader("Access-Control-Allow-Origin");
		
		
		
		rest("").get("/getByIdVenta/{id}").to("direct:getByIdVenta")
		.consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:getByIdVenta").routeId("getByIdVenta")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
		.to(portalCrmDir+"/api?bridgeEndpoint=true&throwExceptionOnFailure=false")
		 .unmarshal(df).removeHeaders("*");
		
		
		rest("").put("/actualizarVenta").to("direct:putactualizarVenta")
		.consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:putactualizarVenta").routeId("actualizarVenta")
        .process(new MyProcessorObjectBefore())
		.setHeader(Exchange.HTTP_METHOD, simple("PUT"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to(portalCrmDir+"/api/actualizarVenta?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter())
		.removeHeader("Access-Control-Allow-Origin");

		rest("").delete("/borrarVenta/{id}").to("direct:borrarVenta")
		.consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:borrarVenta").routeId("borrarVenta")
		.setHeader(Exchange.HTTP_METHOD, simple("DELETE"))
        .setHeader("Accept",constant("application/json"))
		.to(portalCrmDir+"/api?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.log("${headers} ${body}")
		.process( new MyProcessorObjectAfter())
		.removeHeader("Access-Control-Allow-Origin");
		
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

