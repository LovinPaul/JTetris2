
public abstract class Blocks {
    
    private Block[] b = new Block[4];
    
    public abstract boolean down(int[][] playG);
    public abstract void left(int[][] playG);
    public abstract void right(int[][] playG);
    public abstract void rotateClockWise(int[][] playG, boolean clockW);
//    public abstract void rotate(boolean clockW, int x, int y);

    public Blocks(int index, int blocksIndex,int rotatePos, int x, int y) {
        
        for(int i=0; i<=3;i++){
           b[i] = new Block(index + blocksIndex, x, y);
        }
        
    }
    
    public Block[] getBlocks(){
        return b;
    }
    
    
    
    
}
