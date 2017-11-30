package tp;

/**
 * PLFU
 */
public class PLFU implements Politica{

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
}