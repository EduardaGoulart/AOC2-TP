package tp;

public interface Politica {
    
    public Bloco getBloco(Cache cache, int endereco);
}