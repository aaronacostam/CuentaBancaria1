/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import general.Constantes;
import general.Helper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ACOSTA
 */
public class Cuenta extends AhorroMeta{
    
    private String numero;
    private String nombre;
    private float saldo_actual;
    private String cedula;
    private boolean estado;
    private int[][] tarjeta;
    
    public Cuenta(){}

    public Cuenta(String numero, String nombre, float saldo_actual, String cedula, boolean estado) {
        this.numero = numero;
        this.nombre = nombre;
        this.saldo_actual = saldo_actual;
        this.cedula = cedula;
        this.estado = estado;
    }
    
    public Cuenta(String numero, String nombre, float saldo_actual, String cedula, boolean estado, int[][] tarjeta) {
        this.numero = numero;
        this.nombre = nombre;
        this.saldo_actual = saldo_actual;
        this.cedula = cedula;
        this.estado = estado;
        this.tarjeta = tarjeta;
    }
    
    public Cuenta(String numero, String nombre, float saldo_actual, String cedula, boolean estado, int[][] tarjeta, float ahorro_meta) {
        super(ahorro_meta);
        this.numero = numero;
        this.nombre = nombre;
        this.saldo_actual = saldo_actual;
        this.cedula = cedula;
        this.estado = estado;
        this.tarjeta = tarjeta;
    }
    
    /* ENCAPSULLAMIENTO GET - SET */

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(float saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int[][] getTarjeta() {
        return tarjeta;
    }
    
    public void setTarjeta(int[][] tarjeta) {
        this.tarjeta = tarjeta;
    }
    
    @Override
    public Boolean generarInteres() {
        Boolean respuesta = false;
        FileReader fr;
        try {
            //Instancia fichero incial creado
            File FficheroAntiguo = new File(Constantes.archivoCuenta);
            if(FficheroAntiguo.exists()){
                //Instancia de fichero nuevo a crear
                File FficheroNuevo=new File("temporal.txt");
                fr = new FileReader(FficheroAntiguo);
                
                BufferedReader bf = new BufferedReader(fr);
                String lineaLeida = bf.readLine();
                while(lineaLeida!=null){
                    String[] parts = lineaLeida.split(",");
                    String codigoNumeroCuenta = parts[0]; 
                    
                    //si la linea que esta siendo leida contiene el codigo igual al ingresado
                    //se reemplazará linea almacenada por una nueva linea actualizada el valor de saldo
                    if(codigoNumeroCuenta.charAt(0)=='1'){
                        String cedula = parts[1]; 
                        String nombres = parts[2]; 

                        float saldo = Float.parseFloat(parts[3]); 
                        float nuevoSaldo = (float) (saldo+(saldo*0.007));

                        boolean estado = Boolean.parseBoolean(parts[4]); 
                        String coordenadas = parts[5]; 

                        String nuevalineaAhorro = codigoNumeroCuenta+","+cedula+","+nombres+","+nuevoSaldo+","+estado+","+coordenadas;
                        lineaLeida = nuevalineaAhorro;
                    }
                    //Se procederá a registrar el contenido de linea en el nuevo fichero
                    Helper.EcribirFichero(FficheroNuevo, lineaLeida);
                    //leer la siguiente linea
                    lineaLeida = bf.readLine();
                }
                bf.close();
                //Borro el fichero inicial
                FficheroAntiguo.delete(); 
                //renombro el nuevo fichero con el nombre del fichero inicial
                FficheroAntiguo = new File(Constantes.archivoCuenta);
                FficheroNuevo.renameTo(FficheroAntiguo);
                
                fr.close();
                respuesta = true;
            }
            
        } catch (Exception e) {
            respuesta = false;
        }
        return respuesta;
    }
    
    //metodo que se pide
    protected void actualizarSaldo(String numeroCuenta, String lineaActualizar){//Cuenta cuenta, float valor){
        FileReader fr;
        try {
            //Instancia fichero incial creado
            File FficheroAntiguo = new File(Constantes.archivoCuenta);
            //Instancia de fichero nuevo a crear
            File FficheroNuevo=new File("temporal.txt");
            fr = new FileReader(FficheroAntiguo);

            if(FficheroAntiguo.exists()){
                BufferedReader bf = new BufferedReader(fr);
                String lineaLeida = bf.readLine();
                while(lineaLeida!=null){
                    String[] parts = lineaLeida.split(",");
                    String codigoNumeroCuenta = parts[0]; 
                    //si la linea que esta siendo leida contiene el codigo igual al ingresado
                    //se reemplazará linea almacenada por una nueva linea actualizada el valor de saldo
                    if (codigoNumeroCuenta.equals(numeroCuenta))
                        lineaLeida = lineaActualizar;
                    //Se procederá a registrar el contenido de linea en el nuevo fichero
                    Helper.EcribirFichero(FficheroNuevo, lineaLeida);
                    //leer la siguiente linea
                    lineaLeida = bf.readLine();
                }
                bf.close();
                //Borro el fichero inicial
                FficheroAntiguo.delete(); 
                //renombro el nuevo fichero con el nombre del fichero inicial
                FficheroAntiguo = new File(Constantes.archivoCuenta);
                FficheroNuevo.renameTo(FficheroAntiguo);
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("Error exception");
        }
    }
    
    //metodo que se pide
    public Cuenta mostrarDatosCuenta(String codigoCuenta){
        Cuenta cuenta = null;
        
        File fileAhoMeta = new File(Constantes.archivoAhorroMeta);
        
        FileReader fr;
        FileReader fr2;
        
        String codigo = "", nombres = "", cedula = "";
        float saldo = (float) 0.0, ahorroMeta = (float) 0.0;
        int x = 5, y = 9;
        int [][] matrizCoordenadas = new int[x][y];
        
        try{
            fr = new FileReader(Constantes.archivoCuenta);
            BufferedReader bf = new BufferedReader(fr);
            String linea = bf.readLine();
            if(linea!=null){
                while (linea!=null) {
                    //Arreglo de la fila de registro
                    String[] posicion = linea.split(",");
                    //si la posicion 0 que contiene codigo registrado es igual al codigo  que se ingresará
                    //se le asignará datos al objeto Cuenta
                    if(posicion[0].equals(codigoCuenta)){
                        codigo = posicion[0];
                        nombres = posicion[2];
                        saldo = Float.parseFloat(posicion[3]);
                        cedula = posicion[1];
                        String coordenadas = posicion[5];
                        
                        String[] coordenada = coordenadas.split("-");
                        
                        int count = 0;
                        for (int i = 0; i < x; i++) {
                            for (int j = 0; j < y; j++) {
                                matrizCoordenadas[i][j] = Integer.parseInt(coordenada[count]);
                                count++;
                            }
                        }
                    }
                    //leer la siguiente linea
                    linea = bf.readLine();
                }
            }
            bf.close();
            fr.close();
            
            if(fileAhoMeta.exists()){
                fr2 = new FileReader(Constantes.archivoAhorroMeta);
                BufferedReader bf2 = new BufferedReader(fr2);
                String lineaAhorroMeta = bf2.readLine();
                if(lineaAhorroMeta!=null){
                    while(lineaAhorroMeta!=null){
                        String[] posicion = lineaAhorroMeta.split(",");
                        //si la posicion 0 que contiene codigo registrado es igual al codigo  que se ingresará
                        if(posicion[0].equals(codigoCuenta)){
                            ahorroMeta = Float.parseFloat(posicion[1]);
                        }
                        //leer la siguiente linea
                        lineaAhorroMeta = bf2.readLine();
                    }
                }
                bf2.close();
                fr2.close();
            }
            
            if(!codigo.isEmpty() && !nombres.isEmpty() && !cedula.isEmpty()){
                cuenta = new Cuenta(codigo, nombres, saldo, cedula, true, matrizCoordenadas, ahorroMeta);
            }
        } catch(Exception e){ 
            cuenta = null;
        }
        
        return cuenta;
    }
    
    //metodo que se pide
    public static int[][] mostrarTarjetaAsignada(String codigoCuenta, int filas, int columnas){
        Constantes constantes = new Constantes();
        //int x = 5, y = 9;
        int [][] matrizCoordenadas = new int[filas][columnas];
        
        if(codigoCuenta!=null){
            // se mostrara la tarjeta que se le fue asignada al cliente
            FileReader fr;
            try{
                fr = new FileReader(constantes.archivoCuenta);
                BufferedReader bf = new BufferedReader(fr);
                String linea = bf.readLine();
                if(linea!=null){
                    while (linea!=null) {
                        //Arreglo de la fila de registro
                        String[] posicion = linea.split(",");
                        //si la posicion 0 que contiene codigo registrado es igual al codigo  que se ingresará
                        //se retornara la matriz coordenada
                        if(posicion[0].equals(codigoCuenta)){
                            String coordenadas = posicion[5];
                            String[] coordenada = coordenadas.split("-");
                            int count = 0;
                            for (int i = 0; i < filas; i++) {
                                for (int j = 0; j < columnas; j++) {
                                    matrizCoordenadas[i][j] = Integer.parseInt(coordenada[count]);
                                    count++;
                                }
                            }
                        }
                        //leer la siguiente linea
                        linea = bf.readLine();
                    }
                }
                bf.close();
                fr.close();
            } catch(Exception e){ }
        }else{
            //en caso de recibir el valor del parametro nulo, se entendera que se solicitara
            //que se genere una nueva tarjeta de coordenadas
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    Random rand = new Random();
                    int randomNum = rand.nextInt((99 - 00) + 1) + 00;
                    matrizCoordenadas[i][j] = randomNum;
                }
            }
        }
        return matrizCoordenadas;
    }
    
    public ArrayList<Cuenta> obtenerCuentas(String tipo){
        Cuenta cuenta = null;
        ArrayList<Cuenta> resultado = new ArrayList<Cuenta>();
        resultado.clear();
        FileReader fr;
        try {
            fr = new FileReader(Constantes.archivoCuenta);
            BufferedReader bf = new BufferedReader(fr);
            String linea = bf.readLine();
            while(linea!=null){
                String[] posicion = linea.split(",");
                
                if(tipo.equals("TODOS")){
                                        //numero, nombre, saldo_actual, cedula, estado
                    cuenta = new Cuenta(posicion[0], posicion[2], Float.parseFloat(posicion[3]), posicion[1], Boolean.parseBoolean(posicion[4]));
                    resultado.add(cuenta);
                }else{
                    if(tipo.equals("AHORRO")){
                        if(posicion[0].charAt(0)=='1'){
                                                //numero, nombre, saldo_actual, cedula, estado
                            cuenta = new Cuenta(posicion[0], posicion[2], Float.parseFloat(posicion[3]), posicion[1], Boolean.parseBoolean(posicion[4]));
                            resultado.add(cuenta);
                        }
                    }else{
                        //Corriente
                        if(posicion[0].charAt(0)=='2'){
                                                //numero, nombre, saldo_actual, cedula, estado
                            cuenta = new Cuenta(posicion[0], posicion[2], Float.parseFloat(posicion[3]), posicion[1], Boolean.parseBoolean(posicion[4]));
                            resultado.add(cuenta);
                        }
                    }
                }
                linea = bf.readLine();
            }
        } catch (Exception e) { }
        return resultado;
    }
    
}
