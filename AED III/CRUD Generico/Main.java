import java.io.*;

public class Main {

    private static CRUD<Produto>crudProdutos;

    public static void main(String [] args) {
        
        Produto p1 = new Produto(-1, "Lapis", "STABILO");
        Produto p2 = new Produto(-1, "Caneta", "MONTBLANC");
        Produto p3 = new Produto(-1, "Caderno", "MOLESKINE");
    
        int id1, id2, id3;
    
        try {

            new File("dados/produtos.db");
            crudProdutos = new CRUD<>(Produto.class.getConstructor(),"dados/produtos.db");

            // ----------------------------------------- Create

            id1 = crudProdutos.create(p1);
            p1.setID(id1);

            id2 = crudProdutos.create(p2);
            p2.setID(id2);

            id3 = crudProdutos.create(p3);
            p3.setID(id3);
            
            // ----------------------------------------- Read

            System.out.println(crudProdutos.read(id1).toString());
            System.out.println(crudProdutos.read(id3).toString());            

            // ----------------------------------------- Update

            // p1.setFabricante("FABER CASTEL");
            // crudProdutos.update(p1);
            // System.out.println(crudProdutos.read(id1));            

            // p2.setFabricante("BIC");
            // crudProdutos.update(p2);
            // System.out.println(crudProdutos.read(id2));

            // ----------------------------------------- Delete
            
            if(crudProdutos.delete(id2)){
                System.out.println("\nProduto removido.");
            } else {
                System.out.println("\nProduto não foi localizado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
