# Portal do Desenvolvedor

O **Portal do Desenvolvedor** é uma aplicação Spring Boot projetada para acelerar e padronizar a criação de novos projetos e microsserviços. Ele atua como um gerador de projetos (similar ao Spring Initializr), mas focado na padronização interna e na integração com ferramentas de Inteligência Artificial.

## Diferencial: Contexto para Inteligência Artificial

O grande diferencial deste gerador é a injeção automática de regras e boas práticas arquiteturais para assistentes de código IA (como Cursor, GitHub Copilot, etc.). 

Ao gerar um projeto, a aplicação cria automaticamente uma pasta `.context/` contendo 5 arquivos de diretrizes fundamentais:

1. **`01-clean-code.md`**: Garante código legível, pequenas funções e princípios KISS, DRY e YAGNI.
2. **`02-solid.md`**: Força a aplicação estrita dos 5 princípios de design orientado a objetos (SRP, OCP, LSP, ISP, DIP).
3. **`03-java-idioms.md`**: Instruções para gerar código idiomático e moderno (Java Records, Imutabilidade, Effective Java).
4. **`04-arquitetura.md`**: Define o design de microsserviços, Bounded Contexts e princípios do 12-Factor App.
5. **`05-guardrails-seguranca.md`**: Estabelece proteções obrigatórias (OWASP Top 10, sanitização de entrada, gestão de secrets).

Esses arquivos servem de "guardrails" para que qualquer IA operando na base de código do projeto gerado adote os padrões de maturidade exigidos.

## Funcionalidades Principais

- **Formulário Web**: Interface amigável para definição de metadados do projeto (Grupo, Artefato, Nome, Pacote, etc).
- **Catálogo de Dependências**: Seleção visual de dependências separadas por categoria.
- **Empacotamento ZIP Dinâmico**: Geração de estrutura de diretórios, `pom.xml`, `application.yaml` e classes iniciais em memória, devolvendo um `.zip` pronto para uso.

## Como Executar

O projeto utiliza Maven Wrapper e requer o JDK 21+.

```bash
# Compilar e executar
./mvnw spring-boot:run
```

Acesse a interface em `http://localhost:8080`.

## Estrutura do Projeto

- **`controller/PortalController.java`**: Gerencia as requisições web e serve a página HTML.
- **`service/ProjectGeneratorService.java`**: Coração do gerador, responsável por orquestrar a montagem do código-fonte e dos artefatos do novo projeto.
- **`service/CursorRulesProvider.java`**: Carrega os templates da pasta `src/main/resources/templates/context` para inseri-los dinamicamente no momento da geração.
- **`service/ZipArchiveService.java`**: Empacota os mapas de arquivos em um byte array (ZIP) para download.
