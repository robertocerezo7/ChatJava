/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Portatil
 */
public class HiloReceptor extends Thread {

    Socket s;

    HiloReceptor(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        Mensaje m;
        String msgRecibido;
        try {
            
            do {
                ois = new ObjectInputStream(this.s.getInputStream());
                m = (Mensaje) ois.readObject();
                msgRecibido = m.mensaje;
                System.out.println(msgRecibido);
            } while (true);
        } catch (Exception e) {
//            System.out.println("Error recibiendo datos");
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    System.out.println("Error cerrando DataInputStream");
                }
            }
        }
    }

}
