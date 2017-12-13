package tp;

public class Barramento {

    private final CPU[] cpus; //vetor com a quantidade de CPUs
    
    //construtor que gera um vetor de CPUs do tamanho da quantidade de CPUs
    protected Barramento(int nCPUs){

        cpus = new CPU[nCPUs];
    }

    protected void setCPU(int index, CPU cpu){

        cpus[index] = cpu;
    }

    protected CPU getCPU(int index){
        
        return cpus[index];
    }
    
    /*Envia um sinal do barramento conferindo se deu miss ou hit*/
	public void enviarSinal(String sinal, int endereco, CPU origem) {
		
		/*Caso o sinal passado seja um erro na leitura, percorre
		 * toda a CPU buscando quantos hits do endereço ocorrerá,
		 * caso aconteça mais que um acerto, significa que ele está na cache
		 * portanto a CPU origem recebe o sinal do endereço*/
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
        
        /*Caso o sinal recebido seja um aceto ou erro na escrita
         * percorre toda a CPU buscando pelo endereço para receber seu sinal e saber se 
         * irá escrever ou não na cache, mudando ainda seu status*/
        if(sinal.equalsIgnoreCase("write miss") || sinal.equalsIgnoreCase("write hit")){

            for(CPU cpu : cpus){

                if(cpu != origem){

                    cpu.receberSinalWrite(endereco);
                }
            }
        }
	}

}