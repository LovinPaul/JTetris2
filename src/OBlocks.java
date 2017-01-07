
public class OBlocks extends Blocks {

    private Block[] b;

    private int rotate;

    public OBlocks(int[][] playG, int index,int rotatePos, int x, int y) {
        super(index, 3000, rotatePos, x, y);

        b = super.getBlocks();

        rotate(playG, rotatePos, x, y);        
    }
    
    @Override
    public boolean down(int[][] playG) {
        boolean ret=false;
        //
        // 3    2   B
        // 1    0   A
        
            
        if (b[0]!=null && b[2]!=null){
            ret = Move.down4Blocks(playG, b[0], b[1], b[2], b[3]);
        }else{
            if(b[0] != null) {ret = Move.down2Blocks(playG, false, b[0], b[1]);}
            if(b[2] != null) {ret = Move.down2Blocks(playG, false, b[2], b[3]);}
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

    }

    //overloaded methods
    private void rotate(int[][] playG, int rotatePos, int x, int y){
        
        if(y<0){y=0;}
        if(x<2){x=2;}

        if(    Move.isFreeGroundByCoord(playG, x, y, true, b[0]) 
            && Move.isFreeGroundByCoord(playG, x-1, y, true, b[1])
            && Move.isFreeGroundByCoord(playG, x, y-1, true, b[2])
            && Move.isFreeGroundByCoord(playG, x-1, y-1, true, b[3])){

                //
                // 3    2   B
                // 1    0   A


                b[0].setY(y);     b[0].setX(x);     
                b[1].setY(y);     b[1].setX(x-1);   
                b[2].setY(y-1);   b[2].setX(x);   
                b[3].setY(y-1);   b[3].setX(x-1);   
        }        
    }    
    
}
