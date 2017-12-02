package tp;

/**
 * Conjunto
 */
public class Conjunto {
    
    private final Bloco[] blocos;
    private int indexFIFO;
    
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

    protected void incrementarIndexFIFO() {
        
        indexFIFO++;
        indexFIFO = indexFIFO % blocos.length;
    }
}