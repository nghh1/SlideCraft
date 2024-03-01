package SlideCraft.ui.helper;

import SlideCraft.ui.SlideCraftFrame;

import java.io.File;
import java.io.FileNotFoundException;
// import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

// import static SlideCraft.ui.DialogMenu.showWarningMessage;
import static SlideCraft.ui.DialogMenu.showQuestionMessage;
// import static SlideCraft.ui.DialogMenu.YES_NO_OPTION;
import static SlideCraft.ui.DialogMenu.YES_OPTION;
import static SlideCraft.ui.DialogMenu.YES_NO_CANCEL_OPTION;
import static SlideCraft.ui.DialogMenu.NO_OPTION;

import SlideCraft.data.SlidesRepository;
import SlideCraft.ui.action.BackgroundTask;
import SlideCraft.util.Config;

import static java.lang.String.format;

public class FileHelper {
    public static final String SAVE_MODIFIED_QUESTION_MESSAGE
            = "The current file has been modified.\n"
            + "Do you want to save the changes before closing?";
    // private static final String CREATE_FILE_QUESTION_MESSAGE
    //         = "File not found:\n%s\n\nDo you want to create the file?";
    private static final String OPERATION_ERROR_MESSAGE
            = "An error occured during the %s operation:\n%s";
    // private static final String FILE_OVERWRITE_QUESTION_MESSAGE
    //         = "File is already exists:\n%s\n\nDo you want to overwrite?";

    private static final String SLIDECRAFT_DATA_FILES = "SlideCraft Data Files (*.slidecraft)";
    // private static final String XML_FILES = "XML Files (*.xml)";

    private FileHelper() {
        // not intended to be instantiated
    }

    /**
     * Creates a new entries document.
     *
     * @param parent parent component
     */
    public static void createNew(final SlideCraftFrame parent) {
        parent.initSlides(true);
    }

    /**
     * Shows a file chooser dialog and saves a file.
     *
     * @param parent parent component
     * @param saveAs normal 'Save' dialog or 'Save as'
     */
    public static void saveFile(final SlideCraftFrame parent, final boolean saveAs) {
        saveFile(parent, saveAs, () -> {
            //default empty call
        });
    }

    /**
     * Shows a file chooser dialog and saves a file.
     *
     * @param parent parent component
     * @param saveAs normal 'Save' dialog or 'Save as'
     * @param successCallback callback which is called when the file has been
     * successfully saved
     */
    public static void saveFile(final SlideCraftFrame parent, final boolean saveAs, final Runnable successCallback) {
        final String fileName;
        if (saveAs || parent.getModel().getFileName() == null) {
            File file = showFileChooser(parent, "Save", "slidecraft", SLIDECRAFT_DATA_FILES);
            if (file == null) {
                return;
            }
            fileName = checkExtension(file.getPath(), "slidecraft");
            if (!checkFileOverwrite(fileName, parent)) {
                return;
            }
        } else {
            fileName = parent.getModel().getFileName();
        }

        BackgroundTask worker = new BackgroundTask(parent) {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    parent.getModel().getSlides().clearSlidesPreviewPanel();
                    SlidesRepository.newInstance(fileName).writeDocument(parent.getModel().getSlides());
                    parent.getModel().getSlides().initSlidesPreview();
                    parent.getModel().setFileName(fileName);
                    parent.getModel().setModified(false);
                } catch (Throwable e) {
                    throw new Exception(format(OPERATION_ERROR_MESSAGE, "save", e.getMessage()));
                }
                return null;
            }

            @Override
            protected void done() {
                stopProcessing();
                boolean result = true;
                try {
                    get();
                } catch (Exception e) {
                    result = false;
                    showErrorMessage(e);
                }
                if (result) {
                    successCallback.run();
                }
            }
        };
        worker.execute();
    }

    /**
     * Shows a file chooser dialog and opens a file.
     *
     * @param parent parent component
     */
    public static void openFile(final SlideCraftFrame parent) {
        final File file = showFileChooser(parent, "Open", "slidecraft", SLIDECRAFT_DATA_FILES);
        if (file == null) {
            return;
        }
        if (parent.getModel().isModified()) {
            int option = showQuestionMessage(parent, SAVE_MODIFIED_QUESTION_MESSAGE, YES_NO_CANCEL_OPTION);
            if (option == YES_OPTION) {
                saveFile(parent, false, () -> openFileInBackground(file.getPath(), parent));
                return;
            } else if (option != NO_OPTION) {
                return;
            }
        }
        openFileInBackground(file.getPath(), parent);
    }

    /**
     * Loads a file and fills the data model.
     *
     * @param fileName file name
     * @param parent parent component
     */
    public static void openFileInBackground(final String fileName, final SlideCraftFrame parent) {
        parent.clearModel();
        if (fileName == null) {
            return;
        }
        BackgroundTask worker = new BackgroundTask(parent) {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    parent.getModel().setSlides(SlidesRepository.newInstance(fileName).readDocument());
                    parent.getModel().getSlides().initSlidesPreview();
                    parent.getModel().setFileName(fileName);
                    parent.initSlides(false);
                    //parent.getSearchPanel().setVisible(false);
                } catch (FileNotFoundException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new Exception(format(OPERATION_ERROR_MESSAGE, "open", e.getMessage()));
                }
                return null;
            }

            @Override
            protected void done() {
                stopProcessing();
                try {
                    get();
                } catch (Exception e) {
                    if (e.getCause() instanceof FileNotFoundException) {
                        handleFileNotFound(parent, fileName);
                    } else {
                        showErrorMessage(e);
                    }
                }
            }
        };
        worker.execute();
    }

    /**
     * Handles file not found exception.
     *
     * @param parent parent frame
     * @param fileName file name
     */
    static void handleFileNotFound(final SlideCraftFrame parent, final String fileName) {
        int option = NO_OPTION; /*showQuestionMessage(parent, format(CREATE_FILE_QUESTION_MESSAGE, stripString(fileName)), YES_NO_OPTION);*/
        if (option == YES_OPTION) {
            BackgroundTask fileNotFoundWorker = new BackgroundTask(parent) {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        SlidesRepository.newInstance(fileName).writeDocument(parent.getModel().getSlides());
                        parent.getModel().setFileName(fileName);
                    } catch (Exception ex) {
                        throw new Exception(format(OPERATION_ERROR_MESSAGE, "open", ex.getMessage()));
                    }
                    return null;
                }
            };
            fileNotFoundWorker.execute();
        }

    }

    /**
     * Shows file chosing menu
     * 
     * @param parent parent frame
     * @param taskName name of the task
     * @param extension extension of the file
     * @param description description
     */
    private static File showFileChooser(SlideCraftFrame parent, String taskName, String extension, String description) {
        File ret = null;
        String fileChooserDir = Config.getInstance().get("file.chooser.directory", "./");
        JFileChooser fc = new JFileChooser(fileChooserDir.isEmpty() ? null : fileChooserDir);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith("." + extension);
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        int returnVal = fc.showDialog(parent, taskName);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ret = fc.getSelectedFile();
        }
        return ret;
    }


    /**
     * Creates a new slide.
     *
     * @param parent parent component
     */
    public static void newSlide(final SlideCraftFrame parent) {
        parent.addSlide(false);
    }

    /**
     * Checks if overwrite is accepted.
     *
     * @param fileName file name
     * @param parent parent component
     * @return {@code true} if overwrite is accepted; otherwise {@code false}
     */
    private static boolean checkFileOverwrite(String fileName, SlideCraftFrame parent) {
        boolean overwriteAccepted = true;
        
        return overwriteAccepted;
    }

    /**
     * Checks if the file name has the given extension
     *
     * @param fileName file name
     * @param extension extension
     * @return file name ending with the given extension
     */
    private static String checkExtension(final String fileName, final String extension) {
        String separator = fileName.endsWith(".") ? "" : ".";
        if (!fileName.toLowerCase().endsWith(separator + extension)) {
            return fileName + separator + extension;
        }
        return fileName;
    }

}
