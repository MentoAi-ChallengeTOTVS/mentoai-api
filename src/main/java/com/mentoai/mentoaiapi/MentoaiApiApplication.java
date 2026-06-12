package com.mentoai.mentoaiapi;

import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.enums.PrioridadeAlerta;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.Insight;
import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.Severidade;
import com.mentoai.mentoaiapi.analysis.domain.enums.TipoInsight;
import com.mentoai.mentoaiapi.copilot.domain.entity.Chat;
import com.mentoai.mentoaiapi.copilot.domain.entity.PerguntaChat;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.meeting.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.user.domain.entity.DiretorComercial;
import com.mentoai.mentoaiapi.user.domain.entity.ExecutivoComercial;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MentoaiApiApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Usuario> usuarios = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        List<AnaliseIA> analises = new ArrayList<>();

        int opcao;

        do {

            System.out.println("\n=== MENTO AI ===");
            System.out.println("1 - Cadastrar Usuário");
            System.out.println("2 - Visualizar Usuários");
            System.out.println("3 - Simular Reunião");
            System.out.println("4 - Visualizar Reuniões");
            System.out.println("0 - Sair");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:

                    System.out.println("\n1 - Diretor Comercial");
                    System.out.println("2 - Executivo Comercial");

                    int tipo = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    Usuario usuario;

                    if (tipo == 1) {

                        usuario = new DiretorComercial(
                                (long) (usuarios.size() + 1),
                                nome,
                                email,
                                "123",
                                "Vendas"
                        );

                    } else {

                        usuario = new ExecutivoComercial(
                                (long) (usuarios.size() + 1),
                                nome,
                                email,
                                "123",
                                "Equipe Alpha"
                        );

                    }

                    usuarios.add(usuario);

                    System.out.println("\nUsuário cadastrado com sucesso!");
                    break;

                case 2:

                    System.out.println("\n=== USUÁRIOS CADASTRADOS ===");

                    if (usuarios.isEmpty()) {

                        System.out.println("Nenhum usuário cadastrado.");

                    } else {

                        for (Usuario usuarioAtual : usuarios) {

                            System.out.println(usuarioAtual);

                            if (usuarioAtual instanceof DiretorComercial) {
                                System.out.println("Tipo: Diretor Comercial");
                            }

                            if (usuarioAtual instanceof ExecutivoComercial) {
                                System.out.println("Tipo: Executivo Comercial");
                            }

                            System.out.println("--------------------------------");
                        }

                    }

                    break;

                case 3:

                    System.out.print("\nNome do cliente: ");
                    String nomeCliente = scanner.nextLine();

                    Cliente cliente = new Cliente(
                            (long) (clientes.size() + 1),
                            nomeCliente,
                            "Tecnologia",
                            "Grande Porte"
                    );

                    clientes.add(cliente);

                    Reuniao reuniao = new Reuniao(
                            (long) (analises.size() + 1),
                            LocalDateTime.now(),
                            60,
                            StatusProcessamento.PENDENTE,
                            cliente
                    );

                    cliente.adicionarReuniao(reuniao);

                    System.out.print("Resumo da reunião: ");
                    String texto = scanner.nextLine();

                    Transcricao transcricao = new Transcricao(
                            (long) (analises.size() + 1),
                            texto,
                            "PT-BR",
                            reuniao
                    );

                    transcricao.processarTexto();

                    AnaliseIA analise = new AnaliseIA(
                            (long) (analises.size() + 1),
                            "Resumo gerado automaticamente pela IA.",
                            SentimentoGeral.NEUTRO,
                            0.5,
                            reuniao
                    );

                    if (texto.toLowerCase().contains("cancelamento")
                            || texto.toLowerCase().contains("cancelar")
                            || texto.toLowerCase().contains("suporte")) {

                        Insight insight = new Insight(
                                1L,
                                TipoInsight.CHURN,
                                "Possível risco de cancelamento identificado.",
                                Severidade.ALTA,
                                analise
                        );

                        analise.adicionarInsight(insight);

                        Alerta alerta = new Alerta(
                                1L,
                                "Cliente apresenta sinais de risco.",
                                PrioridadeAlerta.ALTA,
                                analise
                        );

                        System.out.println("\nALERTA GERADO");
                        System.out.println(alerta);
                    }

                    if (texto.toLowerCase().contains("interesse")
                            || texto.toLowerCase().contains("novo módulo")
                            || texto.toLowerCase().contains("expansão")) {

                        Insight insight = new Insight(
                                2L,
                                TipoInsight.OPORTUNIDADE,
                                "Cliente demonstrou oportunidade comercial.",
                                Severidade.MEDIA,
                                analise
                        );

                        analise.adicionarInsight(insight);
                    }

                    analises.add(analise);

                    Chat chat = new Chat(
                            (long) (analises.size()),
                            "Análise " + cliente.getNome(),
                            LocalDateTime.now(),
                            usuarios.isEmpty()
                                    ? new DiretorComercial(
                                    999L,
                                    "Sistema",
                                    "sistema@mento.ai",
                                    "123",
                                    "Automático"
                            )
                                    : usuarios.get(0)
                    );

                    PerguntaChat pergunta = new PerguntaChat(
                            1L,
                            "Existe algum risco para este cliente?",
                            analise.gerarInsights().isEmpty()
                                    ? "Nenhum risco relevante encontrado."
                                    : "Foram encontrados insights que merecem atenção.",
                            chat
                    );

                    chat.adicionarPergunta(pergunta);

                    System.out.println("\n=== REUNIÃO REGISTRADA ===");

                    System.out.println("\nCLIENTE");
                    System.out.println(cliente);

                    System.out.println("\nREUNIÃO");
                    System.out.println(reuniao);

                    System.out.println("\nTRANSCRIÇÃO");
                    System.out.println(transcricao);

                    System.out.println("\nANÁLISE");
                    System.out.println(analise);

                    System.out.println("\nINSIGHTS");

                    if (analise.gerarInsights().isEmpty()) {

                        System.out.println("Nenhum insight encontrado.");

                    } else {

                        for (Insight insight : analise.gerarInsights()) {
                            System.out.println(insight);
                        }

                    }

                    System.out.println("\nCOPILOTO");
                    System.out.println(chat);

                    break;

                case 4:

                    System.out.println("\n=== REUNIÕES REGISTRADAS ===");

                    if (analises.isEmpty()) {

                        System.out.println("Nenhuma reunião registrada.");

                    } else {

                        for (AnaliseIA analiseAtual : analises) {

                            System.out.println("\n--------------------------------");

                            System.out.println("REUNIÃO " + analiseAtual.getReuniao().getCliente().getNome());
                            System.out.println(analiseAtual.getReuniao());

                            System.out.println("\nANÁLISE");
                            System.out.println(analiseAtual);

                            System.out.println("\nINSIGHTS");

                            if (analiseAtual.gerarInsights().isEmpty()) {

                                System.out.println("Nenhum insight encontrado.");

                            } else {

                                for (Insight insight : analiseAtual.gerarInsights()) {
                                    System.out.println(insight);
                                }

                            }

                            System.out.println("--------------------------------");
                        }

                    }

                    break;

                case 0:

                    System.out.println("\nSistema encerrado.");
                    break;

                default:

                    System.out.println("\nOpção inválida.");

            }

        } while (opcao != 0);

        scanner.close();
    }
}