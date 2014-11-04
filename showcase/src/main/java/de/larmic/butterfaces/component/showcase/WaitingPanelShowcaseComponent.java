package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class WaitingPanelShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    public void doWaitingClick() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getXHtml() {
        return null;
    }

}
