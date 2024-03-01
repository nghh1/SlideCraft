package SlideCraft.xml;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class CodeEditor extends JFrame{
    private JTextArea editArea;
    private JScrollPane sp;
    private JFileChooser fileChooser;
    private CodeEditor INSTANCE;

    public CodeEditor(){
        setLayout(new BorderLayout());
        setSize(800,600);
        editArea = new JTextArea();
        sp = new JScrollPane(editArea);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(sp,BorderLayout.CENTER);
        fileChooser = new JFileChooser();
        JPanel numberLine = new JPanel(new BorderLayout());
        JTextArea numbering = new JTextArea("1.");
        numbering.setForeground(Color.WHITE);
        numbering.setEditable(false);
        numbering.setBackground(Color.BLUE);
        numberLine.add(numbering,BorderLayout.CENTER);
        this.add(numberLine,BorderLayout.WEST);
        editArea.getDocument().addDocumentListener(new DocumentListener(){
            public String setNumbering(){
                int position = editArea.getDocument().getLength();
                Element rootElement = editArea.getDocument().getDefaultRootElement();
                String number = "1.\n";
                for(int i=2;i<2+rootElement.getElementIndex(position);i++){
                    number+=i+".\n";
                }
                return number;
            }
            @Override
            public void insertUpdate(DocumentEvent e){
                numbering.setText(setNumbering());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }

        });

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem newfile = new JMenuItem("New");
        this.INSTANCE = this;
        newfile.addActionListener(e->editArea.setText(""));
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(fileChooser.showOpenDialog(INSTANCE)==JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try{
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        editArea.read(reader,null);
                        reader.close();
                    }catch(IOException ev){
                        ev.printStackTrace();
                    }
                }
            } 
        });
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(fileChooser.showOpenDialog(INSTANCE)==JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try{
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        editArea.write(writer);
                        writer.close();
                    }catch(IOException ev){
                        ev.printStackTrace();
                    }
                }
            }
        });
        file.add(newfile);
        file.add(open);
        file.add(save);
        menu.add(file);
        this.setJMenuBar(menu);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}