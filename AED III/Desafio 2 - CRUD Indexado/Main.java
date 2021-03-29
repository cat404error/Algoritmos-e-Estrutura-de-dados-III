import java.io.*;

public class Main {

    private static CRUD<Produto>crudProdutos;

    public static void main(String [] args) {
        
        Produto p1 = new Produto(-1, "Lapis", "STABILO");
        Produto p2 = new Produto(-1, "Caneta", "MONTBLANC");
        Produto p3 = new Produto(-1, "Caderno", "MOLESKINE");
        Produto p4 = new Produto(-1, "Borracha", "FABER CASTELL");
        Produto p5 = new Produto(-1, "Corretivo", "TILIBRA");
    
        int id1, id2, id3, id4, id5;
    
        try {

            new File("dados/produtos.db").delete(); 
            new File("dados/produtos.hashd.db").delete();          
            new File("dados/produtos.hashc.db").delete();          
            
            new File("dados/produtos.db");           
            
            crudProdutos = new CRUD<>(Produto.class.getConstructor(),"dados/produtos.db");
            

            // ----------------------------------------- Create

            id1 = crudProdutos.create(p1);
            p1.setID(id1);            

            id2 = crudProdutos.create(p2);
            p2.setID(id2);

            id3 = crudProdutos.create(p3);
            p3.setID(id3);    
            
            id4 = crudProdutos.create(p4);
            p4.setID(id4);

            id5 = crudProdutos.create(p5);
            p5.setID(id5);
            
            // ----------------------------------------- Read

            System.out.println(crudProdutos.read(id1));
            System.out.println(crudProdutos.read(id3));
            System.out.println("");

            // ----------------------------------------- Update
            
            p1.setFabricante("FABER CASTEL");
            boolean ok = crudProdutos.update(p1);
            System.out.println("atualizado: " + ok + "\nregistro atualizado para: " + crudProdutos.read(id1));
            System.out.println("");

            p3.setFabricante("TILIBRA");
            ok = crudProdutos.update(p3);
            System.out.println("atualizado: " + ok + "\nregistro atualizado para:" + crudProdutos.read(id3));

            // ----------------------------------------- Delete            
            
            if(crudProdutos.delete(id2) == true){
                System.out.println("\nProduto removido.");
            } else {
                System.out.println("\nNão existe no registro.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
