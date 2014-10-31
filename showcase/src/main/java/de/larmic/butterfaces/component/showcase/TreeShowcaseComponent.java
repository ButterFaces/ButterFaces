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

    private String glyphicon;
    private String collapsingClass;
    private String expansionClass;

    public Node getTree() {
        final Node secondFirstFirstChild = new DefaultNodeImpl("secondFirstFirstChild");

        final Node secondFirstChild = new DefaultNodeImpl("secondFirstChild");
        secondFirstChild.getSubNodes().add(secondFirstFirstChild);
        final Node secondSecondChild = new DefaultNodeImpl("secondSecondChild");

        final Node firstChild = new DefaultNodeImpl("firstChild");
        final Node secondChild = new DefaultNodeImpl("secondChild");
        secondChild.getSubNodes().add(secondFirstChild);
        secondChild.getSubNodes().add(secondSecondChild);
        final Node thirdChild = new DefaultNodeImpl("thirdChild");

        final Node rootNode = new DefaultNodeImpl("rootNode");
        rootNode.getSubNodes().add(firstChild);
        rootNode.getSubNodes().add(secondChild);
        rootNode.getSubNodes().add(thirdChild);

        return rootNode;
    }

    public List<SelectItem> getGlyphicons() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("bootstrap", "Butterfaces default"));
        items.add(new SelectItem("other-bootstrap", "other Bootstrap example"));
        items.add(new SelectItem("font-awesome", "Font-Awesome example"));

        return items;
    }

    @Override
    public String getXHtml() {
        return null;
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
            case "font-awesome":
                collapsingClass = "fa fa-minus-square-o";
                expansionClass = "fa fa-plus-square-o";
                break;
        }
    }
}
