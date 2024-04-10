/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import java.io.*;
import java.net.Socket;
import clientechat.Mensaje;

/**
 *
 * @author Portatil
 */
public class HiloServidor extends Thread {

    ObjetoCompartido oc;

    Socket s;
    String id;

    public HiloServidor(ObjetoCompartido oc, Socket s, int contador) {
        this.oc = oc;
        this.s = s;
        this.id = Integer.toString(contador);
    }

    public void run() {

        DataInputStream dis = null;
        DataOutputStream dos = null;
        ObjectInputStream ois = null;
        Mensaje m;
        Boolean repe;

        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            do {
                this.id = dis.readUTF();
                repe = oc.niknameRepetido(id);
                if (repe) {
                    System.out.println("Nickname repetido");
                    dos.writeBoolean(repe);
                } else {
                    oc.altaUsuario(this.id, this.s);
                    dos.writeBoolean(repe);
                }
            } while (repe);
            
            do {
                System.out.println(id);
                m = (Mensaje) ois.readObject();
                System.out.println(m.toString());
                oc.comando(m);
                
            } while (true);
        } catch (Exception e) {
            System.out.println("Desconexion del cliente " + this.id);
        } finally {
            oc.bajaUsuario(this.id);
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException ex) {
                    System.out.println("Error cerrando el DataInputStream del cliente" + this.id);
                }
                try {
                    s.close();
                } catch (IOException ex) {
                    System.out.println("Error cerrando el socket del cliente " + this.id);
                }
            }

        }
    }

}
