# Contrato do Projeto

## Projeto

Framework IoT — Trabalho RA2 e RA3

## Objetivo

Este documento reunirá as decisões compartilhadas sobre a estrutura do projeto, as classes, os métodos e a comunicação entre os integrantes da equipe.

As assinaturas definidas aqui deverão ser respeitadas durante a implementação.

## Regra para alterações

Qualquer alteração neste contrato deverá ser discutida e aprovada pela equipe antes de ser implementada.

## Status

Contrato em definição.

## Pacotes do projeto

| Pacote | Responsabilidade |
|---|---|
| `modelo` | Classes centrais da casa, cômodos e dispositivos |
| `interfaces` | Interfaces utilizadas pelo sistema |
| `sensores` | Sensores e suas especializações |
| `atuadores` | Atuadores e suas especializações |
| `excecoes` | Exceções personalizadas da aplicação |
| `logica` | Regras de controle e execução da simulação |
| `log` | Registro dos eventos da aplicação |
| `persistencia` | Leitura de dados, P1 e restauração dos objetos |
| `interfacegrafica` | P2 e componentes da interface gráfica |

## Responsabilidade inicial por integrante

| Integrante | Responsabilidade |
|---|---|
| Pessoa 1 | Modelo-base, interfaces e integração da arquitetura |
| Pessoa 2 | Sensores e lógica de controle |
| Pessoa 3 | Atuadores |
| Pessoa 4 | Persistência e registros |
| Pessoa 5 | Interface gráfica e P2 |

## Programas

### P1

O programa P1 deverá ler os dados iniciais de um arquivo CSV, criar os objetos da casa e salvá-los em um arquivo binário.

### P2

O programa P2 deverá restaurar os objetos do arquivo binário, abrir a interface gráfica, executar a simulação e registrar os resultados.

## Classes do pacote `modelo`

### `Dispositivo`

Classe abstrata que representa qualquer dispositivo instalado na casa.

- Implementa `Serializable`.
- Atributos privados:
  - `id`
  - `nome`
  - `comodoNome`
  - `x`
  - `y`
- Declara o método abstrato:

```java
public abstract String getStatus();
```

### `Comodo`

Representa um cômodo da casa.

- Implementa `Serializable`.
- Atributos privados:
  - `id`
  - `nome`
  - `x`
  - `y`
  - `largura`
  - `altura`

Métodos definidos:

```java
public Comodo(
        int id,
        String nome,
        int x,
        int y,
        int largura,
        int altura);

public int getId();

public String getNome();

public int getX();

public int getY();

public int getLargura();

public int getAltura();

public void setNome(String nome);

public void setX(int x);

public void setY(int y);

public void setLargura(int largura);

public void setAltura(int altura);
```

### `Casa`

Representa a casa monitorada.

- Implementa `Serializable`.
- Possui:
  - `nome`;
  - lista de cômodos;
  - lista de dispositivos.
- A lista de dispositivos deve usar o tipo abstrato `Dispositivo` e será a fonte única dos dispositivos da casa.
- `Casa` utilizará os tipos `List<Comodo>`, `List<Dispositivo>`, `List<Sensor>` e `List<Atuador>`.
- Os métodos `getSensores()` e `getAtuadores()` deverão filtrar a lista de dispositivos e retornar os objetos correspondentes, sem manter listas duplicadas.

Métodos definidos:

```java
public Casa(String nome);

public String getNome();

public void setNome(String nome);

public List<Comodo> getComodos();

public List<Dispositivo> getDispositivos();

public void adicionarComodo(Comodo comodo);

public void adicionarDispositivo(Dispositivo dispositivo);

public List<Sensor> getSensores();

public List<Atuador> getAtuadores();

public Comodo buscarComodoPorNome(String nome);

public Dispositivo buscarDispositivoPorId(String id);
```

## Interfaces

### `Monitoravel`

Interface implementada pelos sensores.

Responsabilidade:

- Permitir a leitura do valor atual de um sensor.

Método definido:

```java
double lerValor() throws SensorFalhaException;
```

### `Registravel`

Interface implementada pelos atuadores.

Responsabilidade:

- Fornecer uma representação textual do estado ou da ação de um atuador.

Método definido:

```java
String gerarRegistro();
```

### Decisão de implementação

- `Sensor` implementará `Monitoravel`.
- `Atuador` implementará `Registravel`.
- As subclasses concretas herdarão essas implementações por meio de suas respectivas classes abstratas.

## Exceções personalizadas

### `SensorFalhaException`

Pacote:

```text
excecoes
```

Responsável por representar falhas durante a leitura de um sensor.

Declaração:

```java
public class SensorFalhaException extends Exception {
    public SensorFalhaException(String mensagem);
}
```

Essa exceção deverá ser criada e lançada com `throw` quando um sensor estiver em falha ou quando sua leitura for inválida.

### `AtuadorIndisponivelException`

Pacote:

```text
excecoes
```

Responsável por representar uma tentativa de acionar ou desligar um atuador indisponível.

Declaração:

```java
public class AtuadorIndisponivelException extends Exception {
    public AtuadorIndisponivelException(String mensagem);
}
```

Essa exceção deverá ser criada e lançada com `throw` quando um atuador estiver indisponível ou defeituoso.

### Regra comum

As duas exceções deverão:

- Estender diretamente `Exception`;
- Ser exceções verificadas;
- Ser instanciadas com `new`;
- Ser lançadas com `throw`;
- Ser tratadas ou repassadas pelo código chamador.

## Classes abstratas

### `Sensor`

Pacote:

```text
sensores
```

Classe abstrata que representa um sensor da casa.

- Estende `Dispositivo`.
- Implementa `Monitoravel`.
- Possui um estado que indica se está funcionando.
- Deve permitir alterar e consultar esse estado.

Métodos definidos:

```java
public abstract double lerValor()
        throws SensorFalhaException;

public abstract String getTipo();

public boolean isFuncionando();

public void setFuncionando(boolean funcionando);

@Override
public abstract String getStatus();
```

### `Atuador`

Pacote:

```text
atuadores
```

Classe abstrata que representa um atuador da casa.

- Estende `Dispositivo`.
- Implementa `Registravel`.
- Possui estado de disponibilidade.
- Possui estado de funcionamento, indicando se está ligado ou desligado.

Métodos definidos:

```java
public abstract void acionar()
        throws AtuadorIndisponivelException;

public abstract void desligar()
        throws AtuadorIndisponivelException;

public boolean isDisponivel();

public void setDisponivel(boolean disponivel);

public boolean isLigado();

@Override
public abstract String getStatus();

@Override
public abstract String gerarRegistro();
```

## Sensores concretos

### `Termometro`

Pacote:

```text
sensores
```

Representa um sensor de temperatura.

- Estende `Sensor`.
- Mantém a temperatura atual em graus Celsius.
- A leitura deve retornar a temperatura atual.
- Deve permitir aumentar e diminuir a temperatura pela interface gráfica.

Métodos definidos:

```java
public double getTemperaturaAtual();

public void setTemperaturaAtual(double temperaturaAtual);

public void aumentarTemperatura(double valor);

public void diminuirTemperatura(double valor);

@Override
public double lerValor()
        throws SensorFalhaException;

@Override
public String getTipo();

@Override
public String getStatus();
```

Regras:

- Se o sensor estiver com falha, `lerValor()` deverá lançar `SensorFalhaException`.
- O tipo retornado por `getTipo()` deverá identificar o sensor como termômetro.
- A temperatura será expressa em graus Celsius.

### `SensorFumaca`

Pacote:

```text
sensores
```

Representa um sensor de fumaça.

- Estende `Sensor`.
- Mantém a informação sobre a presença ou ausência de fumaça.
- Deve permitir forçar e dissipar fumaça pela interface gráfica.

Métodos definidos:

```java
public boolean isFumacaDetectada();

public void setFumacaDetectada(boolean fumacaDetectada);

public void forcarFumaca();

public void dissiparFumaca();

@Override
public double lerValor()
        throws SensorFalhaException;

@Override
public String getTipo();

@Override
public String getStatus();
```

Convenção da leitura:

- `1.0`: fumaça detectada;
- `0.0`: ausência de fumaça.

Regras:

- Se o sensor estiver com falha, `lerValor()` deverá lançar `SensorFalhaException`.
- O tipo retornado por `getTipo()` deverá identificar o sensor como sensor de fumaça.

## Atuadores concretos

### `ArCondicionado`

Pacote:

```text
atuadores
```

Representa o aparelho de ar-condicionado da casa.

- Estende `Atuador`.
- Deve ser acionado quando a temperatura estiver acima do limite definido.
- Deve ser desligado quando a temperatura voltar ao nível normal.

Métodos definidos:

```java
@Override
public void acionar()
        throws AtuadorIndisponivelException;

@Override
public void desligar()
        throws AtuadorIndisponivelException;

@Override
public String getStatus();

@Override
public String gerarRegistro();
```

### `Sirene`

Pacote:

```text
atuadores
```

Representa a sirene de alerta da casa.

- Estende `Atuador`.
- Deve ser acionada quando houver fumaça.
- Deve ser desligada quando não houver fumaça.

Métodos definidos:

```java
@Override
public void acionar()
        throws AtuadorIndisponivelException;

@Override
public void desligar()
        throws AtuadorIndisponivelException;

@Override
public String getStatus();

@Override
public String gerarRegistro();
```

### Regras dos atuadores

Os métodos `acionar()` e `desligar()` deverão:

- Verificar se o atuador está disponível;
- Lançar `AtuadorIndisponivelException` quando estiver indisponível;
- Atualizar o estado ligado/desligado quando a operação for realizada;
- Permitir que a interface gráfica consulte o estado por meio de `getStatus()`.

A classe `ArCondicionado` deverá ser localizada pelo controlador quando houver temperatura acima do limite.

A classe `Sirene` deverá ser localizada pelo controlador quando houver fumaça detectada.

## Controlador

### `Controlador`

Pacote:

```text
logica
```

Responsável por executar os ciclos de monitoramento da casa.

O controlador deverá receber:

- Uma lista de sensores;
- Uma lista de atuadores;
- Um objeto `LogSistema`.

A lista deve utilizar os tipos abstratos `Sensor` e `Atuador`.

Métodos definidos:

```java
public Controlador(
        List<Sensor> sensores,
        List<Atuador> atuadores,
        LogSistema logSistema);

public void executarCiclo()
        throws IOException;

public void iniciar();

public void pausar();

public boolean isExecutando();

public List<Sensor> getSensores();

public List<Atuador> getAtuadores();
```

### Regras de `executarCiclo()`

A cada ciclo, o controlador deverá:

1. Percorrer a lista de sensores.
2. Chamar `lerValor()` por meio de uma referência do tipo abstrato `Sensor`.
3. Registrar as leituras no `LogSistema`.
4. Se a temperatura for maior que `28 °C`, acionar o `ArCondicionado`.
5. Se a temperatura for igual ou menor que `28 °C`, desligar o `ArCondicionado`.
6. Se a leitura do sensor de fumaça for `1.0`, acionar a `Sirene`.
7. Se a leitura do sensor de fumaça for `0.0`, desligar a `Sirene`.
8. Registrar as ações dos atuadores.
9. Capturar `SensorFalhaException`.
10. Capturar `AtuadorIndisponivelException`.
11. Registrar as falhas sem interromper toda a simulação.

### Polimorfismo

O controlador deverá utilizar referências abstratas, por exemplo:

```java
for (Sensor sensor : sensores) {
    double valor = sensor.lerValor();
}
```

O código não deve depender diretamente de uma classe concreta para realizar a leitura de todos os sensores.

## Registro de eventos

### `LogSistema`

Pacote:

```text
log
```

Responsável por registrar os resultados parciais e finais da execução do P2.

O log deverá ser salvo em arquivo `.csv` ou `.txt`, sempre incluindo data e hora do evento.

A classe deverá possuir:

- O caminho do arquivo de saída;
- Métodos para registrar leituras;
- Métodos para registrar ações dos atuadores;
- Métodos para registrar falhas;
- Método para registrar o encerramento da simulação.

Métodos definidos:

```java
public LogSistema(String caminhoArquivo);

public String getCaminhoArquivo();

public void registrar(String mensagem)
        throws IOException;

public void registrarLeitura(
        String sensorId,
        String tipoSensor,
        double valor)
        throws IOException;

public void registrarAcao(
        String atuadorId,
        String acao,
        String status)
        throws IOException;

public void registrarFalha(
        String origem,
        String mensagem)
        throws IOException;

public void registrarEncerramento()
        throws IOException;
```

## Leitura dos dados iniciais

### `LeitorDados`

Pacote:

```text
persistencia
```

Responsável por ler o arquivo `dados_iniciais.csv`, validar seus registros e criar o objeto `Casa` com seus cômodos, sensores e atuadores.

Método definido:

```java
public Casa lerDadosIniciais(String caminhoArquivo)
        throws IOException;
```

O método deverá:

- Abrir o arquivo CSV;
- Ler cada linha;
- Identificar o tipo de registro;
- Validar a quantidade de campos;
- Validar valores numéricos;
- Verificar IDs duplicados;
- Verificar se o cômodo informado existe;
- Criar os objetos correspondentes;
- Associar cada dispositivo ao cômodo informado;
- Retornar uma instância de `Casa`.

### Formato dos registros

```text
CASA;nome
COMODO;id;nome;x;y;largura;altura
TERMOMETRO;id;nomeDoComodo;x;y
SENSORFUMACA;id;nomeDoComodo;x;y
ARCONDICIONADO;id;nomeDoComodo;x;y
SIRENE;id;nomeDoComodo;x;y
```

### Política para erros no CSV

Durante o P1, se for encontrado um erro de formato, ID duplicado ou cômodo inexistente, o processamento deverá ser interrompido e o erro deverá ser informado ao usuário.

O arquivo binário não deverá ser gerado quando a leitura do CSV falhar.

## Programa P1

### `P1`

Pacote:

```text
persistencia
```

O P1 será executado separadamente e será responsável por:

1. Ler o arquivo `dados_iniciais.csv`;
2. Criar os objetos da casa por meio de `LeitorDados`;
3. Salvar a casa e todos os seus objetos em formato binário;
4. Gerar o arquivo `casa.dat`.

Declaração principal:

```java
public static void main(String[] args);
```

### Fluxo esperado

```text
dados_iniciais.csv
        |
        v
LeitorDados
        |
        v
Casa e dispositivos
        |
        v
casa.dat
```

### Regras

- O P1 não deverá abrir a interface gráfica.
- O P1 deverá ser executável independentemente do P2.
- O arquivo `casa.dat` somente deverá ser criado se o CSV for válido.
- O P1 deverá informar ao usuário se a criação do arquivo foi concluída ou se ocorreu algum erro.

## Restauração dos objetos

### `Restaurador`

Pacote:

```text
persistencia
```

Responsável por restaurar em memória o objeto `Casa` e todos os objetos associados a partir do arquivo binário gerado pelo P1.

Método definido:

```java
public Casa restaurar(String caminhoArquivo)
        throws IOException, ClassNotFoundException;
```

O método deverá:

- Abrir o arquivo binário com `ObjectInputStream`;
- Ler o objeto serializado;
- Verificar se o objeto restaurado é uma instância de `Casa`;
- Retornar a casa restaurada;
- Repassar `IOException` e `ClassNotFoundException` ao método chamador.

### Regras

- O método `restaurar()` não deverá capturar internamente as exceções declaradas com `try-catch`.
- O tratamento dessas exceções deverá ocorrer no P2 ou em outra classe chamadora.
- O arquivo binário deverá ter sido criado anteriormente pelo P1.
- O P2 não deverá recriar os objetos a partir do CSV; deverá restaurá-los usando o arquivo binário.

## Programa P2

### `P2`

Pacote:

```text
interfacegrafica
```

O P2 será executado separadamente e será responsável por:

1. Restaurar a casa a partir do arquivo `casa.dat`;
2. Obter os sensores e atuadores restaurados;
3. Criar o `LogSistema`;
4. Criar o `Controlador`;
5. Abrir a interface gráfica;
6. Executar a simulação;
7. Registrar os resultados parciais e finais.

Declaração principal:

```java
public static void main(String[] args);
```

### Fluxo esperado

```text
casa.dat
   |
   v
Restaurador
   |
   v
Casa restaurada
   |
   v
Controlador + LogSistema
   |
   v
Interface gráfica
```

### Regras

- O P2 não deverá ler o arquivo CSV diretamente.
- O P2 deverá usar o `Restaurador` para carregar os objetos.
- O P2 deverá ser executável separadamente do P1, desde que o arquivo `casa.dat` já exista.
- O P2 deverá registrar eventos parciais durante a simulação.
- O P2 deverá registrar o encerramento da simulação no arquivo de resultados.

### Obtenção dos dispositivos

Após restaurar a casa, o P2 deverá obter os dispositivos por meio dos métodos da própria `Casa`:

```java
Casa casa = restaurador.restaurar("casa.dat");

List<Sensor> sensores = casa.getSensores();
List<Atuador> atuadores = casa.getAtuadores();

Controlador controlador =
        new Controlador(sensores, atuadores, logSistema);
```

A `Casa` será a fonte única dos dispositivos. O P2 não deverá criar listas independentes a partir de classes concretas nem duplicar os dispositivos restaurados.

## Interface gráfica

### `JanelaPrincipal`

Pacote:

```text
interfacegrafica
```

Responsável por exibir a planta da casa, os dispositivos e os controles da simulação.

A janela deverá:

- Exibir os cômodos como retângulos;
- Posicionar sensores e atuadores usando suas coordenadas;
- Mostrar o status dos dispositivos;
- Permitir iniciar e pausar a simulação;
- Permitir forçar e dissipar fumaça;
- Permitir aumentar e diminuir a temperatura;
- Atualizar a tela periodicamente;
- Encerrar corretamente o temporizador ao fechar a janela.

A classe deverá utilizar `JFrame` e `javax.swing.Timer`.

Construtor e métodos definidos:

```java
public JanelaPrincipal(
        Casa casa,
        Controlador controlador);

public void atualizarTela();

public void iniciarSimulacao();

public void pausarSimulacao();

public void forcarFumaca();

public void dissiparFumaca();

public void aumentarTemperatura();

public void diminuirTemperatura();
```

### Regras da interface

- A janela não deverá acessar diretamente os atributos privados dos objetos.
- A comunicação deverá ocorrer por métodos públicos, como `getStatus()`, `forcarFumaca()` e `aumentarTemperatura()`.
- A interface deverá trabalhar com referências do tipo `Dispositivo`, `Sensor` e `Atuador` sempre que possível.
- O `javax.swing.Timer` deverá atualizar a simulação sem bloquear a interface.
- O controlador deverá executar os ciclos de monitoramento.

## Arquivo de dados iniciais

### `dados_iniciais.csv`

O arquivo utilizará ponto e vírgula (`;`) como separador.

Cada linha deverá representar a casa, um cômodo ou um dispositivo.

### Registros permitidos

```text
CASA;nome
COMODO;id;nome;x;y;largura;altura
TERMOMETRO;id;nomeDoComodo;x;y
SENSORFUMACA;id;nomeDoComodo;x;y
ARCONDICIONADO;id;nomeDoComodo;x;y
SIRENE;id;nomeDoComodo;x;y
```

### Exemplo

```text
CASA;Casa Principal
COMODO;1;Sala;0;0;200;150
COMODO;2;Cozinha;200;0;150;150
TERMOMETRO;T1;Sala;50;50
SENSORFUMACA;F1;Cozinha;250;60
ARCONDICIONADO;A1;Sala;100;20
SIRENE;S1;Cozinha;300;20
```

### Validações obrigatórias

O P1 deverá verificar:

- Tipo de registro válido;
- Quantidade correta de campos;
- Valores numéricos válidos;
- IDs não duplicados;
- Existência do cômodo informado;
- Existência de uma única casa;
- Valores positivos para largura e altura dos cômodos.

Se houver erro no CSV, o P1 deverá informar o problema e não deverá gerar o arquivo `casa.dat`.

## Arquivo de resultados

### Arquivo de log

O P2 deverá gerar um arquivo de resultados em formato `.csv` ou `.txt`.

O nome padrão será:

```text
resultado.csv
```

Cada linha deverá seguir o formato:

```text
timestamp;tipo;identificacao;descricao
```

Exemplos:

```text
2026-11-11T16:50:00;LEITURA;T1;Temperatura=30.5
2026-11-11T16:50:01;ACAO;A1;ACIONAR - status=ligado
2026-11-11T16:50:02;FALHA;F1;Falha na leitura
2026-11-11T16:51:00;FIM;SISTEMA;Simulacao encerrada
```

Tipos de registro previstos:

- `LEITURA`: leitura realizada por um sensor;
- `ACAO`: acionamento ou desligamento de um atuador;
- `FALHA`: erro ou exceção ocorrida durante a simulação;
- `FIM`: encerramento da simulação.

## Construtores dos dispositivos

### `Dispositivo`

```java
protected Dispositivo(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

O construtor é `protected` porque `Dispositivo` é abstrata e será inicializada pelas classes derivadas.

### `Sensor`

```java
protected Sensor(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

### `Atuador`

```java
protected Atuador(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

Os construtores de `Sensor` e `Atuador` deverão inicializar os atributos herdados de `Dispositivo` e seus estados iniciais.

### `Termometro`

```java
public Termometro(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y,
        double temperaturaInicial);
```

A temperatura inicial deverá ser armazenada em graus Celsius.

### `SensorFumaca`

```java
public SensorFumaca(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

O estado inicial deverá indicar ausência de fumaça.

### `ArCondicionado`

```java
public ArCondicionado(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

O estado inicial deverá ser disponível e desligado.

### `Sirene`

```java
public Sirene(
        String id,
        String nome,
        String comodoNome,
        int x,
        int y);
```

O estado inicial deverá ser disponível e desligado.


## Checklist dos requisitos

### RA2

- [ ] Duas exceções personalizadas derivadas de `Exception`;
- [ ] Uso de `throw` nas duas exceções;
- [ ] Dois métodos com `throws` sem `try-catch` interno;
- [ ] Pelo menos dois pacotes;
- [ ] Atributos encapsulados;
- [ ] Generalização e especialização;
- [ ] Polimorfismo;
- [ ] Classe abstrata;
- [ ] Método abstrato;
- [ ] Interface implementada por uma classe;
- [ ] Chamada de método polimórfica.

### RA3

- [ ] P1 lê o arquivo CSV;
- [ ] P1 cria os objetos da casa;
- [ ] P1 salva os objetos em formato binário;
- [ ] P2 restaura os objetos em memória;
- [ ] P2 possui interface gráfica;
- [ ] P2 registra resultados parciais;
- [ ] P2 registra o resultado final.

### Entregáveis

- [ ] Código-fonte completo;
- [ ] `dados_iniciais.csv`;
- [ ] `casa.dat`;
- [ ] `resultado.csv` ou arquivo equivalente;
- [ ] README atualizado;
- [ ] Vídeo de até 10 minutos.
