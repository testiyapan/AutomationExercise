package gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckBoxTreeNode extends DefaultMutableTreeNode {
    private boolean selected;

    public CheckBoxTreeNode(Object userObject) {
        super(userObject);
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        // Alt düğümleri de etkiler
        if (children != null) {
            for (Object obj : children) {
                ((CheckBoxTreeNode) obj).setSelected(selected);
            }
        }
    }
}
