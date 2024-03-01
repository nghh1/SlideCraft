
// package SlideCraft.ui;

// import javax.swing.JTextArea;
// import javax.swing.JTextField;

// import SlideCraft.ui.action.TextCompActionType;
// import SlideCraft.ui.action.TextCompPopupListener;

// /**
//  * Factory for creating text components with context menus.
//  *
//  * @author Maksym Yahnyshchak
//  *
//  */
// public final class TextFactory {

//     private TextFactory() {
//         // not intended to be instantiated
//     }

//     /**
//      * Creates a new {@link JTextField} instance with a context pop-up menu by
//      * default.
//      *
//      * @return the new instance
//      */
//     public static JTextField newTextField() {
//         return newTextField(null);
//     }

//     /**
//      * Creates a new {@link JTextField} instance with a context pop-up menu by
//      * default.
//      *
//      * @param text the initial text
//      * @return the new instance
//      */
//     public static JTextField newTextField(String text) {
//         JTextField textField = text == null ? new JTextField() : new JTextField(text);
//         textField.addMouseListener(new TextComponentPopupListener());
//         TextComponentActionType.bindAllActions(textField);
//         return textField;
//     }


//     /**
//      * Creates a new {@link JTextArea} instance with a context pop-up menu by
//      * default.
//      *
//      * @return the new instance
//      */
//     public static JTextArea newTextArea() {
//         return newTextArea(null);
//     }

//     /**
//      * Creates a new {@link JTextArea} instance with a context pop-up menu by
//      * default.
//      *
//      * @param text the initial text
//      * @return the new instance
//      */
//     public static JTextArea newTextArea(String text) {
//         JTextArea textArea = text == null ? new JTextArea() : new JTextArea(text);
//         textArea.addMouseListener(new TextComponentPopupListener());
//         TextComponentActionType.bindAllActions(textArea);
//         return textArea;
//     }
// }
