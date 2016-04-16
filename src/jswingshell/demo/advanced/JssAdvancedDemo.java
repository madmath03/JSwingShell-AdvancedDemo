package jswingshell.demo.advanced;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Arrays;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import jswingshell.IJssController;
import jswingshell.action.AbstractJssAction;
import jswingshell.action.IJssAction;
import jswingshell.demo.advanced.action.ClearAction;
import jswingshell.demo.advanced.action.EchoAction;
import jswingshell.demo.advanced.action.ExitAction;
import jswingshell.demo.advanced.action.HelpAction;
import jswingshell.demo.advanced.action.LoadCommandFile;
import jswingshell.demo.advanced.action.SleepAction;
import jswingshell.demo.advanced.action.TimeAction;
import jswingshell.demo.advanced.action.WaitAction;
import jswingshell.gui.JssTextAreaController;

/**
 *
 * @author brunot
 */
public abstract class JssAdvancedDemo {

    /**
     * A simple JFrame.
     */
    static class SimpleShellFrame extends JFrame {

        private static int globalCount = 0;

        private final int number;

        private final IJssController controller;

        private javax.swing.JScrollPane jShellScrollPane;

        public SimpleShellFrame(IJssController controller) throws HeadlessException {
            super();
            number = globalCount++;
            this.controller = controller;
            initComponents();
        }

        public SimpleShellFrame(IJssController controller, String title) throws HeadlessException {
            super(title);
            number = globalCount++;
            this.controller = controller;
            initComponents();
        }

        public IJssController getController() {
            return controller;
        }

        private void initComponents() {

            jShellScrollPane = new javax.swing.JScrollPane();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setPreferredSize(new java.awt.Dimension(320, 240));

            if (controller != null && controller.getView() instanceof java.awt.Component) {
                jShellScrollPane.setViewportView((java.awt.Component) controller.getView());
            }

            getContentPane().add(jShellScrollPane, java.awt.BorderLayout.CENTER);
            jShellScrollPane.getAccessibleContext().setAccessibleName("jShellScrollPane" + number);

            getAccessibleContext().setAccessibleName("jShellFrame" + number);

            pack();
        }

    }

    /**
     * A tabbed JFrame.
     */
    static class TabbedShellFrame extends JFrame {

        private static int globalCount = 0;

        private final int number;

        private final java.util.Collection<IJssController> controllers;

        private javax.swing.JTabbedPane jShellTabbedPane;

        public TabbedShellFrame(IJssController... controllers) throws HeadlessException {
            super();
            number = globalCount++;
            this.controllers = new java.util.ArrayList<>(Arrays.asList(controllers));
            initComponents();
        }

        public TabbedShellFrame(String title, IJssController... controllers) throws HeadlessException {
            super(title);
            number = globalCount++;
            this.controllers = new java.util.ArrayList<>(Arrays.asList(controllers));
            initComponents();
        }

        public java.util.Collection<IJssController> getControllers() {
            return controllers;
        }

        public boolean addController(IJssController controller, boolean createShell) {
            boolean added = this.controllers.add(controller);

            if (added && createShell) {
                added = addShell(controller);
            }

            return added;
        }

        private boolean addShell(IJssController controller) {
            boolean added = false;

            if (controller != null && controller.getView() instanceof java.awt.Component) {
                javax.swing.JScrollPane jShellScrollPane = new javax.swing.JScrollPane();
                jShellScrollPane.setViewportView((java.awt.Component) controller.getView());
                jShellTabbedPane.addTab("Shell " + jShellTabbedPane.getTabCount(), jShellScrollPane);
                added = true;
            }

            return added;
        }

        private void initComponents() {

            jShellTabbedPane = new javax.swing.JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setPreferredSize(new java.awt.Dimension(320, 240));

            for (IJssController controller : controllers) {
                addShell(controller);
            }

            getContentPane().add(jShellTabbedPane, java.awt.BorderLayout.CENTER);
            jShellTabbedPane.getAccessibleContext().setAccessibleName("jShellTabbedPane" + number);

            getAccessibleContext().setAccessibleName("jShellFrame" + number);

            pack();
        }

    }

    static void initializeShellActionSwingProperties(IJssAction action) {
        if (action == null) {
            return;
        }

        String[] commandIdentifiers = action.getCommandIdentifiers();
        if (commandIdentifiers != null && commandIdentifiers.length > 0) {
            action.putValue(Action.ACTION_COMMAND_KEY, commandIdentifiers[0]);
            if (commandIdentifiers[0] != null && !commandIdentifiers[0].isEmpty()) {
                String name = commandIdentifiers[0].substring(0, 1).toUpperCase() + commandIdentifiers[0].substring(1).toLowerCase();
                action.putValue(Action.NAME, name);
            }
        }
        String commandBriefHelp = action.getBriefHelp();
        if (commandBriefHelp != null) {
            action.putValue(Action.SHORT_DESCRIPTION, commandBriefHelp);
        }
        String commandHelp = action.getHelp();
        if (commandHelp != null) {
            action.putValue(Action.LONG_DESCRIPTION, commandHelp);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
                if ("Nimbus".equals(installedLookAndFeel.getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JssAdvancedDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        // #####################################################################
        // Let's create our first shell
        JssTextAreaController shellController = new JssTextAreaController("Welcome to the Advanced JSwingShell demonstration!!\nI advise you to take a look at the loadCommandFile action and the resources/cmd.shell file ;)\n");

        // Add some actions to it...
        shellController.getModel().add(new HelpAction(shellController));
        shellController.getModel().add(new ExitAction(shellController));
        shellController.getModel().add(new EchoAction());
        shellController.getModel().add(new TimeAction());
        shellController.getModel().add(new ClearAction("Clear", shellController, null));
        shellController.getModel().add(new SleepAction());
        shellController.getModel().add(new WaitAction());
        shellController.getModel().add(new LoadCommandFile(shellController));

        // For each action, initialize its Swing properties with the shell ones
        for (IJssAction action : shellController.getAvailableActions()) {
            initializeShellActionSwingProperties(action);
        }

        // and now put its view to a JFrame
        final JssAdvancedDemo.SimpleShellFrame shellFrame = new JssAdvancedDemo.SimpleShellFrame(shellController, "My application shell added to a JFrame");
        // And to show the true power of JSwingShell, let's add a menu!
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem(shellController.getModel().getActionForCommandIdentifier("exit"));
        exitMenuItem.setEnabled(true);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem clearMenuItem = new JMenuItem(shellController.getModel().getActionForCommandIdentifier("clear"));
        clearMenuItem.setEnabled(true);
        viewMenu.add(clearMenuItem);
        JMenuItem hideMenuItem = new JMenuItem(new AbstractJssAction("Hide this shell", shellController, null) {
            @Override
            public String[] getCommandIdentifiers() {
                String[] identifiers = {"hide"};
                return identifiers;
            }

            @Override
            public String getHelp(IJssController shellController) {
                return getBriefHelp();
            }

            @Override
            public String getBriefHelp() {
                return "Hides the first shell frame.";
            }

            @Override
            public int run(IJssController shellController, String... args) {
                int commandReturnStatus = 0;

                shellFrame.setVisible(false);

                return commandReturnStatus;
            }
        });
        shellController.getModel().add((IJssAction) hideMenuItem.getAction());
        hideMenuItem.setEnabled(true);
        viewMenu.add(hideMenuItem);
        menuBar.add(viewMenu);

        JMenu helpMenu = new JMenu("?");
        JMenuItem helpMenuItem = new JMenuItem(shellController.getModel().getActionForCommandIdentifier("help"));
        helpMenuItem.setEnabled(true);
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);

        shellFrame.setJMenuBar(menuBar);

        // #####################################################################
        // Now, let's try to make a copy of our controller
        JssTextAreaController myOtherShellController = new JssTextAreaController(shellController, "You should love JShell!!\n");

        myOtherShellController.getModel().add(new AbstractJssAction(shellController) {
            private final String[] identifiers = {"show"};

            @Override
            public String[] getCommandIdentifiers() {
                return identifiers;
            }

            @Override
            public String getHelp(IJssController shellController) {
                return getBriefHelp();
            }

            @Override
            public String getBriefHelp() {
                return "Shows the first shell frame.";
            }

            @Override
            public int run(IJssController shellController, String... args) {
                int commandReturnStatus = 0;

                shellFrame.setVisible(true);

                return commandReturnStatus;
            }
        });

        final JssAdvancedDemo.SimpleShellFrame otherShellFrame = new JssAdvancedDemo.SimpleShellFrame(myOtherShellController, "My other application shell copied from the first one");

        // #####################################################################
        // Now, let's try to make a copy of our controller
        JssTextAreaController anotherShellController = new JssTextAreaController(shellController, "I will be back!\n");
        JssTextAreaController yetAnotherShellController = new JssTextAreaController(shellController, "It's here again, but it is not the same!!\n");

        final JssAdvancedDemo.TabbedShellFrame tabbedShellFrame = new JssAdvancedDemo.TabbedShellFrame("My tabbed application shell", anotherShellController, yetAnotherShellController);

        // #####################################################################
        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                shellFrame.setMinimumSize(new Dimension(640, 480));
                shellFrame.setVisible(true);

                otherShellFrame.setMinimumSize(new Dimension(640, 480));
                otherShellFrame.setLocation(641, 0);
                otherShellFrame.setVisible(true);

                tabbedShellFrame.setMinimumSize(new Dimension(640, 480));
                tabbedShellFrame.setLocation(0, 481);
                tabbedShellFrame.setVisible(true);
            }
        });
        // Command file for test: cmd.shell
    }

}
