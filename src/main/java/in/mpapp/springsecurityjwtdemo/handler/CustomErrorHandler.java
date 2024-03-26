package in.mpapp.springsecurityjwtdemo.handler;



import java.sql.SQLIntegrityConstraintViolationException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import in.mpapp.springsecurityjwtdemo.enums.EnumMessages;
import in.mpapp.springsecurityjwtdemo.models.responses.ResponseGenerico;

import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.transaction.TransactionSystemException;

@RestControllerAdvice
public class CustomErrorHandler {
	
	   @ResponseStatus(HttpStatus.BAD_REQUEST)  
	    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	    public ResponseEntity<?> handleContraintViolationException( SQLIntegrityConstraintViolationException e, WebRequest request) {
	        ResponseGenerico<?> response = new ResponseGenerico<>();
	        response.setCodigoRespuestaName(HttpStatus.BAD_REQUEST.name());
	        response.setCodigoRespuestaValue(HttpStatus.BAD_REQUEST.value());
	        response.setMensaje(EnumMessages.ERROR.name() + ":" + e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	   
	   
	   @ResponseStatus(HttpStatus.BAD_REQUEST)  
	    @ExceptionHandler(DataIntegrityViolationException.class)
	    public ResponseEntity<?> DataIntegrityViolationException( DataIntegrityViolationException e, WebRequest request) {
	        ResponseGenerico<?> response = new ResponseGenerico<>();
	        StringBuilder violaciones= new StringBuilder(); 
	        response.setCodigoRespuestaName(HttpStatus.BAD_REQUEST.name());
	        response.setCodigoRespuestaValue(HttpStatus.BAD_REQUEST.value());
	        response.setMensaje(EnumMessages.ERROR.name() + " : " + e.getMostSpecificCause().getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	   
//	    for (ConstraintViolation cv : e.getConstraintViolations()) {
//	        report.addConstraintViolation(new RestConstraintViolation(
//	            cv.getPropertyPath().toString(),
//	            cv.getMessage(),
//	            cv.getInvalidValue() == null ? "null" : cv.getInvalidValue().toString()));
//	      }
//	   
	   
	   @ResponseStatus(HttpStatus.BAD_REQUEST)  
	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<?> ConstraintViolationException( ConstraintViolationException e, WebRequest request) {
	        StringBuilder violaciones= new StringBuilder(); 
		    for (ConstraintViolation cv : e.getConstraintViolations()) {
		    	violaciones.append(cv.getPropertyPath().toString() +"\n");
		    	violaciones.append(cv.getMessage()+"\n");
		    	violaciones.append(cv.getInvalidValue() == null ? "null" : cv.getInvalidValue().toString());
		      }
		   
		   ResponseGenerico<?> response = new ResponseGenerico<>();
	        response.setCodigoRespuestaName(HttpStatus.BAD_REQUEST.name());
	        response.setCodigoRespuestaValue(HttpStatus.BAD_REQUEST.value());
	        response.setMensaje(EnumMessages.ERROR.name() + " Data Incorrecta : " + violaciones.toString());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	   

//	   @ExceptionHandler(Exception.class)
//	   @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//	   public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
//		    ResponseGenerico<?> response = new ResponseGenerico<>();
//	        response.setCodigoRespuestaName(HttpStatus.BAD_REQUEST.name());
//	        response.setCodigoRespuestaValue(HttpStatus.BAD_REQUEST.value());
//	        response.setMensaje(EnumMessages.ERROR.name() + ":" + ex.getMessage());
//	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);	   
//	   }
	}
