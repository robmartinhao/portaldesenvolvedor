# Java Idioms — Robustez e Idiomatismo na Plataforma Java

Siga os padrões idiomáticos modernos do ecossistema Java para evitar gargalos de performance e vulnerabilidades de memória. O comportamento padrão do gerador de código deve seguir as melhores práticas descritas por Bloch (2018) em "Effective Java".

## Métodos de Fábrica Estáticos e Builders
- Prefira **static factory methods** a construtores públicos para reutilização de instâncias e melhor legibilidade na criação de objetos.
- Caso a classe possua múltiplos parâmetros opcionais, implemente o **padrão Builder** para evitar construtores telescópicos.
- Nomeie factory methods de forma descritiva: `of()`, `from()`, `valueOf()`, `newInstance()`, `create()`.
- Exemplo: `Duration.ofMinutes(5)` é mais legível que `new Duration(5, TimeUnit.MINUTES)`.

## Imutabilidade por Padrão
- Projete classes imutáveis por padrão utilizando **Java Records** (JEP 395) para garantir thread-safety sem sincronização.
- Marque campos como `final` sempre que possível — mesmo em classes não-record.
- Retorne cópias defensivas de coleções mutáveis em getters para preservar a imutabilidade do objeto.
- Utilize `List.of()`, `Set.of()`, `Map.of()` para criar coleções imutáveis.
- Evite setters em objetos de domínio — prefira métodos que retornem novas instâncias (`withXxx()`).

## Gerenciamento Seguro de Recursos
- Utilize invariavelmente a estrutura **try-with-resources** para abertura de conexões, fluxos de arquivos ou quaisquer recursos que implementem `AutoCloseable`.
- Nunca confie em `finally` manual para fechar recursos — a estrutura try-with-resources é mais segura e legível.
- Encapsule operações de I/O em métodos que gerenciem o ciclo de vida do recurso, eliminando o risco de vazamentos de memória (memory leaks).

## Tratamento de Coleções e Null-Safety
- **Proíba** o retorno de referências `null` para métodos que devolvem coleções ou arrays — substitua por coleções vazias estáveis (ex: `Collections.emptyList()`, `List.of()`).
- Utilize `Optional<T>` para retornos que podem legitimamente não existir — mas nunca use `Optional` como parâmetro de método ou campo de classe.
- Prefira `Objects.requireNonNull()` em construtores e métodos públicos para falhar rápido (fail-fast) com mensagens descritivas.
- Aproveite o operador `instanceof` com pattern matching (JEP 394) para eliminação segura de casts.

## APIs Modernas do Java
- Utilize **Streams** para transformações de coleções, mas prefira loops tradicionais quando a lógica for complexa ou exigir efeitos colaterais.
- Aproveite **text blocks** (`"""`) para strings multilinha (SQL, JSON, XML) ao invés de concatenações manuais.
- Utilize **sealed classes** (JEP 409) e **pattern matching em switch** (JEP 441) quando aplicável para modelagem de domínio fechada.
- Prefira `var` para variáveis locais quando o tipo é óbvio pelo lado direito da atribuição — mas nunca sacrifique clareza por brevidade.
