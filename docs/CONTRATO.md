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

### `Casa`

Representa a casa monitorada.

- Implementa `Serializable`.
- Possui:
  - `nome`;
  - lista de cômodos;
  - lista de dispositivos.
- A lista de dispositivos deve usar o tipo abstrato `Dispositivo`.

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
