package tp;

/**
 * Computador
 */
public class Computador {

    private final CPU[] cpus; //Cria um vetor com a quantidade de CPUs

    private final Barramento barramento; //uma referência do barramento
    
    /*Seta cada CPU com um sinal do barramento*/
    public Computador(CPU[] cpus){

        this.cpus = cpus;

        barramento = new Barramento(cpus.length);

        for(int i = 0; i < cpus.length; i++){

            barramento.setCPU(i, cpus[i]);
            cpus[i].setBarramento(barramento);
        }
    }

    protected CPU[] getCPUs(){

        return cpus;
    }
}