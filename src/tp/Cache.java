package tp;

/**
 * Cache
 */
public class Cache {

    private final Conjunto[] conjuntos; //vetor de Conjuntos que serão adicionados na cache
    private final Politica politica; //política de substituição que será utilizada
    private final int BLOCKSIZE; //tamanho do bloco
    
    
    protected Cache(int CACHESIZE, int numVias, int numBlocos, Politica politicaDeSubstituicao){

        conjuntos = new Conjunto[numVias]; //cada conjunto possui como tamanho a quantidade de vias
        BLOCKSIZE = CACHESIZE / numBlocos; //tamanho do bloco é o tamanho ca cache dividido pela quantidade de blocos
        politica = politicaDeSubstituicao; //recebe a política de substituição escolhida

        for(int i = 0; i < conjuntos.length; i++){ 
            
            /*Preenche cada posição do conjunto com a quantidade de blocos por conjunto, 
            * dado pela quantidade de blocos dividida pelo número de vias*/
            conjuntos[i] = new Conjunto(numBlocos / numVias);
        }
    }
    
	protected Conjunto[] getConjuntos(){
        
        return conjuntos;
    }
    
    protected Conjunto getConjunto(int index) throws ArrayIndexOutOfBoundsException{
        
        return conjuntos[index];
    }
    
    public Politica getPolitica() {
        
        return politica;
    }

    /*cada índice determina a posição que a palavra ocupa no bloco, e é calculado
     * pela divisão do valor do endereço pelo tamanho do bloco, mod o tamanho do conjunto*/
    protected int calcularIndex(int endereco){

        return  (endereco / BLOCKSIZE) % conjuntos.length;
    }
    
    /*para calcular os bits da tag basta dividir o endereço pelo tamanho do bloco e em seguida pelo tamanho do conjunto*/
    protected int calcularTag(int endereco){

        return endereco / BLOCKSIZE / conjuntos.length;
    }

    /*método que irá buscar se o endereço está localizado naquele bloco,
     * para isso em cada bloco irá buscar pelo índice daquele endereço,
     * caso estiver lá, então retorna o bloco onde foi encontrado. Caso contrário,
     * retorna nulo mostrando que não foi possível encontrar o endereço*/
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
    
    /*Busca se existe o endereço no bloco, utilizando o método buscarBloco, caso exista
     * confere se o estado não é inválido, caso não seja o bloco é válido. Se o endereço não estiver
     * em um bloco ou o estado for inávlido, então aquele bloco é inválido*/
    protected boolean hasBlocoValido(int endereco){

        Bloco bloco = buscarBloco(endereco);

        if(bloco != null){
            if(bloco.getMESI() != 'I'){

                return true;
            }
        }

        return false;
    }
}