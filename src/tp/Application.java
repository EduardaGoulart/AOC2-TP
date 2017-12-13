package tp;

import java.io.*;
import java.util.Scanner;

public class Application {
	
    private static Scanner scanner = new Scanner(System.in);
    
    private static Computador computador; //cria uma instância do computador
    
    /*Método usado para limpar a tela quando o usuário selecionar uma nova operação*/
    private static void limparTela() {  
    
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    private static void esperarENTER(){

        System.out.println("Pressioner ENTER para continuar");
        scanner.nextLine();
    }
    
    /*Lê a quantidade de blocos da cahce, confere se o valor digitado não foi zero
     * Caso não for, confere se o número de blocos é menor que o tamanho máximo permitido*/
    private static int lerNumBlocos(int maxBlocos){
        
        int nBlocos;
        
        System.out.print("Digite o número de blocos da Cache: ");

        do{
            try{
                nBlocos = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){

                nBlocos = 0;
            }
            if(!(nBlocos > 0 && (nBlocos & (nBlocos - 1)) == 0 && nBlocos <= maxBlocos)){
                
                System.out.print("Número inválido de blocos, digite novamente (");

                for(int i = 0; Math.pow(2, i) <= maxBlocos; ++i){
                    
                    System.out.print((int) Math.pow(2, i) + (Math.pow(2, i) != maxBlocos ? ", " : ""));
                }

                System.out.print("): "); 
            }

        }while(!(nBlocos > 0 && (nBlocos & (nBlocos - 1)) == 0 && nBlocos <= maxBlocos));

        return nBlocos;
    }
    
    /*Lê a quantidade de vias por bloco, caso a quantidade de vias seja 1, ela é totalmente associativa,
     * caso seja maior que 1, é diretamente mapeada, se for n, é associativa por conjunto*/
    private static int lerNumVias(int numBlocos){

        if(numBlocos == 1){

            return 1;
        }
        else{

            for(int i = 0; Math.pow(2, i) <= numBlocos; i++){

                if(i == 0){

                    System.out.println((i + 1) + ") Totalmente Associativa");
                }
                else if(Math.pow(2, i) == numBlocos){

                    System.out.println((i + 1) + ") Diretamente Mapeada");
                }
                else{

                    System.out.println((i + 1) + ") " + (int) Math.pow(2, i) + " Conjuntos");
                }
            }
            
            /*Teste se a associatividade é um valor válido, se for zero ou maior que a quantidade
             * de blocos, não será possível mapear*/
            System.out.print("Selecione a associatividade: ");

            int numVias;

            do{
                try{
                    
                    numVias = (int) Math.pow(2, Integer.parseInt(scanner.nextLine()) - 1);
                }
                catch(NumberFormatException e){
                    
                    numVias = 0;
                }

                if(!(numVias > 0 && (numVias & (numVias - 1)) == 0 && numVias <= numBlocos)){

                    System.out.print("Opção Inválida, tente de novo: ");
                }

            }while(!(numVias > 0 && (numVias & (numVias - 1)) == 0 && numVias <= numBlocos));

            return numVias;
        }
    }
    
    /*Seleciona a política de substituição que será utilizada*/
    private static Politica lerPolitica(int numBlocos, int numVias){

        int menu;
        
        /*Imprime o menu e confere se o valor digitado é uma entrada válida, caso seja, 
         * realiza a substituição escolhida pelo usuário*/
        if(numVias != numBlocos){

            Politica politica = null;
            
            System.out.println("1) Least Frequently Used (LFU)");
            System.out.println("2) First In, First Out (FIFO)");
            System.out.println("3) Random");
            System.out.print("Selecione a política de substituição da Cache: ");

            do{
                try{
                    menu = Integer.parseInt(scanner.nextLine());
                }
                catch(NumberFormatException e){

                    menu = 0;
                }
    
                switch(menu){
    
                    case 1:
                        politica = new PLFU();
                        break;
    
                    case 2:
                        politica = new PFIFO();
                        break;
    
                    case 3:
                        politica = new PRandom();
                        break;
    
                    default:
                        System.out.print("Opção Inválida, tente de novo: ");
                }
            }while(!(menu == 1 || menu == 2 || menu == 3));

            return politica;
        }
        else{
            
            return new PFIFO();
        }        
    }
    
    /*Cria um computador para gerar as configurações da cache*/
    private static Computador criaComputador(boolean padrao){

        CPU[] cpus = new CPU[4];
        int nBlocosL1 = 8, nBlocosL2 = 64;
        int nViasL1 = 8, nViasL2 = 4;
        Politica politicaL1 = new PFIFO(), politicaL2 = new PLFU();

        if(!padrao){

            System.out.println("Configuração da Cache L1 (64 palavras)");

            nBlocosL1 = lerNumBlocos(64);

            nViasL1 = lerNumVias(nBlocosL1);
            
            politicaL1 = lerPolitica(nBlocosL1, nViasL1);

            System.out.println("Configuração da Cache L2 (512 palavras)");

            nBlocosL2 = lerNumBlocos(512);

            nViasL2 = lerNumVias(nBlocosL2);

            politicaL2 = lerPolitica(nBlocosL2, nViasL2);
        }

        for(int i = 0; i < cpus.length; i++){

            cpus[i] = new CPU(new Cache(64, nViasL1, nBlocosL1, politicaL1), new Cache(512, nViasL2, nBlocosL2, politicaL2), i);
        }
        
        System.out.println("");
        System.out.println("Criando CPUs com:");
        System.out.println("Cache L1: 64 palavras, " + nBlocosL1 + " blocos, " + (nBlocosL1 == nViasL1 ? "diretamente mapeada" : (nViasL1 == 1 ? "totalmente associativa" : nViasL1 + " conjuntos")) + (nViasL1 != nBlocosL1 ? ", política de substituição " + politicaL1 : ""));
        System.out.println("Cache L2: 512 palavras, " + nBlocosL2 + " blocos, " + (nBlocosL2 == nViasL2 ? "diretamente mapeada" : (nViasL2 == 1 ? "totalmente associativa" : nViasL2 + " conjuntos")) + (nViasL2 != nBlocosL2 ? ", política de substituição " + politicaL2 : ""));

        return new Computador(cpus);
    }

    private static void menuConfig(){

        int menu;
        
        do{
            limparTela();
            System.out.println("        TP AOC-II       ");
            System.out.println("                        ");
            System.out.println("                        ");
            System.out.println("1) Usar padrão          ");
            System.out.println("2) Personalizar         ");
            System.out.println("                        ");
            System.out.println("0) Voltar               ");
            System.out.println("                        ");
            System.out.print("tp > config > ");
            
            try{
            
                menu = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){

                menu = -1;
            }
            
            switch(menu){
                
                case 0:
                    break;

                case 1:
                    computador = criaComputador(true);
                    esperarENTER();
                    return;

                case 2:
                    computador = criaComputador(false);
                    esperarENTER();
                    return;

                default:
                    System.out.println("Opção Inválida");
            }

        }while(menu != 0);
    }

    /*Método utilizado para realizar leitura de arquivos*/
    private static void lerArquivo() throws NullPointerException{

        try {
            Scanner leitor = new Scanner(new BufferedReader(new FileReader("trace.txt")));

            while(leitor.hasNext()){

                for(CPU cpu : computador.getCPUs()){
                    if(leitor.next().equalsIgnoreCase("R")){

                        cpu.readWord(Integer.parseInt(leitor.next(), 2));
                    }
                    else {

                        cpu.writeWord(Integer.parseInt(leitor.next(), 2));
                    }
                }
            }

            leitor.close();
            System.out.println("Foram gerados 4 arquivos (1 para cada CPU) contendo os acessos de memória");

            for(CPU cpu : computador.getCPUs()){
                System.out.printf("Taxa de acertos CPU %d: %.2f%% %n", cpu.getID(), cpu.getTaxaDeAcerto());
            }

		} catch (FileNotFoundException e) {
            
			System.out.println("Não foi possível abrir o arquivo trace.txt: File Not Found");
        }
    }

    private static void menuPrincipal(){

        int menu;

        do{
            limparTela();
            System.out.println("                        ");
            System.out.println("        TP AOC-II       ");
            System.out.println("                        ");
            System.out.println("                        ");
            System.out.println("1) Configurar           ");
            System.out.println("2) Executar             ");
            System.out.println("                        ");
            System.out.println("0) Sair                 ");
            System.out.println("                        ");
            System.out.print("tp > ");
            
            try{
            
                menu = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){

                menu = -1;
            }
            
            switch(menu){
                
                case 0:
                    break;

                case 1:
                    menuConfig();
                    break;

                case 2:
                    try{
                        lerArquivo();
                    }
                    catch(Exception e){

                        e.getMessage();
                    }
                    esperarENTER();
                    break;

                default:
                    System.out.println("Opção Inválida");
            }

        }while(menu != 0);
    }

    public static void main(String[] args) {
        
        menuPrincipal();
    }
}