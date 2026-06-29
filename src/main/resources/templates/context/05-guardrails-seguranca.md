# Guardrails e Segurança — Proteção e Conformidade

Todo código gerado deve incorporar práticas de segurança desde o design (Security by Design). Siga as diretrizes do OWASP e as boas práticas de segurança para aplicações Java/Spring Boot.

## Validação de Entrada
- Valide **toda entrada** recebida de fontes externas (requisições HTTP, mensagens de fila, arquivos importados) antes de processá-la.
- Utilize **Bean Validation** (Jakarta Validation / Hibernate Validator) com anotações como `@NotNull`, `@NotBlank`, `@Size`, `@Pattern`, `@Valid` em DTOs de entrada.
- Aplique validação em múltiplas camadas: no controller (formato) e no service (regras de negócio).
- Nunca confie em validação apenas do lado do cliente — o servidor é a última linha de defesa.
- Sanitize dados de entrada antes de persistir ou exibir para prevenir ataques de injeção.

## Proteção Contra Injeção
- **SQL Injection**: Nunca concatene strings para construir queries SQL. Utilize JPA/Hibernate com parâmetros nomeados (`:param`) ou `PreparedStatement`.
- **XSS (Cross-Site Scripting)**: Escape toda saída HTML renderizada pelo servidor. Em APIs REST, utilize Content-Type adequado (`application/json`) e evite renderização de HTML não sanitizado.
- **LDAP Injection**: Sanitize entradas antes de incorporá-las em filtros LDAP.
- **Command Injection**: Nunca passe entrada do usuário diretamente para `Runtime.exec()` ou `ProcessBuilder` sem sanitização estrita.
- **Log Injection**: Sanitize dados antes de incluí-los em mensagens de log para evitar forjamento de entradas de log (CRLF injection).

## Gestão de Segredos e Credenciais
- **Nunca** armazene senhas, tokens, chaves de API ou certificados diretamente no código-fonte ou em arquivos de configuração versionados.
- Utilize mecanismos externos para gestão de segredos: variáveis de ambiente, Spring Cloud Config Server, HashiCorp Vault ou AWS Secrets Manager.
- Em arquivos `application.yaml`, utilize placeholders (`${ENV_VAR}`) ao invés de valores literais para configurações sensíveis.
- Adicione padrões de arquivos sensíveis ao `.gitignore` (ex: `*.pem`, `*.key`, `application-local.yaml`).

## Auditoria de Dependências
- Mantenha dependências atualizadas e auditadas contra vulnerabilidades conhecidas (CVEs).
- Integre ferramentas de análise de segurança no pipeline de CI/CD: OWASP Dependency-Check, Snyk ou Trivy.
- Evite dependências com vulnerabilidades críticas conhecidas — substitua por alternativas seguras quando possível.
- Fixe versões de dependências explicitamente (evite ranges dinâmicos) para reprodutibilidade e segurança.

## OWASP Top 10 — Consciência Obrigatória
Tenha sempre em mente as 10 principais vulnerabilidades web (OWASP, 2021):
1. **Broken Access Control**: Implemente autorização em todas as camadas — não confie apenas na autenticação.
2. **Cryptographic Failures**: Use algoritmos modernos (AES-256, bcrypt/scrypt para senhas). Nunca implemente criptografia própria.
3. **Injection**: Já coberto acima — parametrize todas as queries.
4. **Insecure Design**: Modele ameaças durante o design, não como remediação tardia.
5. **Security Misconfiguration**: Desabilite endpoints de debug em produção. Configure CORS restritivamente.
6. **Vulnerable Components**: Já coberto na auditoria de dependências.
7. **Authentication Failures**: Implemente rate limiting, bloqueio de conta e MFA quando aplicável.
8. **Data Integrity Failures**: Valide integridade de dados desserializados. Evite desserialização de objetos não confiáveis.
9. **Logging & Monitoring Failures**: Registre eventos de segurança (login, falhas de autenticação, acesso negado) com contexto suficiente.
10. **SSRF (Server-Side Request Forgery)**: Valide e restrinja URLs de destino em chamadas HTTP feitas pelo servidor.

## Logging Seguro
- **Nunca** registre dados sensíveis em logs: senhas, tokens, números de cartão, CPF, dados pessoais (LGPD/GDPR).
- Utilize mascaramento de dados para campos sensíveis quando o log for necessário para debug (ex: `***1234` para cartão).
- Estruture logs em formato JSON para facilitar indexação e busca em ferramentas de observabilidade (ELK, Splunk, Datadog).
- Inclua correlation IDs (trace ID) em todas as mensagens de log para rastreabilidade entre serviços distribuídos.

## Headers de Segurança HTTP
- Configure headers de segurança em respostas HTTP via Spring Security ou filtros:
  * `Content-Security-Policy`: Restrinja fontes de conteúdo permitidas.
  * `X-Content-Type-Options: nosniff`: Previna MIME type sniffing.
  * `X-Frame-Options: DENY`: Previna clickjacking.
  * `Strict-Transport-Security`: Force HTTPS.
  * `X-XSS-Protection`: Ative proteção contra XSS do navegador.
