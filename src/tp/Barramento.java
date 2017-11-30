package tp;

public class Barramento {

    private final CPU[] cpus;

    protected Barramento(int nCPUs){

        cpus = new CPU[nCPUs];
    }

    protected void setCPU(int index, CPU cpu){

        cpus[index] = cpu;
    }

    protected CPU getCPU(int index){
        
        return cpus[index];
    }
    
	public void enviarSinal(String sinal, int endereco, CPU origem) {

        if(sinal.equalsIgnoreCase("read miss")){

            int hits = 0;

            for(CPU cpu : cpus){
                if(cpu != origem){
                    if(cpu.receberSinalRead(endereco)){
                        
                        ++hits;
                    }
                }
            }

            if(hits > 0){

                origem.receberSinalRead(endereco);
            }
        }
        if(sinal.equalsIgnoreCase("write miss") || sinal.equalsIgnoreCase("write hit")){

            for(CPU cpu : cpus){

                if(cpu != origem){

                    cpu.receberSinalWrite(endereco);
                }
            }
        }
	}

}