# Banco Imobiliário

**Disciplina:** Estrutura de Dados  
**Aluno:** Miguel Mengue  
**Instituição:** ULBRA — Campus Torres  
**Professor:** Juliano Ramos Matos

---

## Sobre o projeto

É um jogo de tabuleiro inspirado no Banco Imobiliário, rodando no terminal em Java. De 2 a 6 jogadores se revezam lançando dados, comprando imóveis, pagando aluguel, indo pra prisão e participando de leilões.

---

## Como rodar

### Pelo IntelliJ

1. Abra a pasta `chess_game` como projeto
2. Clique com o botão direito em `src/Main.java` → **Run 'Main'**

### Pelo terminal

```bash
# Dentro da pasta chess_game
javac -encoding UTF-8 -d out -cp src src/Main.java src/model/*.java src/service/*.java src/structures/*.java src/util/*.java

java -cp out Main
```

> Precisa do Java 17 ou superior.

---

## Como jogar

Ao abrir, aparece um menu de configuração onde você pode cadastrar jogadores, editar imóveis e ajustar as configurações antes de iniciar.

O jogo já vem com 12 imóveis padrão (cidades brasileiras) e você precisa de pelo menos 2 jogadores cadastrados para iniciar.

**O que dá pra configurar:**
- Saldo inicial (padrão: R$ 1.500.000)
- Salário por volta (padrão: R$ 200.000)
- Fiança da prisão
- Número máximo de rodadas (padrão: 50)
- Tamanho do histórico de rodadas

---

## Casas do tabuleiro

| Casa | Efeito |
|------|--------|
| **INÍCIO** | Ao passar, recebe o salário. Retroceder pelo início não paga. |
| **IMÓVEL** | Compra se estiver livre, paga aluguel se for de outro. O aluguel sobe a cada visita. |
| **IMPOSTO** | Paga 5% do patrimônio total. |
| **RESTITUIÇÃO** | Recebe 10% do salário de volta. |
| **SORTE/REVÉS** | Saca uma carta do baralho. |
| **PRISÃO** | Entra na fila de presos. |
| **LEILÃO** | Um imóvel livre é sorteado e todos fazem lances. |

---

## Personagens

Cada jogador escolhe um personagem na hora de se cadastrar:

| Personagem | Habilidade |
|------------|------------|
| **Especulador** | +20% no salário, mas +10% de imposto |
| **Negociante** | Paga 10% menos de aluguel |
| **Advogado** | Sai da prisão de graça uma vez por jogo |
| **Construtor** | Imóveis comprados têm +15% no aluguel base |

---

## Leilão

Quando um jogador para na casa de Leilão, um imóvel sem dono é sorteado. Os jogadores fazem lances em ordem de turno, começando pelo próximo depois de quem caiu na casa. O lance mínimo é 50% do valor original. Quem der o maior lance leva.

---

## Prisão

O jogador fica preso e entra numa fila. No começo de cada rodada, os presos tentam sair na ordem em que foram presos. Para sair:
1. Pagar a fiança → sai e joga normalmente
2. Tirar dados duplos → sai e avança
3. Depois de 3 tentativas sem dados duplos → sai mas perde a vez

---

## Fim de jogo

A partida termina quando o número máximo de rodadas é atingido ou quando restar só um jogador ativo. No final aparece o relatório com classificação por patrimônio, imóvel destaque e histórico das últimas rodadas.

---

## Estruturas de dados usadas

- **Lista duplamente ligada circular** → tabuleiro (suporta avançar e retroceder no circuito fechado)
- **Pilha** → baralho de cartas (saca do topo, remonta automaticamente ao esvaziar)
- **Fila** → histórico de rodadas e controle de presos (processa na ordem de chegada)

---

## Estrutura do projeto

```
chess_game/
└── src/
    ├── Main.java
    ├── model/
    │   ├── Carta.java
    │   ├── Casa.java
    │   ├── ConfiguracaoJogo.java
    │   ├── Imovel.java
    │   ├── Jogador.java
    │   ├── RegistroHistorico.java
    │   ├── TipoCarta.java
    │   ├── TipoCasa.java
    │   └── TipoPersonagem.java
    ├── service/
    │   ├── CartaService.java
    │   ├── DadoService.java
    │   ├── ImovelService.java
    │   ├── JogadorService.java
    │   ├── JogoService.java
    │   └── TabuleiroService.java
    ├── structures/
    │   ├── Fila.java
    │   ├── ListaDuplamenteLigadaCircular.java
    │   ├── NoCasa.java
    │   └── PilhaCartas.java
    └── util/
        └── ConsoleUtil.java
```

---

## Observação

As funcionalidades bônus do enunciado (negociação entre jogadores, hipoteca, ranking com BST e desfazer com deque) não foram implementadas por ser trabalho individual.