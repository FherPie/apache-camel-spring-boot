package in.mpapp.springsecurityjwtdemo.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import in.mpapp.springsecurityjwtdemo.enums.EnumMessages;
import in.mpapp.springsecurityjwtdemo.models.responses.ResponseGenerico;




public class ControllersUtils {

    

    public static ResponseEntity<?> repuestaGenericoError(Exception e, ResponseGenerico<?> response){
            response.setCodigoRespuestaName(HttpStatus.BAD_REQUEST.name());
            response.setCodigoRespuestaValue(HttpStatus.BAD_REQUEST.value());
            response.setMensaje(EnumMessages.ERROR.name() + ":" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
    public static ResponseEntity<?> repuestaGenericoExitoObject(ResponseGenerico<?> response, Object dto){
        response.setCodigoRespuestaName(HttpStatus.OK.name());
        response.setCodigoRespuestaValue(HttpStatus.OK.value());
        response.setObjetoOb(dto);
        response.setMensaje(EnumMessages.SUCCESS_SAVE.getValor());
        return new ResponseEntity<>(response, HttpStatus.OK);
}


    public static ResponseEntity<?> repuestaGenericoExitoList(ResponseGenerico<?> response, Object list){
        response.setCodigoRespuestaName(HttpStatus.OK.name());
        response.setCodigoRespuestaValue(HttpStatus.OK.value());
        response.setListadoOb(list);
        response.setMensaje(EnumMessages.SUCCESS_SAVE.getValor());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public static ResponseEntity<?> repuestaGenericoExitoObjectEliminacion(ResponseGenerico<?> response){
        response.setCodigoRespuestaName(HttpStatus.OK.name());
        response.setCodigoRespuestaValue(HttpStatus.OK.value());
        response.setMensaje(EnumMessages.DELETE_SAVE.getValor());
        return new ResponseEntity<>(response, HttpStatus.OK);
}

}
