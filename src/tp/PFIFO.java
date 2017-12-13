package tp;

/**
 * PFifo(primeiro adicionado, será o primeiro a ser removido)
 */
public class PFIFO implements Politica{
	
	/*Sobrescreve a interface das políticas de substituição,
	 * cada índice de um bloco recebe um valor de incremento do seu estado
	 * de uso*/
    @Override
    public Bloco getBloco(Cache cache, int endereco){

        Conjunto conjunto = cache.getConjunto(cache.calcularIndex(endereco));
        Bloco[] bloco = conjunto.getBlocos();

        int i = conjunto.getIndexFIFO();
        conjunto.incrementarIndexFIFO();

        return bloco[i];
    }

    public String toString(){
        
        return "FIFO";
    }
}