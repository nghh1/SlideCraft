package SlideCraft.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import SlideCraft.xml.Slides;
import SlideCraft.xml.Converter;

public final class SlidesRepository {

    /**
     * File name to read/write.
     */
    private final String fileName;

    /**
     * Converter between document objects and streams representing XMLs
     */
    private static final Converter<Slides> CONVERTER = new Converter<>();

    /**
     * Creates a DocumentRepository instance.
     *
     * @param fileName file name
     */
    private SlidesRepository(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Creates a document repository with no encryption.
     *
     * @param fileName file name
     * @return a new DocumentHelper object
     */
    public static SlidesRepository newInstance(final String fileName) {
        return new SlidesRepository(fileName);
    }

    /**
     * Reads and XML file to an {@link Entries} object.
     *
     * @return the document
     * @throws FileNotFoundException if file is not exists
     * @throws IOException when I/O error occurred (e.g file format issues)
     * @throws DocumentProcessException when document could not be read
     */
    public Slides readDocument() throws IOException {
        InputStream inputStream = null;
        Slides slides;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(this.fileName));
            slides = CONVERTER.read(inputStream);
        } catch (IOException e) {
            throw e;
        } catch(Exception e){
           e.printStackTrace();
           return null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return slides;
    }

    /**
     * Writes a document into an XML file.
     *
     * @param document the document
     * @throws DocumentProcessException when document could not be saved
     */
    public void writeDocument(final Slides document) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(this.fileName));
            CONVERTER.write(document, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}

