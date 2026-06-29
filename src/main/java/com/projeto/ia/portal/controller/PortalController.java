package com.projeto.ia.portal.controller;

import com.projeto.ia.portal.model.ProjectRequest;
import com.projeto.ia.portal.service.DependencyCatalog;
import com.projeto.ia.portal.service.ProjectGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller principal do Portal do Desenvolvedor.
 * <p>
 * Gerencia a renderização do formulário e o fluxo de geração de projetos,
 * retornando o arquivo ZIP para download automático.
 */
@Controller
public class PortalController {

    private final ProjectGeneratorService projectGeneratorService;
    private final DependencyCatalog dependencyCatalog;

    public PortalController(ProjectGeneratorService projectGeneratorService,
                            DependencyCatalog dependencyCatalog) {
        this.projectGeneratorService = projectGeneratorService;
        this.dependencyCatalog = dependencyCatalog;
    }

    /**
     * Renderiza o formulário principal do portal.
     * Popula o modelo com as dependências disponíveis e valores default.
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("groupedDependencies", dependencyCatalog.findGroupedByCategory());
        model.addAttribute("allDependencies", dependencyCatalog.findAll());
        model.addAttribute("defaultGroupId", "com.example");
        model.addAttribute("defaultArtifactId", "demo");
        model.addAttribute("defaultName", "demo");
        model.addAttribute("defaultDescription", "Projeto gerado pelo Portal do Desenvolvedor");
        model.addAttribute("defaultPackageName", "com.example.demo");
        model.addAttribute("javaVersion", ProjectRequest.DEFAULT_JAVA_VERSION);
        model.addAttribute("springBootVersion", ProjectRequest.DEFAULT_SPRING_BOOT_VERSION);
        return "index";
    }

    /**
     * Recebe os dados do formulário, gera o projeto e retorna o ZIP para download.
     * O Content-Disposition força o download automático pelo navegador.
     */
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProject(
            @RequestParam String groupId,
            @RequestParam String artifactId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String packageName,
            @RequestParam(required = false) List<String> dependencies
    ) {
        var request = new ProjectRequest(
                groupId,
                artifactId,
                name,
                description,
                packageName,
                ProjectRequest.DEFAULT_JAVA_VERSION,
                ProjectRequest.DEFAULT_SPRING_BOOT_VERSION,
                dependencies
        );

        byte[] zipBytes = projectGeneratorService.generateProject(request);
        String filename = request.artifactId() + ".zip";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipBytes.length)
                .body(zipBytes);
    }
}
