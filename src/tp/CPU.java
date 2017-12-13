package tp;

import java.io.*;

/**
 * CPU
 */
public class CPU {

    private final Cache l1;
    private final Cache l2;
    private final int id;
    private Barramento barramento;
    private PrintWriter escritor;

    private float acertos = 0; //conta a quantidade de acertos
    private float erros = 0; //conta a quantidade de erros de acesso
    
    /*Cria uma CPU, que possui caches L1 e L2, e um id. Escreve os processos no arquivo*/
    protected CPU(Cache l1, Cache l2, int id){

        this.l1 = l1;
        this.l2 = l2;
        this.id = id;

        try {
            escritor = new PrintWriter(new File("cpu" + id + "Trace.txt"));
            
		} catch (FileNotFoundException e) {
			
		    System.out.println(e.getMessage());
        }
    }

    protected int getID(){
        return id;
    }

    protected void setBarramento(Barramento barramento){
        
        this.barramento = barramento;
    }
    
    /*Lê uma palavra da cache*/
    protected void readWord(int endereco){

        Bloco blocoL1 = null;
        Bloco blocoL2 = null;
        
        /*Se o endereço não estiver em um bloco da cache L1, haverá duas verificações
         * para conferir se ele está ou não na l2 */
        if(!l1.hasBlocoValido(endereco)){
        	
        	//escreve no arquivo o processo
            escritor.println("L1: MISS - Endereço " + Integer.toBinaryString(endereco));
            
            /*Caso o endereço esteja na L2, escreve no arquivo que está acontecendo na L2
             * busca por aquele endereço dentro do da cachê e seta o bloco como recentemente 
             * usado, para o política LRU;
             * Para a cache L1, será substituido um bloco utilizando uma das políticas de 
             * substituição. Na L1 será selecionado o protocolo MESI para atualizar o status do endereço
             * e em seguida irá marcar como recentemente usado*/
            if(l2.hasBlocoValido(endereco)){

                acertos++;
                escritor.println("L2: HIT - Endereço " + Integer.toBinaryString(endereco));
                blocoL2 = l2.buscarBloco(endereco);
                blocoL2.setRecentementeUsado(true);

                blocoL1 = l1.getPolitica().getBloco(l1, endereco);
                escritor.println("L1: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));
                
                blocoL1.setMESI(blocoL2.getMESI());
                blocoL1.setTag(l1.calcularTag(endereco));
                blocoL1.setRecentementeUsado(true);
                escritor.println("L1: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));

            }
            /*Caso não esteja na L2, o usuário seleciona uma política de substituição para substituir um valor
             * existente na L2 por este endereço. Calcula a tag do endereço(selecionar o fecho de endereços que será
             * copiado pra aquele bloco) e seleciona o estado como exclusivo e seta o bloco como recentemente usado.
             * O mesmo processo será repetido pro bloco da L1*/
            else{

                escritor.println("L2: MISS - Endereço " + Integer.toBinaryString(endereco));
                erros++;

                blocoL2 = l2.getPolitica().getBloco(l2, endereco);
                escritor.println("L2: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL2.getTag()));
                
                blocoL2.setTag(l2.calcularTag(endereco));
                blocoL2.setMESI('E');
                blocoL2.setRecentementeUsado(true);
                escritor.println("L2: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL2.getTag()));
                
                blocoL1 = l1.getPolitica().getBloco(l1, endereco);
                escritor.println("L1: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));

                blocoL1.setTag(l1.calcularTag(endereco));
                blocoL1.setMESI('E');
                blocoL1.setRecentementeUsado(true);
                escritor.println("L1: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL2.getTag()));

                barramento.enviarSinal("read miss", endereco, this);
                escritor.println("Enviando Read Miss pelo barramento - Endereço " + Integer.toBinaryString(endereco));
            }
        }
        
        /*Caso o bloco esteja na L1, será escrito o processo no arquivo*/
        else{

            escritor.println("L1: HIT - Endereço " + Integer.toBinaryString(endereco));
            acertos++;
        }
        escritor.flush();
    }

    /*Método para escrever uma palavra*/
    protected void writeWord(int endereco){

        Bloco blocoL1 = null;
        Bloco blocoL2 = null;
        
        /*Caso o endereço já esteja na l1, será exibida uma mensagem de aviso ao usuário
         * irá buscar o trecho de endereços copiados naquele bloco da l1, o valor será setado como
         * modificado e a política de substituição LRU receberá atualização daquele endereço,
         * o mesmo será feito para o bloco que irá buscar na l2*/
        if(l1.hasBlocoValido(endereco)){
            
            escritor.println("L1: HIT - Endereço " + Integer.toBinaryString(endereco));
            acertos++;

            barramento.enviarSinal("write hit", endereco, this);
            escritor.println("Enviando Write Hit pelo barramento Endereço - " + Integer.toBinaryString(endereco));

            blocoL1 = l1.buscarBloco(endereco);

            blocoL1.setMESI('M');
            blocoL1.setRecentementeUsado(true);

            blocoL2 = l2.buscarBloco(endereco);

            blocoL2.setMESI('M');
            blocoL2.setRecentementeUsado(true);
        }
        /*Caso o endereço não esteja em nenhum bloco da l1*/
        else{
        	
        	/*Confere então se ele está na l2, 
        	 * caso esteja, printa uma mensagem de acerto, o escritor recebe uma string binária
        	 * envia um sinal de write hit pro barramento.
        	 * Com o endereço do bloco na l1, analisa a política de substituição e substitui um bloco 
        	 * ocupado na cache l1 por aquele endereço.
        	 * Busca o mesmo endereço na l2, seta seu estado como modificado e atualiza a política do LRU
        	 * como tru(mostrando que foi usado recentemente)*/
            if(l2.hasBlocoValido(endereco)){
                
                escritor.println("L2: HIT - Endereço " + Integer.toBinaryString(endereco));
                acertos++;

                barramento.enviarSinal("write hit", endereco, this);

                escritor.println("Enviando Write Hit pelo barramento - Endereço " + Integer.toBinaryString(endereco));
                
                blocoL1 = l1.getPolitica().getBloco(l1, endereco);
                escritor.println("L1: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));
                
                blocoL1.setTag(l1.calcularTag(endereco));
                blocoL1.setMESI('M');
                blocoL1.setRecentementeUsado(true);
                escritor.println("L1: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));

                blocoL2 = l2.buscarBloco(endereco);
    
                blocoL2.setMESI('M');
                blocoL2.setRecentementeUsado(true);
            }
            
            /*Caso não esteja na L2, envia um sinal de write miss pro barramento,
             * com o endereço e a política de substituição, substitui um bloco no L2 por esse adicionado
             * Calcula a tag do endereço(para saber o fecho de endereços que será adicionado naquele 
             * bloco). Seta o bloco como modificado. Replica o mesmo processo para o bloco da cache L1*/
            else{

                barramento.enviarSinal("write miss", endereco, this);
                erros++;

                blocoL2 = l2.getPolitica().getBloco(l2, endereco);
                escritor.println("L2: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL2.getTag()));
                
                blocoL2.setTag(l2.calcularTag(endereco));
                blocoL2.setMESI('M');
                blocoL2.setRecentementeUsado(true);
                escritor.println("L2: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL2.getTag()));

                blocoL1 = l1.getPolitica().getBloco(l1, endereco);
                escritor.println("L1: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));
                
                blocoL1.setTag(l1.calcularTag(endereco));
                blocoL1.setMESI('M');
                blocoL1.setRecentementeUsado(true);
                escritor.println("L1: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));
            }
        }
        escritor.flush();
    }

    /*Recebe um endereço e lê a CPU buscando por ele.
     * Se o endereço estiver em um bloco na l1, cria uma instância do bloco com o endereço seta o bloco como Shared,
     * Se o endereço estiver na l2, cria uma instância do bloco referenciando o endereço, 
     * seta como compartilhado o bloco e retorna true mostrando que foi encontrado.
     * Caso não seja encontrado na l2 escreve o endereço lá e retorna falso*/
    protected boolean receberSinalRead(int endereco){
    	
    	//converte o endereço pra uma string binária
        escritor.println("Recebendo Sinal Read Miss do barramento Endereço - " + Integer.toBinaryString(endereco));

        if(l1.hasBlocoValido(endereco)){

            Bloco blocoL1 = l1.buscarBloco(endereco);
            
            blocoL1.setMESI('S');
        }
        if(l2.hasBlocoValido(endereco)){

            Bloco blocoL2 = l2.buscarBloco(endereco);
            
            blocoL2.setMESI('S');

            return true;
        }

        escritor.flush();

        return false;
    }
    
    /*Recebe um sinal binário, confirma se o endereço já existe na l1,
     * se já existir então seu sinal é inválido. Confere se existe na l2
     * caso exista, seu sinal será inválido(vez que não será possível escrever
     * o mesmo endereço duas vezes. Caso não esteja em nenhum dos dois,
     * irá escrever o endereço e retornar True(mostrando que foi possível escrever)*/
    protected void receberSinalWrite(int endereco){

        escritor.println("Recebendo Sinal Write do barramento Endereço - " + Integer.toBinaryString(endereco));

        if(l1.hasBlocoValido(endereco)){
            
            Bloco blocoL1 = l1.buscarBloco(endereco);

            blocoL1.setMESI('I');
            blocoL1.setRecentementeUsado(false);

        }
        if(l2.hasBlocoValido(endereco)){

            Bloco blocoL2 = l2.buscarBloco(endereco);

            blocoL2.setMESI('I');
            blocoL2.setRecentementeUsado(false);
        }
        
        escritor.flush();
    }
    
    /*Calcula a taxa de acertos no acesso à memória*/
    protected float getTaxaDeAcerto(){
        return acertos * 100 / (acertos + erros);
    }
}