package org.butterfaces.component.showcase.tree;

import java.util.Date;
import java.util.UUID;

public class NodeData {

    private final UUID uuid = UUID.randomUUID();

    private final Date createDate = new Date();

    public UUID getUuid() {
        return uuid;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
