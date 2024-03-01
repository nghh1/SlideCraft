package SlideCraft.ui.action;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.Slides;
import SlideCraft.xml.Slide;

import javax.swing.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchAtTextArea extends JPanel {
    public JTextField searchField;
    private static final String textPlaceholder = "Search...";
    private boolean regexToggled = false;

    public SearchAtTextArea(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(175, 25));
        setBackground(SlideCraftFrame.interfaceMainColor);
        setOpaque(true);

        this.searchField = new JTextField();
        this.searchField.setSelectionStart(0);
        this.searchField.setSelectionEnd(0);
        this.searchField.setCaretColor(Color.WHITE);
        this.searchField.setPreferredSize(new Dimension(108, 10));
        this.searchField.setMinimumSize(new Dimension(108, 10));
        this.searchField.setMaximumSize(new Dimension(108, 10));
        this.searchField.setFont(new Font("SansSerif",Font.BOLD,12));
        this.searchField.setBackground(SlideCraftFrame.interfaceMainColor);
        this.searchField.setForeground(Color.white);
        this.searchField.setBorder(null);

        this.searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(textPlaceholder)) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(textPlaceholder);
                }
            }
        });

        
        this.searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            //Plain text components don't fire these events.
            }
        });        
        
        ImageIcon searchIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/zoom.png");
        Image image = searchIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        searchIcon = new ImageIcon(newimg);  // transform it back

        JLabel searchLabel = new JLabel(searchIcon);
        searchLabel.setPreferredSize(new Dimension(10, 10));
        

        JPanel searchAndRegex = new JPanel(new FlowLayout(FlowLayout.LEFT));
        FlowLayout layout = (FlowLayout)searchAndRegex.getLayout();
        layout.setVgap(0);
        searchAndRegex.setPreferredSize(new Dimension(170, 15));
        searchAndRegex.setBackground(SlideCraftFrame.interfaceMainColor);
        searchAndRegex.setOpaque(true);
        searchAndRegex.setBorder(null);
        
        searchAndRegex.add(searchLabel);
        searchAndRegex.add(searchField);

        JToggleButton regex = new JToggleButton("R");
        regex.setPreferredSize(new Dimension(15, 15));
        regex.setFont(new Font("SansSerif",Font.BOLD,8));
        regex.setFocusable(false);
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent){
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    regexToggled = true;
                    search();
                }
                else {
                    regexToggled = false;
                    search();
                }
            }
        };
        regex.addItemListener(itemListener);
        JPanel regexTogglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ((FlowLayout)regexTogglePanel.getLayout()).setVgap(0);
        regexTogglePanel.setPreferredSize(new Dimension(30, 15));
        regexTogglePanel.setBackground(SlideCraftFrame.interfaceMainColor);
        regexTogglePanel.setOpaque(true);
        regexTogglePanel.setBorder(null);
        regexTogglePanel.add(regex);
        searchAndRegex.add(regexTogglePanel);

        this.add(searchAndRegex, BorderLayout.NORTH);
    }

    public synchronized void search(){
        if (searchField.getText().length() == 0 || searchField.getText() == textPlaceholder || SlideCraftFrame.getInstance().getModel().getSlides() == null){
            return;
        }
        Slides slides = SlideCraftFrame.getInstance().getModel().getSlides();
        for (Slide slide: slides.getSlides()){
            for (Component c: slide.getComponents()){
                if ( c instanceof DraggableResizableTextArea ){
                    DraggableResizableTextArea textarea = (DraggableResizableTextArea) c;
                    Highlighter highlighter = textarea.getHighlighter();
                    highlighter.removeAllHighlights();
                    if ( ! regexToggled ){
                        int index = textarea.getText().indexOf(searchField.getText());
                        int matchLength = searchField.getText().length();
                        if (index >= 0){
                            slide.wasModified();
                        }
                        while (index >= 0 && matchLength != 0) {  // indexOf returns -1 if no match found
                            selectText(textarea, index, index + matchLength, Color.YELLOW);
                            index = textarea.getText().indexOf(searchField.getText(), index + matchLength);
                        }
                    }
                    else{
                        int matchLength = searchField.getText().length();
                        try{
                            Pattern pattern = Pattern.compile(searchField.getText());
                            Matcher matcher = pattern.matcher(textarea.getText());
                            while (matcher.find() && matchLength != 0) {
                                int start = matcher.start();
                                int end = matcher.end();
                                selectText(textarea, start, end, Color.YELLOW);
                            }
                        }
                        catch(Exception e){
                            // ignore
                        }
                    }
                }
            }
        }
    }

    private void selectText(DraggableResizableTextArea t, int start, int end, Color color) {
        Highlighter highlighter = t.getHighlighter();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);

        try {
            highlighter.addHighlight(start, end, painter);
        } catch (javax.swing.text.BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override 
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }

    @Override 
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        Thread t = new Thread(){
            public void run(){
                setBorder(new RoundedCornerBorder());
            }
        };
        t.start();
    }

    class RoundedCornerBorder extends AbstractBorder {
        private static final Color ALPHA_ZERO = new Color(0x0, true);
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape border = getBorderShape(x, y, width-1, height-1);
            g2.setPaint(ALPHA_ZERO);
            Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
            corner.subtract(new Area(border));
            g2.fill(corner);
            g2.setPaint(Color.WHITE);
            g2.draw(border);
            g2.dispose();
        }
        public Shape getBorderShape(int x, int y, int w, int h) {
            int r = h; //h / 2;
            return new RoundRectangle2D.Double(x, y, w, h, r, r);
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(5, 6, 4, 6);
        }
        @Override public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(5, 6, 4, 6);
            return insets;
        }
    }

    
}
