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
        arquivo.writeInt(objeto.getID());

        byte[] tba = objeto.toByteArray();

        arquivo.seek(arquivo.length());

        this.arquivo.writeBoolean(true);
        this.arquivo.writeInt(tba.length);
        this.arquivo.write(tba);
        
        this.arquivo.close();        

        return objeto.getID();
    }

    /**
     * Recupera um registro do arquivo.
     * @param id
     * @return registro recuperado no formato de um objeto.
     * @throws Exception
     */
    public T read(int id) throws Exception {
            
        T objeto = null;

        this.arquivo = new RandomAccessFile(this.nArquivo, "r");
              
        this.arquivo.seek(4);        

        while(this.arquivo.read() != -1){

            this.arquivo.seek(this.arquivo.getFilePointer()-1);  

            if(this.arquivo.readBoolean()){

                int tamanhoDoRegistro = this.arquivo.readInt();
                byte[] b = new byte[tamanhoDoRegistro]; 

                this.arquivo.read(b);

                objeto = construtor.newInstance(); 
                objeto.fromByteArray(b);

                if(objeto.getID() == id){
                    return objeto;
                }
            }
        }

        this.arquivo.close();

        return objeto;
    }

    /**
     * Atualiza um registro.
     * @param novoObjeto novos atributos do registro.
     * @return true em caso de sucesso
     * @throws Exception
     */
    public boolean update(T objeto) throws Exception{
        
        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        this.arquivo.seek(4);

        while(this.arquivo.read() != -1){

            this.arquivo.seek(this.arquivo.getFilePointer()-1);
            long pos = this.arquivo.getFilePointer();
            int tamanhoDoRegistro;

            if(this.arquivo.readBoolean()){

                tamanhoDoRegistro = this.arquivo.readInt();
                byte[] b = new byte[tamanhoDoRegistro]; 

                this.arquivo.read(b);

                T temp = construtor.newInstance(); 
                temp.fromByteArray(b);

                if(temp.getID() == objeto.getID()){
                 
                    byte[] tba = objeto.toByteArray();

                    if(tba.length <= tamanhoDoRegistro){
                        
                        this.arquivo.seek(pos);
                        this.arquivo.writeBoolean(true); 
                        this.arquivo.writeInt(tamanhoDoRegistro);
                        this.arquivo.write(tba);

                    } else {
                        
                        this.arquivo.seek(pos);
                        this.arquivo.writeBoolean(false);                        
                        this.arquivo.seek(this.arquivo.length());

                        this.arquivo.writeBoolean(true); 
                        this.arquivo.writeInt(tba.length);
                        this.arquivo.write(tba);
                    }

                    this.arquivo.close();

                    return true;
                }
            } 
            
            this.arquivo.seek(pos + this.arquivo.readInt());            
        }

        this.arquivo.close();
        
        return false;
    }


    public boolean delete(int id) throws Exception{
        
        this.arquivo = new RandomAccessFile(this.nArquivo, "rw");

        this.arquivo.seek(4);

        while(this.arquivo.read() != -1){

            this.arquivo.seek(this.arquivo.getFilePointer()-1);

            long pos = this.arquivo.getFilePointer();
            
            if(this.arquivo.readBoolean()){

                int tamanhoDoRegistro = this.arquivo.readInt();
                byte[] b = new byte[tamanhoDoRegistro]; 

                this.arquivo.read(b);

                T objeto = construtor.newInstance(); 
                objeto.fromByteArray(b);

                if(objeto.getID() == id){

                    this.arquivo.seek(pos);
                    this.arquivo.writeBoolean(false);

                    return true;
                }
            }
        }

        return false;
    }
}