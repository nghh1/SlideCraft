// package SlideCraft.ui.helper;

// import SlideCraft.ui.SlideCraftFrame;
// // import SlideCraft.util.ClipboardUtils;
// import SlideCraft.xml.Slide;

// import static SlideCraft.ui.DialogMenu.showErrorMessage;
// import static SlideCraft.ui.DialogMenu.showWarningMessage;
// import static SlideCraft.ui.DialogMenu.showQuestionMessage;
// import static SlideCraft.ui.DialogMenu.YES_NO_OPTION;
// import static SlideCraft.ui.DialogMenu.YES_OPTION;


// public final class SlideHelper {
//     private SlideHelper() {
//         // not intended to be instantiated
//     }

//     /**
//      * Deletes an entry.
//      *
//      * @param parent parent component
//      */
//     public static void deleteEntry(SlideCraftFrame parent) {
//         if (parent.getModel().getSlides().getSelectedSlide() == -1) {
//             showWarningMessage(parent, "Please select an entry.");
//             return;
//         }
//         int option = showQuestionMessage(parent, "Do you really want to delete this entry?", YES_NO_OPTION);
//         if (option == YES_OPTION) {
//             String title = (String) parent.getEntryTitleTable().getValueAt(parent.getEntryTitleTable().getSelectedRow(), 0);
//             parent.getModel().getEntries().getEntry().remove(parent.getModel().getEntryByTitle(title));
//             parent.getModel().setModified(true);
//             parent.refreshFrameTitle();
//             parent.refreshEntryTitleList(null);
//         }
//     }

//     /**
//      * Duplicates an entry.
//      *
//      * @param parent parent component
//      */
//     public static void duplicateEntry(SlideCraftFrame parent) {
//         if (parent.getEntryTitleTable().getSelectedRow() == -1) {
//             showWarningMessage(parent, "Please select an entry.");
//             return;
//         }
//         String title = (String) parent.getEntryTitleTable().getValueAt(parent.getEntryTitleTable().getSelectedRow(), 0);
//         Entry originalEntry = parent.getModel().getEntryByTitle(title);
//         EntryDialog dialog = new EntryDialog(parent, "Duplicate Entry", originalEntry, true);
//         dialog.getModifiedEntry().ifPresent(entry -> {
//             parent.getModel().getEntries().getEntry().add(entry);
//             parent.getModel().setModified(true);
//             parent.refreshFrameTitle();
//             parent.refreshEntryTitleList(entry.getTitle());
//         });
//     }

//     /**
//      * Edits the entry.
//      *
//      * @param parent parent component
//      */
//     public static void editEntry(SlideCraftFrame parent) {
//         if (parent.getEntryTitleTable().getSelectedRow() == -1) {
//             showWarningMessage(parent, "Please select an entry.");
//             return;
//         }
//         String title = (String) parent.getEntryTitleTable().getValueAt(parent.getEntryTitleTable().getSelectedRow(), 0);
//         Entry originalEntry = parent.getModel().getEntryByTitle(title);
//         EntryDialog dialog = new EntryDialog(parent, "Edit Entry", originalEntry, false);
//         dialog.getModifiedEntry().ifPresent(entry -> {
//             entry.setCreationDate(originalEntry.getCreationDate());
//             parent.getModel().getEntries().getEntry().remove(originalEntry);
//             parent.getModel().getEntries().getEntry().add(entry);
//             parent.getModel().setModified(true);
//             parent.refreshFrameTitle();
//             parent.refreshEntryTitleList(entry.getTitle());
//         });
//     }

//     /**
//      * Adds an entry.
//      *
//      * @param parent parent component
//      */
//     public static void addEntry(SlideCraftFrame parent) {
//         EntryDialog dialog = new EntryDialog(parent, "Add New Entry", null, true);
//         dialog.getModifiedEntry().ifPresent(entry -> {
//             parent.getModel().getEntries().getEntry().add(entry);
//             parent.getModel().setModified(true);
//             parent.refreshFrameTitle();
//             parent.refreshEntryTitleList(entry.getTitle());
//         });
//     }

//     /**
//      * Gets the selected entry.
//      *
//      * @param parent the parent frame
//      * @return the entry or null
//      */
//     public static Entry getSelectedEntry(SlideCraftFrame parent) {
//         if (parent.getEntryTitleTable().getSelectedRow() == -1) {
//             showWarningMessage(parent, "Please select an entry.");
//             return null;
//         }
//         String title = (String) parent.getEntryTitleTable().getValueAt(parent.getEntryTitleTable().getSelectedRow(), 0);
//         return parent.getModel().getEntryByTitle(title);
//     }

//     /**
//      * Copy entry field value to clipboard.
//      *
//      * @param parent the parent frame
//      * @param content the content to copy
//      */
//     public static void copyEntryField(SlideCraftFrame parent, String content) {
//         try {
//             ClipboardUtils.setClipboardContent(content);
//         } catch (Exception e) {
//             showErrorMessage(parent, e.getMessage());
//         }
//     }

// }
