package gui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CheckBoxTreeRenderer extends JPanel implements TreeCellRenderer {
    private JCheckBox checkBox;
    private JLabel label;

    public CheckBoxTreeRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkBox = new JCheckBox();
        label = new JLabel();
        add(checkBox);
        add(label);
        setOpaque(false);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof CheckBoxTreeNode) {
            CheckBoxTreeNode node = (CheckBoxTreeNode) value;
            checkBox.setSelected(node.isSelected());
            label.setText(node.getUserObject().toString());
        }
        return this;
    }
}
