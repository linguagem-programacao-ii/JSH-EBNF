import Comando.ComandoPrompt;
import Comando.ComandosInternos;
import Uitlidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public final class Jsh {

    private static String dir;
    private static String name;
    private static String UID;
    private static String barra;

    public static void promptTerminal() {
        name = System.getProperty("user.name");
        UID = Utilidades.getUID(name);
        barra = System.getProperty("file.separator");

        while(true) {
            exibirPrompt();
            ComandoPrompt comandoEntrado = lerComando();
            System.out.println();
            executarComando(comandoEntrado);
            System.out.println();
        }
    }

    public static void exibirPrompt() {
        dir = System.getProperty("user.dir");
        System.out.print(name + "#" + UID + ":" + dir + "% ");
    }

    public static ComandoPrompt lerComando() {
        Scanner scn = new Scanner(System.in);
        String comando = scn.nextLine();
        return new ComandoPrompt(comando);
    }

    public static void executarComando(ComandoPrompt comando) {
        int out = 1;

        switch (comando.getNome()){
            case "encerrar":
                System.exit(0);
                break;
            case "relogio":
                out = ComandosInternos.exibirRelogio();
                break;
            case "ls":
                out = ComandosInternos.escreverListaArquivos(Optional.of(dir));
                break;
            case "add":
                if (comando.getArgumentosSize() > 1)
                    out = ComandosInternos.criarNovoDiretorio(comando.getArgumentos().get(1), dir);
                break;
            case "del":
                if (comando.getArgumentosSize() > 1)
                    out = ComandosInternos.apagarDiretorio((String)comando.getArgumentos().get(1), dir);
                break;
            case "cd":
                if (comando.getArgumentosSize() > 1)
                    out = ComandosInternos.mudarDiretorioTrabalho((String)comando.getArgumentos().get(1));
                break;
            case "ts":
                if (comando.getArgumentosSize() > 1)
                    try {
                        out = ComandosInternos.reconhecerSentenca(comando.getArgumentos().get(1));
                    }catch (IOException i){
                        System.out.println(i.getMessage());
                        i.getCause();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        e.getCause();
                        e.printStackTrace();
                        System.out.println("Erro!!!");
                    }
                break;
            default:
                out = executarPrograma(comando);
        }

        if (out != 0)
            System.out.println("Ocorreu um erro");
    }

    public static int executarPrograma(ComandoPrompt comando) {
        int saida = 1;
        File dirA = new File(dir);
        List<File> listArq = Arrays.asList(Objects.requireNonNull(dirA.listFiles()));
        boolean executar = false;

        for(int x = 1; x < listArq.size() && !executar; ++x) {
            executar = ((File)listArq.get(x)).getName().equals(comando.getNome());
        }

        if (executar) {
            ProcessBuilder construtor = (new ProcessBuilder(new String[0])).directory(dirA);
            File comandA = new File(dir + barra + comando.getNome());
            if (comandA.canExecute()) {
                try {
                    Process real = construtor.command("." + barra + comando.getNome()).start();
                    saida = real.waitFor();
                    System.out.println((new BufferedReader(new InputStreamReader(real.getInputStream()))).readLine());
                } catch (InterruptedException | IOException var8) {
                    System.out.println(var8.getMessage());
                }
            } else {
                System.out.println("O Jshell não tem permissão para executar o arquivo");
            }
        } else {
            System.out.println("Programa/comando não existe");
        }

        return saida;
    }

    public static void main(String... args) {

        promptTerminal();
    }

    private Jsh() {

    }
}
