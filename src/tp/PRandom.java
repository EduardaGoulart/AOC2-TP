package tp;

import java.util.Random;

public class PRandom implements Politica{

    private final Random random;

    public PRandom(){

        random = new Random();
    }

    @Override
    public Bloco getBloco(Cache cache, int endereco){

        Conjunto conjunto = cache.getConjunto(cache.calcularIndex(endereco));
        Bloco[] bloco = conjunto.getBlocos();

        return bloco[random.nextInt(bloco.length)];
    }
}