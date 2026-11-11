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

## Interfaces

### `Monitoravel`

Interface implementada pelos sensores.

Responsabilidade:

- Permitir a leitura do valor atual de um sensor.

Método definido:

```java
double lerValor() throws SensorFalhaException;
