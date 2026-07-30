package github.axolotl.backend.service;

import com.alibaba.fastjson2.JSONObject;
import dczx.axolotl.util.file.FilesUtil;
import github.axolotl.ai.content.Content;
import github.axolotl.ai.content.SystemContent;
import github.axolotl.backend.entity.template.PromptTemplate;
import github.axolotl.backend.entity.template.PromptTemplateConfigEntries;
import github.axolotl.backend.entity.template.PromptTemplateConfigEntry;
import github.axolotl.backend.entity.template.PromptTemplateType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class PromptTemplateService {
        @Value("${mana-neutron.config.prompt-template-config-path}")
        private String templateConfigPath;
        @Value("${mana-neutron.config.default-prompt-path}")
        private String defaultPromptPath;
        private List<PromptTemplate> templates;

        @PostConstruct
        public void init() throws IOException {
                FilesUtil.keepFileExists(templateConfigPath);
                String text = Files.readString(Path.of(templateConfigPath));
                PromptTemplateConfigEntries entries =
                        JSONObject.parseObject(text, PromptTemplateConfigEntries.class);
                if (entries == null || entries.getEntries().isEmpty()) {
                        PromptTemplateConfigEntry entry = new PromptTemplateConfigEntry(1, PromptTemplateType.INIT, defaultPromptPath);
                        entries = new PromptTemplateConfigEntries(List.of(entry));
                        FilesUtil.keepFileExists(defaultPromptPath);
                        Files.writeString(Path.of(defaultPromptPath), "You are a helpful assistant.");
                        savePromptTemplateEntries(entries);
                }
                if (entries != null && entries.isValid()) {
                        templates = entries.getEntries().stream()
                                .sorted(Comparator.comparingInt(
                                        PromptTemplateConfigEntry::getOrder))
                                .map(entry -> {
                                        String templateContent = null;
                                        try {
                                                templateContent = Files.readString(Path.of(entry.getFilePath()));
                                        } catch (IOException e) {
                                                throw new RuntimeException(e);
                                        }
                                        return new PromptTemplate(entry.getOrder(), entry.getType(), templateContent);

                                }).toList();
                } else {
                        throw new IOException("Prompt template config entries is invalid");
                }
        }

        public List<PromptTemplate> getTemplatesByType(PromptTemplateType type) {
                return templates.stream().filter(template -> template.getType().equals(type)).toList();
        }

        public List<Content> loadPromptTemplateToContentByType(PromptTemplateType type) {
                ArrayList<Content> result = new ArrayList<>();
                switch (type) {
                        case INIT -> {
                                getTemplatesByType(PromptTemplateType.INIT)
                                        .forEach(template -> result.add(new SystemContent(template.putVariables(
                                                Map.of(
                                                        "update_plan", "update_plan"
                                                )
                                        ))));
                        }
                        case null, default -> {

                        }
                }
                return result;
        }

        public void savePromptTemplateEntries(PromptTemplateConfigEntries entries) throws IOException {
                String jsonString = JSONObject.toJSONString(entries);
                Files.writeString(Path.of(templateConfigPath), jsonString);
        }
}
