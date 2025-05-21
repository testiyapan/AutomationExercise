package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestNGXmlGenerator {

    public static void generateXml(List<String> fullyQualifiedClassNames, List<String> browsers, int threadCount) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<suite name=\"DynamicSuite\" parallel=\"tests\" thread-count=\"").append(threadCount).append("\">\n");

        for (String browser : browsers) {
            xml.append("  <test name=\"Tests on ").append(browser).append("\">\n");
            xml.append("    <parameter name=\"browser\" value=\"").append(browser).append("\"/>\n");
            xml.append("    <classes>\n");

            for (String fqcn : fullyQualifiedClassNames) {
                String cleanedClassName = fqcn.replaceFirst("^test[.]", "");
                xml.append("      <class name=\"").append(cleanedClassName).append("\"/>\n");
            }

            xml.append("    </classes>\n");
            xml.append("  </test>\n");
        }

        xml.append("</suite>");

        try {
            Path directory = Path.of(PathsUtil.getTestNgXmlDirectory());
            Files.createDirectories(directory);
            Path filePath = Path.of(PathsUtil.getGeneratedTestNgXmlPath());

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(xml.toString());
                System.out.println("testng.xml generated at: " + filePath);
            }

        } catch (IOException e) {
            System.err.println("Failed to generate testng.xml: " + e.getMessage());
        }
    }
}
