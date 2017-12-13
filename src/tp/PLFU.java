package tp;

/**
 * PLFU(o último mais frequentemente usado)
 */
public class PLFU implements Politica{
	
	/*cada bloco na posição usada recebe um valor, quanto menor o valor
	 * significa que ele foi menos frequentemente usado, 
	 * portanto ele será o bloco a ser excluído*/
	@Override
	public Bloco getBloco(Cache cache, int endereco) {
        
        Conjunto conjunto = cache.getConjunto(cache.calcularIndex(endereco));
        Bloco[] blocos = conjunto.getBlocos();
        Bloco blocoMenosUsado = null;

        int menorUso = Integer.MAX_VALUE;

        for(Bloco bloco : blocos){

            if(bloco.getUsos() < menorUso){

                menorUso = bloco.getUsos();
                blocoMenosUsado = bloco;
            }
        }
        blocoMenosUsado.setRecentementeUsado(false);
        
        return blocoMenosUsado;
    }
    
    public String toString(){
        
        return "LFU";
    }
}