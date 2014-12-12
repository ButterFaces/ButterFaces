package de.larmic.butterfaces.component.showcase.example;

import de.larmic.butterfaces.component.partrenderer.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by larmic on 12.12.14.
 */
public class JavaCodeExample extends AbstractCodeExample {

    private final StringBuilder innerContent = new StringBuilder();
    private final List<String> imports = new ArrayList<>();
    private final List<String> interfaces = new ArrayList<>();

    private final String subPackage;
    private final String className;
    private final boolean cdiBean;

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
        super(tabName, tabId);
        this.subPackage = subPackage;
        this.className = className;
        this.cdiBean = cdiBean;
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
        return innerContent.append(content).append("\n");
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
            stringBuilder.append(" implements");
        }
        if (cdiBean) {
            stringBuilder.append(" Serializable");
        }
        if (cdiBean && !interfaces.isEmpty()) {
            stringBuilder.append(",");
        }

        final Iterator<String> iterator = interfaces.iterator();

        while (iterator.hasNext()) {
            final String anInterface = iterator.next();

            stringBuilder.append(anInterface);
            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(" {\n\n");
    }

    private void buildClassAnnotations(StringBuilder stringBuilder) {
        if (cdiBean) {
            stringBuilder.append("@ViewScoped\n@Named\n");
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
