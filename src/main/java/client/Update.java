package client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import objects.Produto;

// esse aqui e so um exemplo isolado de update, pra ver funcionando rapido
// o update de verdade que o usuario usa ta no menu do Client
public class Update {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        EntityManager em = emf.createEntityManager();

        String novoNome = "Produto Novo";
        Integer novaCategoria = 10;

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // recupera o produto que quer atualizar
            Produto produto = em.find(Produto.class, 3);

            if (produto == null) {
                System.out.println("Produto de código 3 não encontrado.");
            } else {
                // atribui os novos valores
                produto.setNome(novoNome);
                produto.setCategoria(novaCategoria);
            }

            // efetiva a atualizacao no banco
            tx.commit();
        } catch (Exception e) {
            // se algo falhar entre o begin e o commit, desfaz a transacao
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }
    }
}
