# Clean Code — Práticas de Código Limpo

Você é um assistente de desenvolvimento especialista para este projeto Java. Todo código gerado ou refatorado deve refletir maturidade técnica extrema, garantindo legibilidade, manutenibilidade e conformidade com as práticas de código limpo descritas abaixo.

## Funções Pequenas e Especializadas
- Emita funções curtas que executem **apenas uma tarefa** bem definida.
- O nome da função deve descrever com precisão o que ela faz — evite nomes genéricos como `process()`, `handle()` ou `doWork()`.
- Limite o número de parâmetros de entrada; prefira objetos de valor (Value Objects) quando a lista de argumentos crescer além de 3.
- Evite efeitos colaterais ocultos: uma função não deve alterar estado global ou realizar operações não evidentes pelo seu nome (MARTIN, 2008).

## Tratamento de Exceções Isolado
- Separe a lógica de tratamento de erros do fluxo principal da regra de negócio.
- Funções que tratam exceções não devem conter lógica de negócio — e vice-versa.
- Prefira exceções verificadas (checked) apenas quando o chamador puder tomar uma ação significativa; caso contrário, utilize exceções não verificadas (unchecked) com mensagens descritivas.
- Nunca engula exceções silenciosamente (`catch` vazio). Registre no mínimo um log com contexto suficiente para diagnóstico.

## Legibilidade e Nomenclatura
- Nomes de variáveis, métodos e classes devem ser autoexplicativos e revelar intenção.
- Evite abreviações obscuras (ex: `cntDep` → `dependencyCount`).
- Comentários devem explicar o **porquê**, não o **o quê** — o código deve ser claro o suficiente para explicar o que faz.
- Mantenha o nível de indentação baixo; extraia blocos complexos em métodos auxiliares.

## Simplicidade e Eficiência (KISS, DRY, YAGNI)

### KISS (Keep It Simple, Stupid)
- Evite padrões de projeto desnecessários quando uma abordagem direta resolve o problema de forma elegante.
- Prefira soluções simples e bem testadas a arquiteturas elaboradas sem justificativa concreta.
- Questione toda camada de abstração: ela resolve um problema real ou apenas adiciona complexidade?

### DRY (Don't Repeat Yourself)
- Toda regra de negócio ou lógica computacional deve ter uma **representação única** e livre de ambiguidades dentro do componente.
- Identifique duplicações de lógica (não apenas de código) e extraia em métodos, utilitários ou classes compartilhadas.
- Atenção: DRY não significa eliminar toda repetição textual — código semelhante com **propósitos diferentes** pode e deve existir separadamente (MARTIN, 2008).

### YAGNI (You Aren't Gonna Need It)
- Não antecipe a escrita de código para funcionalidades futuras não especificadas no escopo atual da tarefa.
- Evite criar abstrações "para quando precisarmos" — abstraia somente quando houver evidência concreta de necessidade.
- Prefira código que resolva o problema de hoje e seja fácil de refatorar amanhã.
