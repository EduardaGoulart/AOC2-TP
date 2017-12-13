package tp;

import java.util.Random;

/*Política de substituição que escolhe um endereço random para substituir*/
public class PRandom implements Politica{

    private final Random random;

    public PRandom(){

        random = new Random();
    }
    
    /*Calcula dentro da quantidade de blocos do conjunto, um valor qualquer aleatório
     * o endereço que ocupar este valor será substituído*/
    @Override
    public Bloco getBloco(Cache cache, int endereco){

        Conjunto conjunto = cache.getConjunto(cache.calcularIndex(endereco));
        Bloco[] bloco = conjunto.getBlocos();

        return bloco[random.nextInt(bloco.length)];
    }

    public String toString(){

        return "Random";
    }
}