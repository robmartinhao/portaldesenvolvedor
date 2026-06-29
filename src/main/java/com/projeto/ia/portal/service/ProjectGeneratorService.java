package com.projeto.ia.portal.service;

import com.projeto.ia.portal.model.Dependency;
import com.projeto.ia.portal.model.ProjectRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço principal de geração de projetos Spring Boot.
 * <p>
 * Orquestra a criação de toda a estrutura Maven padrão, gera os arquivos
 * de configuração dinamicamente e injeta o arquivo {@code .cursorrules}
 * na raiz do projeto gerado.
 */
@Service
public class ProjectGeneratorService {

    private final DependencyCatalog dependencyCatalog;
    private final CursorRulesProvider cursorRulesProvider;
    private final ZipArchiveService zipArchiveService;

    public ProjectGeneratorService(DependencyCatalog dependencyCatalog,
            CursorRulesProvider cursorRulesProvider,
            ZipArchiveService zipArchiveService) {
        this.dependencyCatalog = dependencyCatalog;
        this.cursorRulesProvider = cursorRulesProvider;
        this.zipArchiveService = zipArchiveService;
    }

    /**
     * Gera o projeto completo empacotado em um array de bytes ZIP.
     *
     * @param request Metadados do projeto informados pelo usuário.
     * @return Array de bytes contendo o arquivo ZIP do projeto.
     */
    public byte[] generateProject(ProjectRequest request) {
        Map<String, String> files = buildProjectFiles(request);
        return zipArchiveService.createZip(files);
    }

    /**
     * Constrói o mapa completo de arquivos do projeto.
     */
    private Map<String, String> buildProjectFiles(ProjectRequest request) {
        String root = request.artifactId() + "/";
        String mainJavaPath = root + "src/main/java/" + request.packagePath() + "/";
        String mainResourcesPath = root + "src/main/resources/";
        String testJavaPath = root + "src/test/java/" + request.packagePath() + "/";

        var files = new LinkedHashMap<String, String>();

        // Arquivos de contexto para assistentes de IA — Regra de Negócio Crítica
        cursorRulesProvider.getContextFiles().forEach((filename, content) ->
                files.put(root + ".context/" + filename, content)
        );

        // Configuração Maven
        files.put(root + "pom.xml", generatePomXml(request));

        // Maven Wrapper
        files.put(root + ".mvn/wrapper/maven-wrapper.properties", generateMavenWrapperProperties());

        // Git ignore
        files.put(root + ".gitignore", generateGitIgnore());

        // Código-fonte principal
        files.put(mainJavaPath + request.applicationClassName() + ".java",
                generateApplicationClass(request));

        // Recursos
        files.put(mainResourcesPath + "application.yaml",
                generateApplicationYaml(request));

        // Testes
        files.put(testJavaPath + request.applicationClassName() + "Tests.java",
                generateTestClass(request));

        return files;
    }

    /**
     * Gera o pom.xml dinamicamente com base nos metadados e dependências
     * selecionadas.
     */
    private String generatePomXml(ProjectRequest request) {
        List<Dependency> resolved = dependencyCatalog.resolveByIds(request.dependencies());
        String dependenciesXml = resolved.stream()
                .map(Dependency::toMavenXml)
                .collect(Collectors.joining("\n"));

        boolean hasLombok = request.dependencies().contains("lombok");

        var sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        sb.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        sb.append(
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        sb.append("    <modelVersion>4.0.0</modelVersion>\n\n");
        sb.append("    <parent>\n");
        sb.append("        <groupId>org.springframework.boot</groupId>\n");
        sb.append("        <artifactId>spring-boot-starter-parent</artifactId>\n");
        sb.append("        <version>").append(request.springBootVersion()).append("</version>\n");
        sb.append("        <relativePath/>\n");
        sb.append("    </parent>\n\n");
        sb.append("    <groupId>").append(request.groupId()).append("</groupId>\n");
        sb.append("    <artifactId>").append(request.artifactId()).append("</artifactId>\n");
        sb.append("    <version>0.0.1-SNAPSHOT</version>\n");
        sb.append("    <name>").append(request.name()).append("</name>\n");
        sb.append("    <description>").append(request.description()).append("</description>\n\n");
        sb.append("    <properties>\n");
        sb.append("        <java.version>").append(request.javaVersion()).append("</java.version>\n");
        sb.append("    </properties>\n\n");
        sb.append("    <dependencies>\n");

        if (!dependenciesXml.isEmpty()) {
            sb.append(dependenciesXml).append("\n\n");
        }

        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-test</artifactId>\n");
        sb.append("            <scope>test</scope>\n");
        sb.append("        </dependency>\n");
        sb.append("    </dependencies>\n\n");
        sb.append("    <build>\n");
        sb.append("        <plugins>\n");
        sb.append("            <plugin>\n");
        sb.append("                <groupId>org.springframework.boot</groupId>\n");
        sb.append("                <artifactId>spring-boot-maven-plugin</artifactId>\n");

        if (hasLombok) {
            sb.append("                <configuration>\n");
            sb.append("                    <excludes>\n");
            sb.append("                        <exclude>\n");
            sb.append("                            <groupId>org.projectlombok</groupId>\n");
            sb.append("                            <artifactId>lombok</artifactId>\n");
            sb.append("                        </exclude>\n");
            sb.append("                    </excludes>\n");
            sb.append("                </configuration>\n");
        }

        sb.append("            </plugin>\n");
        sb.append("        </plugins>\n");
        sb.append("    </build>\n\n");
        sb.append("</project>\n");

        return sb.toString();
    }

    /**
     * Gera a classe principal Application com @SpringBootApplication.
     */
    private String generateApplicationClass(ProjectRequest request) {
        var sb = new StringBuilder();
        sb.append("package ").append(request.packageName()).append(";\n\n");
        sb.append("import org.springframework.boot.SpringApplication;\n");
        sb.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n");
        sb.append("@SpringBootApplication\n");
        sb.append("public class ").append(request.applicationClassName()).append(" {\n\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        SpringApplication.run(").append(request.applicationClassName()).append(".class, args);\n");
        sb.append("    }\n\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Gera o arquivo application.yaml com configurações básicas.
     */
    private String generateApplicationYaml(ProjectRequest request) {
        var sb = new StringBuilder();
        sb.append("spring:\n");
        sb.append("  application:\n");
        sb.append("    name: ").append(request.artifactId()).append("\n");
        return sb.toString();
    }

    /**
     * Gera a classe de teste com @SpringBootTest.
     */
    private String generateTestClass(ProjectRequest request) {
        var sb = new StringBuilder();
        sb.append("package ").append(request.packageName()).append(";\n\n");
        sb.append("import org.junit.jupiter.api.Test;\n");
        sb.append("import org.springframework.boot.test.context.SpringBootTest;\n\n");
        sb.append("@SpringBootTest\n");
        sb.append("class ").append(request.applicationClassName()).append("Tests {\n\n");
        sb.append("    @Test\n");
        sb.append("    void contextLoads() {\n");
        sb.append("    }\n\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Gera o conteúdo do .gitignore padrão para projetos Maven/Java.
     */
    private String generateGitIgnore() {
        var sb = new StringBuilder();
        sb.append("HELP.md\n");
        sb.append("target/\n");
        sb.append("!.mvn/wrapper/maven-wrapper.jar\n");
        sb.append("!**/src/main/**/target/\n");
        sb.append("!**/src/test/**/target/\n\n");
        sb.append("### STS ###\n");
        sb.append(".apt_generated\n");
        sb.append(".classpath\n");
        sb.append(".factorypath\n");
        sb.append(".project\n");
        sb.append(".settings\n");
        sb.append(".springBeans\n");
        sb.append(".sts4-cache\n\n");
        sb.append("### IntelliJ IDEA ###\n");
        sb.append(".idea\n");
        sb.append("*.iws\n");
        sb.append("*.iml\n");
        sb.append("*.ipr\n\n");
        sb.append("### NetBeans ###\n");
        sb.append("/nbproject/private/\n");
        sb.append("/nbbuild/\n");
        sb.append("/dist/\n");
        sb.append("/nbdist/\n");
        sb.append("/.nb-gradle/\n");
        sb.append("build/\n");
        sb.append("!**/src/main/**/build/\n");
        sb.append("!**/src/test/**/build/\n\n");
        sb.append("### VS Code ###\n");
        sb.append(".vscode/\n\n");
        sb.append("### macOS ###\n");
        sb.append(".DS_Store\n");
        return sb.toString();
    }

    /**
     * Gera o arquivo maven-wrapper.properties para o Maven Wrapper.
     */
    private String generateMavenWrapperProperties() {
        var sb = new StringBuilder();
        sb.append(
                "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip\n");
        sb.append(
                "wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar\n");
        return sb.toString();
    }
}
