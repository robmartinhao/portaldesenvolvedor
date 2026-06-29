package com.projeto.ia.portal.model;

import java.util.Optional;

/**
 * Representação imutável de uma dependência selecionável no portal.
 * Encapsula as coordenadas Maven e metadados visuais para o formulário.
 *
 * @param id          Identificador único da dependência (ex: "spring-web")
 * @param name        Nome de exibição (ex: "Spring Web")
 * @param description Descrição breve da funcionalidade
 * @param groupId     GroupId Maven
 * @param artifactId  ArtifactId Maven
 * @param scope       Escopo Maven opcional (ex: "runtime", "provided")
 * @param category    Categoria para agrupamento no formulário (ex: "Web", "Data")
 * @param icon        Emoji ou ícone para exibição no front-end
 */
public record Dependency(
        String id,
        String name,
        String description,
        String groupId,
        String artifactId,
        String scope,
        String category,
        String icon
) {

    /**
     * Gera o fragmento XML de dependência Maven correspondente.
     * Utiliza Text Block (Java 21) para template legível.
     */
    public String toMavenXml() {
        var sb = new StringBuilder();
        sb.append("        <dependency>\n");
        sb.append("            <groupId>").append(groupId).append("</groupId>\n");
        sb.append("            <artifactId>").append(artifactId).append("</artifactId>\n");
        if (hasScope()) {
            sb.append("            <scope>").append(scope).append("</scope>\n");
        }
        sb.append("        </dependency>");
        return sb.toString();
    }

    /**
     * Verifica se a dependência possui escopo definido.
     */
    public boolean hasScope() {
        return scope != null && !scope.isBlank();
    }

    /**
     * Retorna o escopo como Optional, evitando retornos nulos.
     */
    public Optional<String> optionalScope() {
        return hasScope() ? Optional.of(scope) : Optional.empty();
    }
}
