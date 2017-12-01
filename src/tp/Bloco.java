package tp;

/**
 * Bloco
 */
public class Bloco {

    private int tag; //tag do endereço
    private char mesi; //estado em que está no protocolo mesi
	private boolean recentementeUsado; //para marcar a política LRU
	private int usos; 

	/*Construtor, setando os valores iniciais*/
    protected Bloco(Conjunto conjunto){

        tag = 0; //endereço é 0 de início
        mesi = 'I'; //se encontra no estado Invalid, pois não está na cache
		recentementeUsado = false; //não há nada usado, seta como falso
		usos = 0;
    }

	protected int getTag() {
		return tag;
	}

	protected void setTag(int tag) {
		this.tag = tag;
	}

	protected char getMESI() {
		return mesi;
	}

	protected void setMESI(char mesi) {
		this.mesi = mesi;
	}

	protected boolean isRecentementeUsado() {
		return recentementeUsado;
	}
	
	/*Verifica o estado do valor, caso ele seja true, significa que esta sendo utilizado, incrementa
	 * a quantidade de usos, em cado do valor ser false, representa que não está utilizado e seta o valor
	 * de usos para zero novamente*/
	protected void setRecentementeUsado(boolean recentementeUsado) {
		
		this.recentementeUsado = recentementeUsado;
		if(recentementeUsado){
			
			++usos;
		}
		else{

			usos = 0;
		}
	}
	
	protected int getUsos(){

		return usos;
	}
}