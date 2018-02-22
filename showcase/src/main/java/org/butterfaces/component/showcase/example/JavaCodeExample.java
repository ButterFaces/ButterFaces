/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.example;

import org.butterfaces.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lars Michaelis
 */
public class JavaCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();
    private final List<String> imports = new ArrayList<>();
    private final List<String> interfaces = new ArrayList<>();

    private final String subPackage;
    private final String className;
    private final boolean cdiBean;
    private final String classAnnotation;

    public JavaCodeExample(final String subPackage,
                           final String className,
                           final boolean cdiBean) {
        this("java", "java", subPackage, className, cdiBean);
    }

    public JavaCodeExample(final String tabName,
                           final String tabId,
                           final String subPackage,
                           final String className,
                           final boolean cdiBean) {
        this(tabName, tabId, subPackage, className, cdiBean, null);
    }

    public JavaCodeExample(final String tabName,
                           final String tabId,
                           final String subPackage,
                           final String className,
                           final boolean cdiBean,
                           final String classAnnotation) {
        super(tabName, tabId);
        this.subPackage = subPackage;
        this.className = className;
        this.cdiBean = cdiBean;
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
        return interfaces.add(anInterface);
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
        stringBuilder.append("public class ");
        stringBuilder.append(className);
        if (!interfaces.isEmpty() || cdiBean) {
            stringBuilder.append(" implements ");
        }
        if (cdiBean) {
            stringBuilder.append("Serializable");
        }
        if (cdiBean && !interfaces.isEmpty()) {
            stringBuilder.append(", ");
        }

        stringBuilder.append(StringUtils.joinWithCommaSeparator(interfaces));

        stringBuilder.append(" {\n\n");
    }

    private void buildClassAnnotations(StringBuilder stringBuilder) {
        if (cdiBean) {
            stringBuilder.append("@ViewScoped\n@Named\n");
        }
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
        stringBuilder.append("package org.butterfaces.");
        stringBuilder.append(subPackage);
        stringBuilder.append(";\n\n");
    }
}
