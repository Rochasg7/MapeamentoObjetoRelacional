package client;

import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import objects.Categoria;
import objects.Produto;

public class Client {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        int objeto;

        do {
            System.out.println("\n--- MENU (JPA) ---");
            System.out.println("1. Produto");
            System.out.println("2. Categoria");
            System.out.println("0. Sair");
            System.out.print("Escolha o objeto que deseja manipular: ");
            objeto = scanner.nextInt();
            scanner.nextLine();

            switch (objeto) {
                case 1:
                    menuProduto(em, scanner);
                    break;
                case 2:
                    menuCategoria(em, scanner);
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (objeto != 0);

        scanner.close();
        em.close();
        emf.close();
    }

    private static void menuProduto(EntityManager em, Scanner scanner) {
        System.out.println("\n-- PRODUTO --");
        System.out.println("1. Inserir Produto");
        System.out.println("2. Consultar Produtos");
        System.out.println("3. Remover Produto");
        System.out.print("Escolha a funcionalidade desejada: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Código da categoria: ");
                Integer categoria = scanner.nextInt();
                scanner.nextLine();

                Produto novoProduto = new Produto(nome, categoria);

                em.getTransaction().begin();
                em.persist(novoProduto);
                em.getTransaction().commit();
                System.out.println("Produto salvo no banco de dados!");
                break;

            case 2:
                List<Produto> listaProdutos = em.createQuery("FROM Produto", Produto.class).getResultList();
                if (listaProdutos.isEmpty()) {
                    System.out.println("Nenhum registro encontrado no banco.");
                } else {
                    for (Produto p : listaProdutos) {
                        System.out.println(p.toString());
                    }
                }
                break;

            case 3:
                System.out.print("Digite o Código do produto para remover: ");
                Integer codRemoverProduto = scanner.nextInt();

                Produto produtoRemover = em.find(Produto.class, codRemoverProduto);

                if (produtoRemover != null) {
                    em.getTransaction().begin();
                    em.remove(produtoRemover);
                    em.getTransaction().commit();
                    System.out.println("Registro removido do banco com sucesso!");
                } else {
                    System.out.println("Código não encontrado no banco de dados.");
                }
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void menuCategoria(EntityManager em, Scanner scanner) {
        System.out.println("\n-- CATEGORIA --");
        System.out.println("1. Inserir Categoria");
        System.out.println("2. Consultar Categorias");
        System.out.println("3. Remover Categoria");
        System.out.print("Escolha a funcionalidade desejada: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Código: ");
                Integer cod = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Descrição: ");
                String desc = scanner.nextLine();
                System.out.print("Ativo (S/N): ");
                String ativo = scanner.nextLine();

                Categoria novaCategoria = new Categoria(cod, desc, ativo);

                em.getTransaction().begin();
                em.persist(novaCategoria);
                em.getTransaction().commit();
                System.out.println("Categoria salva no banco de dados!");
                break;

            case 2:
                List<Categoria> lista = em.createQuery("FROM Categoria", Categoria.class).getResultList();
                if (lista.isEmpty()) {
                    System.out.println("Nenhum registro encontrado no banco.");
                } else {
                    for (Categoria c : lista) {
                        System.out.println(c.toString());
                    }
                }
                break;

            case 3:
                System.out.print("Digite o Código da categoria para remover: ");
                Integer codRemover = scanner.nextInt();

                Categoria catRemover = em.find(Categoria.class, codRemover);

                if (catRemover != null) {
                    em.getTransaction().begin();
                    em.remove(catRemover);
                    em.getTransaction().commit();
                    System.out.println("Registro removido do banco com sucesso!");
                } else {
                    System.out.println("Código não encontrado no banco de dados.");
                }
                break;

            default:
                System.out.println("Opção inválida!");
        }
    }
}