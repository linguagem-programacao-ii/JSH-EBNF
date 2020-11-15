import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        BufferedReader saida = new BufferedReader(new FileReader(arquivo));
        String out = "";
        while(saida.ready()) {
            out =  out + saida.readLine() + '\n';
        }

        //System.out.println(isType(out));
        System.out.println(isVar(out));
        return 0;
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
                    if (sentencas.equals(primitivas[0] + " " + dem) || sentencas.equals(primitivas[0] + dem)){
                        return true;
                    }
                    for (int i=0; i < sentencas.length(); i++){
                        if (!Character.isJavaIdentifierPart(sentencas.charAt(i))){
                            return false;
                        }
                    }
                    return true;
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

    //reconhecedor de Identifier; n se cominica com nenhum outro metodo a não ser que seja requisitado pelo metodo em questão
    private static boolean isIdentifier(String sentencas){
        if (!Character.isJavaIdentifierStart(sentencas.charAt(0))){
            return false;
        }
        for (int i = 0; i<sentencas.length()-1; i++){
            if (!Character.isJavaIdentifierPart(sentencas.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private ComandosInternos() {

    }
}
