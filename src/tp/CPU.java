package tp;

import java.io.*;

/**
 * CPU
 */
public class CPU {

    private final Cache l1;
    private final Cache l2;
    private Barramento barramento;
    private PrintWriter escritor;

    protected CPU(Cache l1, Cache l2, int id){

        this.l1 = l1;
        this.l2 = l2;

        try {
            escritor = new PrintWriter(new File("cpu" + id + "Trace.txt"));
            
		} catch (FileNotFoundException e) {
			
		    System.out.println(e.getMessage());
        }
    }

    protected void setBarramento(Barramento barramento){
        
        this.barramento = barramento;
    }

    protected void readWord(int endereco){

        Bloco blocoL1 = null;
        Bloco blocoL2 = null;

        if(!l1.hasBlocoValido(endereco)){

            escritor.println("L1: MISS - Endereço " + Integer.toBinaryString(endereco));

            if(l2.hasBlocoValido(endereco)){

                escritor.println("L2: HIT - Endereço " + Integer.toBinaryString(endereco));

                blocoL1 = l1.getPolitica().getBloco(l1, endereco);
                escritor.println("L1: Substituindo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));
                blocoL2 = l2.buscarBloco(endereco);
                blocoL2.setRecentementeUsado(true);
                
                blocoL1.setMESI(blocoL2.getMESI());
                blocoL1.setTag(l1.calcularTag(endereco));
                blocoL1.setRecentementeUsado(true);
                escritor.println("L1: Pelo Bloco - Tag " + Integer.toBinaryString(blocoL1.getTag()));

            }
            else{

                escritor.println("L2: MISS - Endereço " + Integer.toBinaryString(endereco));

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
        else{

            escritor.println("L1: HIT - Endereço " + Integer.toBinaryString(endereco));
        }
        escritor.flush();
    }

    protected void writeWord(int endereco){

        Bloco blocoL1 = null;
        Bloco blocoL2 = null;

        if(l1.hasBlocoValido(endereco)){
            
            escritor.println("L1: HIT - Endereço " + Integer.toBinaryString(endereco));

            barramento.enviarSinal("write hit", endereco, this);
            escritor.println("Enviando Write Hit pelo barramento Endereço - " + Integer.toBinaryString(endereco));

            blocoL1 = l1.buscarBloco(endereco);

            blocoL1.setMESI('M');
            blocoL1.setRecentementeUsado(true);

            blocoL2 = l2.buscarBloco(endereco);

            blocoL2.setMESI('M');
            blocoL2.setRecentementeUsado(true);
        }
        else{
            if(l2.hasBlocoValido(endereco)){
                
                escritor.println("L2: HIT - Endereço " + Integer.toBinaryString(endereco));

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
            else{

                barramento.enviarSinal("write miss", endereco, this);

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

    protected boolean receberSinalRead(int endereco){

        escritor.println("Recebendo Sinal Read Miss do barramento Endereço - " + Integer.toBinaryString(endereco));

        if(l1.hasBlocoValido(endereco)){

            Bloco blocoL1 = l1.buscarBloco(endereco);
            Bloco blocoL2 = l2.buscarBloco(endereco);

            blocoL1.setMESI('S');
            blocoL2.setMESI('S');

            return true;
        }
        if(l2.hasBlocoValido(endereco)){

            Bloco blocoL2 = l2.buscarBloco(endereco);
            
            blocoL2.setMESI('S');

            return true;
        }

        escritor.flush();

        return false;
    }

    protected void receberSinalWrite(int endereco){

        escritor.println("Recebendo Sinal Write do barramento Endereço - " + Integer.toBinaryString(endereco));

        if(l1.hasBlocoValido(endereco)){
            
            Bloco blocoL1 = l1.buscarBloco(endereco);

            blocoL1.setMESI('I');
            blocoL1.setRecentementeUsado(false);
            
            Bloco blocoL2 = l2.buscarBloco(endereco);

            blocoL2.setMESI('I');
            blocoL2.setRecentementeUsado(false);
        }
        else if(l2.hasBlocoValido(endereco)){

            Bloco blocoL2 = l2.buscarBloco(endereco);
            
            blocoL2.setMESI('I');
            blocoL2.setRecentementeUsado(false);
        }
        
        escritor.flush();
    }
}