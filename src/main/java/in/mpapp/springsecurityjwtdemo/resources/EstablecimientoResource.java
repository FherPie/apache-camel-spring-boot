package in.mpapp.springsecurityjwtdemo.resources;


import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.mpapp.springsecurityjwtdemo.dtos.UserPrincipal;
import in.mpapp.springsecurityjwtdemo.facades.IAuthenticationFacade;


@Component
public class EstablecimientoResource extends RouteBuilder {
	
	@Value("${server.port}")
	private String portPortal;
	
	@Value("${ownKey.portalCrmDir}")
	private String portalCrmDir;
	
    @Autowired
    private IAuthenticationFacade authenticationFacade;
	
	public void configure() throws Exception {
		
        JacksonDataFormat df = new JacksonDataFormat();
		restConfiguration().component("servlet").host("localhost").port(portPortal).contextPath("").bindingMode(RestBindingMode.off);
		
//		rest("").get("/listarEstablishment").to("direct:gellalllistarEstablishment")
//		.bindingMode(RestBindingMode.json).produces("application/json");
//		from("direct:gellalllistarEstablishment").routeId("getlistarEstablishment")
//		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
//        .setHeader("Accept",constant("application/json"))
//		.to(portalCrmDir+"/api/listarEstablishment?bridgeEndpoint=true&throwExceptionOnFailure=false")
//		.process(new MyProcessorObjectAfter()).removeHeaders("*");
//		
		rest("").post("/establishmentuser").consumes("multipart/form-data").to("direct:postguardarEstablishment").produces("multipart/form-data");
		from("direct:postguardarEstablishment").routeId("guardarEstablishment")
		.log("${body}")
        .process(new MyProcessorObjectBefore())
		.setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader("Accept",constant("multipart/form-data"))
        .marshal(df)
		.to(portalCrmDir+"/api/establishmentuser?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter()).removeHeader("Access-Control-Allow-Origin");
		
		
		
		rest("").get("/establishmentuser").to("direct:getByIdEstablishment")
		.consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		
		from("direct:getByIdEstablishment").routeId("getByIdEstablishment")
		.setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("Accept",constant("application/json"))
        .process(new putUserId())
        .log("${header.userId}")
		.to(portalCrmDir+"/api/establishmentuser?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.unmarshal(df).removeHeaders("*");
		
		
		rest("").put("/establishment").to("direct:putactualizarEstablishment")
		.consumes("application/json").bindingMode(RestBindingMode.json).produces("application/json");
		from("direct:putactualizarEstablishment").routeId("actualizarEstablishment")
        .process(new MyProcessorObjectBefore())
		.setHeader(Exchange.HTTP_METHOD, simple("PUT"))
        .setHeader("Accept",constant("application/json"))
        .marshal(df)
		.to(portalCrmDir+"/api/establishment?bridgeEndpoint=true&throwExceptionOnFailure=false")
		.process(new MyProcessorObjectAfter())
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
	
	
	public class putUserId implements Processor {
	    public void process(Exchange exchange) throws Exception {
	        Authentication authentication = authenticationFacade.getAuthentication();
	        Long userLogged=((UserPrincipal) authentication.getPrincipal()).getUserDTO().getId();
   		   exchange.getIn().setHeader("userId", userLogged);
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

