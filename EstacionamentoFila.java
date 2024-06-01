import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.Duration;

public class EstacionamentoFila {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite a capacidade máxima do estacionamento:");
        int capacidade = scanner.nextInt();
        scanner.nextLine(); 
        
        FilaGenerica<Carro> estacionamento = new FilaGenerica<>(capacidade); 

        while (true) {
            System.out.println("Escolha uma opção:\n1. Entrar carro\n2. Sair carro\n3. Consultar carro\n4. Sair do programa");
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    if (estacionamento.estaCheia()) {
                        System.out.println("Estacionamento cheio!");
                    } else {
                        System.out.println("Digite a placa do carro:");
                        String placa = scanner.nextLine();
                        estacionamento.enfileira(new Carro(placa, LocalDateTime.now()));
                        System.out.println("Carro adicionado.");
                    }
                    break;
                case 2:
                    if (estacionamento.estaVazia()) {
                        System.out.println("Estacionamento vazio!");
                    } else {
                        Carro carro = estacionamento.desenfileira();
                        LocalDateTime horarioSaida = LocalDateTime.now();
                        long minutos = Duration.between(carro.getHorarioEntrada(), horarioSaida).toMinutes();
                        System.out.println("Carro " + carro.getPlaca() + " saiu. Tempo de permanência: " + minutos + " minutos.");
                    }
                    break;
                case 3:
                    System.out.println("Digite a placa do carro para consulta:");
                    String placaConsulta = scanner.nextLine();
                    int posicao = estacionamento.posicaoNaFila(placaConsulta);
                    if (posicao != -1) {
                        System.out.println("Carro está na posição: " + posicao);
                    } else {
                        System.out.println("Carro não encontrado.");
                    }
                    break;
                case 4:
                    System.out.println("Saindo do programa...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }
}

class FilaGenerica<E> {
    private E[] dados;
    private int primeiro;
    private int ultimo;
    private int tamanho;
    private int capacidade;

    @SuppressWarnings("unchecked")
    public FilaGenerica(int capacidade) {
        this.capacidade = capacidade;
        dados = (E[]) new Object[capacidade];
        primeiro = 0;
        ultimo = -1;
        tamanho = 0;
    }

    public boolean estaCheia() {
        return tamanho == capacidade;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public void enfileira(E elemento) {
        if (!estaCheia()) {
            ultimo = (ultimo + 1) % capacidade;
            dados[ultimo] = elemento;
            tamanho++;
        }
    }

    public E desenfileira() {
        if (!estaVazia()) {
            E elemento = dados[primeiro];
            primeiro = (primeiro + 1) % capacidade;
            tamanho--;
            return elemento;
        }
        return null;
    }

    public int posicaoNaFila(String placa) {
        int pos = 0;
        for (int i = 0; i < tamanho; i++) {
            int idx = (primeiro + i) % capacidade;
            Carro carro = (Carro) dados[idx];
            if (carro.getPlaca().equals(placa)) {
                return pos + 1;
            }
            pos++;
        }
        return -1;
    }
}

class Carro {
    private String placa;
    private LocalDateTime horarioEntrada;

    public Carro(String placa, LocalDateTime horarioEntrada) {
        this.placa = placa;
        this.horarioEntrada = horarioEntrada;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public String getPlaca() {
        return placa;
    }
}
