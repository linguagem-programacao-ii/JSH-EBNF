import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComandoPrompt {
    private final String nome;
    private final String[] argumentos;


    public ComandoPrompt(String comando) {
        this.nome = comando.contains(" ") ? comando.substring(0, comando.indexOf(32)).toLowerCase() : comando.toLowerCase();
        this.argumentos = comando.contains(" ") ? comando.replace(this.nome, "").split(" ") : null;
    }

    public String getNome() {
        return this.nome;
    }

    public int getArgumentosSize(){
        try {
            return argumentos.length;
        }catch (NullPointerException n){
            return 0;
        }
    }

    public List<String> getArgumentos() {
        return Collections.unmodifiableList(Arrays.asList(this.argumentos));
    }
}