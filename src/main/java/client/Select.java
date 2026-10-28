package client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import objects.Produto;

public class Select {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();

		try {
			Produto produto = em.find(Produto.class, 1);
			System.out.println(produto);
		} finally {
			em.close();
			emf.close();
		}
	}
}
