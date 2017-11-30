package tp;

/**
 * Bloco
 */
public class Bloco {

    private int tag;
    private char mesi;
	private boolean recentementeUsado;
	private int usos; 

    protected Bloco(Conjunto conjunto){

        tag = 0;
        mesi = 'I';
		recentementeUsado = false;
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

	protected void setRecentementeUsado(boolean recentementeUsado) {
		
		this.recentementeUsado = recentementeUsado;
		if(recentementeUsado == true){
			
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