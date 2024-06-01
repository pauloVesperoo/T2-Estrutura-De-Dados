import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstacionamentoPilha {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pilha<Carro> estacionamento = new Pilha<>();
        Pilha<Carro> rua = new Pilha<>();

        System.out.println("Olá, digite qual o tamanho do seu estacionamento. Digite 0 para encerrar");
        int capacidade = scanner.nextInt();

        while (capacidade != 0) {
            System.out.println("[1] Adicionar carro;\n[2] Remover carro;\n[3] Consultar carro");
            int controle = scanner.nextInt();

            switch (controle) {
                case 1:
                    if (estacionamento.getTamanho() < capacidade) {
                        System.out.println("Digite a placa do carro:");
                        String placa = scanner.next();
                        LocalDateTime entrada = LocalDateTime.now();

                        estacionamento.push(new Carro(placa, entrada));
                        System.out.println("Carro de placa " + placa + " adicionado ao estacionamento.");
                    } else {
                        System.out.println("Limite de carros atingido, não é possível adicionar mais carros.");
                    }
                    break;
                case 2:
                    if (!estacionamento.estaVazia()) {
                        System.out.println("Qual a placa do carro a remover?");
                        String placaRemover = scanner.next();
                        Carro carroRemovido = null;
                        while (!estacionamento.estaVazia() && !estacionamento.peek().placa.equals(placaRemover)) {
                            rua.push(estacionamento.pop());
                        }
                        if (!estacionamento.estaVazia()) {
                            carroRemovido = estacionamento.pop();
                        }
                        while (!rua.estaVazia()) {
                            estacionamento.push(rua.pop());
                        }
                        if (carroRemovido != null) {
                            LocalDateTime saida = LocalDateTime.now();
                            long minutos = java.time.Duration.between(carroRemovido.horario, saida).toMinutes();
                            System.out.println("Carro removido: \n" + carroRemovido + "\nTempo de permanência: " + minutos + " minutos.");
                        } else {
                            System.out.println("Carro não encontrado.");
                        }
                    } else {
                        System.out.println("Não há carros no estacionamento.");
                    }
                    break;
                case 3:
                    System.out.println("Digite a placa do carro que quer conferir:");
                    String placaConfere = scanner.next();
                    boolean encontrado = estacionamento.estaEstacionado(placaConfere);
                    if (encontrado) {
                        System.out.println("O carro se encontra no estacionamento.");
                    } else {
                        System.out.println("Carro não está no estacionamento.");
                    }
                    break;
                default:
                    System.out.println("Digite um valor entre 1 e 3.");
                    break;
            }

            System.out.println("\nDigite o tamanho do estacionamento para continuar ou 0 para encerrar:");
            capacidade = scanner.nextInt();
        }

        System.out.println("Ok, estacionamento encerrado.");
        scanner.close();
    }
}


class Pilha<Info> {
    private No<Info> topo;
    private int tamanho;

    public boolean estaVazia() {
        return topo == null;
    }

    public void push(Info info) {
        No<Info> novo = new No<>(info, topo);
        topo = novo;
        tamanho++;
    }

    public Info pop() {
        if (estaVazia()) return null;
        Info info = topo.getInfo();
        topo = topo.getProximo();
        tamanho--;
        return info;
    }

    public Info peek() {
        if (estaVazia()) return null;
        return topo.getInfo();
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean estaEstacionado(String placa) {
        No<Info> current = topo;
        while (current != null) {
            Carro carro = (Carro) current.getInfo();
            if (carro.placa.equals(placa)) {
                return true;
            }
            current = current.getProximo();
        }
        return false;
    }

    @Override
    public String toString() {
        if (estaVazia()) return "estacionamento vazio.";
        StringBuilder sb = new StringBuilder();
        No<Info> current = topo;
        while (current != null) {
            sb.append(current.getInfo().toString()).append("\n");
            current = current.getProximo();
        }
        return sb.toString();
    }
}

class Carro {
    String placa;
    LocalDateTime horario;
    int manobras = 0;

    public Carro(String placa, LocalDateTime horario) {
        this.placa = placa;
        this.horario = horario;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "------------------------------\n" +
               "placa: " + placa +
               "\nhorário de entrada: " + formatter.format(horario) +
               "\nnumero de manobras: " + manobras +
               "\n------------------------------";
    }
}

class No<Info> {
    private Info info;
    private No<Info> proximo;

    public No(Info info, No<Info> proximo) {
        this.info = info;
        this.proximo = proximo;
    }

    public Info getInfo() {
        return info;
    }

    public No<Info> getProximo() {
        return proximo;
    }
}
