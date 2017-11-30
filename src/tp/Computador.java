package tp;

/**
 * Computador
 */
public class Computador {

    private final CPU[] cpus;

    private final Barramento barramento;

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