package SlideCraft.data;

import SlideCraft.xml.Slide;
import SlideCraft.xml.Slides;

public class DataModel {
    private static DataModel INSTANCE;

    private Slides slides = null;
    private String fileName = null;
    private boolean modified = false;

    private DataModel() {
        // not intended to be instantiated
    }

    /**
     * Gets list of slides.
     *
     * @return list of slides.
     */
    public final Slides getSlides() {
        return this.slides;
    }

    public final void setSlides(Slides newSlides) {
        this.slides = newSlides;
        this.slides.initSlidesPreview();
        for (Slide slide : this.slides.getSlides()){
            slide.setParent(this.slides);
        }
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the modified state of the data model.
     *
     * @return modified state of the data model
     */
    public final boolean isModified() {
        return this.modified;
    }

    /**
     * Sets the modified state of the data model.
     *
     * @param modified modified state
     */
    public final void setModified(final boolean modified) {
        this.modified = modified;
    }

    /**
     * Gets the DataModel singleton instance.
     *
     * @return instance of the DataModel
     */
    public static synchronized DataModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataModel();
        }
        return INSTANCE;
    }

    /**
     * Clears all fields of the data model.
     */
    public final void clear() {
        this.slides = null;
        this.fileName = null;
        this.modified = false;
    }

    /**
     * Gets entry by index.
     *
     * @param index entry index
     * @return entry (can be null)
     */
    public Slide getSlideByIndex(int index) {
        return this.slides.getSlides().get(index);
    }

}
