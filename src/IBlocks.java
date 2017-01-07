
public class IBlocks extends Blocks {

    private Block[] b;

    private int rotate;
    
    public IBlocks(int[][] playG, int index,int rotatePos, int x, int y) {
        super(index, 0, rotatePos, x, y);

        b = super.getBlocks();

        rotate(playG, rotatePos, x, y);        
        
    }
    
    @Override
    public boolean down(int[][] playG) {
        boolean ret=false;
        
        //(0)                   //(1)
        //  0   1   2   3 A     //  3 D
                                //  2 C
                                //  1 B
                                //  0 A
        
        if(rotate==0){
            
            if (b[0]!=null){
                ret = Move.down4Blocks(playG, b[0], b[1], b[2], b[3]);
            }
            
        }else if(rotate==1){
            
            if (b[0]!=null && b[1]!=null && b[2]!=null && b[3]!=null){
                ret = Move.down4Blocks(playG, b[0], b[1], b[2], b[3]);
            }else{
                if(b[0]!=null){ret = Move.down1Block(playG, b[0]);}
                if(b[1]!=null){ret = Move.down1Block(playG, b[1]);}
                if(b[2]!=null){ret = Move.down1Block(playG, b[2]);}
                if(b[3]!=null){ret = Move.down1Block(playG, b[3]);}
            }
        }
        return ret;    
    }

    @Override
    public void left(int[][] playG) {
        Move.left4Blocks(playG, b);
    }

    @Override
    public void right(int[][] playG) {
        Move.right4Blocks(playG, b);
    }

    @Override
    public void rotateClockWise(int[][] playG, boolean clockW) {
        if(clockW){
            if(rotate==1){
                rotate=0;
            }else{
               rotate++; 
            }
        }else{
            if(rotate==0){
                rotate=1;
            }else{
                rotate--;
            }
        }

        rotate(playG, rotate, b[0].getX(), b[0].getY());          
    }
    
        //overloaded methods
    public void rotate(int[][] playG, int rotatePos, int x, int y){
        switch(rotatePos){
            case 0:
                if(y<0){y=0;}
                if(x>playG[0].length - 5){x=playG[0].length - 5;}
                
                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x+1, y, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x+2, y, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x+3, y, true, b[3])){

                        rotate = 0;
                        //  0   1   2   3


                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y);     b[1].setX(x+1);   
                        b[2].setY(y);   b[2].setX(x+2);   
                        b[3].setY(y);   b[3].setX(x+3);   
                }
             break;
            case 1:
                if(y<0){y=0;}

                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x, y-1, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x, y-2, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x, y-3, true, b[3])){    
                    
                        rotate = 1;
                                //(1)
                                //  3 D
                                //  2 C
                                //  1 B
                                //  0 A                 
                    
                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y-1);   b[1].setX(x);   
                        b[2].setY(y-2);   b[2].setX(x);   
                        b[3].setY(y-3);   b[3].setX(x);   
                }
            break; 
        }
    }    
    
}
