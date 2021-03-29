import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;


public class CRUD<T extends Registro> {

    RandomAccessFile arquivo;    
    Constructor<T> construtor;
    HashExtensivel<CVProduto> hash;
    String nArquivo;    

    /**
     * Metodo construtor
     * @param construtor - construtor do objeto
     * @param nArq - nome do arquivo de registros
     * @param nInd - nome do arquivo de indice
     */
    public CRUD(Constructor<T> construtor, String nArq) throws Exception {
        
        this.nArquivo = nArq;                
        this.construtor = construtor;
        this.hash = new HashExtensivel<>(CVProduto.class.getConstructor(), 5, "dados/produtos.hashd.db", "dados/produtos.hashc.db");
    }

    /**
     * Insere um novo registro no arquivo.
     * @param objeto
     * @return id (int) do objeto inserido
     * @throws Exception
     */
    public int create(T objeto) throws Exception {
        
        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        arquivo.seek(0);  

        int id = 1;

        if(arquivo.length()!=0){    
            arquivo.seek(0);        
            id = arquivo.readInt()+1;
        }
        
        arquivo.seek(0);  
        objeto.setID(id);

        arquivo.writeInt(objeto.getID()); 
        arquivo.seek(arquivo.length());

        byte[] tba = objeto.toByteArray();
        
        long endereco = this.arquivo.getFilePointer();
        hash.create(new CVProduto(objeto.getID(), endereco));

        this.arquivo.writeBoolean(false); 
        this.arquivo.writeInt(tba.length);
        this.arquivo.write(tba);
        
        this.arquivo.close();        

        return objeto.getID();
    }

    /**
     * Recupera um registro do arquivo.
     * @param id - identificador do registro
     * @return registro recuperado no formato de um objeto.
     * @throws Exception
     */
    public T read(int id) throws Exception {
            
        T objeto = null;        

        this.arquivo = new RandomAccessFile(this.nArquivo, "r");

        CVProduto cvp = hash.read(id);

        if (cvp == null){
            return objeto;
        }

        long endereco = cvp.getEndereco();

        if(endereco == -1){
            return objeto;
        }

        this.arquivo.seek(endereco);
        boolean lapide = this.arquivo.readBoolean();

        if (lapide == true){
            return objeto;
        }

        int tamanhoRegistro = this.arquivo.readInt();
        byte [] registro = new byte[tamanhoRegistro];

        this.arquivo.read(registro);

        objeto = construtor.newInstance(); 
        objeto.fromByteArray(registro);

        this.arquivo.close();

        return objeto;
    }

    /**
     * Atualiza um registro.
     * @param obj - referencia do registro a ser atualizado.
     * @return true em caso de sucesso
     * @throws Exception
     */
    public boolean update(T obj) throws Exception {

        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        CVProduto cvp = hash.read(obj.getID());

        if (cvp == null){
            return false;
        }

        long endereco = cvp.getEndereco();

        if (endereco == -1){
            return false;
        }

        this.arquivo.seek(endereco);
        boolean lapide = this.arquivo.readBoolean();

        if(lapide == true){
            return false;
        }

        int tamanhoRegistro = this.arquivo.readInt();
        byte [] fba = new byte [tamanhoRegistro]; 
                
        this.arquivo.read(fba);

        T objetoAntigo = construtor.newInstance(); 
        objetoAntigo.fromByteArray(fba);

        if(obj.getID() != objetoAntigo.getID()){          
            return false;
        }                

        byte [] tba = obj.toByteArray();

        if (tba.length <= tamanhoRegistro){
            
            this.arquivo.seek(endereco);
            this.arquivo.writeBoolean(false);
            this.arquivo.writeInt(tamanhoRegistro);
            this.arquivo.write(tba);

            this.arquivo.close();

            return true;

        } else {                       

            this.arquivo.seek(endereco);
            this.arquivo.writeBoolean(true);
            this.arquivo.seek(this.arquivo.length());

            hash.update(new CVProduto(obj.getID(), this.arquivo.getFilePointer()));
            
            this.arquivo.writeBoolean(false);
            this.arquivo.writeInt(tba.length);
            this.arquivo.write(tba); 

            this.arquivo.close();

            return true;
        }
    }

    /**
     * Remove logicamente um Registro.
     * @param id - identificador do objeto a remover
     * @return valor logico indicando se o objeto foi removido ou nao
     * @throws Exception
     */
    public boolean delete(int id) throws Exception{
        
        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        CVProduto cvp = hash.read(id);

        if (cvp == null){
            return false;
        }

        long endereco = cvp.getEndereco();

        if (endereco == -1){
            return false;
        }

        this.arquivo.seek(endereco);
        this.arquivo.writeBoolean(true);
        this.arquivo.close();

        hash.delete(id);

        return true;        
    }
}