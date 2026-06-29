# Arquitetura — Sistemas Distribuídos e Nuvem

Garanta que os novos componentes nasçam prontos para operar em ambientes de nuvem altamente escaláveis. Respeite os padrões arquiteturais consolidados para microsserviços e aplicações cloud-native.

## Microsserviços e Bounded Contexts
- Respeite o isolamento rígido de **contextos delimitados** (Bounded Contexts) conforme definido por Domain-Driven Design (DDD).
- Imponha o padrão **Database-per-Service**: cada serviço deve possuir seu próprio esquema de dados, impedindo qualquer acoplamento direto entre bancos de dados de serviços distintos (NEWMAN, 2021; RICHARDSON, 2018).
- Evite chamadas síncronas em cascata entre serviços — prefira comunicação assíncrona baseada em eventos (coreografia) para reduzir acoplamento temporal.
- Defina contratos claros via APIs (OpenAPI/Swagger) e eventos de domínio para comunicação entre contextos.

## Resiliência e Tolerância a Falhas
- Implemente padrões de tolerância a falhas para comunicação entre serviços:
  * **Circuit Breaker**: Evite cascata de falhas isolando chamadas a serviços instáveis. Utilize bibliotecas como Resilience4j.
  * **Bulkhead**: Isole pools de recursos para que a falha de um componente não comprometa os demais.
  * **Retry com Backoff Exponencial**: Para falhas transitórias, aplique retentativas com intervalos crescentes e jitter aleatório.
  * **Timeout**: Defina timeouts explícitos para toda chamada externa — nunca confie nos defaults do framework.
- Projete para falha: assuma que qualquer dependência externa pode falhar a qualquer momento (NYGARD, 2018).

## The 12-Factor App
Siga rigorosamente a metodologia nativa em nuvem de Wiggins (2012):

1. **Codebase**: Um repositório por aplicação, múltiplos deploys.
2. **Dependencies**: Declare e isole todas as dependências explicitamente (Maven/Gradle).
3. **Config**: Separe estritamente as configurações do ambiente do código-fonte — utilize variáveis de ambiente ou config servers.
4. **Backing Services**: Trate bancos de dados, mensageria e caches como **recursos anexados** substituíveis via configuração.
5. **Build, Release, Run**: Mantenha separação estrita entre as fases de build, release e execução.
6. **Processes**: Garanta que os processos da aplicação sejam totalmente **sem estado** (stateless), viabilizando escalabilidade horizontal imediata.
7. **Port Binding**: Exporte serviços via binding de porta (Spring Boot faz isso nativamente).
8. **Concurrency**: Escale via processos independentes, não threads internas.
9. **Disposability**: Maximize robustez com startup rápido e shutdown gracioso.
10. **Dev/Prod Parity**: Mantenha desenvolvimento, staging e produção o mais similares possível.
11. **Logs**: Trate logs como **fluxos de eventos** — não gerencie arquivos de log na aplicação.
12. **Admin Processes**: Execute tarefas administrativas como processos pontuais (one-off).

## Camadas Arquiteturais
- Mantenha separação clara entre camadas: **Controller** → **Service** → **Repository**.
- Controllers devem ser finos: apenas recebem requisições, delegam ao serviço e retornam respostas.
- Services contêm a lógica de negócio e orquestração.
- Repositories encapsulam acesso a dados — nunca exponha detalhes de persistência para camadas superiores.
- Utilize DTOs para transferência de dados entre camadas e nunca exponha entidades de domínio diretamente em APIs.
