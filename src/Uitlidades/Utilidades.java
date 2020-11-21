package Uitlidades;

import Uitlidades.Delimitador.Principal.Delimitadores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utilidades {
    public Utilidades() {
    }

    public static String getUID(String nomeUser) {
        String os = System.getProperty("os.name").toLowerCase();
        String UID = "0";
        ProcessBuilder construtor = new ProcessBuilder(new String[0]);
        if (os.contentEquals("windows")) {
            System.out.println("O comando para pegar UID ainda não está disponivel para seu sistema operacional");
        } else if (os.contentEquals("linux")) {
            try {
                Process gUID = construtor.command("id", "-u", nomeUser).start();
                UID = (new BufferedReader(new InputStreamReader(gUID.getInputStream()))).readLine();
            } catch (IOException var6) {
                System.out.println("Não foi possível capturar ID");
                UID = "0";
            }
        }

        return UID;
    }

    public static boolean avaliarDelimitadores(String setenca){
        Delimitadores delimitadores = new Delimitadores();
        if (delimitadores.avalia(setenca) == 's'){
            return true;
        }
        return false;
    }


    public static boolean existsChars(String sentenca, Character[] alvos){
        for (Character alvo : alvos) {
            boolean find = false;
            for (int y = 0; y < sentenca.length(); y++) {
                if (sentenca.charAt(y) == alvo) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                return false;
            }
        }
        return true;
    }

    public static String getSistemPath(){
        return System.getProperty("user.dir") + System.getProperty("file.separator");
    }

    public static String getSistemPath(String arquivo){
        return System.getProperty("user.dir") + System.getProperty("file.separator") + arquivo;
    }
}