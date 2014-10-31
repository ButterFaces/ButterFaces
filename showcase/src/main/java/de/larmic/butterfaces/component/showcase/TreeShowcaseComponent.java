package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.model.tree.DefaultNodeImpl;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    public static final String FONT_AWESOME_MARKER = "font-awesome";

    private String glyphicon;
    private String collapsingClass;
    private String expansionClass;

    public Node getTree() {
        final Node secondFirstChild = new DefaultNodeImpl("secondFirstChild");
        secondFirstChild.getSubNodes().add(new DefaultNodeImpl("secondFirstFirstChild"));

        final Node firstChild = new DefaultNodeImpl("firstChild");
        final Node secondChild = new DefaultNodeImpl("secondChild");
        final Node secondThirdChild = new DefaultNodeImpl("secondThirdChild");
        secondThirdChild.getSubNodes().add(new DefaultNodeImpl("thirdFirstChild"));
        secondThirdChild.getSubNodes().add(new DefaultNodeImpl("thirdSecondChild"));
        secondThirdChild.getSubNodes().add(new DefaultNodeImpl("thirdThirdChild"));
        secondChild.getSubNodes().add(secondFirstChild);
        secondChild.getSubNodes().add(new DefaultNodeImpl("secondSecondChild"));
        secondChild.getSubNodes().add(secondThirdChild);
        secondChild.getSubNodes().add(new DefaultNodeImpl("secondFourthChild"));
        secondChild.getSubNodes().add(new DefaultNodeImpl("secondFifthChild"));

        final Node rootNode = new DefaultNodeImpl("rootNode");
        rootNode.getSubNodes().add(firstChild);
        rootNode.getSubNodes().add(secondChild);
        rootNode.getSubNodes().add(new DefaultNodeImpl("thirdChild"));

        return rootNode;
    }

    @Override
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.demo;\n\n");

        sb.append("import de.larmic.butterfaces.model.tree.Node;\n");
        sb.append("import de.larmic.butterfaces.model.tree.DefaultNodeImpl;\n\n");
        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        sb.append("public class MyBean implements Serializable {\n\n");
        sb.append("    public Node getTreeModel() {\n");
        sb.append("        final Node firstChild = new DefaultNodeImpl(\"firstChild\");\n");
        sb.append("        final Node secondChild = new DefaultNodeImpl(\"second\");\n");
        sb.append("        secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))\n");
        sb.append("        ...\n");
        sb.append("        final Node rootNode = new DefaultNodeImpl(\"rootNode\");\n");
        sb.append("        rootNode.getSubNodes().add(firstChild);\n");
        sb.append("        rootNode.getSubNodes().add(secondChild);\n");
        sb.append("        return rootNode;\n");
        sb.append("    }\n\n");
        sb.append("}");
    }

    public List<SelectItem> getGlyphicons() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("bootstrap", "Butterfaces default"));
        items.add(new SelectItem("other-bootstrap", "other Bootstrap example"));
        items.add(new SelectItem(FONT_AWESOME_MARKER, "Font-Awesome example"));

        return items;
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        if (this.getGlyphicon() != null && FONT_AWESOME_MARKER.equals(this.getGlyphicon())) {
            this.addXhtmlStart(sb, "<h:head>\n    <link href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css\"\n          rel=\"stylesheet\">\n</h:head>");
        } else {
            this.addXhtmlStart(sb);
        }

        sb.append("        <b:tree id=\"input\"\n");

        this.appendString("value", "#{myBean.treeModel}", sb);
        this.appendString("collapsingClass", this.getCollapsingClass(), sb);
        this.appendString("expansionClass", this.getExpansionClass(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </b:tree>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                ";
    }

    public String getCollapsingClass() {
        return collapsingClass;
    }

    public String getExpansionClass() {
        return expansionClass;
    }

    public String getGlyphicon() {
        return glyphicon;
    }

    public void setGlyphicon(final String glyphicon) {
        this.glyphicon = glyphicon;

        switch (glyphicon) {
            case "bootstrap":
                collapsingClass = null;
                expansionClass = null;
                break;
            case "other-bootstrap":
                collapsingClass = "glyphicon glyphicon-resize-small";
                expansionClass = "glyphicon glyphicon-resize-full";
                break;
            case FONT_AWESOME_MARKER:
                collapsingClass = "fa fa-minus-square-o";
                expansionClass = "fa fa-plus-square-o";
                break;
        }
    }
}
