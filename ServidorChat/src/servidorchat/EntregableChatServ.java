/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Portatil
 */
public class EntregableChatServ {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
//        int numeroPuerto = 6500;// Puerto
	ServerSocket sc;
        ObjetoCompartido oc;
        Socket s;
        int contador = 0;
        HiloServidor hs;
        
            sc = new ServerSocket(6500);
            oc = new ObjetoCompartido();
            do{
                contador++;
                s = sc.accept();
                hs = new HiloServidor(oc,s,contador);
                hs.start();
                
            }while(true);
        
        
    }
    
}
