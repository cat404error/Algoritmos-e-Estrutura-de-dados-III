
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Produto implements Registro {

	private int idProduto;
	private String nome;
	private String fabricante;

	public Produto(int id, String nome, String fabricante) {

		this.idProduto = id;
		this.nome = nome;
		this.fabricante = fabricante;
	}

	public Produto() {

		this.idProduto = -1;
		this.nome = "";
		this.fabricante = "";
	}

	public int getID() { return this.idProduto;	}	
	public String getNome(){ return this.nome; }
	public String getFabricante(){ return this.fabricante; }

	public void setID(int id) {	this.idProduto = id; }
	public void setNome(String nome) {	this.nome = nome; }
	public void setFabricante(String fabricante) {	this.fabricante = fabricante; }
	
	
	public String toString() {
		return "\nID....: " + this.idProduto + "\nnome: " + this.nome + "\nfabricante: " + this.fabricante;
	}

	/**
	 * Retorna um vetor de bytes do objeto.
	 * @return vetor de bytes
	 * @throws Exception
	 */
	public byte[] toByteArray() throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeInt(this.idProduto);
		dos.writeUTF(this.nome);
		dos.writeUTF(this.fabricante);

		return baos.toByteArray();
	}

	/**
	 * Faz a leitura de um vetor de bytes e seta os atributos do objeto.
	 * @param b - vetor de bytes.
	 * @throws Exception
	 */
	public void fromByteArray(byte[] b) throws Exception {

		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		DataInputStream dis = new DataInputStream(bais);

		this.idProduto = dis.readInt();
		this.nome = dis.readUTF();
		this.fabricante = dis.readUTF();
	}
}