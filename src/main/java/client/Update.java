package client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import objects.Produto;

public class Update {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();

		String novoNome = "Produto Novo";
		Integer novaCategoria = 10;

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			Produto produto = em.find(Produto.class, 3);

			if (produto == null) {
				throw new RuntimeException("Produto não encontrado.");
			}

			produto.setNome(novoNome);
			produto.setCategoria(novaCategoria);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
			emf.close();
		}
	}
}
