

import general.Helper;

/**
 *
 * @author ACOSTA
 */
abstract class AhorroMeta {
    
    private float ahorro_meta;

    public AhorroMeta(){}
    
    public AhorroMeta(float ahorro_meta) {
        this.ahorro_meta = ahorro_meta;
    }
    
    /* ENCAPSULLAMIENTO GET - SET */
    
    public float getAhorro_meta() {
        return ahorro_meta;
    }

    public void setAhorro_meta(float ahorro_meta) {
        this.ahorro_meta = ahorro_meta;
    }
    
    
    public boolean depositar(Cuenta cuenta, float valor){
        boolean respuesta = false;
        try{
            String cadenaCoordenadas = Helper.convertir_ArregloCoordenadas_a_String(cuenta.getTarjeta(), 5, 9);
            //Se le suma el nuevo valor de deposito al saldo almacenado
            float sumaSaldo = cuenta.getSaldo_actual()+valor;
            //Linea con el saldo actualizado
            String lineaActualizar = cuenta.getNumero() + "," +cuenta.getCedula() + "," +cuenta.getNombre() + "," +sumaSaldo + "," +true + "," +cadenaCoordenadas;
            //Instanciamos el metodo actualizarSaldo declarado en clase Cuenta
            new Cuenta().actualizarSaldo(cuenta.getNumero(), lineaActualizar);
            respuesta = true;
        }catch(Exception e){
            respuesta = false;
        }
        return respuesta;
    }
    
    public boolean retirar(Cuenta cuenta, float valor){
        boolean respuesta = false;
        try {
            String cadenaCoordenadas = Helper.convertir_ArregloCoordenadas_a_String(cuenta.getTarjeta(), 5, 9);
            //Se le resta el valor de retiro al saldo almacenado
            float nuevoSaldo = cuenta.getSaldo_actual()-valor;
            //Linea con el saldo actualizado
            String lineaActualizar = cuenta.getNumero() + "," +cuenta.getCedula() + "," +cuenta.getNombre() + "," +nuevoSaldo + "," +true + "," +cadenaCoordenadas;
            //Instanciamos el metodo actualizarSaldo declarado en clase Cuenta
            new Cuenta().actualizarSaldo(cuenta.getNumero(), lineaActualizar);
            respuesta = true;
        } catch (Exception e) {
        }
        return respuesta;
    }
    
    public abstract Boolean generarInteres();
    
}
