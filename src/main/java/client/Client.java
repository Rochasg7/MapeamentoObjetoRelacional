package client;

import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import objects.Categoria;
import objects.Produto;

public class Client {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
		EntityManager em = emf.createEntityManager();
		Scanner scanner = new Scanner(System.in);

		try {
			int opcao;
			do {
				System.out.println("\n===== MENU =====");
				System.out.println("1 - Produto");
				System.out.println("2 - Categoria");
				System.out.println("0 - Sair");
				opcao = lerInteiro(scanner, "Opção: ");

				try {
					switch (opcao) {
					case 1:
						menuProduto(em, scanner);
						break;
					case 2:
						menuCategoria(em, scanner);
						break;
					case 0:
						System.out.println("Programa encerrado.");
						break;
					default:
						System.out.println("Opção inválida.");
					}
				} catch (RuntimeException e) {
					System.out.println("Erro: " + e.getMessage());
				}
			} while (opcao != 0);
		} finally {
			scanner.close();
			em.close();
			emf.close();
		}
	}

	private static void menuProduto(EntityManager em, Scanner scanner) {
		System.out.println("\n----- PRODUTO -----");
		System.out.println("1 - Cadastrar");
		System.out.println("2 - Consultar");
		System.out.println("3 - Atualizar");
		System.out.println("4 - Remover");
		System.out.println("0 - Voltar");
		int opcao = lerInteiro(scanner, "Opção: ");

		switch (opcao) {
		case 1:
			inserirProduto(em, scanner);
			break;
		case 2:
			consultarProduto(em, scanner);
			break;
		case 3:
			atualizarProduto(em, scanner);
			break;
		case 4:
			removerProduto(em, scanner);
			break;
		case 0:
			break;
		default:
			System.out.println("Opção inválida.");
		}
	}

	private static void inserirProduto(EntityManager em, Scanner scanner) {
		System.out.print("Nome: ");
		String nome = scanner.nextLine().trim();
		Integer codCategoria = lerInteiro(scanner, "Código da categoria: ");

		if (em.find(Categoria.class, codCategoria) == null) {
			System.out.println("Categoria não cadastrada.");
			return;
		}

		Produto produto = new Produto(nome, codCategoria);
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(produto);
			tx.commit();
			System.out.println("Produto cadastrado com o código " + produto.getCod() + ".");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static void consultarProduto(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código do produto: ");
		Produto produto = em.find(Produto.class, cod);

		if (produto == null) {
			System.out.println("Produto não encontrado.");
		} else {
			System.out.println(produto);
		}
	}

	private static void atualizarProduto(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código do produto: ");
		Produto produto = em.find(Produto.class, cod);

		if (produto == null) {
			System.out.println("Produto não encontrado.");
			return;
		}

		System.out.println(produto);
		System.out.print("Novo nome (em branco para manter): ");
		String nome = scanner.nextLine().trim();
		System.out.print("Nova categoria (em branco para manter): ");
		String entradaCategoria = scanner.nextLine().trim();

		Integer novaCategoria = null;
		if (!entradaCategoria.isEmpty()) {
			try {
				novaCategoria = Integer.parseInt(entradaCategoria);
			} catch (NumberFormatException e) {
				System.out.println("Código de categoria inválido.");
				return;
			}
			if (em.find(Categoria.class, novaCategoria) == null) {
				System.out.println("Categoria não cadastrada.");
				return;
			}
		}

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			if (!nome.isEmpty()) {
				produto.setNome(nome);
			}
			if (novaCategoria != null) {
				produto.setCategoria(novaCategoria);
			}
			tx.commit();
			System.out.println("Produto atualizado.");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static void removerProduto(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código do produto: ");
		Produto produto = em.find(Produto.class, cod);

		if (produto == null) {
			System.out.println("Produto não encontrado.");
			return;
		}

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.remove(produto);
			tx.commit();
			System.out.println("Produto removido.");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static void menuCategoria(EntityManager em, Scanner scanner) {
		System.out.println("\n----- CATEGORIA -----");
		System.out.println("1 - Cadastrar");
		System.out.println("2 - Consultar");
		System.out.println("3 - Atualizar");
		System.out.println("4 - Remover");
		System.out.println("0 - Voltar");
		int opcao = lerInteiro(scanner, "Opção: ");

		switch (opcao) {
		case 1:
			inserirCategoria(em, scanner);
			break;
		case 2:
			consultarCategoria(em, scanner);
			break;
		case 3:
			atualizarCategoria(em, scanner);
			break;
		case 4:
			removerCategoria(em, scanner);
			break;
		case 0:
			break;
		default:
			System.out.println("Opção inválida.");
		}
	}

	private static void inserirCategoria(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código: ");

		if (em.find(Categoria.class, cod) != null) {
			System.out.println("Já existe uma categoria com esse código.");
			return;
		}

		System.out.print("Descrição: ");
		String descricao = scanner.nextLine().trim();
		System.out.print("Ativo (S/N): ");
		String ativo = scanner.nextLine().trim().toUpperCase();

		Categoria categoria = new Categoria(cod, descricao, ativo);
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(categoria);
			tx.commit();
			System.out.println("Categoria cadastrada.");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static void consultarCategoria(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código da categoria: ");
		Categoria categoria = em.find(Categoria.class, cod);

		if (categoria == null) {
			System.out.println("Categoria não encontrada.");
		} else {
			System.out.println(categoria);
		}
	}

	private static void atualizarCategoria(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código da categoria: ");
		Categoria categoria = em.find(Categoria.class, cod);

		if (categoria == null) {
			System.out.println("Categoria não encontrada.");
			return;
		}

		System.out.println(categoria);
		System.out.print("Nova descrição (em branco para manter): ");
		String descricao = scanner.nextLine().trim();
		System.out.print("Ativo S/N (em branco para manter): ");
		String ativo = scanner.nextLine().trim().toUpperCase();

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			if (!descricao.isEmpty()) {
				categoria.setDescricao(descricao);
			}
			if (!ativo.isEmpty()) {
				categoria.setAtivo(ativo);
			}
			tx.commit();
			System.out.println("Categoria atualizada.");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static void removerCategoria(EntityManager em, Scanner scanner) {
		Integer cod = lerInteiro(scanner, "Código da categoria: ");
		Categoria categoria = em.find(Categoria.class, cod);

		if (categoria == null) {
			System.out.println("Categoria não encontrada.");
			return;
		}

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.remove(categoria);
			tx.commit();
			System.out.println("Categoria removida.");
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	private static Integer lerInteiro(Scanner scanner, String mensagem) {
		while (true) {
			System.out.print(mensagem);
			String entrada = scanner.nextLine().trim();
			try {
				return Integer.parseInt(entrada);
			} catch (NumberFormatException e) {
				System.out.println("Digite um número inteiro.");
			}
		}
	}
}
