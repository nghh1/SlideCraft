package SlideCraft.util;

import java.awt.Color;
import java.io.*;
import java.util.*;

import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import SlideCraft.ui.action.DraggableResizableTextArea;

import javax.swing.text.*;

public class SpellCheck {
    private static Set<String> dictionary;
    
    public SpellCheck(){
        
    }

    public static void spellCheck(DraggableResizableTextArea textbox){
        if(!textbox.getText().trim().isEmpty()){
            removeHighlight(textbox);
            dictionary = new HashSet<>();
            try(BufferedReader reader = new BufferedReader(new FileReader("SlideCraft/src/main/java/SlideCraft/util/dictionary.txt"))){
                String word;
                while((word = reader.readLine())!=null){
                    dictionary.add(word);
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            String line = textbox.getText();
            String[] words = line.split("\\s+");
            Highlighter highlighter = textbox.getHighlighter();
            HighlightPainter  painter =  new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
            for(String word: words){
                if(!dictionary.contains(word.toLowerCase())){
                    int begin = line.indexOf(word);
                    int end = begin+word.length();
                    try {
                        highlighter.addHighlight(begin, end, painter);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
            return;
    }

    public static void removeHighlight(DraggableResizableTextArea textbox){
        Highlighter highlighter = textbox.getHighlighter();
        Highlight[] highlights = highlighter.getHighlights();
        for(Highlight highlight:highlights){
            if(highlight!=null)
                highlighter.removeHighlight(highlight);
        }
    }

    
}
