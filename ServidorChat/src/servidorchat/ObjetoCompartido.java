/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat;

import clientechat.Mensaje;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Portatil
 */
public class ObjetoCompartido {

    HashMap<String, Socket> general;
    HashMap<String, ArrayList<String>> canales;

    public ObjetoCompartido() {
        this.general = new HashMap<>();
        this.canales = new HashMap<>();
        canales.put("deportes", new ArrayList<>());
        canales.put("videojuegos", new ArrayList<>());
        canales.put("varios", new ArrayList<>());
    }

    public synchronized void altaUsuario(String id, Socket s) {
        this.general.put(id, s);

    }

    public synchronized void bajaUsuario(String id) {
        this.general.remove(id);
    }

    public synchronized boolean niknameRepetido(String id) {
        DataOutputStream dos = null;
        boolean a = this.general.containsKey(id);
        return a;
    }

    public void comando(Mensaje m) {

        int opcion = m.getComando();

        switch (opcion) {
            case 0:
                enviarMensaje(m);
                break;
            case 1:
                enviarPrivado(m);
                break;
            case 2:
                listarUsuarios(m);
                break;
            case 3:
                listarCanales(m);
                break;
            case 4:
                unirseCanal(m);
                break;
            case 5:
                listarMisCanales(m);
                break;
            case 6:
                enviarCanal(m);
                break;
            case 7:
                crearCanal(m);
                break;
            case 8:
                salirCanal(m);
                break;
            default:
                break;
        }

    }

    public synchronized void listarUsuarios(Mensaje m) {
        String usuarios = "";
        ObjectOutputStream oos = null;
        for (String i : this.general.keySet()) {
            usuarios += i + "\n";
        }
        m.setMensaje(usuarios);
        try {
            oos = new ObjectOutputStream(general.get(m.getEmisor()).getOutputStream());
            oos.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error al listar usuarios");
        }
    }

    public void listarCanales(Mensaje m) {
        String listaCanales = "";
        ObjectOutputStream oos = null;
        for (String i : this.canales.keySet()) {
            listaCanales += i + "\n";
        }
        m.setMensaje(listaCanales);
        try {
            oos = new ObjectOutputStream(general.get(m.getEmisor()).getOutputStream());
            oos.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error al listar canales");
        }
    }

    public void enviarPrivado(Mensaje m) {
        ObjectOutputStream oos = null;
        String s = m.getMensaje();
        for (String idUsuario : this.general.keySet()) {
            try {
                if ((m.getReceptor().equals(idUsuario))) {
                    oos = new ObjectOutputStream(general.get(idUsuario).getOutputStream());
                    m.setMensaje(m.getEmisor() + " te ha enviado un mensaje: " + s);
                    oos.writeObject(m);
                }
            } catch (Exception e) {
                System.out.println("Error al enviar un mensaje desde el usuario " + m.getEmisor() + " al usuario " + idUsuario);
            } finally {

            }
        }

    }

    public synchronized void enviarMensaje(Mensaje m) {
        ObjectOutputStream oos = null;

        System.out.println(m.toString());
        for (String idUsuario : this.general.keySet()) {
            try {
                if (!(m.getEmisor().equals(idUsuario))) {
                    oos = new ObjectOutputStream(general.get(idUsuario).getOutputStream());
                    oos.writeObject(m);
                }
            } catch (Exception e) {
                System.out.println("Error al enviar un mensaje desde el usuario " + m.getEmisor() + " al usuario " + idUsuario);
            } finally {

            }
        }
    }

    public synchronized void enviarCanal(Mensaje m) {
        ObjectOutputStream oos = null;
        String a;

        String s = m.getMensaje();
        for (String canal : canales.keySet()) {
            if (canal.equals(m.getCanal())) {
                for (int j = 0; j < this.canales.get(canal).size(); j++) {
                    a = this.canales.get(canal).get(j);
                    if (m.getEmisor().equals(a)) {
                        for (int i = 0; i < this.canales.get(canal).size(); i++) {
                            String b = this.canales.get(canal).get(i);
                            for (String idUsuario : this.general.keySet()) {
                                try {
                                    if (!(b.equals(m.getEmisor()))) {
                                        if (b.equals(idUsuario)) {
                                            oos = new ObjectOutputStream(general.get(idUsuario).getOutputStream());
                                            m.setMensaje(m.getEmisor() + " te ha enviado un mensaje desde el canal " + m.getCanal() + ": " + s);
                                            oos.writeObject(m);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error al enviar un mensaje desde el usuario " + m.getEmisor() + " al usuario " + idUsuario);
                                } finally {

                                }
                            }
                        }

                    }
                }
            }

        }
        System.out.println(m.toString());
    }

    public synchronized void unirseCanal(Mensaje m) {

        ObjectOutputStream oos = null;

        for (String i : this.canales.keySet()) {
            if (i.equals(m.getCanal())) {
                canales.get(i).add(m.getEmisor());

                for (String idUsuario : this.general.keySet()) {
                    try {
                        if ((m.getReceptor().equals(idUsuario))) {
                            oos = new ObjectOutputStream(general.get(idUsuario).getOutputStream());
                            m.setMensaje("Te has unido al canal " + m.getCanal());
                            oos.writeObject(m);
                        }
                    } catch (Exception e) {
                        System.out.println("Error al enviar un mensaje desde el usuario " + m.getEmisor() + " al usuario " + idUsuario);
                    } finally {

                    }
                }
            }
        }
        System.out.println("Te has unido al canal " + m.getCanal());
    }

    public void listarMisCanales(Mensaje m) {
        String listaCanales = "";
        String a = "";
        ObjectOutputStream oos = null;
        for (String i : this.canales.keySet()) {
            for (int j = 0; j < this.canales.get(i).size(); j++) {
                a = this.canales.get(i).get(j);
                if (m.getEmisor().equals(a)) {
                    listaCanales += i + "\n";
                }
            }

        }
        m.setMensaje(listaCanales);
        try {
            oos = new ObjectOutputStream(general.get(m.getEmisor()).getOutputStream());
            oos.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error al listar canales");
        }
    }

    public void salirCanal(Mensaje m) {
        String a = "";
        ObjectOutputStream oos = null;
        for (String i : this.canales.keySet()) {
            if (m.getCanal().equals(i)) {
                for (int j = 0; j < this.canales.get(i).size(); j++) {
                    a = this.canales.get(i).get(j);
                    if (m.getEmisor().equals(a)) {
                        this.canales.get(i).remove(j);
                    }
                }

            }
        }
        m.setMensaje("Te has salido del canal " + m.getCanal());
        try {
            oos = new ObjectOutputStream(general.get(m.getEmisor()).getOutputStream());
            oos.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error al listar canales");
        }

    }

    public synchronized void crearCanal(Mensaje m) {
        ObjectOutputStream oos = null;

        canales.put(m.getCanal(), new ArrayList<>());

        try {
            oos = new ObjectOutputStream(general.get(m.getEmisor()).getOutputStream());
            m.setMensaje("Has creado el canal " + m.getCanal());
            oos.writeObject(m);
        } catch (Exception e) {
            System.out.println("Error al crear canales");
        }

    }
}
