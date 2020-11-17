package Uitlidades.Delimitador.Principal;
import Uitlidades.Delimitador.Pilha.Pilha;

public class Delimitadores {

    private Pilha<Character> pilha;
    private char[] chaveAberta = {'{','[','('};
    private char[] chaveFechada = {'}',']',')'};

    public Delimitadores(){
    };

    public char avalia(String text) throws SemCorrespondenteException{
        pilha = new Pilha<Character>(text.length());
        for (int xx=0; xx < text.length(); xx++) {
            for (int x = 0; x < 2; x++) {
                if (text.charAt(xx) == chaveAberta[x]){
                    this.pilha.insert(chaveAberta[x]);
                }else if (text.charAt(xx) == chaveFechada[x]){
                    if (this.pilha.size() > 0) {
                        if (this.pilha.remove() != chaveAberta[x]) {
                            return chaveFechada[x];
                        }
                    } else {
                            return text.charAt(xx);
                    }
                }
            }
        }
        if (this.pilha.isEmpty() == true) {
            return 's';
        }else {
            return pilha.remove();
        }
    };

}
