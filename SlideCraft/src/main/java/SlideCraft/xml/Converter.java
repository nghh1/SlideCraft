package SlideCraft.xml;

import java.io.*;

public class Converter<T> {

    public Converter() {

    }

    /**
     * Maps the given object to the given output stream.
     */
    public void write(T document, OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(document);
        System.out.println("Object serialized and saved into file");
    }

    /**
     * Maps the given input stream to a document object.
     */
    @SuppressWarnings("unchecked")
    public T read(InputStream inputStream) throws IOException {
        T document = null;
        try{
            ObjectInputStream in = new ObjectInputStream(inputStream);
            document = (T) in.readObject();
            in.close();
            inputStream.close();
            
        }
        catch(IOException i){
            i.printStackTrace();
        }
        catch(ClassNotFoundException e){
            System.out.println("Couldn't find the class");
            e.printStackTrace();
        }
        return document;
    }
}

