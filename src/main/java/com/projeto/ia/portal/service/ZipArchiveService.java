package com.projeto.ia.portal.service;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Serviço utilitário para empacotamento de arquivos em formato ZIP.
 * <p>
 * Toda a operação ocorre em memória (sem I/O em disco) utilizando
 * {@link ByteArrayOutputStream} e {@link ZipOutputStream} com
 * try-with-resources para gerenciamento seguro de recursos.
 */
@Component
public class ZipArchiveService {

    /**
     * Empacota um mapa de arquivos em um array de bytes ZIP.
     *
     * @param files Mapa onde a chave é o caminho relativo do arquivo dentro do ZIP
     *              e o valor é o conteúdo textual do arquivo.
     * @return Array de bytes contendo o arquivo ZIP completo.
     * @throws ZipCreationException se ocorrer erro durante a criação do ZIP.
     */
    public byte[] createZip(Map<String, String> files) {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8)) {

            for (var entry : files.entrySet()) {
                addEntry(zipOutputStream, entry.getKey(), entry.getValue());
            }

            zipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException ex) {
            throw new ZipCreationException("Falha ao gerar o arquivo ZIP do projeto", ex);
        }
    }

    /**
     * Adiciona uma entrada individual ao ZipOutputStream.
     */
    private void addEntry(ZipOutputStream zos, String path, String content) throws IOException {
        var entry = new ZipEntry(path);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /**
     * Exceção específica para falhas na criação de arquivos ZIP.
     * Encapsula a IOException original mantendo a rastreabilidade.
     */
    public static class ZipCreationException extends RuntimeException {
        public ZipCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
