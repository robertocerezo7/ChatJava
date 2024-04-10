/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientechat;

/**
 *
 * @author rober
 */
public class Mensaje implements java.io.Serializable {
    
    int comando;
    String emisor;
    String receptor;
    String canal;
    String mensaje;

    public void tratar(String msg,String nickname) {
       String[] a = msg.split(" ");
       String s = "";
       
       this.emisor = nickname;
        
        if (a[0].equalsIgnoreCase(".private")) {
            this.comando = 1;
            this.receptor = a[1];
            this.canal = null;
            for (int i = 2; i < a.length; i++) {
                s = s + " " + a[i];
            }
            this.mensaje = s;
        } else if (a[0].equalsIgnoreCase(".listusers")) {
            this.comando = 2;
            this.receptor = nickname;
            this.canal = null;
            this.mensaje = null;
        } else if (a[0].equalsIgnoreCase(".listchannels")) {
            this.comando = 3;
             this.receptor = nickname;
            this.canal = null;
            this.mensaje = null;
        } else if (a[0].equalsIgnoreCase(".join")) {
            this.comando = 4;
            this.receptor = nickname;
            this.canal = a[1];
            this.mensaje = null;
        } else if (a[0].equalsIgnoreCase(".listmychannels")) {
            this.comando = 5;
            this.receptor = nickname;
            this.canal = null;
            this.mensaje = null;
        } else if (a[0].equalsIgnoreCase(".channel")) {
            this.comando = 6;
            this.receptor = null;
            this.canal = a[1];
            for (int i = 2; i < a.length; i++) {
                s = s + " " + a[i];
            }
            this.mensaje = s;
        } else if (a[0].equalsIgnoreCase(".createchannel")) {
            this.comando = 7;
            this.receptor = nickname;
            this.canal = a[1];
            this.mensaje = null;
        } else if (a[0].equalsIgnoreCase(".leave")) {
            this.comando = 8;
            this.receptor = nickname;
            this.canal = a[1];
            this.mensaje = null;
        } else {
            this.comando = 0;
            this.receptor = null;
            this.canal = null;
            for (int i = 0; i < a.length; i++) {
                s = s + " " + a[i];
            }
            this.mensaje = s;
        }
    }

    public int getComando() {
        return comando;
    }

    public void setComando(int comando) {
        this.comando = comando;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "comando=" + comando + ", emisor=" + emisor + ", receptor=" + receptor + ", canal=" + canal + ", mensaje=" + mensaje + '}';
    }
    
}

