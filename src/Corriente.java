

import general.Constantes;
import general.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author ACOSTA
 */
public class Corriente extends Cuenta{
    
    private String oficial_cta;
    private float sobre_giro;
    
    public Corriente(){}

    public Corriente(String oficial_cta, float sobre_giro) {
        this.oficial_cta = oficial_cta;
        this.sobre_giro = sobre_giro;
    }
    
    public Corriente(String numero, String nombre, float saldo_actual, String cedula, boolean estado, int[][] tarjeta, String oficial_cta, float sobre_giro) {
        super(numero, nombre, saldo_actual, cedula, estado, tarjeta);
        this.oficial_cta = oficial_cta;
        this.sobre_giro = sobre_giro;
    }
    
    /* ENCAPSULLAMIENTO GET - SET */

    public String getOficial_cta() {
        return oficial_cta;
    }

    public void setOficial_cta(String oficial_cta) {
        this.oficial_cta = oficial_cta;
    }

    public float getSobre_giro() {
        return sobre_giro;
    }

    public void setSobre_giro(float sobre_giro) {
        this.sobre_giro = sobre_giro;
    }
    
    public String depositarCuentaCorriente(Cuenta cuenta, float valor){
        String respuesta = "";
        try{
            Corriente _corriente = obtenerDatosPrestamo(cuenta.getNumero());
            //si corriente tiene datos entonces el cliente esta registrado en el archivo con alguna deduda
            if(_corriente!=null){
                //si el valor a depositar es igual o mayor a la que a deuda
                //si es igual se eliminará la deuda del registro
                //si el valor es mayor entonces se eliminará la deuda del registro y se agregará saldo nuevo a su fondo.
                if(valor>=_corriente.getSobre_giro()){
                    if(valor==_corriente.getSobre_giro()){
                        deudaPagada(cuenta);
                        agregarSaldo(cuenta, valor);
                        respuesta = "SOBRE GIRO CANCELADO SATISFACTORIAMENTE";
                    }else if(valor>_corriente.getSobre_giro()){
                        //deudad pagada
                        deudaPagada(cuenta);
                        //acreditar a su fondo el sobrante
                        agregarSaldo(cuenta, valor);
                        respuesta = "SOBRE GIRO CANCELADO SATISFACTORIAMENTE, Y SALDO RESTANTE AGREGADO A SU FONDOS";
                    }
                }else{
                    //no va depositar toda la deuda
                    float deudaRestante = _corriente.getSobre_giro() - valor;
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fecha = dateFormat.format(date);
                    String lineaActualizar = cuenta.getNumero()+","+_corriente.getOficial_cta()+","+deudaRestante+","+fecha;
                    FileReader fr;
                    try {
                        //Instancia fichero incial creado
                        File FficheroAntiguo = new File(Constantes.archivoCorrientePrestamo);
                        //Instancia de fichero nuevo a crear
                        File FficheroNuevo=new File("temporal.txt");
                        fr = new FileReader(FficheroAntiguo);
                        if(FficheroAntiguo.exists()){
                            BufferedReader bf = new BufferedReader(fr);
                            String lineaLeida = bf.readLine();
                            while(lineaLeida!=null){
                                String[] parts = lineaLeida.split(",");
                                String codigoNumeroCuenta = parts[0]; 
                                if (codigoNumeroCuenta.equals(cuenta.getNumero()))
                                    lineaLeida = lineaActualizar;
                                Helper.EcribirFichero(FficheroNuevo, lineaLeida);
                                lineaLeida = bf.readLine();
                            }
                            bf.close();
                            //Borro el fichero inicial
                            FficheroAntiguo.delete(); 
                            //renombro el nuevo fichero con el nombre del fichero inicial
                            FficheroAntiguo = new File(Constantes.archivoCorrientePrestamo);
                            FficheroNuevo.renameTo(FficheroAntiguo);
                            respuesta = "VALOR RESTADO A DEUDA PENDIENTE, CUENTA CON UNA DEUDA DE "+deudaRestante;
                            
                            //acreditar a su fondo el sobrante
                            agregarSaldo(cuenta, valor);
                        }
                        fr.close();
                    } catch (Exception e) {
                    }
                }
            }else{
                agregarSaldo(cuenta, valor);
                respuesta = "DEPOSITO REALIZADO CON EXITO.";
            }
        }catch(Exception e){
            respuesta = "";
        }
        return respuesta;
    }
    
    public int retirarCuentaCorriente(Cuenta cuenta, float valor){
        int respuesta = 0;
        try {
            if(cuenta.getSaldo_actual() > 0.00){
                String cadenaCoordenadas = Helper.convertir_ArregloCoordenadas_a_String(cuenta.getTarjeta(), 5, 9);
                if(valor > cuenta.getSaldo_actual()){
                    //se emitirá un prestamo debido al sobregiro presentado
                    File archivo = new File(Constantes.archivoCorrientePrestamo);

                    float cantidadPrestamo = cuenta.getSaldo_actual()-valor;

                    Random rand = new Random();
                    int enteroRandom = rand.nextInt((50 - 1) + 1) + 1;
                    
                    Corriente corri = new Corriente();
                    corri.setOficial_cta("Oficial_nombre_xxxx"+enteroRandom);
                    corri.setSobre_giro((-1*cantidadPrestamo));
                    
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fecha = dateFormat.format(date);
                    
                    String sCadena = cuenta.getNumero()+","+corri.getOficial_cta()+","+corri.getSobre_giro()+","+fecha;
                    //Se escribirá en el archivo de texto
                    Helper.EcribirFichero(archivo, sCadena);
                    
                    String lineaActualizar = cuenta.getNumero() + "," +cuenta.getCedula() + "," +cuenta.getNombre() + "," +cantidadPrestamo + "," +true + "," +cadenaCoordenadas;
                    actualizarSaldo(cuenta.getNumero(), lineaActualizar);
                    
                    respuesta = 2;
                    //respuesta = "LA CANTIDAD A RETIRAR ES MAYOR A LA DE SUS FONDO, SE LE REALIZÓ UN SOBRE GIRO.\n TIENE UN PRESTAMO DE $"+(-1*cantidadPrestamo);
                }else{
                    //tiene saldo suficiente al que se pretende retirar
                    float nuevoSaldo = cuenta.getSaldo_actual()-valor;
                    //Linea con el saldo actualizado
                    String lineaActualizar = cuenta.getNumero() + "," +cuenta.getCedula() + "," +cuenta.getNombre() + "," +nuevoSaldo + "," +true + "," +cadenaCoordenadas;
                    //Instanciamos el metodo actualizarSaldo declarado en clase Cuenta
                    actualizarSaldo(cuenta.getNumero(), lineaActualizar);
                    respuesta = 1;
                    //respuesta = "SE REALIZO EL RETIRO DE SU CUENTA CON EXITO.";
                }
            }else{
                respuesta = 3;
                //respuesta = "NO CUENTA CON FONDOS SUFICIENTES, YA SE REALIZÓ UN SOBRE GIRO CON ANTERIORIDAD.";
            }
        } catch (Exception e) {
            respuesta = 0;
        }
        return respuesta;
    }
    
    
    private Corriente obtenerDatosPrestamo(String codigoCuenta){
        Corriente corriente = null;
        
        FileReader fr;
        try{
            fr = new FileReader(Constantes.archivoCorrientePrestamo);
            BufferedReader bf = new BufferedReader(fr);
            String linea = bf.readLine();
            if(linea!=null){
                while (linea!=null) {
                    //Arreglo de la fila de registro
                    String[] posicion = linea.split(",");
                    //si la posicion 0 que contiene codigo registrado es igual al codigo  que se ingresará
                    //se le asignará datos al objeto Cuenta
                    if(posicion[0].equals(codigoCuenta)){
                        //String codigo = posicion[0];
                        String oficial = posicion[1];
                        float prestamo = Float.parseFloat(posicion[2]);
                        //String fecha = posicion[3];
                        corriente = new Corriente(oficial, prestamo);
                    }
                    //leer la siguiente linea
                    linea = bf.readLine();
                }
            }
            bf.close();
            fr.close();

        } catch(Exception e){ 
            corriente = null;
        }
        return corriente;
    }
    
    private void deudaPagada(Cuenta cuenta){
        FileReader fr;
        try {
            //Instancia fichero incial creado
            File FficheroAntiguo = new File(Constantes.archivoCorrientePrestamo);
            //Instancia de fichero nuevo a crear
            File FficheroNuevo=new File("temporal.txt");
            fr = new FileReader(FficheroAntiguo);
            if(FficheroAntiguo.exists()){
                BufferedReader bf = new BufferedReader(fr);
                String lineaLeida = bf.readLine();
                while(lineaLeida!=null){
                    String[] parts = lineaLeida.split(",");
                    String codigoNumeroCuenta = parts[0]; 
                    String oficial = parts[1];
                    String deuda = parts[2];
                    String fecha = parts[3];
                    if (!codigoNumeroCuenta.equals(cuenta.getNumero())){
                        String sCadena = codigoNumeroCuenta+","+oficial+","+deuda+","+fecha;
                        Helper.EcribirFichero(FficheroNuevo, sCadena);
                    }
                    lineaLeida = bf.readLine();
                }
                bf.close();
                //Borro el fichero inicial
                FficheroAntiguo.delete(); 
                //renombro el nuevo fichero con el nombre del fichero inicial
                FficheroAntiguo = new File(Constantes.archivoCorrientePrestamo);
                FficheroNuevo.renameTo(FficheroAntiguo);
            }
            fr.close();
        } catch (Exception e) {
        }
    }
    
    private void agregarSaldo(Cuenta cuenta, float valor){
        String cadenaCoordenadas = Helper.convertir_ArregloCoordenadas_a_String(cuenta.getTarjeta(), 5, 9);
        //Se le suma el nuevo valor de deposito al saldo almacenado
        float sumaSaldo = cuenta.getSaldo_actual()+valor;
        //Linea con el saldo actualizado
        String lineaActualizar = cuenta.getNumero() + "," +cuenta.getCedula() + "," +cuenta.getNombre() + "," +sumaSaldo + "," +true + "," +cadenaCoordenadas;
        //Instanciamos el metodo actualizarSaldo declarado en clase Cuenta
        actualizarSaldo(cuenta.getNumero(), lineaActualizar);
    }
    
}
