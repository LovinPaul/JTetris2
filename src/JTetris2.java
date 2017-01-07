
import java.io.File;
import java.util.ArrayList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class JTetris2 {
    
//<editor-fold defaultstate="collapsed" desc="instance variables">
    
    private GameLoopRunnable work;
    private Thread gameLoopThread;
    
//    private AudioPlayer sound = new AudioPlayer();
    
    private int[][] playGround;
    private boolean gameRunning;
    private boolean gamePaused;
    private boolean gameOver;
    private int gamePlusScore;
    private int gameSpeed = 500;
    private int nextPiece= -1;
    private boolean muteSound;
    private boolean blockControls;
    private double volumeLineReduce = 1.0;
    private double volumeAccept = 1.0;
    
    //Audio players
    private File lineReduceAudioFile = new File("sound/lineReduce.mp3");
    private File acceptAudioFile = new File("sound/accept.mp3");
    
    private Media lineReduceMedia = new Media(lineReduceAudioFile.toURI().toString());
    private Media acceptMedia = new Media(acceptAudioFile.toURI().toString());
    
    private MediaPlayer lineReducePlayer = new MediaPlayer(lineReduceMedia);
    private MediaPlayer acceptPlayer = new MediaPlayer(acceptMedia);
    
//<editor-fold defaultstate="collapsed" desc="7 pieces ints">
    private int pI=1;//0
    private int pJ=1;//1
    private int pL=1;//2
    private int pO=1;//3
    private int pS=1;//4
    private int pT=1;//5
    private int pZ=1;//6
//</editor-fold>

    private ArrayList<Blocks> blocksList = new ArrayList();

//</editor-fold>

    public JTetris2() {   ///CONSTRUCTOR
        JFXPanel fxPanel = new JFXPanel();
    }

    public void startGame(){
        
        gameRunning = true;
        gamePaused = false;
        
        work = new GameLoopRunnable();
        gameLoopThread = new Thread(work);
        gameLoopThread.start();        
    }
    public void stopGame(){
        gameRunning = false;
        gameLoopThread = null;
    }
    
    private boolean  downAllBlocks(){
        boolean ret = false;
        for(Blocks blocks : blocksList){   
            if(blocks.down(playGround)){
                ret = true;
            }
        }
        updatePlayground();
        return ret;
    }
    
    public void toLeft(){
        if(blockControls){return;}
        blocksList.get(blocksList.size()-1).left(playGround);
//        toLeft=false;
        updatePlayground();
    }
    public void toRight(){
        if(blockControls){return;}
        blocksList.get(blocksList.size()-1).right(playGround);
//        toRight=false;
        updatePlayground();
    }
    public boolean toBottom(){
        boolean ret = false;
        if(!blockControls){  
            ret = blocksList.get(blocksList.size()-1).down(playGround);
            updatePlayground();
        }
        return ret;
    }
    public void rotateClockWise(boolean clockW){
        if(blockControls){return;}
        blocksList.get(blocksList.size()-1).rotateClockWise(playGround, clockW);
        updatePlayground();        
    }    
    
    private void removeLine(int line){
        if(!muteSound){
            lineReducePlayer.dispose();
            lineReducePlayer = new MediaPlayer(lineReduceMedia);
            lineReducePlayer.setVolume(volumeLineReduce);
            lineReducePlayer.play();
        }
        for(Blocks blocks : blocksList){
            for(int i=0; i<=3; i++){
                if((blocks.getBlocks()[i] != null) && (blocks.getBlocks()[i].getY() == line)){
                    blocks.getBlocks()[i] = null;
                }                
            }
        }
        updatePlayground();
    }
    private boolean checkForLineCancellation(){
        boolean ret=false;
        boolean x;
        
        for(int i=1; i<=playGround.length-2; i++){
            for(int j=1; j<=playGround[0].length-1; j++){
                //check every line for reduction
                if(playGround[i][j] != 0){
                    x = true;
                }else{
                    break;
                }
                
                if((j==playGround[0].length-1) && (x = true)){
                    System.out.println("RemoveLine : " + i);
                    gamePlusScore += playGround[0].length;
                    removeLine(i);
                }
            }
        }
        return ret;
    }

    private boolean checkForGameOver(){
        Block[] b = blocksList.get(blocksList.size()-1).getBlocks();
        for(Block x : b){
            if(x.getY() <= 7){
                gameOver = true;
                gameRunning = false;
                break;
            }
        }
        return gameOver;
    }
    
    private Blocks generateNextPiece(){
        int pice;
        Blocks blocks = null;
        
        if(nextPiece==-1){
            nextPiece =(int) (Math.random() * 7);
            pice =(int) (Math.random() * 7);
        }else{
            pice = nextPiece;
            nextPiece =(int) (Math.random() * 7);
        }

        switch(pice){
            case 0:
                blocks = new IBlocks(playGround ,pI++, (int) (Math.random() * 2) , playGround[0].length/2, 7);
                System.out.println("New I : " + pI);
                break;
            case 1:
                blocks = new JBlocks(playGround ,pJ++, (int) (Math.random() * 4) , playGround[0].length/2, 7);
                System.out.println("New J : " + pJ);
                break;
            case 2:
                blocks = new LBlocks(playGround ,pL++, (int) (Math.random() * 4) , playGround[0].length/2, 7);
                System.out.println("New L : " + pL);
                break;
            case 3:
                blocks = new OBlocks(playGround ,pO++, 0, playGround[0].length/2, 7);
                System.out.println("New O : " + pO);
                break;    
            case 4:
                blocks = new SBlocks(playGround ,pS++, (int) (Math.random() * 2), playGround[0].length/2, 7);
                System.out.println("New S : " + pS);
                break;   
            case 5:
                blocks = new TBlocks(playGround ,pT++, (int) (Math.random() * 4) , playGround[0].length/2, 7);
                System.out.println("New T : " + pT);                
                break;
            case 6:
                blocks = new ZBlocks(playGround ,pZ++, (int) (Math.random() * 2), playGround[0].length/2, 7);
                System.out.println("New Z : " + pZ);
                break;                
                
        }
        updatePlayground();
        return blocks;
        
    }
    public int getNextPiece(){
        return nextPiece;
    }
    
    private void updatePlayground(){
        resetPlayGround();
        for(Blocks blocks : blocksList){
            for(int i=0; i<=3; i++){
                if(blocks.getBlocks()[i] != null){
                    playGround[blocks.getBlocks()[i].getY()][blocks.getBlocks()[i].getX()] = blocks.getBlocks()[i].getIndex();              
                }
            }
        }
    }
    private void resetPlayGround(){
        
        for(int i=0; i<=playGround.length-1;i++){
            for(int j=0; j<=playGround[0].length-1;j++){
                playGround[i][j] = 0;
                if(i==playGround.length-1){playGround[i][j]=-1;}
                if(j==0){playGround[i][j]=-1;}
                if(j==playGround[0].length-1){playGround[i][j]=-1;}
            }
        }
    }
    public void resetGame(){
        gameLoopThread.suspend();
        resetPlayGround();
        blocksList.clear();
        blocksList.add(generateNextPiece());
        blockControls=false;
        gameLoopThread.resume();
    }
    public int[][] getPlayGround(){
        return playGround;
    }
    public void setUpPlayGround(int lines, int col){
        if(lines<15){lines = 15;}
        if(col<10){col = 10;}
        playGround = new int[lines][col+2];
    }
    
    public boolean isGameOver(){
        return gameOver;
    }    
    public boolean isGameRunning(){
        return gameRunning;
    }
    public boolean isGamePaused(){
        return gamePaused;
    } 
    public int getGamePlusScore(){
        int score = gamePlusScore;
        gamePlusScore = 0;
        return score;
    }
    public int getGameSpeed(){
        return gameSpeed;
    }
    public void setGameRunning(boolean gameR){
        gameRunning = gameR;
    }
    public void setGamePaused(boolean gameP){
        gamePaused = gameP;
    }
    public void setGameSpeed(int speed){
        if(speed<1 || speed > 1500){
            gameSpeed = 500;
        }else{
            gameSpeed = speed;
        }
    }
    public void setMuteSound(boolean mute){
        muteSound = mute;
    }


    class GameLoopRunnable implements Runnable{
        @Override
        public void run() {

            blocksList.add(generateNextPiece());
            updatePlayground();
            
            while(gameRunning && !gameOver){
                try {
                    if(!gamePaused){
                        if (!toBottom()){
                            blockControls = true;
                            if(!muteSound){
                                acceptPlayer.dispose();
                                acceptPlayer = new MediaPlayer(acceptMedia);
                                acceptPlayer.setVolume(volumeAccept);
                                acceptPlayer.play();
                            }
                            checkForGameOver();
                            do{
                                checkForLineCancellation();
                                while(downAllBlocks()){
                                    Thread.sleep(100);
                                }
                                checkForLineCancellation();
                            }while(downAllBlocks());

                            blocksList.add(generateNextPiece());      
                            blockControls=false;
                        }



                    }else{
                    //paused code
                    }
                
                //<editor-fold defaultstate="collapsed" desc="Thread.sleep">
                
                    Thread.sleep(gameSpeed);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
                //</editor-fold>
            }
        }
    }
    
}
