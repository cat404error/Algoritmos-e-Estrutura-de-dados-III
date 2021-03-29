import java.io.*;

public interface RegistroHash <T>{
    
    // chave numérica para ser usada no diretório
    public int hashCode(); 

    // tamanho FIXO do registro
    public short size(); 
  
    // representação do elemento em um vetor de bytes
    public byte[] toByteArray() throws IOException; 
  
    // vetor de bytes a ser usado na construção do elemento
    public void fromByteArray(byte[] ba) throws IOException; 
}
