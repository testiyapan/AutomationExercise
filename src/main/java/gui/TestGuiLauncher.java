package gui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class TestGuiLauncher {
    private JFrame frame;
    private JTree testTree;
    private JPanel browserPanel;
    private JComboBox<Integer> threadCountBox;
    private JCheckBox chromeCheck, firefoxCheck, edgeCheck;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestGuiLauncher().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("TestNG Dynamic Test Runner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        CheckBoxTreeNode root = new CheckBoxTreeNode("test");
        populateTestTree(new File(utils.PathsUtil.getTestSourceDir()), root);

        testTree = new JTree(root);
        testTree.setCellRenderer(new CheckBoxTreeRenderer());
        testTree.setRootVisible(false);
        testTree.setShowsRootHandles(true);
        testTree.setToggleClickCount(0);
        testTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                TreePath path = testTree.getPathForLocation(e.getX(), e.getY());
                if (path == null) return;
                CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
                boolean newState = !node.isSelected();
                node.setSelected(newState);
                ((DefaultTreeModel) testTree.getModel()).nodeStructureChanged(node);
            }
        });

        JScrollPane treeScrollPane = new JScrollPane(testTree);
        treeScrollPane.setPreferredSize(new Dimension(400, 600));
        frame.add(treeScrollPane, BorderLayout.WEST);

        // Sağ panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        browserPanel = new JPanel();
        browserPanel.setLayout(new BoxLayout(browserPanel, BoxLayout.Y_AXIS));
        browserPanel.setBorder(BorderFactory.createTitledBorder("Browsers"));
        chromeCheck = new JCheckBox("Chrome");
        firefoxCheck = new JCheckBox("Firefox");
        edgeCheck = new JCheckBox("Edge");
        browserPanel.add(chromeCheck);
        browserPanel.add(firefoxCheck);
        browserPanel.add(edgeCheck);
        rightPanel.add(browserPanel);

        JPanel threadPanel = new JPanel();
        threadPanel.setBorder(BorderFactory.createTitledBorder("Parallel Threads"));
        threadPanel.add(new JLabel("Thread count:"));
        Integer[] options = {1, 2, 3, 4, 5};
        threadCountBox = new JComboBox<>(options);
        threadPanel.add(threadCountBox);
        rightPanel.add(threadPanel);

        JButton runButton = new JButton("Run Selected Tests");
        runButton.addActionListener(e -> onRunButtonClick());
        rightPanel.add(runButton);

        frame.add(rightPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void populateTestTree(File directory, CheckBoxTreeNode parent) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory() && !file.getName().equalsIgnoreCase("base")) {
                CheckBoxTreeNode dirNode = new CheckBoxTreeNode(file.getName());
                parent.add(dirNode);
                populateTestTree(file, dirNode);
            } else if (file.isFile() && file.getName().endsWith("Test.java") && !file.getName().equalsIgnoreCase("BaseTest.java")) {
                parent.add(new CheckBoxTreeNode(file.getName()));
            }
        }
    }

    private void onRunButtonClick() {
        CheckBoxTreeNode root = (CheckBoxTreeNode) testTree.getModel().getRoot();
        java.util.List<String> selectedClasses = new java.util.ArrayList<>();
        collectSelectedClasses(root, "", selectedClasses);

        if (selectedClasses.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Lütfen en az bir test sınıfı seçin.");
            return;
        }

        java.util.List<String> selectedBrowsers = new java.util.ArrayList<>();
        if (chromeCheck.isSelected()) selectedBrowsers.add("chrome");
        if (firefoxCheck.isSelected()) selectedBrowsers.add("firefox");
        if (edgeCheck.isSelected()) selectedBrowsers.add("edge");

        if (selectedBrowsers.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Lütfen en az bir browser seçin.");
            return;
        }

        int threadCount = (int) threadCountBox.getSelectedItem();

        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        System.setProperty("allure.results.directory", utils.PathsUtil.getAllureResultsPath(timestamp));

        utils.TestNGXmlGenerator.generateXml(selectedClasses, selectedBrowsers, threadCount);

        utils.TestNGExecutor.runSuite(utils.PathsUtil.getGeneratedTestNgXmlPath());

        frame.dispose();
    }

    private void collectSelectedClasses(CheckBoxTreeNode node, String path, java.util.List<String> classList) {
        String nodeName = node.getUserObject().toString();
        String currentPath = path.isEmpty() ? nodeName : path + "." + nodeName;

        if (node.getChildCount() == 0 && node.isSelected() && nodeName.endsWith("Test.java")) {
            String className = nodeName.replace(".java", "");
            classList.add(currentPath.replace("." + nodeName, "") + "." + className);
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                collectSelectedClasses((CheckBoxTreeNode) node.getChildAt(i), currentPath, classList);
            }
        }
    }


}
