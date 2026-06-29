package com.projeto.ia.portal.service;

import com.projeto.ia.portal.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Catálogo de dependências disponíveis para seleção no portal.
 * <p>
 * Mantém uma lista imutável de dependências com suas coordenadas Maven,
 * descrições e categorias para exibição no formulário.
 */
@Component
public class DependencyCatalog {

    private final Map<String, Dependency> catalog;

    public DependencyCatalog() {
        var dependencies = new LinkedHashMap<String, Dependency>();

        // --- Web ---
        dependencies.put("spring-web", new Dependency(
                "spring-web",
                "Spring Web",
                "Construa aplicações web RESTful com Spring MVC e Tomcat embarcado.",
                "org.springframework.boot",
                "spring-boot-starter-web",
                null,
                "Web",
                "🌐"
        ));

        // --- Data ---
        dependencies.put("spring-data-jpa", new Dependency(
                "spring-data-jpa",
                "Spring Data JPA",
                "Persista dados em SQL com Java Persistence API e Spring Data.",
                "org.springframework.boot",
                "spring-boot-starter-data-jpa",
                null,
                "Data",
                "🗄️"
        ));

        dependencies.put("h2-database", new Dependency(
                "h2-database",
                "H2 Database",
                "Banco de dados relacional em memória para desenvolvimento e testes.",
                "com.h2database",
                "h2",
                "runtime",
                "Data",
                "💾"
        ));

        dependencies.put("postgresql", new Dependency(
                "postgresql",
                "PostgreSQL Driver",
                "Driver JDBC para o banco de dados PostgreSQL.",
                "org.postgresql",
                "postgresql",
                "runtime",
                "Data",
                "🐘"
        ));

        dependencies.put("mysql", new Dependency(
                "mysql",
                "MySQL Driver",
                "Driver JDBC para o banco de dados MySQL.",
                "com.mysql",
                "mysql-connector-j",
                "runtime",
                "Data",
                "🐬"
        ));

        // --- Validação ---
        dependencies.put("validation", new Dependency(
                "validation",
                "Validation",
                "Validação de beans com Hibernate Validator e Jakarta Bean Validation.",
                "org.springframework.boot",
                "spring-boot-starter-validation",
                null,
                "Validação",
                "✅"
        ));

        // --- Ferramentas do Desenvolvedor ---
        dependencies.put("lombok", new Dependency(
                "lombok",
                "Lombok",
                "Reduza código boilerplate com anotações para getters, setters e builders.",
                "org.projectlombok",
                "lombok",
                "provided",
                "Ferramentas",
                "🔧"
        ));

        dependencies.put("devtools", new Dependency(
                "devtools",
                "Spring Boot DevTools",
                "Reinicialização automática e configurações de desenvolvimento.",
                "org.springframework.boot",
                "spring-boot-devtools",
                "runtime",
                "Ferramentas",
                "⚡"
        ));

        // --- Observabilidade ---
        dependencies.put("actuator", new Dependency(
                "actuator",
                "Spring Boot Actuator",
                "Endpoints de monitoramento, métricas e health checks para produção.",
                "org.springframework.boot",
                "spring-boot-starter-actuator",
                null,
                "Observabilidade",
                "📊"
        ));

        // --- Segurança ---
        dependencies.put("spring-security", new Dependency(
                "spring-security",
                "Spring Security",
                "Autenticação e autorização altamente customizáveis para aplicações Spring.",
                "org.springframework.boot",
                "spring-boot-starter-security",
                null,
                "Segurança",
                "🔒"
        ));

        this.catalog = Collections.unmodifiableMap(dependencies);
    }

    /**
     * Retorna todas as dependências disponíveis no catálogo.
     * Nunca retorna null — retorna coleção vazia estável se necessário.
     */
    public List<Dependency> findAll() {
        return List.copyOf(catalog.values());
    }

    /**
     * Busca uma dependência pelo seu ID.
     * Retorna Optional para evitar referências nulas.
     */
    public Optional<Dependency> findById(String id) {
        return Optional.ofNullable(catalog.get(id));
    }

    /**
     * Retorna as dependências agrupadas por categoria para exibição no formulário.
     * Nunca retorna null — retorna mapa vazio se não houver dependências.
     */
    public Map<String, List<Dependency>> findGroupedByCategory() {
        var grouped = new LinkedHashMap<String, List<Dependency>>();
        catalog.values().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Dependency::category,
                        LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ))
                .forEach(grouped::put);
        return Collections.unmodifiableMap(grouped);
    }

    /**
     * Resolve uma lista de IDs de dependências para seus respectivos fragmentos XML Maven.
     * IDs desconhecidos são silenciosamente ignorados.
     */
    public List<Dependency> resolveByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .map(catalog::get)
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
