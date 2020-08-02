package general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ACOSTA
 */
public class Helper {
    
    
    /**
     * REFERENCIA
     * https://codeday.me/es/qa/20190617/902173.html
     * http://www.forosdelweb.com/f45/encriptar-desencriptar-md5-1035292/
     * http://www.forosdelweb.com/f18/puede-desencriptar-md5-963319/
     * https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     */
    
    public static String encryptDataMD5(String password){
        String stringEncrypted = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            stringEncrypted = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return stringEncrypted;
    }
    
    public static boolean ficheroExiste(File Ffichero){
        try {
            if(!Ffichero.exists()){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static void EcribirFichero(File Ffichero, String SCadena){
        try {
            if(!Ffichero.exists()){
                Ffichero.createNewFile();
            }
            BufferedWriter Fescribe = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Ffichero,true), "utf-8"));
            Fescribe.write(SCadena + "\n");
            Fescribe.close();
        } catch (Exception ex) { System.out.println(ex.getMessage()); } 
        
    }
    
    public static boolean esAccesoCorrecto(String user, String password){
        boolean respuesta = false;
        FileReader fr;
        try {
            fr = new FileReader(Constantes.archivoUsuario);
            BufferedReader bf = new BufferedReader(fr);
            String linea = bf.readLine();
            while (linea!=null) {
                String[] parts = linea.split(",");
                String part1 = parts[0]; 
                String part2 = parts[1];
                String userEncrypted = Helper.encryptDataMD5(user);
                String passEncrypted = Helper.encryptDataMD5(password);
                if(part1.equals(userEncrypted) && part2.equals(passEncrypted)){
                    respuesta = true;
                    break;
                }
                linea = bf.readLine();
            }
        } catch (Exception e) {}
        return respuesta;
    }
    
    public static String convertir_ArregloCoordenadas_a_String(int[][] matrizCoordenadas, int x, int y){
        String respuesta = "";
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                respuesta = respuesta + matrizCoordenadas[i][j]+((i!=x-1)?"-": (j!=y-1) ? "-" : "");
            }
        }
        return respuesta;
    }
    
    public static String generarNumeroCuenta(boolean ficheroExiste, char primerDigito){
        String respuesta = "";
        FileReader fr;
        String numeroCuenta = "";
        try {
            if(ficheroExiste){
                fr = new FileReader(Constantes.archivoCuenta);
                BufferedReader bf = new BufferedReader(fr);
                String linea = bf.readLine();
                if(linea!=null){
                    while (linea!=null) {
                        String[] posicion = linea.split(",");
                        if(posicion[0].charAt(0)==primerDigito){
                            numeroCuenta = posicion[0];
                        }
                        linea = bf.readLine();
                    }
                }
                if(!numeroCuenta.isEmpty()){
                    int sumar1 = Integer.parseInt(numeroCuenta)+1;
                    respuesta = String.valueOf(sumar1);
                }else{
                    if(primerDigito=='1'){
                        respuesta = "1000";
                    }else if(primerDigito=='2'){
                        respuesta = "2000";
                    }
                }
                bf.close();
                fr.close();
            }else{
                if(primerDigito=='1'){
                    respuesta = "1000";
                }else if(primerDigito=='2'){
                    respuesta = "2000";
                }
            }
        } catch (Exception e) { }
        
        return respuesta;
    }
    
}
