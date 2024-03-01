package SlideCraft.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
/**
 * Utility class for displaying message dialog.
 *
 * @author Maksym Yahnyshchak
 *
 */
public final class DialogMenu extends JDialog implements ActionListener {

    public static final int DEFAULT_OPTION = -1;
    public static final int YES_NO_OPTION = 0;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;

    public static final int YES_OPTION = 0;
    public static final int OK_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int CLOSED_OPTION = -1;

    private int selectedOption;

    private DialogMenu(final DialogMenu parent, final Object message, final String title, ImageIcon icon, int optionType) {
        super(parent);
        initializeDialog(parent, message, title, icon, optionType);
    }

    private DialogMenu(final Frame parent, final Object message, final String title, ImageIcon icon, int optionType) {
        super(parent);
        initializeDialog(parent, message, title, icon, optionType);
    }

    private void initializeDialog(final Component parent, final Object message, final String title, ImageIcon icon, int optionType) {
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(title);
        this.selectedOption = CLOSED_OPTION;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton defaultButton;
        switch (optionType) {
            case YES_NO_OPTION:
                defaultButton = createButton("Yes", YES_OPTION, getIcon("accept"));
                buttonPanel.add(defaultButton);
                buttonPanel.add(createButton("No", NO_OPTION, getIcon("close")));
                break;
            case YES_NO_CANCEL_OPTION:
                defaultButton = createButton("Yes", YES_OPTION, getIcon("accept"));
                buttonPanel.add(defaultButton);
                buttonPanel.add(createButton("No", NO_OPTION, getIcon("close")));
                buttonPanel.add(createButton("Cancel", CANCEL_OPTION, getIcon("cancel")));
                break;
            case OK_CANCEL_OPTION:
                defaultButton = createButton("OK", OK_OPTION, getIcon("accept"));
                buttonPanel.add(defaultButton);
                buttonPanel.add(createButton("Cancel", CANCEL_OPTION, getIcon("cancel")));
                break;
            default:
                defaultButton = createButton("OK", OK_OPTION, getIcon("accept"));
                buttonPanel.add(defaultButton);
                break;
        }
        getRootPane().setDefaultButton(defaultButton);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 0));

        float widthMultiplier;
        JPanel messagePanel = new JPanel(new BorderLayout());
        if (message instanceof JScrollPane) {
            widthMultiplier = 1.0f;
            messagePanel.add((Component) message, BorderLayout.CENTER);
        } else if (message instanceof Component) {
            widthMultiplier = 1.5f;
            messagePanel.add((Component) message, BorderLayout.NORTH);
        } else {
            widthMultiplier = 1.0f;
            messagePanel.setBorder(new EmptyBorder(10, 0, 10, 10));
            messagePanel.add(new JLabel("<html>" + String.valueOf(message)
                    .replaceAll("\\n", "<br />") + "</html>"), BorderLayout.CENTER);
        }
        mainPanel.add(messagePanel, BorderLayout.CENTER);

        if (icon != null) {
            JLabel image = new JLabel(icon);
            image.setVerticalAlignment(SwingConstants.TOP);
            image.setBorder(new EmptyBorder(10, 10, 0, 10));
            mainPanel.add(image, BorderLayout.WEST);
        }
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setResizable(false);
        pack();
        setSize((int) (getWidth() * widthMultiplier), getHeight());
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JButton createButton(String name, int option, ImageIcon icon) {
        JButton button = new JButton(name, icon);
        button.setMnemonic(name.charAt(0));
        button.setActionCommand(String.valueOf(option));
        button.addActionListener(this);
        return button;
    }

    private int getSelectedOption() {
        return this.selectedOption;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.selectedOption = Integer.parseInt(event.getActionCommand());
        dispose();
    }

    private static void showDialog(final Component parent, final Object message, final String title, ImageIcon icon) {
        showDialog(parent, message, title, icon, DEFAULT_OPTION);
    }

    private static int showDialog(final Component parent, final Object message, final String title, ImageIcon icon, int optionType) {
        int ret = CLOSED_OPTION;
        DialogMenu dialog = null;
        if (parent instanceof Frame) {
            dialog = new DialogMenu((Frame) parent, message, title, icon, optionType);
        } else if (parent instanceof DialogMenu) {
            dialog = new DialogMenu((DialogMenu) parent, message, title, icon, optionType);
        }
        if (dialog != null) {
            ret = dialog.getSelectedOption();
        }
        return ret;
    }

    /**
     * Shows a warning message.
     *
     * @param parent parent component
     * @param message dialog message
     */
    public static void showWarningMessage(final Component parent, final String message) {
        showDialog(parent, message, "Warning", getIcon("dialog_warning"));
    }

    /**
     * Shows an error message.
     *
     * @param parent parent component
     * @param message dialog message
     */
    public static void showErrorMessage(final Component parent, final String message) {
        showDialog(parent, message, "Error", getIcon("dialog_error"));
    }

    /**
     * Shows an information message.
     *
     * @param parent parent component
     * @param message dialog message
     */
    public static void showInformationMessage(final Component parent, final String message) {
        showDialog(parent, message, "Information", getIcon("dialog_info"));
    }

    /**
     * Shows a question dialog.
     *
     * @param parent parent component
     * @param message dialog message
     * @param optionType question type
     * @return selected option
     */
    public static int showQuestionMessage(final Component parent, final String message, final int optionType) {
        return showDialog(parent, message, "Confirmation", getIcon("dialog_question"), optionType);
    }
    
    /* 
     * Returns an image resource.
     *
     * @param name image name without path and extension
     * @return ImageIcon object
     */
    public static ImageIcon getIcon(String name) {
        return getIcon(name, 0, 0);
    }

    /**
     * Returns an image resource.
     *
     * @param name image name without path and extension
     * @param width the image width
     * @param height the image height
     * @return ImageIcon object
     */
    public static ImageIcon getIcon(String name, int width, int height) {
        try {
            return new ImageIcon("resources/images/" + name + ".svg"/* , width, height*/);
        } catch (Exception e) {
            return null;
        }
    }
}
