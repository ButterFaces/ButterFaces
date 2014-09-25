package de.larmic.butterfaces.component.showcase;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@SessionScoped
public class TableShowcaseComponent extends AbstractInputShowcaseComponent implements Serializable {

    public List<StringContainer> getStringRows() {
        final ArrayList<StringContainer> strings = new ArrayList<>();

        strings.add(new StringContainer("r1c1", "r1c2"));
        strings.add(new StringContainer("r2c1", "r2c2"));
        strings.add(new StringContainer("r3c1", "r3c2"));
        strings.add(new StringContainer("r4c1", "r4c2"));
        strings.add(new StringContainer("r5c1", "r5c2"));
        strings.add(new StringContainer("r6c1", "r6c2"));
        strings.add(new StringContainer("r7c1", "r7c2"));

        return strings;
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        return null;
    }

    @Override
    public String getXHtml() {
        return null;
    }

    public class StringContainer {
        private final String a;
        private final String b;

        public StringContainer(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }
    }
}
