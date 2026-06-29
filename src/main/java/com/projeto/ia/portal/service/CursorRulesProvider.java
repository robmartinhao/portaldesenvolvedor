package com.projeto.ia.portal.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fornecedor dos arquivos de contexto para assistentes de IA.
 * <p>
 * Carrega todos os arquivos {@code .md} da pasta {@code templates/context/}
 * no classpath e os disponibiliza como um mapa ordenado (nome → conteúdo).
 * Cada arquivo é injetado na pasta {@code .context/} na raiz de cada
 * projeto gerado.
 */
@Component
public class CursorRulesProvider {

    private static final String CONTEXT_PATTERN = "classpath:templates/context/*.md";

    private final Map<String, String> contextFiles;

    /**
     * Carrega todos os arquivos de contexto da pasta {@code templates/context/}
     * no momento da inicialização, mantendo a ordem alfabética dos nomes.
     */
    public CursorRulesProvider() {
        this.contextFiles = loadContextFiles();
    }

    /**
     * Retorna um mapa imutável com os arquivos de contexto.
     * A chave é o nome do arquivo (ex: {@code 01-clean-code.md})
     * e o valor é o conteúdo completo do arquivo.
     */
    public Map<String, String> getContextFiles() {
        return contextFiles;
    }

    private Map<String, String> loadContextFiles() {
        var resolver = new PathMatchingResourcePatternResolver();
        var result = new LinkedHashMap<String, String>();

        try {
            var resources = resolver.getResources(CONTEXT_PATTERN);

            for (var resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) {
                    continue;
                }

                try (InputStream inputStream = resource.getInputStream()) {
                    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    result.put(filename, content);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Falha ao carregar os arquivos de contexto do classpath", ex);
        }

        if (result.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum arquivo de contexto encontrado em templates/context/");
        }

        return Map.copyOf(result);
    }
}
