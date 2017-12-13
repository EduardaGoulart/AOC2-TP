package tp;

/*Interface responsável por todas as políticas de substituição*/
public interface Politica {
    
    public Bloco getBloco(Cache cache, int endereco);
}