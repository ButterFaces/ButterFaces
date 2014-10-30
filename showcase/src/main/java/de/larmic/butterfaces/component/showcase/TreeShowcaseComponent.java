package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.model.tree.DefaultNodeImpl;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

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

    @Override
    public String getXHtml() {
        return null;
    }
}
