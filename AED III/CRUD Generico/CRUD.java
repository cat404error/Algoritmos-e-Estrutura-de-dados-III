import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;


public class CRUD<T extends Registro> {

    RandomAccessFile arquivo;
    Constructor<T> construtor;
    String nArquivo;

    /**
     * Metodo construtor
     */
    public CRUD(Constructor<T> construtor, String nArq) throws Exception {
        this.nArquivo = nArq;        
        this.construtor = construtor;
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

        arquivo.writeInt(objeto.getID()); // atualizar quantidade de itens
        arquivo.seek(arquivo.length());

        byte[] tba = objeto.toByteArray();

        this.arquivo.writeBoolean(false); // lapide
        this.arquivo.writeInt(tba.length); // tamanho
        this.arquivo.write(tba); // dados
        
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
              
        this.arquivo.seek(4);
        
        int pointer = (int)this.arquivo.getFilePointer();         

        while(pointer < this.arquivo.length()){            

            if(!this.arquivo.readBoolean()){

                int tamanhoRegistro = this.arquivo.readInt();
                byte [] fba = new byte[tamanhoRegistro];

                this.arquivo.read(fba);

                objeto = construtor.newInstance();
                objeto.fromByteArray(fba);                

                if(objeto.getID() == id){

                    this.arquivo.close();
                    return objeto;
                }

                pointer = (int)this.arquivo.getFilePointer(); 
                this.arquivo.seek(pointer);

            } else {

                int tamR = this.arquivo.readInt();
                pointer = (int)this.arquivo.getFilePointer();
                this.arquivo.seek(pointer + tamR);
            }
        }

        this.arquivo.close();

        return objeto;
    }

    /**
     * Atualiza um registro.
     * @param objeto - referencia do registro a ser atualizado.
     * @return true em caso de sucesso
     * @throws Exception
     */
    public boolean update(T objeto) throws Exception{
        
        if(delete(objeto.getID()) == false) return false;
        createUpdate(objeto);
        return true;        
    }
    
    private void createUpdate(T objeto) throws Exception {
        
        byte[] tba = objeto.toByteArray();

        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        this.arquivo.seek(this.arquivo.length());

        this.arquivo.writeBoolean(false);
        this.arquivo.writeInt(tba.length);
        this.arquivo.write(tba);
        
        this.arquivo.close();        
    }

    /**
     * Remove logicamente um Registro.
     * @param id - identificador do objeto a remover
     * @return valor logico indicando se o objeto foi removido ou nao
     * @throws Exception
     */
    public boolean delete(int id) throws Exception{
        
        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        this.arquivo.seek(4);

        int pointer = (int)this.arquivo.getFilePointer();

        while(pointer < this.arquivo.length()){   
            
            int pos = pointer;
            
            if(!this.arquivo.readBoolean()){

                int tamanhoDoRegistro = this.arquivo.readInt();
                byte[] b = new byte[tamanhoDoRegistro]; 

                this.arquivo.read(b);

                T objeto = construtor.newInstance(); 
                objeto.fromByteArray(b);

                if(objeto.getID() == id){
                    
                    this.arquivo.seek(pos);
                    this.arquivo.writeBoolean(true);
                    this.arquivo.close();

                    return true;
                }

                pointer = (int)this.arquivo.getFilePointer(); 
                this.arquivo.seek(pointer);     

            } else {

                int tamR = this.arquivo.readInt();
                pointer = (int)this.arquivo.getFilePointer();
                this.arquivo.seek(pointer + tamR);     
            }
        }

        this.arquivo.close();

        return false;
    }

    /**
     * Atualiza um registro.
     * @param obj - referencia do registro a ser atualizado.
     * @return true em caso de sucesso
     * @throws Exception
     */
    public boolean update1(T obj) throws Exception {

        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");
              
        this.arquivo.seek(4);
        
        int pointer = (int)this.arquivo.getFilePointer();         

        while(pointer < this.arquivo.length()){ 
            
            int pos = pointer;

            if(!this.arquivo.readBoolean()){

                int tamanhoRegistro = this.arquivo.readInt();
                
                byte [] tba = obj.toByteArray(); 
                byte [] fba = new byte [tamanhoRegistro]; 
                
                this.arquivo.read(fba);

                T objeto = construtor.newInstance(); 
                objeto.fromByteArray(fba);

                if(obj.getID() == objeto.getID()){

                    if (tba.length <= tamanhoRegistro){
                        
                        this.arquivo.seek(pos);
                        this.arquivo.writeBoolean(false);
                        this.arquivo.writeInt(tamanhoRegistro);
                        this.arquivo.write(tba);

                    } else {                       

                        this.arquivo.seek(pos);
                        this.arquivo.writeBoolean(true);
                        this.arquivo.seek(this.arquivo.length());
                        
                        this.arquivo.writeBoolean(false);
                        this.arquivo.writeInt(tba.length);
                        this.arquivo.write(tba); 
                    }

                    this.arquivo.close();
                    
                    return true;
                }                
                
                pointer = (int)this.arquivo.getFilePointer(); 
                this.arquivo.seek(pointer);                            

            } else {

                int tamR = this.arquivo.readInt();
                pointer = (int)this.arquivo.getFilePointer();
                this.arquivo.seek(pointer + tamR);
            }
        }

        this.arquivo.close();

        return false;
    }
}