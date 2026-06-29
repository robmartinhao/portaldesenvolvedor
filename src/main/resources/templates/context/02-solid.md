# SOLID — Princípios de Design Orientado a Objetos

Aplique estritamente os cinco princípios SOLID de design orientado a objetos (MARTIN, 2017) em todo código gerado ou refatorado. Esses princípios garantem que os componentes sejam coesos, desacoplados e fáceis de estender.

## S — Single Responsibility Principle (SRP)
- Cada classe deve possuir **uma única razão para mudar**.
- Se uma classe acumula múltiplas responsabilidades (ex: validação, persistência e formatação), extraia cada responsabilidade em sua própria classe.
- Serviços devem orquestrar, não implementar diretamente lógica de domínio e infraestrutura simultaneamente.
- Indicadores de violação: classes com muitos métodos públicos, injeção excessiva de dependências, ou nomes genéricos como `Manager`, `Handler`, `Processor`.

## O — Open/Closed Principle (OCP)
- Classes devem estar **abertas para extensão** e **fechadas para modificação**.
- Utilize polimorfismo, interfaces e composição para permitir novos comportamentos sem alterar código existente.
- Evite cadeias de `if/else` ou `switch` que crescem a cada novo tipo — prefira o padrão Strategy ou um registry de implementações.
- Exemplo prático: ao adicionar um novo formato de exportação, crie uma nova implementação da interface `Exporter` sem modificar as existentes.

## L — Liskov Substitution Principle (LSP)
- Subtipos devem ser **substituíveis** por seus tipos base sem alterar o comportamento esperado do programa.
- Não lance exceções inesperadas em subclasses que a classe base não declara.
- Não restrinja pré-condições nem relaxe pós-condições em implementações de interfaces ou classes abstratas.
- Cuidado com herança que viola contratos: se `Quadrado extends Retangulo` quebra invariantes, prefira composição.

## I — Interface Segregation Principle (ISP)
- Clientes não devem ser forçados a depender de interfaces que não utilizam.
- Prefira **interfaces pequenas e focadas** a interfaces "gordas" com muitos métodos.
- Divida interfaces genéricas em contratos específicos por papel (ex: `Readable`, `Writable` ao invés de `ReadWritable`).
- Em Spring, aplique este princípio ao definir contratos de serviços e repositórios — exponha apenas o necessário.

## D — Dependency Inversion Principle (DIP)
- Módulos de alto nível não devem depender de módulos de baixo nível; ambos devem depender de **abstrações**.
- Abstrações não devem depender de detalhes de implementação — os detalhes é que devem depender das abstrações.
- Utilize injeção de dependências (preferencialmente via construtor) para desacoplar componentes.
- Em Spring Boot, aproveite o container de IoC: injete interfaces, não implementações concretas.
- Evite instanciar dependências manualmente com `new` dentro de serviços — delegue ao framework.
