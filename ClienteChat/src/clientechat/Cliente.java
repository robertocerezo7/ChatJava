/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Portatil
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket s = null;
        Scanner sc = new Scanner(System.in);
        String msg = "", nickname;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        ObjectOutputStream oos = null;
        HiloReceptor hr = null;
        Mensaje m = null;
        try {
            s = new Socket("localhost", 6500);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());

            boolean repe;
            do {
                System.out.println("Nickname: ");
                nickname = sc.nextLine();
                dos.writeUTF(nickname);
                repe = dis.readBoolean();
                if (repe) {
                    System.out.println("Nickname repetido");
                } else {
                    System.out.println("Bienvenido " + nickname);
                }
            } while (repe);
            hr = new HiloReceptor(s);
            hr.start();

            do {
                m = new Mensaje();
                msg = sc.nextLine();
                if (msg.equals(".help")) {
                    Ayuda();
                } else if (!(msg.equals(".exit"))) {
                    m.tratar(msg, nickname);

                    oos.writeObject(m);
                    System.out.println(m.toString());
                }
            } while (!(msg.equals(".exit")));
        } catch (Exception e) {
            System.out.println("Error en la comunicación");
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    System.out.println("Error cerrando DataOutputStream");
                }
            }
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                    System.out.println("Error cerrando el Socket");
                }
            }
        }

    }

    public static void Ayuda() {
        System.out.println(".exit Cierra la conexion\n"
                + ".listusers Lista los usuarios conectados\n"
                + ".private (nickname) (msg) Envia un mensaje privado a nickname\n"
                + ".listchannels Lista todos los canales\n"
                + ".join (channel) Unirse a un canal\n"
                + ".listmychannels Lista los canales en los que estas\n"
                + ".channel (channel) (msg) Manda un mensaje al canal seleccionado\n"
                + ".createchannel (channel) Crea un canal \n"
                + ".leave (channel) Salir del canal \n");

    }
}
