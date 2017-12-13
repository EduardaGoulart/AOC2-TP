package tp;

/**
 * Conjunto
 */
public class Conjunto {
    
    private final Bloco[] blocos; //possui um vetor de blocos
    private int indexFIFO; //possui um valor para a política do FIFO
    
    /*Seta o bloco com a quantidade de blocos e a política de first in first out como zero*/
    protected Conjunto(int SETSIZE){
        
        blocos = new Bloco[SETSIZE];
        
        for(int i = 0; i < blocos.length; i++){
            
            blocos[i] = new Bloco(this);
        }
        
        indexFIFO = 0;
    }
    
	protected Bloco[] getBlocos() {
        return blocos;
    }
    
    protected int getIndexFIFO() {
        return indexFIFO;
    }
    
    /*Quando o bloco é usado, o valor é atualizado somando mais um mod o tamanho do bloco*/
    protected void incrementarIndexFIFO() {
        
        indexFIFO++;
        indexFIFO = indexFIFO % blocos.length;
    }
}