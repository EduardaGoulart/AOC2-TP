package tp;

/**
 * PFifo
 */
public class PFIFO implements Politica{

    @Override
    public Bloco getBloco(Cache cache, int endereco){

        Conjunto conjunto = cache.getConjunto(cache.calcularIndex(endereco));
        Bloco[] bloco = conjunto.getBlocos();

        int i = conjunto.getIndexFIFO();
        conjunto.incrementarIndexFIFO();

        return bloco[i];
    }
}