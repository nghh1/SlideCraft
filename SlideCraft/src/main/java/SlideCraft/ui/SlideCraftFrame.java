package SlideCraft.ui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import SlideCraft.ui.action.*;
import SlideCraft.ui.helper.DiagramHelper;
import SlideCraft.ui.helper.HyperLinkHelper;
import SlideCraft.ui.helper.PercentageSlider;
import SlideCraft.data.DataModel;
import SlideCraft.xml.Slides;
import SlideCraft.xml.Slide;
import SlideCraft.ui.helper.ShapeInserter;
import SlideCraft.ui.helper.TableHelper;

import java.awt.event.ActionEvent;

public class SlideCraftFrame extends JFrame{
    //Maks
    private static SlideCraftFrame INSTANCE;

    // Maks
    private final DataModel model = DataModel.getInstance();

    public static final String PROGRAM_NAME = "Slide Craft presentation tool";

    public static final Color backgroundColor = new Color(217, 217, 217);
    public static final Color interfaceMainColor = new Color(0, 74, 173);
    public static final Color selectColor = new Color(255,100,100);
    public static final Color popupMenuColor = new Color(84,84,84);

    private final JPanel leftContainerPanel;

    // Nik 
    private final JPopupMenu viewingPopUp;

    // Maks
    private final JButton fileButton;
    private final JButton homeButton;
    private final JButton insertButton;
    private final JButton drawButton;
    private final JButton designButton;
    private final JButton helpButton;

    private final JButton redoButton;
    private final JButton undoButton;
    private final JButton codeEditorButton;
    private final JButton spellCheckButton;
    private final JButton saveButton;
    private final JButton presentButton;
    private final JButton settingButton;
    private final JButton speakerNoteButton;
    private final JToolBar toolBarTop;
    private final JToolBar toolBarCenter;
    private final JToolBar toolBarBottom;

    private final FileActionsMenu fileActionsMenu;
    private final HomeActionsMenu homeActionsMenu;
    private final InsertActionsMenu insertActionsMenu;
    private final DrawActionsMenu drawActionsMenu;
    private final DesignActionsMenu designActionsMenu;

    private JScrollPane scrollPane = null;
    private final JPanel rightMenu;
    private JPanel slidePreviewPanel;
    private final JPanel rightMenuTopTools;
    private final JButton addSlideButton;
    private final SearchAtTextArea search;
    private final PercentageSlider slider;

    private final JPanel toolBarPopups;
    private final JPanel slidesZone;
    private final JPanel slideCraftCentrePanel;
    private final JPanel speakerNotesPanel;
    //private final JPanel slide;
    private final HyperLinkHelper hyperLinkHelper;
    private final ShapeInserter shapeInserter;
    private final TableHelper tableHelper;
    private final DiagramHelper diagramHelper;

    private Color bucket = null;
    private Color drawingColor = null;

    private SlideCraftFrame(String fileName) {
        super(fileName);

        //Maks
        this.leftContainerPanel = new JPanel();
        this.leftContainerPanel.setLayout(new BorderLayout());
        this.leftContainerPanel.setPreferredSize(new Dimension(100, 0));
        this.leftContainerPanel.setBackground(interfaceMainColor);

        // Maks
        this.toolBarTop = new JToolBar();
        //this.toolBarTop.setPreferredSize(new Dimension(80, (int)this.getSize().getHeight()/3));
        this.toolBarTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.toolBarTop.setOrientation(SwingConstants.VERTICAL);
        this.toolBarTop.setFloatable(false);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(120, 0, 0, 0));
        centerPanel.setBackground(interfaceMainColor);
        
        this.toolBarCenter = new JToolBar();
        this.toolBarCenter.setPreferredSize(new Dimension(60, 220));
        this.toolBarCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.toolBarCenter.setFloatable(false);
        centerPanel.add(this.toolBarCenter,new GridBagConstraints());

        this.toolBarBottom = new JToolBar();
        this.toolBarBottom.setLayout(new GridLayout(4,2));
        this.toolBarBottom.setFloatable(false);

        this.toolBarTop.setBackground(interfaceMainColor);
        this.toolBarCenter.setBackground(interfaceMainColor);
        this.toolBarBottom.setBackground(interfaceMainColor);

        // Nik 
        // Viewing options pop up menu: Presentation and editing modes 
        this.viewingPopUp = new JPopupMenu();
        this.viewingPopUp.add(new JMenuItem(new AbstractAction("Presentation Mode") {
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(leftContainerPanel, "Presentation Mode");
                PresentationFrame presentationFrame = new PresentationFrame(getModel().getSlides().getSlides());
                presentationFrame.setVisible(true);
            }
        }));
        this.viewingPopUp.add(new JMenuItem(new AbstractAction("Editing Mode") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(leftContainerPanel, "You're already on Editing Mode!");
            }
        }));

        JButton viewingButton = new ToolBarButton("Viewing", null);
        viewingButton.addActionListener( e-> this.viewingPopUp.show(viewingButton, viewingButton.getX(), viewingButton.getY() ));
        viewingButton.setBackground(Color.WHITE);
        toolBarTop.add(viewingButton);
        
        // create a popup menu
        fileActionsMenu = new FileActionsMenu();
        homeActionsMenu = new HomeActionsMenu();
        insertActionsMenu = new InsertActionsMenu();
        drawActionsMenu = new DrawActionsMenu();
        designActionsMenu = new DesignActionsMenu();
 
        // Maks
        this.fileButton = new ToolBarButton("File", fileActionsMenu);
        this.homeButton = new ToolBarButton("Home", homeActionsMenu);
        this.insertButton = new ToolBarButton("Insert", insertActionsMenu);
        this.drawButton = new ToolBarButton("Draw", drawActionsMenu);
        this.designButton = new ToolBarButton("Design", designActionsMenu);
        this.helpButton = new ToolBarButton("Help",null);
        this.helpButton.setAction(ToolBarAction.HELP.getAction());        

        this.toolBarCenter.add(fileButton);
        this.toolBarCenter.add(homeButton);
        this.toolBarCenter.add(insertButton);
        this.toolBarCenter.add(drawButton);
        this.toolBarCenter.add(designButton);
        this.toolBarCenter.add(helpButton);

        this.toolBarPopups = new JPanel(new GridBagLayout());
        this.toolBarPopups.setVisible(false);
        this.toolBarPopups.setBackground(backgroundColor);
        
        this.slidesZone = new JPanel();
        this.slidesZone.setBackground(backgroundColor);

        this.undoButton = quickAccessButton("Undo", ToolBarAction.UNDO_SLIDE.getAction());
        this.toolBarBottom.add(undoButton);

        this.redoButton = quickAccessButton("Redo", ToolBarAction.REDO_SLIDE.getAction());
        this.toolBarBottom.add(redoButton);
        
        this.codeEditorButton = quickAccessButton("Code", ToolBarAction.ENTER_CODE.getAction());
        this.toolBarBottom.add(codeEditorButton);

        this.spellCheckButton = quickAccessButton("Spell", ToolBarAction.CHECK_SPELL.getAction());
        this.toolBarBottom.add(spellCheckButton);

        this.saveButton = quickAccessButton("Save", ToolBarAction.SAVE_FILE.getAction());
        this.toolBarBottom.add(saveButton);

        this.presentButton = quickAccessButton("Play", ToolBarAction.PLAY_SLIDE.getAction());
        this.toolBarBottom.add(presentButton);

        this.settingButton = quickAccessButton("Config", ToolBarAction.CONFIG_SETTINGS.getAction());
        this.toolBarBottom.add(settingButton);

        this.speakerNoteButton = quickAccessButton("Note", ToolBarAction.SPEAKERNOTE.getAction());
        this.toolBarBottom.add(speakerNoteButton);

        // Maks
        this.leftContainerPanel.add(this.toolBarTop, BorderLayout.NORTH);
        this.leftContainerPanel.add(centerPanel);
        this.leftContainerPanel.add(this.toolBarBottom, BorderLayout.SOUTH);
        this.add(leftContainerPanel, BorderLayout.WEST);

        //Slides scroller
        this.rightMenu = new JPanel();
        this.rightMenu.setPreferredSize(new Dimension(265, 0));
        this.rightMenu.setLayout(new BorderLayout());
        //this.slide = new JPanel();
        this.rightMenuTopTools = new JPanel(new BorderLayout());
        this.rightMenuTopTools.setPreferredSize(new Dimension(100, 58));
        this.rightMenuTopTools.setBorder(BorderFactory.createEmptyBorder());
        this.rightMenuTopTools.setBackground(interfaceMainColor);
        this.rightMenuTopTools.setForeground(Color.white);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(interfaceMainColor);
        
        Icon icon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/add.png");
        JPanel slideControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        FlowLayout layout = (FlowLayout)slideControlPanel.getLayout();
        layout.setVgap(0);
        slideControlPanel.setBackground(interfaceMainColor);
        this.addSlideButton = new JButton(icon);
        this.addSlideButton.setBackground(interfaceMainColor);
        this.addSlideButton.setFocusPainted(false);
        this.addSlideButton.setBorderPainted(false);
        this.addSlideButton.setBorder(new EmptyBorder(0, 20, 0, 0));
        this.addSlideButton.setPreferredSize(new Dimension(20,20));
        this.addSlideButton.setBorder(BorderFactory.createLineBorder(Color.white, 1 ,true));
        this.addSlideButton.addActionListener(e -> addSlide(false));
        slideControlPanel.add(this.addSlideButton);

        this.rightMenuTopTools.add(slideControlPanel, BorderLayout.SOUTH);

        this.search = new SearchAtTextArea();
        searchPanel.add(search, BorderLayout.NORTH);
        this.rightMenuTopTools.add(searchPanel, BorderLayout.NORTH);
        
        this.slider = new PercentageSlider(0, 100);
        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                slider.updatePercentageLabel(model);
                refreshAll();
            }
        });
        this.slider.setBorder(null);
        Icon zIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/zoom.png");
        JLabel zoomIcon = new JLabel(zIcon);
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliderPanel.setBackground(interfaceMainColor);
        sliderPanel.add(zoomIcon);
        sliderPanel.add(slider);
        this.rightMenu.add(sliderPanel, BorderLayout.SOUTH);
        this.rightMenu.add(this.rightMenuTopTools, BorderLayout.NORTH);
        this.add(this.rightMenu, BorderLayout.EAST);

        slideCraftCentrePanel = new JPanel(new BorderLayout());
        slideCraftCentrePanel.add(this.toolBarPopups, BorderLayout.WEST);
        speakerNotesPanel = new JPanel(new BorderLayout());
        speakerNotesPanel.setPreferredSize(new Dimension(slideCraftCentrePanel.getWidth(),100));
        speakerNotesPanel.setBackground(new Color(217, 217, 217));
        slideCraftCentrePanel.add(speakerNotesPanel,BorderLayout.SOUTH);
        this.add(slideCraftCentrePanel);
        this.initSlides(true);

        JScrollPane sp = new JScrollPane(this.slidesZone);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setBackground(backgroundColor);
        sp.getHorizontalScrollBar().setBackground(backgroundColor);
        sp.getHorizontalScrollBar().setForeground(Color.WHITE);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.getVerticalScrollBar().setUnitIncrement(14);
        slideCraftCentrePanel.add(sp, BorderLayout.CENTER);
        this.add(slideCraftCentrePanel, BorderLayout.CENTER);


        hyperLinkHelper = new HyperLinkHelper();
        shapeInserter = new ShapeInserter();
        tableHelper = new TableHelper();
        diagramHelper = new DiagramHelper();

        refreshAll();

        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static SlideCraftFrame getInstance() {
        return getInstance(null);
    }

    public static synchronized SlideCraftFrame getInstance(String fileName) {
        if (INSTANCE == null) {
            INSTANCE = new SlideCraftFrame("SlideCraft" + (fileName != null ? " - "+fileName : ""));
        }
        return INSTANCE;
    }

    public void initSlides(boolean create){
        this.slidesZone.removeAll();
        if (create){
            Slides slides = new Slides();
            this.clearModel();
            this.model.setSlides(slides);
            JTextArea speakerNotes = this.getModel().getSlides().getSlides().get(0).getSpeakerNotes();
            speakerNotesPanel.add(speakerNotes, BorderLayout.CENTER);
        }
        this.slidesZone.add(this.model.getSlides());
        this.initSlidesPreview();
        slider.updatePercentageLabel(model);
        this.refreshAll();
    }

    public void refreshToolBarPopups(JPanel actionsMenu){
        if ( ! java.util.Arrays.asList(toolBarPopups.getComponents()).contains(actionsMenu) ){
            this.toolBarPopups.removeAll();
            this.toolBarPopups.add(actionsMenu, new GridBagConstraints());
            this.toolBarPopups.setVisible(true);
        }
        else{
            this.toolBarPopups.removeAll();
            this.toolBarPopups.setVisible(false);
        }
        refreshAll();
    }

    public JButton quickAccessButton(String iconName, AbstractAction action){
        Icon icon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/"+iconName+".png");
        JButton newButton = new JButton(iconName,icon);
        newButton.setForeground(Color.WHITE);
        newButton.setHorizontalTextPosition(JButton.CENTER);
        newButton.setVerticalTextPosition(JButton.BOTTOM);
        newButton.setPreferredSize(new Dimension(25,40));
        newButton.setBackground(interfaceMainColor);
        newButton.setFocusPainted(false);
        newButton.setBorderPainted(false);
        newButton.setBackground(SlideCraftFrame.interfaceMainColor);
        if (action != null){
            newButton.addActionListener(action);
        }
        return newButton;
    } 

    public void initSlidesPreview(){
        if (this.scrollPane != null){
            this.rightMenu.remove(this.scrollPane);
        }
        this.slidePreviewPanel = this.model.getSlides().getPreviewPanel();

        this.scrollPane = new JScrollPane(this.slidePreviewPanel);
        this.scrollPane.setBorder(null);
        this.scrollPane.getVerticalScrollBar().setBackground(Color.white);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.rightMenu.add(this.scrollPane);
    }

    public void addSlide(boolean isDuplicate){
        //Add slide to slidezone
        this.model.getSlides().newSlide(this.slider.getValue(), isDuplicate);
        refreshAll();
    }
    
    public void removeSlide(){
        this.model.getSlides().removeSlide();
        refreshAll();
    }

    public void insertHyperLink(){
        hyperLinkHelper.insertHyperLink();
    }

    public void insertImage(){
        if(model.getSlides() != null) {
            for(Slide slide: model.getSlides().getSlides()) {
                if(slide.isSelected()) {
                    slide.addImage();
                }
            }
        }
    }

    public void insertSound(){
        if(model.getSlides() != null) {
            for(Slide slide: model.getSlides().getSlides()) {
                if(slide.isSelected()) {
                    slide.addSound();
                }
            }
        }
    }

    public void pickupPencil() {
        boolean anyDrawing = false;
        if(model.getSlides() != null) {
            for(Slide slide: model.getSlides().getSlides()) {
                if (slide.pencilUsed()){
                    slide.dropPencil();
                }
                else{
                    slide.setDrawing();
                    anyDrawing = true;
                }
            }
        }
        if (anyDrawing){
            this.drawingColor = JColorChooser.showDialog(this, "Choose painting colour", Color.white);
        }
    }

    public void pickupEraser() {
        if(model.getSlides() != null) {
            for(Slide slide: model.getSlides().getSlides()) {
                if (slide.eraserUsed()){
                    slide.dropEraser();
                }
                else{
                    slide.setErasing();
                }
            }
        }
    }

    public void pickupBucket() {
        if (bucketUsed()){
            dropBucket();
            return;
        }
        Color newColor = JColorChooser.showDialog(this, "Choose Bucket colour", Color.white);
        setBucket(newColor);
    }

    public Color getDrawingColor(){
        return this.drawingColor;
    }

    public boolean bucketUsed(){
        return this.bucket != null;
    }

    public void dropBucket(){
        this.bucket = null;
    }

    public void setBucket(Color color){
        this.bucket = color;
    }

    public Color getBucket(){
        return this.bucket;
    }

    public void insertRectangle(){
        shapeInserter.insertRectangle();
    }

    public void insertCircle(){
        shapeInserter.insertCircle();
    }

    public void insertTable(){
        tableHelper.insertTable();
    }

    public void insertLineDiagram(){
        diagramHelper.insertLineDiagram();
    }

    public void insertBarChart(){
        diagramHelper.insertBarChart();
    }

    public int getPencilSize(){
        return this.drawActionsMenu.getPencilSize();
    }

    /**
     * Gets the data model of this frame.
     *
     * @return data model
     */
    public DataModel getModel() {
        return this.model;
    }

    public JPanel getSpeakerNotesPanel(){
        return this.speakerNotesPanel;
    }

    public JPanel getSlideCraftCentrePanel(){
        return this.slideCraftCentrePanel;
    }

    public JPanel getSlidesZone(){
        return this.slidesZone;
    }

    public SearchAtTextArea getSearchField(){
        return this.search;
    }

    public PercentageSlider getPercentageSlider(){
        return this.slider;
    }
    /**
     * Clears data model.
     */
    public void clearModel() {
        this.model.clear();
    }

    /**
     * Refresh frame title based on data model.
     */
    public void refreshFrameTitle() {
        setTitle(PROGRAM_NAME);
    }

    public void setProcessing(boolean processing) {
        // this.processing = processing;
        // for (MenuActionType actionType : MenuActionType.values()) {
        //     actionType.getAction().setEnabled(!processing);
        // }
        // this.searchPanel.setEnabled(!processing);
        // this.entryDetailsTable.setEnabled(!processing);
        // this.statusPanel.setProcessing(processing);
    }

    /**
     * Refresh frame title and Slide list.
     */
    public void refreshAll() {
        this.validate();
        this.repaint();
    }

}
