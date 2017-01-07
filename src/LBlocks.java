
public class LBlocks extends Blocks {

    private Block[] b;
    
    private int rotate;
    //private int index=0;//2000;
    public LBlocks(int[][] playG, int index,int rotatePos, int x, int y) {
        super(index, 2000, rotatePos, x, y);

        b = super.getBlocks();

        rotate(playG, rotatePos, x, y); 
    }
    
    @Override
    public boolean down(int[][] playG) {
        boolean ret=false;
        //(0)              //(1)
        // 3        C      //  1    2   3   A
        // 2        B      //  0            B
        // 1   0    A

        //(2)             //(3)
        //  0   1   A     //          0     B
        //      2   B     //  3   2   1     A
        //      3   C

        if(rotate==0 || rotate==2){
            
            if (b[0]!=null && b[2]!=null && b[3]!=null){
                ret = Move.down4Blocks(playG, b[0], b[1], b[2], b[3]);
            }else{
                String indxCode="";
                if(b[0]==null){indxCode=indxCode + 'A';}
                if(b[2]==null){indxCode=indxCode + 'B';}
                if(b[3]==null){indxCode=indxCode + 'C';}

                switch(indxCode){
                    case "A":
//                        ret = Move.down2Blocks(playG, true, b[2], b[3]); 
                        ret = Move.down1Block(playG, b[3]);
                        ret = Move.down1Block(playG, b[2]);
                        break;                
                    case "B" :
                        ret = Move.down2Blocks(playG, false, b[0], b[1]);
                        ret = Move.down1Block(playG, b[3]);
                        break;
                    case "C" :
                        ret = Move.down3Blocks(playG, true, b[0], b[1], b[2]);
                        break;
                    case "AB":
                        ret = Move.down1Block(playG, b[3]);
                        break;
                    case "AC":
                        ret = Move.down1Block(playG, b[2]);
                        break;
                    case "BC":
                        ret = Move.down2Blocks(playG, false, b[0], b[1]);               
                        break;
                }                
            }
            
//<editor-fold defaultstate="collapsed" desc="Old Algorithm">


/*            if(b[2]==null){
                //b3 go down
                if(b[3]!=null){Tehnics.down1Block(playG, b[3]);}
                //if b1 & b0 are live the go down
                if(b[0]!=null && b[1]!=null){
                Tehnics.down2Blocks(playG, false, b[0], b[1]);
                }else{
                if(b[0]!=null){Tehnics.down1Block(playG, b[0]);}
                if(b[1]!=null){Tehnics.down1Block(playG, b[1]);}
                }

                }else if(b[0]==null){
                if(b[2]!=null && b[3]!=null){
                Tehnics.down2Blocks(playG, true, b[2], b[3]);
                }else{
                if(b[2]!=null){Tehnics.down1Block(playG, b[2]);}
                if(b[3]!=null){Tehnics.down1Block(playG, b[3]);}
                }

                }else if(b[3]==null){
                if(b[0]!=null && b[1]!=null && b[2]!=null){
                Tehnics.down3Blocks(playG, true, b[0], b[1], b[2]);
                }else if(b[2]==null){
                Tehnics.down2Blocks(playG, true, b[0], b[1]);
                }else{
                if(b[2]!=null){Tehnics.down1Block(playG, b[2]);}
                }
                }else{
                Tehnics.down4Blocks(playG, b[0], b[1], b[2], b[3]);
                }*/


//</editor-fold>
    
        }else if(rotate==1 || rotate==3){
            
            if (b[0]!=null && b[1]!=null){
                ret = Move.down4Blocks(playG, b[0], b[1], b[2], b[3]);
            }else{
                String indxCode="";
                if(b[1]==null){indxCode=indxCode + 'A';}
                if(b[0]==null){indxCode=indxCode + 'B';}

                switch(indxCode){
                    case "A":
                        ret = Move.down1Block(playG, b[0]);              
                        break;                
                    case "B" :
                        ret = Move.down3Blocks(playG, false, b[1], b[2], b[3]);
                        break;
                }
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
            if(rotate==3){
                rotate=0;
            }else{
               rotate++; 
            }
        }else{
            if(rotate==0){
                rotate=3;
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
                if(x<2){x=2;}
                
                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x-1, y, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x-1, y-1, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x-1, y-2, true, b[3])){

                        rotate = 0;
                        // 3
                        // 2
                        // 1   0

                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y);     b[1].setX(x-1);   
                        b[2].setY(y-1);   b[2].setX(x-1);   
                        b[3].setY(y-2);   b[3].setX(x-1);   
                }
             break;
            case 1:
                if(y<0){y=0;}
                if(x>playG[0].length - 4){x=playG[0].length - 4;}

                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x, y-1, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x+1, y-1, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x+2, y-1, true, b[3])){    
                    
                        rotate = 1;
                        //  1    2   3
                        //  0                    
                    
                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y-1);   b[1].setX(x);   
                        b[2].setY(y-1);   b[2].setX(x+1);   
                        b[3].setY(y-1);   b[3].setX(x+2);   
                }
            break; 
            case 2:
                if(y<0){y=0;}  
                if(x>playG[0].length - 3){x=playG[0].length - 3;}

                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x+1, y, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x+1, y+1, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x+1, y+2, true, b[3])){ 
                    
                        rotate = 2;
                        //  0   1
                        //      2
                        //      3                    
                    
                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y);     b[1].setX(x+1);   
                        b[2].setY(y+1);   b[2].setX(x+1);   
                        b[3].setY(y+2);   b[3].setX(x+1);        
                }
            break;
            case 3:
                if(y<0){y=0;}        
                if(x<3){x=3;}

                if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
                    && Move.isFreeGroundByCoord(playG, x, y+1, true, b[1])
                    && Move.isFreeGroundByCoord(playG, x-1, y+1, true, b[2])
                    && Move.isFreeGroundByCoord(playG, x-2, y+1, true, b[3])){  
                        
                        rotate = 3;
                        //          0
                        //  3   2   1                    
                    
                        b[0].setY(y);     b[0].setX(x);     
                        b[1].setY(y+1);   b[1].setX(x);   
                        b[2].setY(y+1);   b[2].setX(x-1);   
                        b[3].setY(y+1);   b[3].setX(x-2);   
                }
                break;
        }
    }
    

    
    
    
}
