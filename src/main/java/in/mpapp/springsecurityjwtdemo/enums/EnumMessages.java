package in.mpapp.springsecurityjwtdemo.enums;

public enum EnumMessages {

   SUCCESS_SAVE("Guardado con Éxito"),
   DELETE_SAVE("Eliminado con Éxito"),
   ERROR("ERROR SERVICE");

   private String valor;


   EnumMessages(String nombreValor){
    this.valor= nombreValor;
   }


    public String getValor() {
        return valor;
    }


    public void setValor(String valor) {
        this.valor = valor;
    }


}
