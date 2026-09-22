package client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import objects.Produto;

// exemplo isolado de select, a consulta de verdade ta no menu do Client
public class Select {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        EntityManager em = emf.createEntityManager();
        try {
            // igual o material: abre a transacao antes do find
            // aqui pode deixar sem commit, porque essa classe roda uma vez so
            // e o em.close() logo depois encerra tudo de qualquer jeito
            em.getTransaction().begin();
            Produto produto = em.find(Produto.class, 1);
            System.out.println(produto);
        } finally {
            em.close();
            emf.close();
        }
    }
}
