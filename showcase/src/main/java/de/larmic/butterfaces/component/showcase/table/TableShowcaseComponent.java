package de.larmic.butterfaces.component.showcase.table;

import de.larmic.butterfaces.component.showcase.AbstractShowcaseComponent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class TableShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private final StringPairList<StringPair> stringPairs = new StringPairList();

    public List<StringPair> getStringRows() {
        if (stringPairs.isEmpty()) {
            stringPairs.add(new StringPair("r1c1", "r1c2"));
            stringPairs.add(new StringPair("r2c1", "r2c2"));
            stringPairs.add(new StringPair("r3c1", "r3c2"));
            stringPairs.add(new StringPair("r4c1", "r4c2"));
            stringPairs.add(new StringPair("r5c1", "r5c2"));
            stringPairs.add(new StringPair("r6c1", "r6c2"));
            stringPairs.add(new StringPair("r7c1", "r7c2"));
        }
        return stringPairs;
    }

    public StringPair getSelectedValue() {
        return stringPairs.getSelectedValue();
    }

    @Override
    public String getXHtml() {
        return null;
    }
}
