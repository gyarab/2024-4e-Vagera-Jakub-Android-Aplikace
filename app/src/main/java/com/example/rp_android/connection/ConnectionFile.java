package com.example.rp_android.connection;

/**
 * Soubor pripojuje uzivatele k serveru
 */
public class ConnectionFile {
    /**Adresa musi byt ve formatu
     * http://adresa/
     * */
    static String connection = "http://10.9.9.127:8000/";
    static String connectionCut  = connection.substring( 0, connection.length()-1);
    public static String returnURL() {
        return connection ;
    }
    public static String returnURLRaw() {
        return connectionCut ;
    }

}
