package com.projeto.ia.portal.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Fornecedor do conteúdo do arquivo {@code .cursorrules}.
 * <p>
 * O conteúdo é carregado a partir de um recurso no classpath
 * ({@code templates/cursorrules.txt}) para facilitar manutenção
 * e evitar problemas de escape em código Java.
 * É injetado na raiz de cada projeto gerado.
 */
@Component
public class CursorRulesProvider {

    private final String content;

    /**
     * Carrega o conteúdo do .cursorrules a partir do classpath no momento
     * da inicialização, utilizando try-with-resources para gerenciamento
     * seguro do InputStream.
     */
    public CursorRulesProvider() {
        var resource = new ClassPathResource("templates/cursorrules.txt");
        try (InputStream inputStream = resource.getInputStream()) {
            this.content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Falha ao carregar o template .cursorrules do classpath", ex);
        }
    }

    /**
     * Retorna o conteúdo completo do arquivo .cursorrules
     * conforme especificação da regra de negócio.
     */
    public String getContent() {
        return content;
    }
}
