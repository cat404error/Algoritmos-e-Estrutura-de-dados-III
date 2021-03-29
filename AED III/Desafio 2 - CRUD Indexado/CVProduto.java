
import java.io.*;

public class CVProduto implements RegistroHash<CVProduto> {

    private int id;
    private long endereco;
    private short TAMANHO = 12;

    public CVProduto() {
        this(-1, -1);
    }

    public CVProduto(int id, long endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public long getEndereco(){
        return this.endereco;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return this.id + ";" + this.endereco;
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeLong(endereco);

        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAMANHO];

        for (int i = 0; i < TAMANHO; i++)
            bs2[i] = ' ';

        for (int i = 0; i < bs.length && i < TAMANHO; i++)
            bs2[i] = bs[i];

        return bs;
    }

    public void fromByteArray(byte[] ba) throws IOException {
        
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.endereco = dis.readLong();
    }
}
