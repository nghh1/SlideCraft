package SlideCraft.ui.action;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.drjekyll.fontchooser.FontDialog;

import SlideCraft.ui.PresentationFrame;
import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.ui.helper.PercentageSlider;
import SlideCraft.xml.CodeEditor;
import SlideCraft.xml.Slide;

// import static javax.swing.KeyStroke.getKeyStroke;
import static SlideCraft.ui.helper.FileHelper.openFile;
import static SlideCraft.ui.helper.FileHelper.saveFile;
import static SlideCraft.ui.helper.FileHelper.createNew;
import static SlideCraft.ui.helper.FileHelper.newSlide;
import static SlideCraft.ui.helper.InsertHelper.insertHyperLink;
import static SlideCraft.ui.helper.InsertHelper.insertImage;
import static SlideCraft.ui.helper.InsertHelper.insertRectangle;
import static SlideCraft.ui.helper.InsertHelper.insertBarChart;
import static SlideCraft.ui.helper.InsertHelper.insertCircle;
import static SlideCraft.ui.helper.InsertHelper.insertSound;
import static SlideCraft.ui.helper.InsertHelper.insertTable;
import static SlideCraft.ui.helper.InsertHelper.insertLineDiagram;
import static SlideCraft.ui.helper.DrawHelper.pickupPencil;
import static SlideCraft.ui.helper.DrawHelper.pickupEraser;
import static SlideCraft.ui.helper.DrawHelper.pickupBucket;
import static SlideCraft.ui.helper.UndoRedoHelper.undoAction;
import static SlideCraft.ui.helper.UndoRedoHelper.redoAction;
import static SlideCraft.util.SpellCheck.spellCheck;
// import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
// import static java.awt.event.InputEvent.ALT_DOWN_MASK;

public enum ToolBarAction {
    NEW_FILE(new AbstractAction("New", getIcon("new")) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            createNew(SlideCraftFrame.getInstance());
        }
    }),
    OPEN_FILE(new AbstractAction("Open File...", getIcon("open")) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            openFile(SlideCraftFrame.getInstance());
        }
    }),
    SAVE_FILE(new AbstractAction("Save",getIcon("floppy-disk")) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            saveFile(SlideCraftFrame.getInstance(), false);
        }
    }),
    SAVE_AS_FILE(new AbstractAction("Save As...", getIcon("saveas")) {
        @Override
        public void actionPerformed(ActionEvent ev) {
            saveFile(SlideCraftFrame.getInstance(), true);
        }
    }),
    FONT(new AbstractAction("Set Text Font") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    FontDialog.showDialog(textArea);
                }
            }
        }
    }),
    COLOUR(new AbstractAction("Set Text Colour") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    Color newColor = JColorChooser.showDialog(textArea, "Choose Text Colour", textArea.getForeground());
                    if(newColor != null) {
                        textArea.setForeground(newColor);
                    }
                }
            }
        }
    }),
    BOLD(new AbstractAction("Set Text Bold") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            // tbc
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        if(textArea.getFont().getStyle() == 3) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
                        } else if(textArea.getFont().getStyle() == 2) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC + Font.BOLD));
                        }else if(textArea.getFont().getStyle() == 1) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
                        } else if(textArea.getFont().getStyle() == 0) { 
                            textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
                        }
                    }
                }
            }
        }
    }),
    ITALIC(new AbstractAction("Set Text Italic") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            // tbc
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        if(textArea.getFont().getStyle() == 3) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
                        } else if(textArea.getFont().getStyle() == 2) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
                        }else if(textArea.getFont().getStyle() == 1) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC + Font.BOLD));
                        } else if(textArea.getFont().getStyle() == 0) {
                            textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
                        }
                    }
                }
            }
        }
    }),
    UNDERLINE(new AbstractAction("Set Text Underline") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            //tbc
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
                        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                        Font underlineFont = new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), textArea.getFont().getSize()).deriveFont(fontAttributes);
                        textArea.setFont(underlineFont);
                    }
                }
            }
        }
    }),
    TEXT_SPACING(new AbstractAction("Text Spacing") {
        @Override
        public void actionPerformed(ActionEvent ev) {
            //tbc
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()) {
                if(component instanceof DraggableResizableTextArea) {
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    Map<TextAttribute, Object> fontAttributes = new HashMap<TextAttribute, Object>();
                    fontAttributes.put(TextAttribute.TRACKING, 0.5);
                    textArea.setFont(textArea.getFont().deriveFont(fontAttributes));
                }
            }
        }
    }),
    BULLETEDLIST(new AbstractAction("Set bulleted list"){
        @Override
        public void actionPerformed(ActionEvent ev){
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()){
                if(component instanceof DraggableResizableTextArea){
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        String text = textArea.getText();
                        String selectedText = textArea.getSelectedText();
                        //Selected lines on textbox
                        if(selectedText!=null){
                            String[] selectedtextlines = selectedText.split("\n");
                            StringBuilder newTextlines = new StringBuilder();
                            for(int i=0;i<selectedtextlines.length;i++){
                                selectedtextlines[i] = selectedtextlines[i].replaceAll("^\\d+\\.\\s*", "");
                                selectedtextlines[i] = "• "+selectedtextlines[i].replaceAll("^•\\s*","");
                            }
                            for(String line:selectedtextlines){
                                newTextlines.append(line).append("\n");
                            }
                            int last = newTextlines.lastIndexOf("\n");
                            newTextlines.deleteCharAt(last);
                            textArea.replaceSelection(newTextlines.toString());
                        }
                        //No selected lines on textbox
                        else{
                            String[] textlines = text.split("\n");
                            StringBuilder builder = new StringBuilder();
                            for(int i=0;i<textlines.length;i++){
                                textlines[i] = textlines[i].replaceAll("^\\d+\\.\\s*","");
                                textlines[i] = "• "+textlines[i].replaceAll("^•\\s*","");
                            }
                            for(String textline:textlines){
                                builder.append(textline).append("\n");
                            }
                            int last = builder.lastIndexOf("\n");
                            builder.deleteCharAt(last);
                            textArea.setText(builder.toString());
                        }
                    }
                }
            }
        }
    }),
    NUMBEREDLIST(new AbstractAction("Set numbered list"){
        @Override
        public void actionPerformed(ActionEvent ev){
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()){
                if(component instanceof DraggableResizableTextArea){
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        String text = textArea.getText();
                        String selectedText = textArea.getSelectedText();
                        if(selectedText!=null){
                            String[] selectedtextlines = selectedText.split("\n");
                            StringBuilder newTextlines = new StringBuilder();
                            for(int i=0;i<selectedtextlines.length;i++){
                                selectedtextlines[i] = selectedtextlines[i].replaceAll("^•\\s*","");
                                selectedtextlines[i] = (i+1)+". "+selectedtextlines[i].replaceAll("^\\d+\\.\\s*","");
                            }
                            for(String line:selectedtextlines){
                                newTextlines.append(line).append("\n");
                            }
                            int last = newTextlines.lastIndexOf("\n");
                            newTextlines.deleteCharAt(last);
                            textArea.replaceSelection(newTextlines.toString());
                        }
                        else{
                            String[] textlines = text.split("\n");
                            StringBuilder builder = new StringBuilder();
                            for(int i=0;i<textlines.length;i++){
                                textlines[i] = textlines[i].replaceAll("^•\\s*","");
                                textlines[i] = (i+1)+". "+textlines[i].replaceAll("^\\d+\\.\\s*","");
                            }
                            for(String textline:textlines){
                                builder.append(textline).append("\n");
                            }
                            int last = builder.lastIndexOf("\n");
                            builder.deleteCharAt(last);
                            textArea.setText(builder.toString());
                        }
                    }
                }
            }
        }
    }),
    LISTCANCEL(new AbstractAction("List cancel"){
        @Override
        public void actionPerformed(ActionEvent ev){
            for(Component component: SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getComponents()){
                if(component instanceof DraggableResizableTextArea){
                    DraggableResizableTextArea textArea = (DraggableResizableTextArea) component;
                    if(textArea.isSelected()){
                        String text = textArea.getText();
                        String[] textlines = text.split("\n");
                        StringBuilder builder = new StringBuilder();
                        for(int i=0;i<textlines.length;i++){
                            textlines[i] = textlines[i].replaceAll("^•\\s*","");
                            textlines[i] = textlines[i].replaceAll("^\\d+\\.\\s*","");
                        }
                        for(String textline:textlines){
                            builder.append(textline).append("\n");
                        }
                        textArea.setText(builder.toString());
                    }
                }
            }
        }
    }),
    NEW_SLIDE(new AbstractAction("New Slide"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            newSlide(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_HYPERLINK(new AbstractAction("Insert Hyperlink"){
        @Override
        public void actionPerformed(ActionEvent e){
            insertHyperLink(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_IMAGE(new AbstractAction("Insert Image"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            insertImage(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_SOUND(new AbstractAction("Insert Sound"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            insertSound(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_TABLE(new AbstractAction("Insert Table"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            insertTable(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_SHAPE(new AbstractAction("Insert Shape"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            //Shape popup menu shown by InsertActionsMenu.java
        }
    }),
    INSERT_RECTANGLE(new AbstractAction("Insert Rectangle"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            insertRectangle(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_CIRCLE(new AbstractAction("Insert Circle"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            insertCircle(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_DIAGRAM(new AbstractAction("Insert Diagram"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            //Diagram popup menu shown by InsertActionsMenu.java
        }
    }),
    INSERT_LINE_DIAGRAM(new AbstractAction("Insert Line diagram"){
        @Override
        public void actionPerformed(ActionEvent ex){
            insertLineDiagram(SlideCraftFrame.getInstance());
        }
    }),
    INSERT_BARCHART(new AbstractAction("Insert Bar chart"){
        @Override
        public void actionPerformed(ActionEvent ex){
            insertBarChart(SlideCraftFrame.getInstance());
        }
    }),
    PENCIL(new AbstractAction("Pickup Pencil"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            pickupPencil(SlideCraftFrame.getInstance());
        }
    }),
    ERASER(new AbstractAction("Pickup Eraser"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            pickupEraser(SlideCraftFrame.getInstance());
        }
    }),
    COLOURBUCKET(new AbstractAction("Pickup Colourbucket"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            pickupBucket(SlideCraftFrame.getInstance());
        }
    }),
    TEXTBOX(new AbstractAction("Add textbox"){
        @Override
        public void actionPerformed(ActionEvent ev) {
            DraggableResizableTextArea textArea = new DraggableResizableTextArea();
            SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().add(textArea);
            SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide(), textArea);
        }
    }),
    HELP(new AbstractAction("Help"){
        @Override
        public void actionPerformed(ActionEvent ev){
            File f = new File("SlideCraft/src/main/resources/resourses/images/help.txt");
            try(BufferedReader reader = new BufferedReader(new FileReader(f))){
                StringBuilder textContent = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    textContent.append(line).append("\n");
                }
                JTextArea text = new JTextArea(textContent.toString());
                text.setEditable(false);
                JScrollPane sp = new JScrollPane(text);
                sp.setPreferredSize(new Dimension(800,600));
                JOptionPane.showMessageDialog(SlideCraftFrame.getInstance(), sp, "Help", JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }),
    UNDO_SLIDE(new AbstractAction("Undo",getIcon("undo")){
        @Override
        public void actionPerformed(ActionEvent ev){
            undoAction(SlideCraftFrame.getInstance().getModel().getSlides().getHelper());
        }
    }),
    REDO_SLIDE(new AbstractAction("Redo",getIcon("redo2")){
        @Override
        public void actionPerformed(ActionEvent ev){
            redoAction(SlideCraftFrame.getInstance().getModel().getSlides().getHelper());
        }
    }),
    ENTER_CODE(new AbstractAction("Button",getIcon("embed2")){
        @Override
        public void actionPerformed(ActionEvent ev){
            CodeEditor editor = new CodeEditor();
            editor.setVisible(true);
        }
    }),
    CHECK_SPELL(new AbstractAction("Spell Check",getIcon("spell-check")){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            for(Component c:s.getComponents()){
                if(c instanceof DraggableResizableTextArea){
                    spellCheck((DraggableResizableTextArea)c);
                }
            }
        }
    }),
    PLAY_SLIDE(new AbstractAction("Play Slides",getIcon("play2")){
        @Override
        public void actionPerformed(ActionEvent ev){
            PresentationFrame presentationFrame = new PresentationFrame(SlideCraftFrame.getInstance().getModel().getSlides().getSlides());
            presentationFrame.setVisible(true);
        }
    }),
    CONFIG_SETTINGS(new AbstractAction("Config Settings",getIcon("cog")){
        @Override
        public void actionPerformed(ActionEvent ev){
            String[] labels = {"On","Off"};
            ButtonGroup bg = new ButtonGroup();
            JRadioButton[] autoSave = new JRadioButton[2];
            JPanel settingPanel = new JPanel(new GridLayout(autoSave.length,1));
            for(int i=0;i<autoSave.length;i++){
                autoSave[i] = new JRadioButton(labels[i]);
                bg.add(autoSave[i]);
            }
            for(JRadioButton b:autoSave)
                settingPanel.add(b);
            int finish = JOptionPane.showConfirmDialog(null,settingPanel,"AutoSave:",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(finish==JOptionPane.OK_OPTION){
                for(JRadioButton b:autoSave){
                    Timer autoSaveTimer = new Timer(30000,null);
                    if(b.isSelected() && b.getText().equals("On")){
                        autoSaveTimer = new Timer(30000,new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                saveFile(SlideCraftFrame.getInstance(), false);
                            }
                        });
                        autoSaveTimer.start();
                    }else if(b.isSelected() && b.getText().equals("Off")){
                        autoSaveTimer.stop();
                    }
                } 
            }
        }
    }),
    SPEAKERNOTE(new AbstractAction("Speaker note",getIcon("microphone")){
        @Override
        public void actionPerformed(ActionEvent ev){
            if(SlideCraftFrame.getInstance().getSpeakerNotesPanel().getComponentCount()==0){
                JTextArea speakerNotes = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().getSpeakerNotes();
                SlideCraftFrame.getInstance().getSpeakerNotesPanel().removeAll();
                SlideCraftFrame.getInstance().getSpeakerNotesPanel().add(speakerNotes);
            }else{
                SlideCraftFrame.getInstance().getSpeakerNotesPanel().removeAll();
            }
            SlideCraftFrame.getInstance().refreshAll();
        }
    }), 
    TITLE_CONTENT(new AbstractAction("Title and Content"){
        @Override
        public void actionPerformed(ActionEvent ev){
            PercentageSlider p = SlideCraftFrame.getInstance().getPercentageSlider();
            int percentage = p.getValue();
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            DraggableResizableTextArea textAreaTitle = new DraggableResizableTextArea();
            textAreaTitle.setText("Title");
            DraggableResizableTextArea textAreaMain = new DraggableResizableTextArea();
            textAreaMain.setText("Paragraph");
            s.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(ComponentEvent e){
                    textAreaTitle.setSize(new Dimension(s.getWidth()-40, (int)(percentage*1.5))); //Weight s.getWidth()-40=560, height 80 default
                    textAreaTitle.setLocation(20,10); 
                    textAreaMain.setSize(new Dimension(s.getWidth()-40, (int) (s.getHeight()-percentage*2.5))); //height s.getHeight()-200
                    textAreaMain.setLocation(20,(int)2.4*percentage); //default x is 20 and y is 120
                }
            });
            //textAreaTitle.setSize(new Dimension(s.getWidth()-40, 80)); //Weight s.getWidth()-40=560, height 80 default
            //textAreaMain.setSize(new Dimension(s.getWidth()-40, s.getHeight()-200)); //height s.getHeight()-200
            s.add(textAreaTitle, BorderLayout.NORTH);
            s.add(textAreaMain, BorderLayout.CENTER);
            s.revalidate();
            s.repaint();
            s.wasModified();
            SlideCraftFrame.getInstance().refreshAll();
        }
    }),
    TITLE_2CONTENTS(new AbstractAction("Title & 2Contents"){
        @Override
        public void actionPerformed(ActionEvent ev){
            PercentageSlider p = SlideCraftFrame.getInstance().getPercentageSlider();
            int percentage = p.getValue();
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            DraggableResizableTextArea textAreaTitle = new DraggableResizableTextArea();
            textAreaTitle.setText("Title");
            DraggableResizableTextArea textAreaBottomLeft = new DraggableResizableTextArea();
            textAreaBottomLeft.setText("Paragraph");
            DraggableResizableTextArea textAreaBottomRight = new DraggableResizableTextArea();
            textAreaBottomRight.setText("Paragraph");
            s.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(ComponentEvent e){
                    textAreaTitle.setSize(new Dimension(s.getWidth()-40, (int)(percentage*1.5))); //Weight s.getWidth()-40=560, height 80 default
                    textAreaTitle.setLocation(20,10); 
                    textAreaBottomLeft.setSize(new Dimension((s.getWidth()-40)/2, (int) (s.getHeight()-percentage*2.5))); //height s.getHeight()-200
                    textAreaBottomLeft.setLocation(20,(int)2.4*percentage); //default x is 20 and y is 120
                    textAreaBottomRight.setSize(new Dimension((s.getWidth()-40)/2, (int) (s.getHeight()-percentage*2.5))); //height s.getHeight()-200
                    textAreaBottomRight.setLocation(textAreaBottomLeft.getX()+(s.getWidth()-40)/2,(int)2.4*percentage); //default x is 20 and y is 120
                }
            });
            //textAreaTitle.setSize(new Dimension(s.getWidth()-30, 80)); //Weight s.getWidth()-40=560, height 80 default
            //textAreaBottomLeft.setSize(new Dimension(s.getWidth()/2, (int) (s.getHeight()-percentage*2.5))); //height s.getHeight()-200
            //textAreaBottomRight.setSize(new Dimension(s.getWidth()/2, (int) (s.getHeight()-percentage*2.5))); //height s.getHeight()-200
            s.add(textAreaTitle, BorderLayout.NORTH);
            s.add(textAreaBottomLeft, BorderLayout.WEST);
            s.add(textAreaBottomRight, BorderLayout.EAST);
            s.revalidate();
            s.repaint();
            s.wasModified();
            SlideCraftFrame.getInstance().refreshAll();
        }
    }),
    TWO_CONTENTS(new AbstractAction("Two contents"){
        @Override
        public void actionPerformed(ActionEvent ev){
            PercentageSlider p = SlideCraftFrame.getInstance().getPercentageSlider();
            int percentage = p.getValue();
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            DraggableResizableTextArea textAreaLeft = new DraggableResizableTextArea();
            DraggableResizableTextArea textAreaRight = new DraggableResizableTextArea();
            s.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(ComponentEvent e){
                    textAreaLeft.setSize(new Dimension((s.getWidth()-40)/2, (int) (s.getHeight()-percentage*1.5))); //height s.getHeight()-200
                    textAreaLeft.setLocation(20,30); //default x is 20 and y is 120
                    textAreaRight.setSize(new Dimension((s.getWidth()-40)/2, (int) (s.getHeight()-percentage*1.5))); //height s.getHeight()-200
                    textAreaRight.setLocation(textAreaLeft.getX()+(s.getWidth()-40)/2,30); //default x is 20 and y is 120
                }
            });
            //textAreaLeft.setSize(new Dimension(s.getWidth()/2, s.getWidth()));
            //textAreaRight.setSize(new Dimension(s.getWidth()/2, s.getWidth()));
            s.add(textAreaLeft, BorderLayout.WEST);
            s.add(textAreaRight, BorderLayout.EAST);
            s.revalidate();
            s.repaint();
            s.wasModified();
            SlideCraftFrame.getInstance().refreshAll();
        }
    }), TRANSITION_NORTH(new AbstractAction("Transition North"){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            s.setTransitionNorth();
        }
    }), TRANSITION_SOUTH(new AbstractAction("Transition South"){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            s.setTransitionSouth();
        }
    }), TRANSITION_EAST(new AbstractAction("Transition East"){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            s.setTransitionEast();
        }
    }), TRANSITION_WEST(new AbstractAction("Transition West"){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            s.setTransitionWest();
        }
    }), REMOVE_TRANSITION(new AbstractAction("Remove Transition"){
        @Override
        public void actionPerformed(ActionEvent ev){
            Slide s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
            s.removeTransition();
        }
    });

    private final String name;
    private final AbstractAction action;

    private ToolBarAction(AbstractAction action) {
        this.name = String.format("slidecraft.menu.%s_action", this.name().toLowerCase());
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public AbstractAction getAction() {
        return this.action;
    }

    public KeyStroke getAccelerator() {
        return (KeyStroke) this.action.getValue(Action.ACCELERATOR_KEY);
    }

    public static void bindAllActions(JComponent component) {
        ActionMap actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap();
        for (ToolBarAction type : values()) {
            actionMap.put(type.getName(), type.getAction());
            KeyStroke acc = type.getAccelerator();
            if (acc != null) {
                inputMap.put(type.getAccelerator(), type.getName());
            }
        }
    }

    /**
     * Returns an image resource.
     *
     * @param name image name without path and extension
     * @return ImageIcon object
     */
    public static ImageIcon getIcon(String name) {
        try {
            return createImageIcon("resources/images/" + name + ".svg", name);
        } catch (Exception e) {
            return null;
        }
    }

    protected static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = ImageIcon.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
