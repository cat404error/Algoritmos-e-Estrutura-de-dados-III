public interface Registro {

    public int getID();
    public void setID(int id);
    public String getNome();
    public void setNome(String nome);
    public String getFabricante();
    public void setFabricante(String fabricante);

    public byte[] toByteArray() throws Exception;  
    public void fromByteArray(byte[] b) throws Exception;  
}