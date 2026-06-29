package com.projeto.ia.portal.model;

import java.util.List;

/**
 * Representação imutável dos metadados do formulário de geração de projeto.
 * Utiliza Java Record (JEP 395) para garantir imutabilidade e thread-safety.
 *
 * @param groupId         Identificador do grupo Maven (ex: com.example)
 * @param artifactId      Identificador do artefato Maven (ex: meu-servico)
 * @param name            Nome descritivo do projeto
 * @param description     Descrição do projeto
 * @param packageName     Nome do pacote Java raiz
 * @param javaVersion     Versão do Java (fixo em 21)
 * @param springBootVersion Versão do Spring Boot (fixo em 3.5.16)
 * @param dependencies    Lista de IDs das dependências selecionadas
 */
public record ProjectRequest(
        String groupId,
        String artifactId,
        String name,
        String description,
        String packageName,
        String javaVersion,
        String springBootVersion,
        List<String> dependencies
) {

    /** Versão padrão do Java utilizada nos projetos gerados. */
    public static final String DEFAULT_JAVA_VERSION = "21";

    /** Versão padrão do Spring Boot utilizada nos projetos gerados. */
    public static final String DEFAULT_SPRING_BOOT_VERSION = "3.5.16";

    /**
     * Construtor compacto que aplica valores padrão para campos nulos.
     */
    public ProjectRequest {
        groupId = defaultIfBlank(groupId, "com.example");
        artifactId = defaultIfBlank(artifactId, "demo");
        name = defaultIfBlank(name, artifactId);
        description = defaultIfBlank(description, "Projeto gerado pelo Portal do Desenvolvedor");
        packageName = defaultIfBlank(packageName, groupId + "." + artifactId.replace("-", ""));
        javaVersion = DEFAULT_JAVA_VERSION;
        springBootVersion = DEFAULT_SPRING_BOOT_VERSION;
        dependencies = dependencies != null ? List.copyOf(dependencies) : List.of();
    }

    /**
     * Converte o packageName em um caminho de diretório (ex: com.example.demo → com/example/demo).
     */
    public String packagePath() {
        return packageName.replace('.', '/');
    }

    /**
     * Extrai o nome simples da classe Application a partir do nome do projeto.
     */
    public String applicationClassName() {
        String normalized = artifactId.replace("-", " ").replace("_", " ");
        StringBuilder className = new StringBuilder();
        for (String word : normalized.split("\\s+")) {
            if (!word.isEmpty()) {
                className.append(Character.toUpperCase(word.charAt(0)))
                          .append(word.substring(1).toLowerCase());
            }
        }
        className.append("Application");
        return className.toString();
    }

    private static String defaultIfBlank(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value.strip();
    }
}
