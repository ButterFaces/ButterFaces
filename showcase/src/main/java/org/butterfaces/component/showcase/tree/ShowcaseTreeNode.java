package org.butterfaces.component.showcase.tree;

import org.butterfaces.model.tree.DefaultNodeImpl;
import org.butterfaces.model.tree.Node;

public class ShowcaseTreeNode {

    private TreeIconType selectedIconType = TreeIconType.IMAGE;

    private Node rootNode;

    public ShowcaseTreeNode() {
        final Node inbox = createNode("Inbox", "resources/images/arrow-down.png", "glyphicon-download", "43 unread");
        final Node drafts = createNode("Drafts", "resources/images/compose.png", "glyphicon-edit", "5");
        final Node sent = createNode("Sent", "resources/images/arrow-up.png", "glyphicon-send", "529 sent, 1 sending");

        final Node tagged = createNode("Tagged", "resources/images/shop.png", "glyphicon-tag", "9 unread");
        tagged.setCollapsed(true);
        tagged.getSubNodes().add(createNode("Important", "resources/images/shop.png", "glyphicon-tag", null));
        tagged.getSubNodes().add(createNode("Private", "resources/images/shop.png", "glyphicon-tag", null));

        final Node folders = createNode("Folders", "resources/images/folder.png", "glyphicon-folder-open", "27 files");
        folders.setCollapsed(true);
        folders.getSubNodes().add(createNode("Office", "resources/images/folder.png", "glyphicon-folder-open", "13 files"));
        folders.getSubNodes().add(createNode("Building", "resources/images/folder.png", "glyphicon-folder-open", "2 files"));
        folders.getSubNodes().add(createNode("Bills", "resources/images/folder.png", "glyphicon-folder-open", "12 files"));
        final Node trash = createNode("Trash", "resources/images/recycle.png", "glyphicon-trash", "7293 kB");

        final Node mail = createNode("Mail", "resources/images/mail.png", "glyphicon-envelope", "43 unread");
        mail.getSubNodes().add(inbox);
        mail.getSubNodes().add(drafts);
        mail.getSubNodes().add(sent);
        mail.getSubNodes().add(tagged);
        mail.getSubNodes().add(folders);
        mail.getSubNodes().add(trash);

        rootNode = createNode("rootNode", "resources/images/folder.png", "glyphicon-folder-open", "Project X");
        rootNode.getSubNodes().add(mail);
        rootNode.getSubNodes().add(createNode("Special Sign \"\'", "resources/images/folder.png", "glyphicon-folder-open", "Special Sign \"'"));
        rootNode.getSubNodes().add(createNode("Title </script>", "resources/images/folder.png", "glyphicon-folder-open", "Script end tag"));
    }

    private DefaultNodeImpl createNode(final String title, final String icon, final String glyphicon, final String description) {
        return new DefaultNodeImpl(title) {
            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String getImageIcon() {
                return selectedIconType == TreeIconType.IMAGE ? icon : null;
            }

            @Override
            public String getGlyphiconIcon() {
                return selectedIconType == TreeIconType.GLYPHICON ? "glyphicon " + glyphicon : null;
            }
        };
    }

    public Node getTree() {
        return rootNode;
    }

    public TreeIconType getSelectedIconType() {
        return selectedIconType;
    }

    public void setSelectedIconType(TreeIconType selectedIconType) {
        this.selectedIconType = selectedIconType;
    }

}
