/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.example;

import java.util.ArrayList;
import java.util.List;

import de.larmic.butterfaces.util.StringUtils;

/**
 * @author Lars Michaelis
 */
public class EnumCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();
    private final List<String> imports = new ArrayList<>();
    private final List<String> interfaces = new ArrayList<>();

    private final String subPackage;
    private final String className;
    private final String classAnnotation;

    public EnumCodeExample(final String tabName,
                           final String tabId,
                           final String subPackage,
                           final String className,
                           final String classAnnotation) {
        super(tabName, tabId);
        this.subPackage = subPackage;
        this.className = className;
        this.classAnnotation = classAnnotation;
    }

    @Override
    public String getPrettyPrintLang() {
        return "lang-java";
    }

    public boolean addImport(final String anImport) {
        return imports.add(anImport);
    }

    public boolean addInterfaces(String anInterface) {
        return interfaces.add(" " + anInterface);
    }

    public StringBuilder appendInnerContent(final String content) {
        return this.appendInnerContent(content, true);
    }

    public StringBuilder appendInnerContent(final String content, final boolean lineBreak) {
        if (lineBreak) {
            return innerContent.append(content).append("\n");
        } else {
            return innerContent.append(content);
        }
    }

    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        this.buildPackage(stringBuilder);
        this.buildImports(stringBuilder);
        this.buildClassAnnotations(stringBuilder);
        this.buildClassDefinition(stringBuilder);

        stringBuilder.append(innerContent.toString());

        stringBuilder.append("\n}");

        return stringBuilder.toString();
    }

    private void buildClassDefinition(StringBuilder stringBuilder) {
        stringBuilder.append("public enum ");
        stringBuilder.append(className);
        if (!interfaces.isEmpty()) {
            stringBuilder.append(" implements");
        }

        stringBuilder.append(StringUtils.joinWithCommaSeparator(interfaces));

        stringBuilder.append(" {\n\n");
    }

    private void buildClassAnnotations(StringBuilder stringBuilder) {
        if (classAnnotation != null) {
            stringBuilder.append(classAnnotation + "\n");
        }
    }

    private void buildImports(StringBuilder stringBuilder) {
        for (String anImport : imports) {
            if (StringUtils.isEmpty(anImport)) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append("import ");
                stringBuilder.append(anImport);
                stringBuilder.append(";\n");
            }
        }

        // build empty line after imports
        if (!imports.isEmpty()) {
            stringBuilder.append("\n");
        }
    }

    private void buildPackage(StringBuilder stringBuilder) {
        stringBuilder.append("package de.larmic.");
        stringBuilder.append(subPackage);
        stringBuilder.append(";\n\n");
    }
}
