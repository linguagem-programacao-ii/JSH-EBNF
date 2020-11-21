package Uitlidades.Delimitador.Pilha;

public class Pilha<E> {
    private int maxSize;
    private E[] stackArray;
    private int topo;

    /*
    Atribua o parâmetro ao atributo maxSize, crie o array stackArray com o tamanho especificado no parâmetro,
    atribua -1 ao atributo topo.
    */
    public Pilha(int s){
        maxSize = s;
        stackArray = (E[]) new Object[maxSize];
        topo = -1;
    }

    /*
    Incremente 1 ao atributo topo, atribua o parâmetro ao elemento do array indicado como topo.
    A inserção deve ocorrer somente se a pilha não está cheia.
    */
    public void insert(E j) throws PilhaCheiaException {
        if(isFull() == false){
            stackArray[++topo] = j;
        }else{
            throw new PilhaCheiaException("Está cheia");
        }
    }

    /*
        Retorne o valor do elemento que está no topo da pilha, decremente 1 do atributo topo.
        Se a pilha está vazia, lance a exception PilhaVaziaException
    */
    public E remove() throws PilhaVaziaException {
        E temporario;
        if (!isEmpty()){
            temporario = stackArray[topo--];
        }else{
            throw new PilhaVaziaException("Está vazio");
        }
        return temporario;
    }

    /*
        Retorne o valor do elemento que está no topo da pilha
        Se a pilha está vazia, lance a exception PilhaVaziaException
    */
    public E getTopo() throws PilhaVaziaException{
        E temporario;
        if (!isEmpty()){
            temporario = stackArray[topo];
        }else {
            throw new PilhaVaziaException("Está vazio");
        }
        return temporario;
    }

    /*
        Retorne true se a pilha está vazia, senão retorne false.
    */
    public boolean isEmpty(){
        if (topo == -1){
            return true;
        }else {
            return false;
        }
    }

    /*
        Retorne true se a fila está cheia, senão retorne false.
    */
    public boolean isFull(){
        if (size() == maxSize){
            return true;
        }else {
            return false;
        }
    }

    /*
        Retorne a quantidade de elementos contidos na pilha
    */
    public int size(){
        return (topo+1);
    }


}