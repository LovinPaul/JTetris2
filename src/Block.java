
public class Block {

    private int x;
    private int y;
    private int index;

    public Block(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public boolean isAtPoint(int x, int y) {
        boolean ret = (this.x == x) && (this.y == y);
        return ret;
    }

    //Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    //Setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
