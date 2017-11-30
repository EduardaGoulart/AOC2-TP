package tp;

/**
 * Cache
 */
public class Cache {

    private final Conjunto[] conjuntos;
    private final Politica politica;
    private final int BLOCKSIZE;
    
    protected Cache(int CACHESIZE, int numVias, int numBlocos, Politica politicaDeSubstituicao){

        conjuntos = new Conjunto[numVias];
        BLOCKSIZE = CACHESIZE / numBlocos;
        politica = politicaDeSubstituicao;

        for(int i = 0; i < conjuntos.length; i++){

            conjuntos[i] = new Conjunto(numBlocos / numVias);
        }
    }

	protected Conjunto[] getConjuntos(){
        
        return conjuntos;
    }
    
    protected Conjunto getConjunto(int index) throws ArrayIndexOutOfBoundsException{
        
        return conjuntos[index];
    }

    protected int calcularIndex(int endereco){

        return (int) ((endereco / BLOCKSIZE) % conjuntos.length);
    }

    protected int calcularTag(int endereco){

        return endereco / BLOCKSIZE / conjuntos.length;
    }

    protected Bloco buscarBloco(int endereco){

        int tag = calcularTag(endereco);
        int index = calcularIndex(endereco);

        for(Bloco bloco : conjuntos[index].getBlocos()){

            if(bloco.getTag() == tag){

                return bloco;
            }
        }
        return null;
    }

    protected boolean hasBlocoValido(int endereco){

        Bloco bloco = buscarBloco(endereco);

        if(bloco != null){
            if(bloco.getMESI() != 'I'){

                return true;
            }
        }

        return false;
    }

    protected boolean writeHit(int endereco){
        
        Bloco bloco = buscarBloco(endereco);

        if(bloco != null){
            if(bloco.getMESI() != 'I'){

                return true;
            }
        }
        
        return false;
    }

	public Politica getPolitica() {
        
        return politica;
	}
}