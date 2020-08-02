

import general.Constantes;
import general.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ACOSTA
 */
public class Transaccion {
    
    private String codigo;
    private String cedula_cliente;
    private String numero_cuenta;
    private String tipo;
    private String fehca_transaccion;
    private float valor;
    private String numero_cheque;
    private boolean estado;
    
    public Transaccion(){}

    public Transaccion(String codigo, String cedula_cliente, String numero_cuenta, String tipo, String fehca_transaccion, float valor, String numero_cheque, boolean estado) {
        this.codigo = codigo;
        this.cedula_cliente = cedula_cliente;
        this.numero_cuenta = numero_cuenta;
        this.tipo = tipo;
        this.fehca_transaccion = fehca_transaccion;
        this.valor = valor;
        this.numero_cheque = numero_cheque;
        this.estado = estado;
    }
    
    /* ENCAPSULLAMIENTO GET - SET */

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getCedula_cliente() {
        return cedula_cliente;
    }

    public void setCedula_cliente(String cedula_cliente) {
        this.cedula_cliente = cedula_cliente;
    }


    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getFehca_transaccion() {
        return fehca_transaccion;
    }

    public void setFehca_transaccion(String fehca_transaccion) {
        this.fehca_transaccion = fehca_transaccion;
    }


    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }


    public String getNumero_cheque() {
        return numero_cheque;
    }

    public void setNumero_cheque(String numero_cheque) {
        this.numero_cheque = numero_cheque;
    }


    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
    public void registrarTransaccion(Cuenta cuenta, String tipo, float valor){
        
        File archivoTransaccion = new File(Constantes.archivoTransaccion);
        
        boolean ficheroExiste = Helper.ficheroExiste(archivoTransaccion);
        String _codi = generarCodigo(ficheroExiste);
        
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(date);
        
        Transaccion transaccion = new Transaccion(_codi, cuenta.getCedula(),cuenta.getNumero(),tipo,fecha,valor,"XXXXXX",true);
        
        String _codigo = transaccion.getCodigo();
        String _cedula = transaccion.getCedula_cliente();
        String _numero_cuenta = transaccion.getNumero_cuenta();
        String _tipo = transaccion.getTipo();
        String _fecha = transaccion.getFehca_transaccion();
        float _valor = transaccion.getValor();
        String _numero_cheque = transaccion.getNumero_cheque();
        boolean _estado = transaccion.isEstado();
        
        String sCadena = _codigo + "," +_cedula + "," +_numero_cuenta + "," +_tipo + ","+_fecha+","+_valor +","+_numero_cheque + "," +_estado;
        //Se escribirá en el archivo de texto
        Helper.EcribirFichero(archivoTransaccion, sCadena);
        
    }
    
    private String generarCodigo(boolean ficheroExiste){
        String respuesta = "";
        FileReader fr;
        String numeroCuenta = "";
        try {
            if(ficheroExiste){
                fr = new FileReader(Constantes.archivoTransaccion);
                BufferedReader bf = new BufferedReader(fr);
                String linea = bf.readLine();
                if(linea!=null){
                    while (linea!=null) {
                        String[] posicion = linea.split(",");
                        numeroCuenta = posicion[0];
                        linea = bf.readLine();
                    }
                }
                if(!numeroCuenta.isEmpty()){
                    int sumar1 = Integer.parseInt(numeroCuenta)+1;
                    respuesta = String.valueOf(sumar1);
                }else{
                    respuesta = "1";
                }
                bf.close();
                fr.close();
            }else{
                respuesta = "1";
            }
        } catch (Exception e) { }
        
        return respuesta;
    }

    
}
