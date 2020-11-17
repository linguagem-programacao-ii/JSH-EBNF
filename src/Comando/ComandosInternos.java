package Comando;

import Uitlidades.Utilidades;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public final class ComandosInternos {

    public static int exibirRelogio() {
        Date DHatual = new Date();
        String data = (new SimpleDateFormat("dd/MM/yyyy")).format(DHatual);
        String hora = (new SimpleDateFormat("HH:mm:ss")).format(DHatual);
        System.out.println(hora + '\n' + data);
        return 0;
    }

    public static int escreverListaArquivos(Optional<String> nomeDir) {
        File dir = new File((String)nomeDir.get());
        List<File> listArq = Arrays.asList(dir.listFiles());
        StringBuilder out = new StringBuilder();

        for(int x = 0; x < listArq.size(); ++x) {
            out.append(((File)listArq.get(x)).getName() + '\n');
        }

        System.out.println(out.toString());
        return 0;
    }

    public static int criarNovoDiretorio(String nomeDir, String path) {
        File dir = new File(path + System.getProperty("file.separator") + nomeDir);
        dir.mkdir();
        String out = dir.exists() && dir.isDirectory() ? "Criado" : "Erro";
        System.out.println(out);
        return dir.exists() && dir.isDirectory() ? 0 : 1;
    }

    public static int apagarDiretorio(String nomeDir, String dirPath) {
        File deleted = new File(dirPath + System.getProperty("file.separator") + nomeDir);
        String var10000;
        if (deleted.delete()) {
            var10000 = "Sucesso";
        } else {
            var10000 = !deleted.isDirectory() ? "Não é um diretório" : "Não existe";
        }

        return !deleted.exists() ? 0 : 1;
    }

    public static int mudarDiretorioTrabalho(String newPath) {
        String barra = System.getProperty("file.separator");
        String pathAtual = System.getProperty("user.dir");
        int points = 0;
        File dir;
        int[] fr;
        if (newPath.startsWith("..")) {
            int x;
            for(x = 0; x < newPath.length() && newPath.charAt(x) == '.'; ++x) {
                ++points;
            }

            for(x = 0; x < points - 1; ++x) {
                pathAtual = pathAtual.substring(0, pathAtual.lastIndexOf(barra));
            }

            dir = new File(pathAtual);
        } else if (pathAtual.contains(newPath)) {
            dir = new File(pathAtual.substring(0, pathAtual.indexOf(newPath)) + newPath);
            pathAtual = dir.getPath();
        } else {
            pathAtual = pathAtual + barra + newPath;
            dir = new File(pathAtual);
        }

        if (dir.exists() && dir.isDirectory()) {
            System.setProperty("user.dir", pathAtual);
            return 0;
        } else {
            System.out.println("Caminho inválido");
            return 1;
        }
    }

    public static int reconhecerSentenca(String arquivo) throws IOException {
        BufferedReader saida = new BufferedReader(new FileReader(Utilidades.getSistemPath(arquivo)));
        String out = "";
        while(saida.ready()) {
            out =  out + saida.readLine() + '\n';
        }
        boolean pass = false;

        if (!out.isBlank() && !out.isEmpty()){
            if (out.startsWith("public ")){
                pass = isMethod(out);
            }
            
        }

        if(pass){
            System.out.println("Tudo certo por aqui");
        }else {
            System.out.println("A sentença está errada");
        }

        return 0;
    }

    private static boolean isMethod(String sentenca){
        sentenca = sentenca.trim();

        if (sentenca.contains(" [] ")){
            sentenca = sentenca.replace(" [] ", "[]");
        }

        if (Utilidades.avaliarDelimitadores(sentenca)){

            Character[] delimitadores = {'(',')','{','}'};
            List<String> sentencas;
            if(Utilidades.existsChars(sentenca,delimitadores)){

                Character[] delimitadoresPossiveis = {'(',')','{','}',','};

                sentencas = dividir(sentenca,delimitadoresPossiveis);

                if (sentencas.size() >= 6){

                    if (declaration(sentencas.get(0), sentencas.get(1), sentencas.get(2)) && sentencas.get(3).equals("(")){

                        int x;
                        for (x=4; x < sentencas.size(); x++){
                            if (sentencas.get(x).equals(")")){
                                break;
                            }
                        }

                        if (parametros(sentencas, 3, x) && sentencas.get(x+1).equals("{")){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }



    private static boolean isType(String sentencas){
        sentencas = sentencas.trim();

        if (Character.isJavaIdentifierStart(sentencas.charAt(0))){
            String[] primitivas = {"int", "boolean"};
            String dem = "[]";
                if(sentencas.split(" ").length == 1) {
                    for (String primitiva : primitivas) {
                        if (primitiva.equals(sentencas)) {
                            return true;
                        }
                    }
                    if(sentencas.equals(primitivas[0] + dem)){
                        return true;
                    }
                    return isIdentifier(sentencas);
                }else{
                    if (sentencas.equals(primitivas[0] + " " + dem)){
                        return true;
                    }

                    return isIdentifier(sentencas);
                }
        }
        
        return false;
    }


    //Reconhecedor de var; ele se comunica com isType() e isIdentifier
    private static boolean isVar(String sentencas){
        boolean isType, isIdentifier;
        int fim;
        if (sentencas.contains("[") || sentencas.contains("]")){
            if (sentencas.indexOf("]") > sentencas.indexOf("[")){
                fim = sentencas.indexOf("]");
            }else {
                fim = sentencas.indexOf("[");
            }
            //System.out.println(sentencas.substring(0, fim + 1));
            isType = isType(sentencas.substring(0, fim + 1));

        }else if (!sentencas.contains(" ")){
            if (!sentencas.contains(";")){
                isType = isType(sentencas);
            }else {
                isType = false;
            }
            fim = sentencas.lastIndexOf(sentencas);
        }else {
            fim = sentencas.indexOf(" ");
            isType = isType(sentencas.substring(0, fim - 1));
        }
        String subSent = sentencas.substring(fim + 1, sentencas.length() - 1).trim();
        isIdentifier = isIdentifier(subSent);

        sentencas.trim();
        sentencas = sentencas.replace("\n", "").replace("\r", "");
        if (isIdentifier && isType && sentencas.charAt(sentencas.length()-1) == ';'){
            return true;
        }else {
            return false;
        }

    }


    //isVarM é o isVar sem ";"
    private static boolean isVarSpecial(String sentencas){
        boolean isType, isIdentifier;
        int fim;
        if (sentencas.contains("[") || sentencas.contains("]")){
            if (sentencas.indexOf("]") > sentencas.indexOf("[")){
                fim = sentencas.indexOf("]");
            }else {
                fim = sentencas.indexOf("[");
            }
            //System.out.println(sentencas.substring(0, fim + 1));
            isType = isType(sentencas.substring(0, fim + 1));

        }else if (!sentencas.contains(" ")){
            isType = isType(sentencas);
            fim = sentencas.lastIndexOf(sentencas);
        }else {
            fim = sentencas.indexOf(" ");
            isType = isType(sentencas.substring(0, fim - 1));
        }
        String subSent = sentencas.substring(fim + 1, sentencas.length() - 1).trim();
        isIdentifier = isIdentifier(subSent);

        sentencas.trim();
        sentencas = sentencas.replace("\n", "").replace("\r", "");
        if (isIdentifier && isType){
            return true;
        }else {
            return false;
        }

    }

    //reconhecedor de Identifier; n se cominica com nenhum outro metodo a não ser que seja requisitado pelo metodo em questão
    private static boolean isIdentifier(String sentencas){
        if (!Character.isJavaIdentifierStart(sentencas.charAt(0))){
            return false;
        }

        for (int i = 0; i<sentencas.length(); i++){
            if (!Character.isJavaIdentifierPart(sentencas.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private static List<String> dividir(String sentenca, Character[] delimitadores){
        List<String> palavras;
        List<Character> letras = new ArrayList<>();

        for (int x=0; x < sentenca.length(); x++){
            letras.add(sentenca.charAt(x));
        }

        palavras = Utilidades.montarLisString(letras,delimitadores,' ');

        return palavras;
    }

    private static boolean declaration(String modifier, String type, String identifier){
        return (modifier.equals("public") && isType(type) && isIdentifier(identifier));
    }

    private static boolean parametros(List<String> sentencas, int _init, int _end){
        int init = _init;
        int end = _end - 1;
        int res = end - init++;
        System.out.println(res);

        if (res != 0){
            if (res%2 != 0){
                int aux = init;
                for (int x=init; x < end; x = x + 3){
                    if (sentencas.get(x).equals(",") || sentencas.get(x).equals(")")){
                        if (!parametros(sentencas,init,x)){
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }else {
                if (!isVarSpecial(sentencas.get(init) + " " + sentencas.get(end))){
                    return false;
                }
            }
        }

        return true;
    }


    private ComandosInternos() {

    }
}
