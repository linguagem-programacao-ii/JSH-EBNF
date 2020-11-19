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
        File dir = new File((String) nomeDir.get());
        List<File> listArq = Arrays.asList(dir.listFiles());
        StringBuilder out = new StringBuilder();

        for (int x = 0; x < listArq.size(); ++x) {
            out.append(((File) listArq.get(x)).getName() + '\n');
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
            for (x = 0; x < newPath.length() && newPath.charAt(x) == '.'; ++x) {
                ++points;
            }

            for (x = 0; x < points - 1; ++x) {
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
        while (saida.ready()) {
            out = out + saida.readLine() + '\n';
        }
        out = out.trim();
        boolean pass = false;

        if (!out.isBlank() && !out.isEmpty()) {
            String[] separacao = out.split(" ");
            if (separacao[0].equals("public")) {
                pass = isMethod(out);
            } else if (separacao.length == 1) {
                pass = isType(out);
            } else {
                if (separacao.length == 2 && out.endsWith("]")) {
                    pass = isType(out);
                } else if (separacao.length == 2 && out.endsWith(" ;")){
                    out = out.replace(" ;", ";");
                    int i = reconhecerSentenca(out);
                } else if (separacao.length >= 2){

                }
            }
        }

        if (pass) {
            System.out.println("Tudo certo por aqui");
        } else {
            System.out.println("A sentença está errada");
        }

        return 0;
    }

    private static boolean isType(String sentencas) {
        sentencas = sentencas.trim();
        if (!sentencas.isEmpty() && !sentencas.isBlank()) {
            if (Character.isJavaIdentifierStart(sentencas.charAt(0))) {
                String aux;
                boolean pass = false;
                if (sentencas.endsWith("]")) {
                    if (sentencas.contains("[")) {
                        aux = sentencas.substring(sentencas.lastIndexOf('['), sentencas.lastIndexOf("]"));
                        if (aux.equals("[]")) {
                            pass = true;
                        } else {
                            String replace = "[";
                            for (int x = 1; x < (aux.length() - 1); x++) {
                                if (aux.charAt(x) != ' ') {
                                    return false;
                                }
                                replace = replace + ' ';
                            }
                            replace = replace + ']';
                            sentencas = sentencas.replace(replace, "[]");
                            pass = true;
                        }
                    }else {
                        return false;
                    }
                }

                if (pass) {
                    if (sentencas.equals("int[]")){
                        return true;
                    }
                    String[] div = sentencas.split(" ");
                    if (div.length == 2){
                        if (div[0].equals("int") && div[1].equals("[]")){
                            return true;
                        }
                    }
                } else {
                    return isIdentifier(sentencas);
                }
            }
        }
        return false;
    }


    //Reconhecedor de var; ele se comunica com isType() e isIdentifier
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
            if (!isType){
                return false;
            }
        }else if (!sentencas.contains(" ")){
            if (!sentencas.contains(";")){
                isType = isType(sentencas);
            }else {
                return false;
            }
            fim = sentencas.lastIndexOf(sentencas);
        }else {
            fim = sentencas.indexOf(" ");
            isType = isType(sentencas.substring(0, fim - 1));
        }
        String subSent = sentencas.substring(fim + 1, sentencas.length() - 2).trim();
        isIdentifier = isIdentifier(subSent);

        sentencas.trim();
        sentencas = sentencas.replace("\n", "").replace("\r", "");

        int count = 0;
        for (int i = 0; i<sentencas.length(); i++){
            if (sentencas.charAt(i) == ' ' && Character.isJavaIdentifierStart(sentencas.charAt(i+1))){
                count = count+1;
            }
        }
        if (count != 1){
            return false;
        }

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
            isType = isType(sentencas.substring(0, fim + 1));
            if (!isType){
                return false;
            }

        }else if (!sentencas.contains(" ")){

            isType = isType(sentencas);
            fim = sentencas.lastIndexOf(sentencas);
        }else {
            fim = sentencas.indexOf(" ");
            System.out.println(fim);
            isType = isType(sentencas.substring(0, fim));
        }
        String subSent = sentencas.substring(fim + 1, sentencas.length() - 2).trim();
        isIdentifier = isIdentifier(subSent);

        sentencas.trim();

        int count = 0;
        for (int i = 0; i<sentencas.length(); i++){
            if (sentencas.charAt(i) == ' ' && Character.isJavaIdentifierStart(sentencas.charAt(i+1))){
                count = count+1;
            }
        }
        if (count != 1){
            return false;
        }

        if (isIdentifier && isType){
            return true;
        }else {
            return false;
        }

    }
    //reconhecedor de Identifier; n se cominica com nenhum outro metodo a não ser que seja requisitado pelo metodo em questão
    private static boolean isIdentifier(String sentencas){
        if (!sentencas.isBlank() && !sentencas.isEmpty()) {
            if (!Character.isJavaIdentifierStart(sentencas.charAt(0))) {
                return false;
            }

            for (int i = 0; i < sentencas.length(); i++) {
                if (!Character.isJavaIdentifierPart(sentencas.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isMethod(String sentenca){
        sentenca = sentenca.trim();
        if(sentenca.contains("(")){
            String aux = sentenca.substring(0,sentenca.indexOf('(') - 1).trim(); //Até antes de encontrar (
            if (declaration(aux)){
                aux = sentenca.substring(sentenca.indexOf('('), sentenca.length() - 1).trim();
                if (aux.contains(")")){
                    aux = sentenca.substring(sentenca.indexOf('(') + 1, sentenca.lastIndexOf(')') - 1).trim();
                    boolean pass = false;
                    if (!aux.isEmpty() && !aux.isBlank()){
                        pass = parametros(aux.trim());
                    }else {
                        pass = true;
                    }
                    if (pass){
                        aux = sentenca.substring(sentenca.indexOf(')'), sentenca.length() - 1).trim();
                        if (aux.contains("{")){
                            aux = sentenca.substring(sentenca.indexOf(')') + 1, sentenca.indexOf('{') - 1).trim();
                            if (aux.isBlank() || aux.isEmpty()){
                                aux = sentenca.substring(sentenca.indexOf('{'), sentenca.length() - 1).trim();
                                if (aux.endsWith("}")){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }



    private static boolean declaration(String sentenca){
        if (!sentenca.isBlank() && !sentenca.isEmpty()) {
            String[] palavras = sentenca.split(" ");
            if (palavras[0].equals("public")) {
                if (isIdentifier(palavras[palavras.length - 1])) {
                    String resto = "";
                    for (int x = 1; x < palavras.length - 1; x++) {
                        resto = resto + ' ' + palavras[x];
                    }
                    resto = resto.trim();
                    return isType(resto);
                }
            }
        }

        return false;
    }

    public static boolean parametros(String sentenca){
        if (!sentenca.isEmpty() && !sentenca.isBlank()) {
            if (sentenca.contains(",")) {
                if (!sentenca.endsWith(",") && !sentenca.startsWith(",")){

                    String[] palavras = sentenca.split(",");
                    for (int x=0; x < palavras.length; x++){
                        if (!parametros(palavras[x])){
                            return false;
                        }
                    }
                    return true;
                }
            }else{
                if (!isVarSpecial(sentenca)){
                    return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean avaliarDuplicidade(String sentenca){
        String aux = sentenca.trim().replace(',',' ').trim();
        String nomeRepetido = "";

        if (!aux.isBlank() && !aux.isEmpty()){
            String[] itens = sentenca.split(",");
            for (int x=0; x < itens.length; x++){
                if (itens[x].split(" ").length == 3){
                    
                }
            }
        }

        return false;
    }

    private ComandosInternos() {

    }
}
