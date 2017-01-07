import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;


import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



//import javafx.scene.media.me

public class Gui {
    private JTetris2 jT;
    private JFrame frame;
    
    int i = 0;

    //primitives
    private int blockSize = 20;
    private int[] highScoreValue = {0,0,0,0,0,0,0,0};//= new int[8];
    private String[] highScoreName ={"(empty)","(empty)","(empty)","(empty)","(empty)","(empty)","(empty)","(empty)" };//= new String[8];
    private int nextPiceGround;
    private boolean invertBlock;
    private int score;
    private int scorePLus;
    private int randomInvertBlocks;
    private int challengeTime;
    private boolean muteMusic;//
    private boolean muteSoundEff;
    private int volumeMusic;
    private int volumeSoundEff;
    private String skin = "mario";
    private Color inGame = Color.black;
    
    //moovement
    private boolean toRight;
    private boolean toLeft;
    private boolean toBottom;
    private boolean rotateClockW;
    private boolean rotateAnteClockW;
    private boolean pause;
    
    private int toRightKey;
    private int toLeftKey;
    private int toDownKey;
    private int rotateClockWKey;
    private int rotateAnteClockWKey;
    private int pauseKey;
    
    //Game
    private int level=1;
    private boolean campaignGame;
    private boolean continousGame;
    private boolean freeGame;
    private boolean challengeGame;
    
    
    //MainMenu Controls
    private GuiMenuPanel mainMenuPanel;
    //Menu
    private JButton newGameBut;
    private JButton loadGameBut;
    private JButton hallOfFameBut;
    private JButton settingsBut;
    private JButton quitBut;
    //NewGame
    private JButton campaignBut;
    private JButton continousGameBut;
    private JButton challengeBut;
    private JButton freeGameBut;
    private JButton backNewGameBut;
    ////FreeGame
    private JButton startFreeGameBut;
    private JButton backFreeGameBut;
    private final String[] lvlItems = {"Easy Difficulty", "Medium Difficulty", "Hard Difficulty", "Impossible Difficulty"};
    private final String[] linItems = {"20", "25", "30", "35", "40", "45"};
    private final String[] colItems = { "10" ,"15" ,"20", "25", "30", "35", "40", "45", "50"};
    private final String[] strMods = {"Sand Mode", "Clasic Mode"};
    private JComboBox modsCombo;
    private JComboBox difCombo;
    private JComboBox linCombo;
    private JComboBox colCombo;
    private JLabel collinLbl;
    private JCheckBox invertCheck;
    private JCheckBox liteCheck;
    //HallOfFame
    private JButton goOnlineBut;
    private JButton backHallOfFameBut;
    private JLabel fameLbl;
    //Settings
    private JCheckBox musicCheck;
    private JCheckBox soundEffCheck;
    private JSlider volumeMusicSlider;
    private JSlider volumeSoundEffSlider;
    private JButton backSettingsBut;
    private JTabbedPane tabbedPane;
    private BlockPanel[] skinBlockPanel;
    
    //InGameControls
    private GuiPlayGroundPanel guiPlayGround;
    private BlockPanel[][] blockPanel;
    private BlockPanel[][] nextBlockPanel;
    private GuiInGame guiInGame;
    //GuiPlayground
    private JLabel gameOverLabel;
    //GuiInGame
    private JLabel scoreLbl;
    private JLabel scorePlusLbl;
    private JLabel nextPiceLbl;
    private JButton backToMenuBut;
    private JPanel nextPicePanel;
    private JButton resetGame;
    private JButton pauseGame;
    ////Campaign
    private JLabel levelLabel;
    
    //AudioPlayer

    private final File mainMenuAudioFile = new File("sound/mainMenu.mp3");
    private final File challengePianoAudioFile = new File("sound/challengepiano.mp3");
    private final File freeGameAudioFile = new File("sound/freegame.mp3");
    private final File MenuSelectionClickAudioFile = new File("sound/MenuSelectionClick.mp3");
    
    private final Media soundEffMedia = new Media(MenuSelectionClickAudioFile.toURI().toString());
    private Media musicMedia = new Media(mainMenuAudioFile.toURI().toString());
    private MediaPlayer sounfEffPlayer = new MediaPlayer(soundEffMedia);
    private MediaPlayer musicPlayer = new MediaPlayer(musicMedia);
    
    //Threads
    private UpdateGuiPlayGroundThread work;
    private Thread updateGuiPlayGroundThread;
    private Thread scoreIncrementT;
    

    public Gui() {
        
    }
    
    //Static Methods
    private static String SpecialChar(int keyCode){
        String charTxt = Character.toUpperCase(Character.toChars(keyCode)[0])+"";
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
                charTxt = "R.Arrow";
                break;
            case KeyEvent.VK_LEFT:
                charTxt = "L.Arrow";
                break;
            case KeyEvent.VK_UP:
                charTxt = "UpArrow";
                break;
            case KeyEvent.VK_DOWN:
                charTxt = "D.Arrow";
                break;
        }
        return charTxt;
    }
    
    public static void main(String[] args) {
        JFXPanel fxPanel = new JFXPanel();
        Gui gui = new Gui();
        gui.makeMainGui();
    }    

    public void startGame(int diff ,int linii, int col){
        
        pause=false;
        jT = new JTetris2();
        jT.setUpPlayGround(linii, col);
        jT.setGameSpeed(diff);
        jT.setMuteSound(muteSoundEff);
        jT.startGame();
        
        work = new UpdateGuiPlayGroundThread();
        updateGuiPlayGroundThread = new Thread(work);
        updateGuiPlayGroundThread.start();
    }
    public void stopGame(){
        challengeGame=false;        
        campaignGame=false;
        continousGame=false;
        freeGame=false;
        
        score += scorePLus;
        gameOverLabel.setText("Game Over");
        gameOverLabel.setVisible(true);
        updateGuiPlayGroundThread = null;
        
        if(jT!=null){
            jT.stopGame();
            jT = null;
            checkForHighScore();
        }
        
        score =0;
        level=1;
    }
    
    public void makeMainGui(){
        frame = new JFrame("JTetris2");
  
        frame.setDefaultCloseOperation(3);
        frame.setMinimumSize(new Dimension(300,400));
        frame.setResizable(false);
        
        frame.setVisible(true);
        frame.setLayout(null);
        frame.requestFocus();
        
        loadSettings();
        makeMainMenuGui();
        
        Thread t = new Thread(new FtpUploadHighScoreThread());
        t.start();
    }
    public void makeMainMenuGui(){
        playMusic(0);
        
        //MainMenu Controls
        //Menu
        newGameBut = new JButton("New Game");
        loadGameBut = new JButton("Load Game");
        hallOfFameBut = new JButton("Hall of Fame");
        settingsBut = new JButton("Settings");
        quitBut = new JButton("Quit");
        //NewGame
        campaignBut = new JButton("Campaign");
        continousGameBut = new JButton("Continuous");
        challengeBut  = new JButton("Challenge");
        freeGameBut = new JButton("Free Game");
        backNewGameBut = new JButton("Back");
        ////FreeGame
        startFreeGameBut  = new JButton("Launch");
        backFreeGameBut  = new JButton("Back");
        modsCombo = new JComboBox(strMods);
        difCombo = new JComboBox(lvlItems);
        linCombo = new JComboBox(linItems);
        colCombo = new JComboBox(colItems);
        collinLbl = new JLabel("x - y");
        invertCheck = new JCheckBox("Invert");
        liteCheck = new JCheckBox("Lite");
        //HallOfFame
        goOnlineBut = new JButton("Refresh");
        backHallOfFameBut = new JButton("Back");
        fameLbl = new JLabel("<html><pre> HALL OF FAME<br><br><br><br>" 
                                    + highScoreName[0] + "\t\t" + highScoreValue[0] + "<br><br>"
                                    + highScoreName[1] + "\t\t" + highScoreValue[1] + "<br><br>"
                                    + highScoreName[2] + "\t\t" + highScoreValue[2] + "<br><br>"
                                    + highScoreName[3] + "\t\t" + highScoreValue[3] + "</pre></html>", SwingConstants.LEFT);
        //Settingd
        musicCheck = new JCheckBox("Mute Music");// 
        soundEffCheck = new JCheckBox("Mute Sound Effects");
        volumeMusicSlider = new JSlider(0, 100);
        volumeSoundEffSlider = new JSlider(0, 100);
        backSettingsBut = new JButton("Back");
        tabbedPane = new JTabbedPane();
        JPanel musicPanel = new JPanel(null);
        JPanel hotKeyPanel = new JPanel(null);
        JPanel skinPanel = new JPanel(null);
        //HotKeyTab
        JLabel leftLabel = new JLabel("Left:   " + SpecialChar(toLeftKey));
        JLabel rightLabel = new JLabel("Right: " + SpecialChar(toRightKey));
        JLabel bottomLabel = new JLabel("Down: " + SpecialChar(toDownKey));
        JLabel rotateCWLabel = new JLabel("1 Rotate: " + SpecialChar(rotateClockWKey));
        JLabel rotateACWLabel = new JLabel("2 Rotate: " + SpecialChar(rotateAnteClockWKey));
        JLabel pauseLabel = new JLabel("Pause:     " + SpecialChar(pauseKey));
        //SkinTab
        JComboBox blocksSkinCombo = new JComboBox(getSkinValsCombo());
        skinBlockPanel = new BlockPanel[7];
        for(int i=0; i<=6; i++){
            skinBlockPanel[i] = new BlockPanel();
        }

        
        mainMenuPanel = new GuiMenuPanel();
        mainMenuPanel.setLayout(null);
        mainMenuPanel.setLocation(0,0);
        mainMenuPanel.setSize(300, 400);
        
        frame.setMinimumSize(new Dimension(300,400));
        frame.setSize(300, 400);
        frame.add(mainMenuPanel);

        //SetSize
        //Menu
        newGameBut.setSize(130,30);
        loadGameBut.setSize(130,30);
        hallOfFameBut.setSize(130,30);
        settingsBut.setSize(130,30);
        quitBut.setSize(130,30);
        //NewGame
        campaignBut.setSize(130, 30);
        continousGameBut.setSize(130, 30);
        challengeBut.setSize(130,30);
        freeGameBut.setSize(130,30);
        backNewGameBut.setSize(130,30);
        ////FreeGame
        modsCombo.setSize(130,30);
        difCombo.setSize(130,30);
        linCombo.setSize(50,30);
        colCombo.setSize(50,30);
        collinLbl.setSize(50,30);
        invertCheck.setSize(65,30);
        liteCheck.setSize(65,30);
        startFreeGameBut.setSize(130,30);
        backFreeGameBut.setSize(130,30);
        //HallOfFame
        goOnlineBut.setSize(130,30);
        backHallOfFameBut.setSize(130,30);
        fameLbl.setSize(200, 200);
        //Settings
        musicCheck.setSize(130,30);
        soundEffCheck.setSize(150,30);
        volumeMusicSlider.setSize(130, 30);
        volumeSoundEffSlider.setSize(130, 30);
        backSettingsBut.setSize(130, 30);
        tabbedPane.setSize(270, 300);
        leftLabel.setSize(100, 30);
        rightLabel.setSize(100,30);
        bottomLabel.setSize(100, 30);
        rotateCWLabel.setSize(110, 30);
        rotateACWLabel.setSize(110, 30);
        pauseLabel.setSize(110, 30);
        blocksSkinCombo.setSize(135,30);
        for(int i=0; i<=6; i++){
            skinBlockPanel[i].setSize(blockSize, blockSize);
        }
        //SetLocation
        //Menu
        newGameBut.setLocation(50, 50);
        loadGameBut.setLocation(50, 80);
        hallOfFameBut.setLocation(50, 110);
        settingsBut.setLocation(50, 140);
        quitBut.setLocation(50, 170);
        //NewGame
        campaignBut.setLocation(50, 50);
        continousGameBut.setLocation(50, 80);
        challengeBut.setLocation(50,110);
        freeGameBut.setLocation(50, 140);
        backNewGameBut.setLocation(50, 320);
        ////FreeGame
        modsCombo.setLocation(50, 50);
        difCombo.setLocation(50, 80);
        linCombo.setLocation(50, 110);
        colCombo.setLocation(130, 110);
        collinLbl.setLocation(103, 110);
        invertCheck.setLocation(50, 140);
        liteCheck.setLocation(130, 140);
        startFreeGameBut.setLocation(50, 290);
        backFreeGameBut.setLocation(50, 320);
        //HallOfFame
        goOnlineBut.setLocation(50, 290);
        backHallOfFameBut.setLocation(50, 320);
        fameLbl.setLocation(50, 50);
        //Settings
        musicCheck.setLocation(40, 50);
        volumeMusicSlider.setLocation(40, 80);
        soundEffCheck.setLocation(40, 110);
        volumeSoundEffSlider.setLocation(40, 140);
        backSettingsBut.setLocation(50, 320);
        tabbedPane.setLocation(10, 10);
        leftLabel.setLocation(20, 50);
        rightLabel.setLocation(20, 80);
        bottomLabel.setLocation(20, 110);
        rotateCWLabel.setLocation(130, 50);
        rotateACWLabel.setLocation(130, 80);       
        pauseLabel.setLocation(130, 110);
        blocksSkinCombo.setLocation(40, 50);
        skinBlockPanel[0].setLocation(40, 85);
        for(int i=1; i<=6; i++){
            skinBlockPanel[i].setLocation(skinBlockPanel[i-1].getX()+20, 85);
        }
        //SetVisible
        //NewGame
        campaignBut.setVisible(false);
        continousGameBut.setVisible(false);
        challengeBut.setVisible(false);
        freeGameBut.setVisible(false);
        backNewGameBut.setVisible(false);
        ////FreeGame
        startFreeGameBut.setVisible(false);
        modsCombo.setVisible(false);
        difCombo.setVisible(false);
        linCombo.setVisible(false);
        collinLbl.setVisible(false);
        colCombo.setVisible(false);
        invertCheck.setVisible(false);
        liteCheck.setVisible(false);
        backFreeGameBut.setVisible(false);
        //HallOfFame
        goOnlineBut.setVisible(false);
        backHallOfFameBut.setVisible(false);
        fameLbl.setVisible(false);
        //Settings
        soundEffCheck.setVisible(false);
        musicCheck.setVisible(false);
        volumeMusicSlider.setVisible(false);
        volumeSoundEffSlider.setVisible(false);
        backSettingsBut.setVisible(false);
        tabbedPane.setVisible(false);
//        leftLabel.setVisible(false);
//        rightLabel.setVisible(false);
//        bottomLabel.setVisible(false);
//        rotateCWLabel.setVisible(false);
//        rotateACWLabel.setVisible(false);
//        pauseLabel.setVisible(false);
            
        //ActionListener
        //MainMenu
        newGameBut.addActionListener(new NewGameListener(true));
        backNewGameBut.addActionListener(new NewGameListener(false));
        quitBut.addActionListener(new QuitListener());
        //NewGame
        ////Campaign
        campaignBut.addActionListener(new CampaignListener());
        ////Continous
        continousGameBut.addActionListener(new ContinousListener());
        ////Challenge
        challengeBut.addActionListener(new ChallengeListener());
        ////FreeGame
        freeGameBut.addActionListener(new FreeGameListener(true));
        startFreeGameBut.addActionListener(new FreeGameLaunchlistener());
        //modsCombo
        //difCombo
        //linCombo
        //colCombo
        backFreeGameBut.addActionListener(new FreeGameListener(false));
        //HallOfFame
        hallOfFameBut.addActionListener(new HallOfFameListener(true));
        goOnlineBut.addActionListener(new GoOnlineListener());
        backHallOfFameBut.addActionListener(new HallOfFameListener(false));
        //Settings
        settingsBut.addActionListener(new SettingsListener(true));
        backSettingsBut.addActionListener(new SettingsListener(false));
        ////Music
        musicCheck.addItemListener(new MusicCheckListener());
        soundEffCheck.addItemListener(new SoundEffCheckListener());
        volumeMusicSlider.addChangeListener(new MusicVolumeListener());
        volumeSoundEffSlider.addChangeListener(new SoundEffVolumeListener());
        ////HotKey
        //////HetKey
        leftLabel.addFocusListener(new SetKeyFocus(leftLabel));
        rightLabel.addFocusListener(new SetKeyFocus(rightLabel));
        bottomLabel.addFocusListener(new SetKeyFocus(bottomLabel));
        rotateACWLabel.addFocusListener(new SetKeyFocus(rotateACWLabel));
        rotateCWLabel.addFocusListener(new SetKeyFocus(rotateCWLabel));
        pauseLabel.addFocusListener(new SetKeyFocus(pauseLabel));
        //////GetKeyFocus
        leftLabel.addMouseListener(new GetKeyFocus(leftLabel));
        rightLabel.addMouseListener(new GetKeyFocus(rightLabel));
        bottomLabel.addMouseListener(new GetKeyFocus(bottomLabel));
        rotateACWLabel.addMouseListener(new GetKeyFocus(rotateACWLabel));
        rotateCWLabel.addMouseListener(new GetKeyFocus(rotateCWLabel));
        pauseLabel.addMouseListener(new GetKeyFocus(pauseLabel));
        ////Skin
        blocksSkinCombo.addItemListener(new SkinComboListener(blocksSkinCombo));
        
        //MaineMenu
        mainMenuPanel.add(newGameBut);
        mainMenuPanel.add(loadGameBut);
        mainMenuPanel.add(hallOfFameBut);
        mainMenuPanel.add(settingsBut);
        mainMenuPanel.add(quitBut);
        //NewGame
        mainMenuPanel.add(campaignBut);
        mainMenuPanel.add(continousGameBut);
        mainMenuPanel.add(challengeBut);
        mainMenuPanel.add(freeGameBut);
        mainMenuPanel.add(backNewGameBut);
        ////FreeGame
        mainMenuPanel.add(startFreeGameBut);
        mainMenuPanel.add(modsCombo);
        mainMenuPanel.add(difCombo);
        mainMenuPanel.add(linCombo);
        mainMenuPanel.add(collinLbl);
        mainMenuPanel.add(colCombo);
        mainMenuPanel.add(invertCheck);
        mainMenuPanel.add(liteCheck);
        mainMenuPanel.add(backFreeGameBut);
        //HallOfFame
        mainMenuPanel.add(backHallOfFameBut);
        mainMenuPanel.add(goOnlineBut);
        mainMenuPanel.add(fameLbl);
        //Settings
        musicPanel.add(soundEffCheck);
        musicPanel.add(musicCheck);
        musicPanel.add(volumeMusicSlider);
        musicPanel.add(volumeSoundEffSlider);
        hotKeyPanel.add(leftLabel);
        hotKeyPanel.add(rightLabel);
        hotKeyPanel.add(bottomLabel);
        hotKeyPanel.add(rotateCWLabel);
        hotKeyPanel.add(rotateACWLabel);
        hotKeyPanel.add(pauseLabel);
        skinPanel.add(blocksSkinCombo);
        for(int i=0; i<=6; i++){
            skinPanel.add(skinBlockPanel[i]);
        }
        mainMenuPanel.add(backSettingsBut);
        mainMenuPanel.add(tabbedPane);
        tabbedPane.addTab("Sound", musicPanel);
        tabbedPane.addTab("Input", hotKeyPanel);
        tabbedPane.addTab("Skin", skinPanel);


        
        //setUp Components
        //NewGame
        ////FreeGame
        linCombo.setSelectedIndex(2);
        difCombo.setSelectedIndex(1);
        modsCombo.setSelectedIndex(1);
        invertCheck.setOpaque(false);
        liteCheck.setOpaque(false);
        musicCheck.setOpaque(false);
        volumeMusicSlider.setOpaque(false);
        volumeSoundEffSlider.setOpaque(false);
        soundEffCheck.setOpaque(false);
        //Settings
        musicCheck.setSelected(muteMusic);
        soundEffCheck.setSelected(muteSoundEff);
        volumeMusicSlider.setValue(volumeMusic);
        volumeSoundEffSlider.setValue(volumeSoundEff);
        musicPanel.setOpaque(false);
        hotKeyPanel.setOpaque(false);
        skinPanel.setOpaque(false);
        tabbedPane.setOpaque(false);
        blocksSkinCombo.setSelectedItem((String)skin);
//        leftLabel.setOpaque(true);
//        rightLabel.setOpaque(true);
//        bottomLabel.setOpaque(true);
//        rotateCWLabel.setOpaque(true);
//        rotateACWLabel.setOpaque(true);
//        pauseLabel.setOpaque(true);

        //Disabled features
        loadGameBut.setEnabled(false);
//        soundEffCheck.setEnabled(false);
//        settingsBut.setEnabled(false);
        //challengeBut.setEnabled(false);
        modsCombo.setEnabled(false);
        
        //Back To Main Menu
        frame.repaint();
        mainMenuPanel.requestFocus();
        if(guiPlayGround!=null){
            frame.remove(guiPlayGround);
            frame.remove(guiInGame);
            guiPlayGround = null;
            guiInGame = null;              
        }

    }
    public void makeInGameGui(int linii, int col){
        scoreLbl = new JLabel("Score : 0");
        scorePlusLbl = new JLabel("");
        nextPiceLbl = new JLabel("Next : ");
        pauseGame = new JButton("Pause");
        resetGame = new JButton("Restart");
        backToMenuBut  = new JButton("Menu");
        nextPicePanel = new JPanel(null);
        nextBlockPanel = new BlockPanel[4][2];
        gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);

        setUpNextPicePanel();
        
        guiPlayGround = new GuiPlayGroundPanel();
        guiPlayGround.setLayout(null);
        guiPlayGround.setBackground(Color.red);
        guiPlayGround.setLocation(-blockSize, -(blockSize * 8));
        //guiPlayGround.setSize(jT.getPlayGround()[0].length * blockSize, jT.getPlayGround().length * blockSize);
        guiPlayGround.setSize(col * blockSize + blockSize, linii * blockSize);
        
        gameOverLabel.setVisible(false);
        gameOverLabel.setSize(150,30);
        gameOverLabel.setFont(new Font("Arial", 1, 25));
        gameOverLabel.setForeground(new Color((int) (Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255)));
        gameOverLabel.setLocation((guiPlayGround.getWidth()/2) - (gameOverLabel.getWidth()/2), blockSize*12);
        gameOverLabel.setOpaque(false);
        guiPlayGround.add(gameOverLabel);
        
        guiInGame = new GuiInGame();
        guiInGame.setLayout(null);
        guiInGame.setLocation((col * blockSize)  , 0);//2*- blockSize
        guiInGame.setSize(100, guiPlayGround.getHeight()-(blockSize * 7));          
        
        //SetLocation
        scoreLbl.setLocation(10,10);
        scorePlusLbl.setLocation(10,20);
        nextPiceLbl.setLocation(10, 50);
        nextPicePanel.setLocation(10,70);
        pauseGame.setLocation(10, (guiPlayGround.getHeight()-(blockSize * 7))-100);
        resetGame.setLocation(10, (guiPlayGround.getHeight()-(blockSize * 7))-80);
        backToMenuBut.setLocation(10, (guiPlayGround.getHeight()-(blockSize * 7))-60);
        
        //setSize
        scoreLbl.setSize(80, 20);
        scorePlusLbl.setSize(80, 20);
        nextPiceLbl.setSize(80, 20);
        pauseGame.setSize(80,20);
        resetGame.setSize(80,20);
        backToMenuBut.setSize(80,20);
        nextPicePanel.setSize(40, 80);

        frame.setMinimumSize(new Dimension(guiPlayGround.getWidth() + guiInGame.getWidth() - blockSize, guiPlayGround.getHeight()-(blockSize * 7)));
        frame.setSize(guiPlayGround.getWidth() + guiInGame.getWidth() - blockSize, guiPlayGround.getHeight()-(blockSize * 7));//2*
        frame.getContentPane().add(guiInGame);
        frame.getContentPane().add(guiPlayGround);
        guiInGame.add(scoreLbl);
        guiInGame.add(nextPicePanel);
        guiInGame.add(scorePlusLbl);
        guiInGame.add(nextPiceLbl);
        guiInGame.add(pauseGame);
        guiInGame.add(resetGame);
        guiInGame.add(backToMenuBut);
        
        
        
        backToMenuBut.addActionListener(new BackToMenuListener());
        frame.addKeyListener(new FrameKeyListener());
        guiPlayGround.addKeyListener(new FrameKeyListener());
        guiInGame.addKeyListener(new FrameKeyListener());
        pauseGame.addActionListener(new PauseListener());
        pauseGame.addKeyListener(new FrameKeyListener());
        resetGame.addActionListener(new ResetListener());
        resetGame.addKeyListener(new FrameKeyListener());
        backToMenuBut.addKeyListener(new FrameKeyListener());
        
        
        nextPicePanel.setOpaque(false);
        guiPlayGround.requestFocus();
        frame.remove(mainMenuPanel);
        mainMenuPanel = null;      
        
        
    }
    public void makeInCampaignGameGui(){
        campaignGame = true;
        makeInGameGui(30, 10);
        resetGame.setVisible(false);
        pauseGame.setLocation(10, (guiPlayGround.getHeight()-(blockSize * 7))-80);
        
        levelLabel = new JLabel("Level : ");
        levelLabel.setLocation(10, (guiPlayGround.getHeight()-(blockSize * 7))-130);
        levelLabel.setSize(80, 20);
        guiInGame.add(levelLabel);
        
        
    }
    public void makeInContinousGameGui(){
        continousGame=true;
        makeInGameGui(30,10);

    }
    public void makeInChallengeGameGui(){
            challengeGame= true;
            makeInGameGui(30, 10);
            pauseGame.setVisible(false);
            resetGame.setVisible(false);
    }
    public void makeInFreeGameGui(int linii, int col){
        freeGame=true;
        invertBlock = invertCheck.isSelected();
        makeInGameGui(linii, col);
        
        scoreLbl.setVisible(false);
        
        nextPiceLbl.setLocation(10, 10);
        nextPicePanel.setLocation(10, 30);
        
        //Lite version
        if(liteCheck.isSelected()){
            backToMenuBut  = new JButton("Menu");
            backToMenuBut.setSize(80,20);
            backToMenuBut.setLocation(20,(blockSize * 8));
            guiPlayGround.add(backToMenuBut);
            

            
            backToMenuBut.addActionListener(new BackToMenuListener());
            backToMenuBut.addKeyListener(new FrameKeyListener());

            guiInGame.setVisible(false);
            frame.setMinimumSize(new Dimension(guiPlayGround.getWidth() - 2*blockSize, guiPlayGround.getHeight()-(blockSize * 7)));
            frame.setSize(guiPlayGround.getWidth() - 2*blockSize, guiPlayGround.getHeight()-(blockSize * 7));         
        }
    }
    

    private void playMusic(int index){//boolean play,
        if(muteMusic){
            if(index==0 || index==1 || index==2){
                musicPlayer.dispose();
                return;
            }
        }
        musicPlayer.dispose();

        switch(index){
            case 0:
//                if(play){
                    musicMedia = new Media(mainMenuAudioFile.toURI().toString());
                    musicPlayer = new MediaPlayer(musicMedia);
                    musicPlayer.setVolume((double)(volumeMusic/100.0));
                    musicPlayer.play();
//                }else{
//                    musicPlayer.stop();
//                }
            break;
            case 1:
//                if(play){
                    musicMedia = new Media(freeGameAudioFile.toURI().toString());
                    musicPlayer = new MediaPlayer(musicMedia);
                    musicPlayer.setVolume((double)(volumeMusic/100.0));
                    musicPlayer.play();                    
                    
//                }else{
//                    musicPlayer.stop();
//                }                
            break;
            case 2:
//                if(play){
                    musicMedia = new Media(challengePianoAudioFile.toURI().toString());
                    musicPlayer = new MediaPlayer(musicMedia);
                    musicPlayer.setVolume((double)(volumeMusic/100.0));
                    musicPlayer.play();
//                }else{
//                    musicPlayer.stop();
//                }
            break;
//            case 3:
//                if(play){
//                    System.out.println(sounfEffPlayer);
//                    sounfEffPlayer.setVolume((double)(volumeSoundEff/100.0));
//                    sounfEffPlayer.play();
//                }else{
//                    sounfEffPlayer.stop();
//                }
//                break;
        }
    }
    private void playSoundEff(){
        if(muteSoundEff){
            return;
        }
        sounfEffPlayer.dispose();
        sounfEffPlayer = new MediaPlayer(soundEffMedia);
        sounfEffPlayer.setVolume((double) (volumeSoundEff/100.0));
        sounfEffPlayer.play();
    }
    
    private void openMainMenu(){
        newGameBut.setVisible(true);
        loadGameBut.setVisible(true);
        hallOfFameBut.setVisible(true);
        settingsBut.setVisible(true);
        quitBut.setVisible(true); 

        //newgame
        campaignBut.setVisible(false);
        continousGameBut.setVisible(false);
        challengeBut.setVisible(false);
        freeGameBut.setVisible(false);
        backNewGameBut.setVisible(false);
        ////FreeGame
        startFreeGameBut.setVisible(false);
        modsCombo.setVisible(false);
        difCombo.setVisible(false);
        linCombo.setVisible(false);
        collinLbl.setVisible(false);
        colCombo.setVisible(false);
        backFreeGameBut.setVisible(false);   
        invertCheck.setVisible(false);
        liteCheck.setVisible(false);
        //HallOfFame
        goOnlineBut.setVisible(false);
        backHallOfFameBut.setVisible(false);
        fameLbl.setVisible(false);
        //Settings
        soundEffCheck.setVisible(false);
        musicCheck.setVisible(false);
        volumeMusicSlider.setVisible(false);
        backSettingsBut.setVisible(false);
        tabbedPane.setVisible(false);
        
    } 
    private void openNewGame(boolean open){
        campaignBut.setVisible(open);
        continousGameBut.setVisible(open);
        challengeBut.setVisible(open);
        freeGameBut.setVisible(open);
        backNewGameBut.setVisible(open);

        newGameBut.setVisible(!open);
        loadGameBut.setVisible(!open);
        hallOfFameBut.setVisible(!open);
        settingsBut.setVisible(!open);
        quitBut.setVisible(!open);        
    }
    private void openFreeGame(boolean open){
        startFreeGameBut.setVisible(open);
        modsCombo.setVisible(open);
        difCombo.setVisible(open);
        linCombo.setVisible(open);
        collinLbl.setVisible(open);
        colCombo.setVisible(open);
        invertCheck.setVisible(open);
        liteCheck.setVisible(open);
        backFreeGameBut.setVisible(open);
        
        campaignBut.setVisible(!open);
        continousGameBut.setVisible(!open);
        challengeBut.setVisible(!open);
        freeGameBut.setVisible(!open);
        backNewGameBut.setVisible(!open);        
        
    }
    private void openHallOfFame(boolean open){
            goOnlineBut.setVisible(open);
            backHallOfFameBut.setVisible(open);
            fameLbl.setVisible(open);

            newGameBut.setVisible(!open);
            loadGameBut.setVisible(!open);
            hallOfFameBut.setVisible(!open);
            settingsBut.setVisible(!open);
            quitBut.setVisible(!open);
    }
    private void openSettings(boolean open){
            soundEffCheck.setVisible(open);
            musicCheck.setVisible(open);
            volumeMusicSlider.setVisible(open);
            volumeSoundEffSlider.setVisible(open);
            backSettingsBut.setVisible(open);
            tabbedPane.setVisible(open);

            newGameBut.setVisible(!open);
            loadGameBut.setVisible(!open);
            hallOfFameBut.setVisible(!open);
            settingsBut.setVisible(!open);
            quitBut.setVisible(!open);        
    }
    
    private void updateConsolePlayG(int[][] playG){//int[][] playG
            for(int i=0; i<=playG.length-1; i++){
                System.out.print("\n");
                for(int j=0; j<=playG[0].length-1; j++){
                    System.out.print(playG[i][j] + "\t");
                }
            } 
    }
    private int getDifficultyCombo(){
        int ret = 250;
        switch(difCombo.getSelectedIndex()){
        case 0:
            ret = 750;
            break;
        case 1:
            ret = 250;
            break;
        case 2:
            ret = 150;
            break;
        case 3:
            ret = 100;
            break;
        }
        return ret;
    }
    private String[] getSkinValsCombo(){
        ArrayList<String> dirList = new ArrayList();
        
        String[] x;
        
        File fil = new File(System.getProperty("user.dir") + "\\img\\blocks");
        for(File cont : fil.listFiles()){
            if(cont.isDirectory()){
                dirList.add(cont.getName());
            }
        }
        x =  dirList.toArray(new String[0]);
        return x;
    }
    private void checkForHighScore(){
//        boolean isHighScore = false;
//        int highScoreIndex=0;
        String name;
        
        for(int i=0; i<=3; i++){
            if(score>=highScoreValue[i]){
                name = JOptionPane.showInputDialog(frame, "New High Score!\nYou got " + (i+1) 
                                            + "-th place in Hall of Fame with " + score 
                                            + " points.\n(previously held by " + highScoreName[i] 
                                            + " with " + highScoreValue[i] + " points.)\n\n\nPlease enter your name.", "Hall Of Fame", 3);
                if(name!=null){
                    for(int j=2; j>=i; j--){
                        highScoreName[j+1] = highScoreName[j];
                        highScoreValue[j+1] = highScoreValue[j];                        
                    }
                    highScoreName[i] = name;
                    highScoreValue[i] = score;
//                    int ans = JOptionPane.showConfirmDialog(frame, "Do you want to synnc with online game server (you can do this at any time later in the Hall Of Fame menu)", "Sync Now.", 0);
//                    if(ans==0){
                        System.out.println("got online");
                        Thread t = new Thread(new FtpUploadHighScoreThread());
                        t.start();                    
//                    }

                }                
                break;
            }
        }
        
    }
    private int blocksRange(int index){
        if(index==0){return -1;}
        else if(index>0 && index<999){return 0;}
        else if(index>999 && index<1999){return 1;}
        else if(index>1999 && index<2999){return 2;}
        else if(index>2999 && index<3999){return 3;}
        else if(index>3999 && index<4999){return 4;}
        else if(index>4999 && index<5999){return 5;}
        else if(index>5999 && index<6999){return 6;}
        else{return 7;} /// nu exista
    }
    private void setUpGuiPlayGround(int [][] playG){
        blockPanel = new BlockPanel[playG.length][playG[0].length];

        for(int x=0; x<=blockPanel.length-1; x++){
            for(int y=0; y<=blockPanel[0].length-1; y++){
                blockPanel[x][y] = new BlockPanel();
                //blockPanel[x][y].setBackground(Color.red);
                blockPanel[x][y].setVisible(false);
                blockPanel[x][y].setLocation(setPlayGroundCoord(y, x));
                blockPanel[x][y].setSize(blockSize, blockSize);
                blockPanel[x][y].setLayout(null);
                guiPlayGround.add(blockPanel[x][y]);                
            }
        }
    }
    private void setUpNextPicePanel(){

        for(int x=0; x<=3; x++){
            for(int y=0; y<=1; y++){
                nextBlockPanel[x][y] = new BlockPanel();
                nextBlockPanel[x][y].setVisible(false);
                nextBlockPanel[x][y].setLocation(new Point(y * 20, x * 20));
                nextBlockPanel[x][y].setSize(20, 20);
                nextBlockPanel[x][y].setLayout(null);
                nextPicePanel.add(nextBlockPanel[x][y]);                
            }
        }        
    }
    private void updateNextPicePanel(int nextPice){
//        System.out.println(nextPice);
        if(nextPiceGround != nextPice){
            nextPiceGround = nextPice;
            
            //generate a random background blocks for invert option
            randomInvertBlocks = (int)(Math.random() * 7);
            
            for(int x=0; x<=3; x++){
                for(int y=0; y<=1; y++){
                    nextBlockPanel[x][y].setVisible(false);
                }
            }


            switch(nextPice){
            case 0:
                    nextBlockPanel[0][1].setPaint(0, skin);
                    nextBlockPanel[0][1].setVisible(true);
                    nextBlockPanel[1][1].setPaint(0, skin);
                    nextBlockPanel[1][1].setVisible(true);
                    nextBlockPanel[2][1].setPaint(0, skin);
                    nextBlockPanel[2][1].setVisible(true);  
                    nextBlockPanel[3][1].setPaint(0, skin);
                    nextBlockPanel[3][1].setVisible(true);
                break;
            case 1:
                    nextBlockPanel[0][1].setPaint(1, skin);
                    nextBlockPanel[0][1].setVisible(true);
                    nextBlockPanel[1][1].setPaint(1, skin);
                    nextBlockPanel[1][1].setVisible(true);
                    nextBlockPanel[2][1].setPaint(1, skin);
                    nextBlockPanel[2][1].setVisible(true);  
                    nextBlockPanel[2][0].setPaint(1, skin);
                    nextBlockPanel[2][0].setVisible(true);
                break;
            case 2:
                    nextBlockPanel[0][0].setPaint(2, skin);
                    nextBlockPanel[0][0].setVisible(true);
                    nextBlockPanel[1][0].setPaint(2, skin);
                    nextBlockPanel[1][0].setVisible(true);
                    nextBlockPanel[2][0].setPaint(2, skin);
                    nextBlockPanel[2][0].setVisible(true);  
                    nextBlockPanel[2][1].setPaint(2, skin);
                    nextBlockPanel[2][1].setVisible(true);
                break;
            case 3:
                    nextBlockPanel[0][0].setPaint(3, skin);
                    nextBlockPanel[0][0].setVisible(true);
                    nextBlockPanel[0][1].setPaint(3, skin);
                    nextBlockPanel[0][1].setVisible(true);
                    nextBlockPanel[1][0].setPaint(3, skin);
                    nextBlockPanel[1][0].setVisible(true);  
                    nextBlockPanel[1][1].setPaint(3, skin);
                    nextBlockPanel[1][1].setVisible(true);
                break;
            case 4:
                    nextBlockPanel[0][0].setPaint(4, skin);
                    nextBlockPanel[0][0].setVisible(true);
                    nextBlockPanel[1][0].setPaint(4, skin);
                    nextBlockPanel[1][0].setVisible(true);
                    nextBlockPanel[1][1].setPaint(4, skin);
                    nextBlockPanel[1][1].setVisible(true);  
                    nextBlockPanel[2][1].setPaint(4, skin);
                    nextBlockPanel[2][1].setVisible(true);
                break;
            case 5:
                    nextBlockPanel[0][1].setPaint(5, skin);
                    nextBlockPanel[0][1].setVisible(true);
                    nextBlockPanel[1][1].setPaint(5, skin);
                    nextBlockPanel[1][1].setVisible(true);
                    nextBlockPanel[2][1].setPaint(5, skin);
                    nextBlockPanel[2][1].setVisible(true);  
                    nextBlockPanel[1][0].setPaint(5, skin);
                    nextBlockPanel[1][0].setVisible(true);
                break;
            case 6:
                    nextBlockPanel[0][1].setPaint(6, skin);
                    nextBlockPanel[0][1].setVisible(true);
                    nextBlockPanel[1][1].setPaint(6, skin);
                    nextBlockPanel[1][1].setVisible(true);
                    nextBlockPanel[1][0].setPaint(6, skin);
                    nextBlockPanel[1][0].setVisible(true);  
                    nextBlockPanel[2][0].setPaint(6, skin);
                    nextBlockPanel[2][0].setVisible(true);
                break;
            }        
        }
    }
    private void updateGuiPlayGround(int [][] playG){
            for(int x=0; x<=playG.length-1; x++){
                for(int y=0; y<=playG[0].length-1; y++){
                    int index = playG[x][y];
                    
                    switch(blocksRange(index)){
                        case -1:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(randomInvertBlocks, skin);
                                blockPanel[x][y].setVisible(true);   
                            }else{
                                blockPanel[x][y].setPaint(5, skin);
                                blockPanel[x][y].setVisible(false);                                
                            }
                           
                            break;
                        case 0:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(0, skin);
                                blockPanel[x][y].setVisible(false);                                
                            }else{
                                blockPanel[x][y].setPaint(0, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 1:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(1, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(1, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 2:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(2, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(2, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 3:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(3, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(3, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 4:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(4, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(4, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 5:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(5, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(5, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                        case 6:
                            if(invertBlock){
                                blockPanel[x][y].setPaint(6, skin);
                                blockPanel[x][y].setVisible(false);                                   
                            }else{
                                blockPanel[x][y].setPaint(6, skin);
                                blockPanel[x][y].setVisible(true);                                
                            }
                            break;
                    }
                }   
            }
            frame.repaint();            
            
    }
    private void resetGuiPlayGround(int [][] playG){
        for(int x=0; x<=playG.length-1; x++){
            for(int y=0; y<=playG[0].length-1; y++){
                if(invertBlock){
                    blockPanel[x][y].setPaint(randomInvertBlocks, skin);
                    blockPanel[x][y].setVisible(true);   
                }else{
                    blockPanel[x][y].setPaint(0, skin);
                    blockPanel[x][y].setVisible(false);                                
                }
            } 
        }
    }
    public Point setPlayGroundCoord(int x, int y){
        Point pn = new Point(0, 0);
        pn.setLocation(blockSize*x, blockSize*y);
        return pn;
    }
    public Point getPlayGroundCoord(JPanel jP){
        Point pn = new Point(0, 0);
        if(jP != null){
            pn.setLocation((jP.getLocation().x)/blockSize, (jP.getLocation().y)/blockSize);
        }
        return pn;
    }    
  
    private void saveSettings(){
        File fil = new File(System.getProperty("user.dir") + "\\config.dat");
        FileWriter filWrite;
        int intMuteMusi;
        int intMuteSoundOff;
        if(muteMusic){intMuteMusi = 1;}else{intMuteMusi=0;}
        if(muteSoundEff){intMuteSoundOff=1;}else{intMuteSoundOff=0;}
        
        try {
            filWrite = new FileWriter(fil);
            filWrite.write(intMuteMusi + "<%>" + intMuteSoundOff + "<%>" + volumeMusic + "<%>" + volumeSoundEff +
                                        "<%>" + toLeftKey + "<%>" + toRightKey + "<%>" + toDownKey + 
                                        "<%>" + rotateClockWKey + "<%>" + rotateAnteClockWKey + "<%>" + pauseKey +
                                        "<%>" + skin);
            filWrite.close();
        } catch (IOException ex) {
            System.out.println("saveSettings writeFile IOException : " + ex.getMessage());
        } 
    }
    private void loadSettings(){
            FileReader filRead;
            File fil = new File(System.getProperty("user.dir") + "\\config.dat");
            String filData = null;
            try {
                filRead = new FileReader(fil);
                BufferedReader reader = new BufferedReader(filRead);
                filData=reader.readLine();
                reader.close();
                
                String[] filDataParse = filData.split("<%>");
                muteMusic = filDataParse[0].equals("1");
                muteSoundEff = filDataParse[1].equals("1");
                
                volumeMusic = Integer.parseInt(filDataParse[2]);
                volumeSoundEff = Integer.parseInt(filDataParse[3]);
                
                toLeftKey = Integer.parseInt(filDataParse[4]);
                toRightKey = Integer.parseInt(filDataParse[5]);
                toDownKey = Integer.parseInt(filDataParse[6]);
                rotateClockWKey = Integer.parseInt(filDataParse[7]);
                rotateAnteClockWKey = Integer.parseInt(filDataParse[8]);
                pauseKey = Integer.parseInt(filDataParse[9]);
                skin = filDataParse[10];
                
                
            } catch (FileNotFoundException ex) {
                System.out.println("loadSettings ReadFile FileNotFoundException : " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("loadSettings ReadFile IOException : " + ex.getMessage());
            }
    }

    public void campaignLevels(){
        if(!muteSoundEff){
            File fil = new File(System.getProperty("user.dir") + "\\sound\\levelUp.mp3");
            Media med = new Media(fil.toURI().toString());
            MediaPlayer player = new MediaPlayer(med);
            player.play();            
        }

        jT.resetGame();
        
        guiPlayGround.setPaint((int)(Math.random()*29) + 1);
        
        if(level==2){
            jT.setGameSpeed(400);
            invertBlock=false;
        }
        if(level==3){
            jT.setGameSpeed(350);
            invertBlock=true;
        }
        if(level==4){
            jT.setGameSpeed(250);
            invertBlock=true;
        }
        if(level==5){
            jT.setGameSpeed(200);
            invertBlock=false;
        }
        if(level==6){
            jT.setGameSpeed(150);
            invertBlock=false;
        }        
        if(level==7){
            jT.setGameSpeed(100);
            invertBlock=false;
        }
        if(level==8){
            jT.setGameSpeed(100);
            invertBlock=true;
        }
        if(level==9){
            jT.setGameSpeed(100);
            invertBlock=false;
        }
        if(level==10){
            jT.setGameSpeed(100);
            invertBlock=true;
        }
        if(level==11){
            jT.setGameSpeed(50);
            invertBlock=false;
        }            

    }
    
    private void scoreIncrement(){
        
        scorePLus += jT.getGamePlusScore();
        if(scorePLus>0 && scoreIncrementT==null){
            Runnable job = new ScoreIncrement();
            scoreIncrementT = new Thread(job);
            scoreIncrementT.start();
        }
    }
    
    //Nested Class extends Runnable
    class UpdateGuiPlayGroundThread implements Runnable{

        @Override
        public void run() {
            setUpGuiPlayGround(jT.getPlayGround());
            
            while(jT!=null){
//                updateConsolePlayG(jT.getPlayGround());
                updateGuiPlayGround(jT.getPlayGround());
                updateNextPicePanel(jT.getNextPiece());

                if(challengeGame){
                    if(pause){pause=false;}
                    if(toBottom){toBottom=false;}
                }
                
                if(freeGame){}
                if(continousGame){
                    scoreIncrement();
                    jT.setGameSpeed(500-(score/2));
                }
                
                if(campaignGame){
                    levelLabel.setText("Level : " + level);
                    scoreIncrement();
                    if(score>=100 && level<2){level=2;campaignLevels();}
                    if(score>=200 && level<3){level=3;campaignLevels();}
                    if(score>=300 && level<4){level=4;campaignLevels();}
                    if(score>=400 && level<5){level=5;campaignLevels();}
                    if(score>=500 && level<6){level=6;campaignLevels();}
                    if(score>=600 && level<7){level=7;campaignLevels();}
                    if(score>=700 && level<8){level=8;campaignLevels();}
                    if(score>=800 && level<9){level=9;campaignLevels();}
                    if(score>=900 && level<10){level=10;campaignLevels();}
                    if(score>=1000 && level<11){level=11;campaignLevels();}
                }
                
                
                jT.setGamePaused(pause);
                if(jT.isGamePaused()){
                    pauseGame.setText("Resume");
                    gameOverLabel.setText("Pause");
                    gameOverLabel.setForeground(new Color((int) (Math.random()*255),(int) (Math.random()*255),(int) (Math.random()*255)));
                    gameOverLabel.setVisible(true);
                }else{
                    //Movement
                    if(toLeft){jT.toLeft();toLeft=false;}//
                    if(toRight){jT.toRight();toRight=false;}//
                    if(toBottom){jT.toBottom();toBottom=false;}//
                    if(rotateClockW){jT.rotateClockWise(true);rotateClockW=false;}//
                    if(rotateAnteClockW){jT.rotateClockWise(false);rotateAnteClockW=false;}//
                    //PauseLable
                    pauseGame.setText("Pause");
                    gameOverLabel.setVisible(false);
                }
                if(jT==null || jT.isGameOver()){
                    stopGame();
                    break;
                }

                

                
                try {
                    Thread.sleep(50);//50
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    class ScoreIncrement implements Runnable {
        @Override
        public void run() {
            //int gPSTemp = gameScore;
            scorePlusLbl.setVisible(true);
            while(scorePLus>=1){
                
                scoreLbl.setText("Score : " + score++);
                scorePlusLbl.setText("                 +" + scorePLus--);

                
                if(scorePlusLbl.getForeground()== Color.black){
                    scorePlusLbl.setForeground(Color.red);
                }else{
                    scorePlusLbl.setForeground(Color.black);
                }
                if (scorePLus <= 2){scorePlusLbl.setVisible(false);}
                
                int sleepTime = 100;
                if( (scorePLus) > 0 ){
                    sleepTime=130;
                    if( (scorePLus) > 3 ){
                        sleepTime=80;
                        if( (scorePLus) > 10 ){
                            sleepTime=50;
                            if( (scorePLus) > 50 ){
                                sleepTime=20;
                            }                            
                        }
                    }
                }
                

                
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            scoreIncrementT = null;
        }
    }
    class ChallengeThread implements Runnable{
        @Override
        public void run() {
            challengeTime =0;
            while(jT!=null){
                challengeTime++;
                
                if(challengeTime==10){jT.setGameSpeed(100);}
                if(challengeTime==30){jT.setGameSpeed(250);}
                if(challengeTime==70){jT.setGameSpeed(100);}
                if(challengeTime==90){jT.setGameSpeed(750);}
                if(challengeTime==178){jT.setGameSpeed(50);}
                if(challengeTime==212){jT.setGameSpeed(250);}
                if(challengeTime==365){jT.setGameSpeed(750);}
                if(challengeTime==448){jT.setGameSpeed(500);}
                
                if(challengeTime>=70 && challengeTime<=91){invertBlock=!invertBlock;}
                if(challengeTime>=178 && challengeTime<=212){invertBlock=!invertBlock;}
                
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
    }
    
    //Nested Classes extends KeyListener
    class FrameKeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
          
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == toLeftKey){toLeft=true;}
            if(e.getKeyCode() == toRightKey){toRight=true;}
            if(e.getKeyCode() == toDownKey){toBottom=true;}
            if(e.getKeyCode() == rotateClockWKey){rotateClockW=true;}
            if(e.getKeyCode() == rotateAnteClockWKey){rotateAnteClockW=true;}
            if(e.getKeyCode() == pauseKey){pause=!pause;}
        }

        @Override
        public void keyReleased(KeyEvent e) {
//            if(e.getKeyCode() == toLeftKey){toLeft=false;}
//            if(e.getKeyCode() == toRightKey){toRight=false;}
//            if(e.getKeyCode() == toDownKey){toBottom=false;}
//            if(e.getKeyCode() == rotateClockWKey){rotateClockW=false;}
//            if(e.getKeyCode() == rotateAnteClockWKey){rotateAnteClockW=false;}
////            if(e.getKeyCode() == pauseKey){pause=!pause;}
        }
        
    }

    //NestedClasses extends ActionListener
    //mainMenu
    class NewGameListener implements ActionListener{
        boolean open;

        public NewGameListener(boolean open) {
            this.open = open;
        }        
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();//true,
            openNewGame(open);
        }
    }
    class HallOfFameListener implements ActionListener{
        boolean open;

        public HallOfFameListener(boolean open) {
            this.open = open;
        }        
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            openHallOfFame(open);
        }
    }
    class SettingsListener implements ActionListener{
        boolean open;

        public SettingsListener(boolean open) {
            this.open = open;
        }        
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            openSettings(open);
            if(!open){saveSettings();}
        }
        
    }
    class QuitListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
        
    }
    ////Campaign
    class CampaignListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            playMusic(1);

            makeInCampaignGameGui();
            startGame(500, 30, 10);
        }
    }
    ////ContinousGame
    class ContinousListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {            
            playSoundEff();
            playMusic(1);

            makeInContinousGameGui();
            startGame(500, 30, 10);
        }
        
    }
    ////freeGame
    class FreeGameListener implements ActionListener{
        boolean open;

        public FreeGameListener(boolean open) {
            this.open = open;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            openFreeGame(open);
        }
    }   
    class FreeGameLaunchlistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

            playSoundEff();
            playMusic(1);
        
            makeInFreeGameGui(Integer.parseInt(linCombo.getSelectedItem().toString()),
                                Integer.parseInt(colCombo.getSelectedItem().toString()));
            
            startGame(getDifficultyCombo(),
                Integer.parseInt(linCombo.getSelectedItem().toString()),
                Integer.parseInt(colCombo.getSelectedItem().toString()));
        }
    }
    ////Challenge
    class ChallengeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            playMusic(2);
            
            makeInChallengeGameGui();
            startGame(750, 30, 10);
            
            ChallengeThread workChallenge = new ChallengeThread();
            Thread challengeT = new Thread(workChallenge);
            challengeT.start();
        }
        
    }
    ////hallOffame
    class GoOnlineListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
//            int x = JOptionPane.showConfirmDialog(frame, "Going online will update and overrride your score. Continue?", "GoOnline", 2);
//            if (x==0){
                Thread t = new Thread(new FtpUploadHighScoreThread());
                t.start();
//            }
        }
        
    }    
    //Settings
    ////HotKeyTab
    class GetKeyListener implements KeyListener{
        JLabel label;
        public GetKeyListener(JLabel label) {
            this.label = label;
        }
 
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            
            switch(label.getText().charAt(0)){
                case 'L':
                    label.setText("Left:   " + SpecialChar(e.getKeyCode()));//Character.toUpperCase(e.getKeyChar())
                    toLeftKey = e.getKeyCode();
                break;
                case 'R':
                    label.setText("Right: " + SpecialChar(e.getKeyCode()));
                    toRightKey = e.getKeyCode();
                break;                
                case 'D':
                    label.setText("Down: " + SpecialChar(e.getKeyCode()));
                    toDownKey = e.getKeyCode();
                break;
                case '1':
                    label.setText("1 Rotate: " + SpecialChar(e.getKeyCode()));
                    rotateClockWKey = e.getKeyCode();
                break;
                case '2':
                    label.setText("2 Rotate: " + SpecialChar(e.getKeyCode()));
                    rotateAnteClockWKey = e.getKeyCode();
                break;
                case 'P':
                    label.setText("Pause:     " + SpecialChar(e.getKeyCode()));
                    pauseKey = e.getKeyCode();
                break;                

            }
            
            backSettingsBut.requestFocus();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
        
    }
    class GetKeyFocus implements MouseListener{

        JLabel label;
        
        public GetKeyFocus(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(label.isFocusOwner()){
                backSettingsBut.requestFocus();
            }else{
                label.requestFocus();
            }
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
    class SetKeyFocus implements FocusListener{
        
        private JLabel label;
        private GetKeyListener keyLis;
        
        public SetKeyFocus(JLabel label) {
            this.label = label;
            keyLis = new GetKeyListener(label);
        }

        @Override
        public void focusGained(FocusEvent e) {
            label.setForeground(Color.blue);
            label.addKeyListener(keyLis);
        }

        @Override
        public void focusLost(FocusEvent e) {
            label.setForeground(Color.black);
            label.removeKeyListener(keyLis);
        }
        
    }
    ////SoundTab
    class MusicCheckListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            muteMusic = musicCheck.isSelected();
            playMusic(0);//true, 
        }
        
    }
    class SoundEffCheckListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            muteSoundEff = soundEffCheck.isSelected();
            
//            playSound(true, 0);
        }
        
    }  
    class MusicVolumeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            volumeMusic = volumeMusicSlider.getValue();
            musicPlayer.setVolume(volumeMusic/100.0);
        }
        
    }
    class SoundEffVolumeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            volumeSoundEff = volumeSoundEffSlider.getValue();
        }
        
    }
    ////SkinTab
    class SkinComboListener implements ItemListener{

        JComboBox combo;

        public SkinComboListener(JComboBox combo) {
            this.combo = combo;
        }
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            System.out.println((String)combo.getSelectedObjects()[0]);
            skin = (String) (combo.getSelectedObjects()[0]);
            for(int i=0; i<=6; i++){
                skinBlockPanel[i].setPaint(i, skin);
            }
        }
        
    }
    
    //in game
    class BackToMenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            stopGame();
            makeMainMenuGui();
        }
        
    }
    class PauseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            pause=!pause;
        }
    }
    class ResetListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            playSoundEff();
            int x = JOptionPane.showConfirmDialog(frame, "Confirm game reset?", "Reset Game", 0,2);
            if(x==0){
                int dificulty = jT.getGameSpeed();
                int linii = jT.getPlayGround().length;
                int coloane = jT.getPlayGround()[0].length;

                updateGuiPlayGroundThread.suspend();

                score =0;
                scoreLbl.setText("Score : 0");
                frame.repaint();
                pause=false;
                jT.stopGame();
                jT = new JTetris2();
                jT.setUpPlayGround(linii, coloane-2);
                jT.setGameSpeed(dificulty);
                jT.setMuteSound(muteSoundEff);
                jT.startGame();
//                updateGuiPlayGround(jT.getPlayGround());

                updateGuiPlayGroundThread.resume();                
            }
        }
    }

    //Nested Classes extends JPanel
    class BlockPanel extends JPanel{
        private int paintColor;
        private Image img;
        private String blockSkin;
        
        public BlockPanel(){
        }

        public void setPaint(int paintColor, String skin){
            if((paintColor<0) || (paintColor>7)){paintColor=1;}
            this.paintColor = paintColor;     
            this.blockSkin = skin;
        }
        
        @Override
        public void paintComponent(Graphics g){
            img = new ImageIcon(System.getProperty("user.dir") + "\\img\\blocks\\" + blockSkin + "\\" + paintColor + ".png").getImage();
            g.drawImage(img, 0 , 0, this);
            
        }
        
    }
    class GuiPlayGroundPanel extends JPanel{
        private Image img;
        private int rndImg = (int)(Math.random()*29) + 1;
        
        public void setPaint(int index){
            if (index <1 || index > 29 ){
                rndImg=1;
            }else{
                rndImg = index;
            }
            
        }
        
        
        @Override
        public void paintComponent(Graphics g){
                    switch(rndImg){
                        case 1:
                            inGame = Color.black;
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;
                        case 2:
                            inGame = new Color(0, 11, 28);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                        
                        case 3:
                            inGame = new Color(47, 0, 25);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;
                        case 4:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;
                        case 5:
                            inGame = Color.black;
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                        
                        case 6:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;   
                        case 7:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                         
                        case 8:
                            inGame = new Color(67, 74, 82);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 9:
                            inGame = new Color(29, 34, 34);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 10:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 11:
                            inGame = new Color(37, 37, 37);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 12:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 13:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 14:
                            inGame = new Color(35, 67, 120);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 15:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 16:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 17:
                            inGame = new Color(36, 69, 124);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 18:
                            inGame = new Color(0, 116, 107);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 19:
                            inGame = new Color(66, 58, 74);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 20:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 21:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 22:
                            inGame = new Color(102, 206, 255);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 23:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 24:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 25:
                            inGame = new Color(26, 26, 26);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 26:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;
                        case 27:
                            inGame = new Color(38, 38, 38);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 28:
                            inGame = new Color(255, 255, 255);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;                              
                        case 29:
                            inGame = new Color(0, 0, 0);
                            g.setColor(inGame);
                            g.fillRect(0, 0, 1000, 1000);                             
                        break;
                    }

            
                    img = new ImageIcon(System.getProperty("user.dir") + "\\img\\res" + rndImg +".gif").getImage();
                    g.drawImage(img, (guiPlayGround.getWidth()/2)-90, (guiPlayGround.getHeight()/2) - (blockSize * 8)+110, this);

        }
    }    
    class GuiMenuPanel extends JPanel{
        private Image img;
        @Override
        public void paintComponent(Graphics g){
            img = new ImageIcon(System.getProperty("user.dir") + "\\img\\mainMenu.gif").getImage();
            g.drawImage(img, 0 , 0, this); 
        }
    }
    class GuiInGame extends JPanel {
       private int playGrLenght = Integer.parseInt(linCombo.getSelectedItem().toString());
        @Override
        public void paintComponent(Graphics g){
            //g.drawLine(5, 0, 5, 400);
            Graphics2D g2d = (Graphics2D) g; //70 70 150 150
            GradientPaint grad = new GradientPaint(10, 10, Color.white, 140, 140, inGame);//Color.lightGray
            g2d.setPaint(grad);
            g2d.fillRect(0, 0, 100, playGrLenght * blockSize); // fill rect pe tot panelul!
            
        }
    }

    //Nested Classes 
    class FtpUploadHighScoreThread implements Runnable  {

        @Override
        public void run() {
            
//            highScoreName[0] = "(empty)"; highScoreValue[0] = 0;
//            highScoreName[1] = "(empty)"; highScoreValue[1] = 0;
//            highScoreName[2] = "(empty)"; highScoreValue[2] = 0;
//            highScoreName[3] = "(empty)"; highScoreValue[3] = 0;          

//            name<@>100<%>name2<@>75<%>name3<@>50<%>name4<@>25<%>

            goOnlineBut.setEnabled(false);
            goOnlineBut.setText("Connecting");

            File fil = new File(System.getProperty("user.dir") + "\\hof.dat");
//            writeFile(fil);
            try {
                
                if(fil.exists()){
                    goOnlineBut.setText("Reading");
                    parseFile(fil);
                }

                FTPClient client = new FTPClient();
                client.connect("ftp.byethost4.com", 21);
                client.login("b4_17824743", "RussianK3*");
                goOnlineBut.setText("Dowloading");
                client.download("/htdocs/hof.dat", fil);

                parseFile(fil);
                goOnlineBut.setText("Reading");
                writeFile(fil);
                client.changeDirectory("/htdocs");
                client.upload(fil);
                goOnlineBut.setText("Uploading");
                client.disconnect(true);
                fil.delete();
                //work on gui
                goOnlineBut.setText("Refresh");
                goOnlineBut.setEnabled(true);
                fameLbl.setText("<html><pre> HALL OF FAME<br><br><br><br>" 
                                                    + highScoreName[0] + "\t\t" + highScoreValue[0] + "<br><br>"
                                                    + highScoreName[1] + "\t\t" + highScoreValue[1] + "<br><br>"
                                                    + highScoreName[2] + "\t\t" + highScoreValue[2] + "<br><br>"
                                                    + highScoreName[3] + "\t\t" + highScoreValue[3] + "</pre></html>");
                frame.repaint();
//
            } catch(IllegalStateException | UnknownHostException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException ex){
                    JOptionPane.showMessageDialog(frame, "Unable to connect to game server.\nPlease make sure you have a valid internet connection or try again later.\nLocal data loaded.", "Hall of Fame sync fail.", 0);
                    
                    parseFile(fil);
                    writeFile(fil);                    

                    //work on gui
                    goOnlineBut.setText("Refresh");
                    goOnlineBut.setEnabled(true);
                    fameLbl.setText("<html><pre> HALL OF FAME<br><br><br><br>" 
                                                        + highScoreName[0] + "\t\t" + highScoreValue[0] + "<br><br>"
                                                        + highScoreName[1] + "\t\t" + highScoreValue[1] + "<br><br>"
                                                        + highScoreName[2] + "\t\t" + highScoreValue[2] + "<br><br>"
                                                        + highScoreName[3] + "\t\t" + highScoreValue[3] + "</pre></html>");
                    frame.repaint();
            } catch (IOException ex) {
                System.out.println("IOException : " + ex.getMessage());
            }
        }
    
        
        private String readFile(File fil){
            FileReader filRead;
            String filData = null;
            try {
                filRead = new FileReader(fil);
                BufferedReader reader = new BufferedReader(filRead);
                filData=reader.readLine();
                reader.close();
            } catch (FileNotFoundException ex) {
                System.out.println("ReadFile FileNotFoundException : " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("ReadFile IOException : " + ex.getMessage());
            }
            return filData;
        }
        private void writeFile(File fil){
                FileWriter filWrite;
            try {
                filWrite = new FileWriter(fil);
                filWrite.write(highScoreName[0] + "<@>" + highScoreValue[0] + "<%>"
                            + highScoreName[1] + "<@>" + highScoreValue[1] + "<%>"
                            + highScoreName[2] + "<@>" + highScoreValue[2] + "<%>"
                            + highScoreName[3] + "<@>" + highScoreValue[3] + "<%>");
                filWrite.close();
            } catch (IOException ex) {
                System.out.println("writeFile IOException : " + ex.getMessage());
            }                
        }
        private void parseFile(File fil){
            String[] filDataParse = null;
            //read and parse data  String[]
            if(!fil.exists()){return;}
            filDataParse = readFile(fil).split("<%>");
            
            
  


            boolean duplicate = false;

            for(int i=0; i<=3;i++){
                String[] x = filDataParse[i].split("<@>");
                //check for duplicates
                for(int j=0; j<=7; j++){
                    if(highScoreValue[j]==Integer.parseInt(x[1]) && highScoreName[j].equals(x[0])){
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate){
                    highScoreName[i+4] = x[0];
                    highScoreValue[i+4] = Integer.parseInt(x[1]);                        
                }
            }

                for(int i=0; i<=7;i++){
                    System.out.println(highScoreName[i] + " - " + highScoreValue[i]);
                }  

           //Sort 
           String strTmp;
           int intTmp;
           for(int i=0; i<=7;i++){
                for(int j=i; j<=7;j++){
                    if(highScoreValue[i] < highScoreValue[j]){
                        intTmp = highScoreValue[i];
                        highScoreValue[i] = highScoreValue[j];
                        highScoreValue[j] = intTmp;

                        strTmp = highScoreName[i];
                        highScoreName[i] = highScoreName[j];
                        highScoreName[j] = strTmp;
                    }
                }
            }
        }
    }
}