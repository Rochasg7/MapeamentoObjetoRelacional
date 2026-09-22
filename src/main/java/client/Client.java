package client;

import java.util.List;
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
            int objeto;
            do {
                System.out.println("\n--- MENU (JPA) ---");
                System.out.println("1. Produto");
                System.out.println("2. Categoria");
                System.out.println("0. Sair");
                objeto = lerInteiro(scanner, "Escolha o objeto que deseja manipular: ");
                try {
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
                } catch (RuntimeException e) {
                    // o executarComTransacao ja fez o rollback e relancou o erro
                    // (igual o UpdateCT do professor). aqui em cima e so pra pegar
                    // esse throw e nao deixar o app inteiro fechar por causa de
                    // uma operacao que falhou
                    System.out.println("Operação falhou: " + e.getMessage());
                }
            } while (objeto != 0);
        } finally {
            // fecha tudo no finally pra garantir que fecha mesmo se der erro no meio
            scanner.close();
            em.close();
            emf.close();
        }
    }

    // ---------- PRODUTO ----------

    private static void menuProduto(EntityManager em, Scanner scanner) {
        System.out.println("\n-- PRODUTO --");
        System.out.println("1. Inserir Produto");
        System.out.println("2. Consultar Produtos");
        System.out.println("3. Atualizar Produto");
        System.out.println("4. Remover Produto");
        System.out.println("0. Voltar");
        int opcao = lerInteiro(scanner, "Escolha a funcionalidade desejada: ");
        switch (opcao) {
            case 1:
                inserirProduto(em, scanner);
                break;
            case 2:
                consultarProdutos(em);
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
                System.out.println("Opção inválida!");
        }
    }

    private static void inserirProduto(EntityManager em, Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        Integer categoria = lerInteiro(scanner, "Código da categoria: ");
        Produto novoProduto = new Produto(nome, categoria);
        executarComTransacao(em, "Produto salvo no banco de dados!", () -> {
            em.persist(novoProduto);
        });
    }

    private static void consultarProdutos(EntityManager em) {
        // igual o material mostra pro find: abre a transacao antes da consulta
        // e fecha com commit logo depois (aqui nao pode ficar sem fechar
        // porque o mesmo EntityManager e reaproveitado nas proximas operacoes)
        em.getTransaction().begin();
        List<Produto> listaProdutos = em.createQuery("FROM Produto", Produto.class).getResultList();
        em.getTransaction().commit();
        if (listaProdutos.isEmpty()) {
            System.out.println("Nenhum registro encontrado no banco.");
        } else {
            for (Produto p : listaProdutos) {
                System.out.println(p.toString());
            }
        }
    }

    private static void atualizarProduto(EntityManager em, Scanner scanner) {
        Integer cod = lerInteiro(scanner, "Digite o Código do produto para atualizar: ");
        Produto produto = em.find(Produto.class, cod);
        if (produto == null) {
            System.out.println("Código não encontrado no banco de dados.");
            return;
        }
        System.out.println("Produto atual: " + produto);
        System.out.print("Novo nome (ENTER para manter \"" + produto.getNome() + "\"): ");
        String nome = scanner.nextLine();
        System.out.print("Novo código de categoria (ENTER para manter \"" + produto.getCategoria() + "\"): ");
        String categoriaEntrada = scanner.nextLine();
        executarComTransacao(em, "Produto atualizado com sucesso!", () -> {
            // o produto ja veio do em.find, entao ele ta sendo gerenciado
            // so mudar o atributo e o hibernate ja percebe e da o update sozinho no commit
            if (!nome.trim().isEmpty()) {
                produto.setNome(nome);
            }
            if (!categoriaEntrada.trim().isEmpty()) {
                produto.setCategoria(Integer.parseInt(categoriaEntrada));
            }
        });
    }

    private static void removerProduto(EntityManager em, Scanner scanner) {
        Integer cod = lerInteiro(scanner, "Digite o Código do produto para remover: ");
        Produto produto = em.find(Produto.class, cod);
        if (produto == null) {
            System.out.println("Código não encontrado no banco de dados.");
            return;
        }
        executarComTransacao(em, "Registro removido do banco com sucesso!", () -> {
            em.remove(produto);
        });
    }

    // ---------- CATEGORIA ----------

    private static void menuCategoria(EntityManager em, Scanner scanner) {
        System.out.println("\n-- CATEGORIA --");
        System.out.println("1. Inserir Categoria");
        System.out.println("2. Consultar Categorias");
        System.out.println("3. Atualizar Categoria");
        System.out.println("4. Remover Categoria");
        System.out.println("0. Voltar");
        int opcao = lerInteiro(scanner, "Escolha a funcionalidade desejada: ");
        switch (opcao) {
            case 1:
                inserirCategoria(em, scanner);
                break;
            case 2:
                consultarCategorias(em);
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
                System.out.println("Opção inválida!");
        }
    }

    private static void inserirCategoria(EntityManager em, Scanner scanner) {
        Integer cod = lerInteiro(scanner, "Código: ");
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        System.out.print("Ativo (S/N): ");
        String ativo = scanner.nextLine();
        Categoria novaCategoria = new Categoria(cod, desc, ativo);
        executarComTransacao(em, "Categoria salva no banco de dados!", () -> {
            em.persist(novaCategoria);
        });
    }

    private static void consultarCategorias(EntityManager em) {
        em.getTransaction().begin();
        List<Categoria> lista = em.createQuery("FROM Categoria", Categoria.class).getResultList();
        em.getTransaction().commit();
        if (lista.isEmpty()) {
            System.out.println("Nenhum registro encontrado no banco.");
        } else {
            for (Categoria c : lista) {
                System.out.println(c.toString());
            }
        }
    }

    private static void atualizarCategoria(EntityManager em, Scanner scanner) {
        Integer cod = lerInteiro(scanner, "Digite o Código da categoria para atualizar: ");
        Categoria categoria = em.find(Categoria.class, cod);
        if (categoria == null) {
            System.out.println("Código não encontrado no banco de dados.");
            return;
        }
        System.out.println("Categoria atual: " + categoria);
        System.out.print("Nova descrição (ENTER para manter \"" + categoria.getDescricao() + "\"): ");
        String desc = scanner.nextLine();
        System.out.print("Novo status Ativo S/N (ENTER para manter \"" + categoria.getAtivo() + "\"): ");
        String ativo = scanner.nextLine();
        executarComTransacao(em, "Categoria atualizada com sucesso!", () -> {
            if (!desc.trim().isEmpty()) {
                categoria.setDescricao(desc);
            }
            if (!ativo.trim().isEmpty()) {
                categoria.setAtivo(ativo);
            }
        });
    }

    private static void removerCategoria(EntityManager em, Scanner scanner) {
        Integer cod = lerInteiro(scanner, "Digite o Código da categoria para remover: ");
        Categoria categoria = em.find(Categoria.class, cod);
        if (categoria == null) {
            System.out.println("Código não encontrado no banco de dados.");
            return;
        }
        executarComTransacao(em, "Registro removido do banco com sucesso!", () -> {
            em.remove(categoria);
        });
    }

    // ---------- transacao e leitura de input ----------

    // metodo unico que cuida de begin/commit/rollback
    // assim nao fica repetindo esse bloco em cada operacao e correndo o risco
    // de esquecer o tratamento de erro em algum lugar
    // (segue o mesmo esqueleto do UpdateCT que o professor passou: begin,
    // unidade de trabalho, commit, e no catch rollback + throw de novo)
    private static void executarComTransacao(EntityManager em, String mensagemSucesso, Runnable operacao) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operacao.run();
            tx.commit();
            System.out.println(mensagemSucesso);
        } catch (RuntimeException e) {
            // se der erro no meio, desfaz a transacao pra nao ficar aberta
            // nem deixar meio registro salvo
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // antes tava usando nextInt() e nextLine() misturado e isso da bug
    // (o famoso pulo de linha e o crash quando digita letra no lugar de numero)
    // entao criei esse metodo que so trabalha com nextLine + parseInt
    private static Integer lerInteiro(Scanner scanner, String mensagem) {
        Integer valor = null;
        while (valor == null) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                valor = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
        return valor;
    }
}
