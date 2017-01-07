
public abstract class Move {

    public static boolean isFreeGroundByCoord(int[][] playG, int x, int y, boolean ignoreSamePice, Block b) {
        boolean isFreeGround = false;

        if (playG[y][x] == 0) {
            isFreeGround = true;
        } else if (ignoreSamePice) {
            isFreeGround = playG[y][x] == b.getIndex();
        }
        return isFreeGround;
    }
    public static boolean isFreeGroundByBlock(int[][] playG, int plusX, int plusY, boolean ignoreSamePice, Block b) {
        boolean isFreeGround = false;

        if (playG[b.getY() + plusY][b.getX() + plusX] == 0) {
            isFreeGround = true;
        } else if (ignoreSamePice) {
            isFreeGround = playG[b.getY() + plusY][b.getX() + plusX] == b.getIndex();
        }
        return isFreeGround;
    }
    public static boolean isInMidAir(int[][] playG, boolean ignoreSamePice, Block b) {
        boolean isInMidAir = false;

        if (playG[b.getY() + 1][b.getX()] == 0) {
            isInMidAir = true;
        } else if (ignoreSamePice) {
            isInMidAir = playG[b.getY() + 1][b.getX()] == b.getIndex();
        }
        return isInMidAir;
    }
    
    public static boolean down1Block(int[][] playG, Block b) {
        if (isInMidAir(playG, false, b)) {
            b.setY(b.getY() + 1);
            return true;
        } else {
            return false;
        }
    }
    public static boolean down2Blocks(int[][] playG, boolean ignoreSamePice, Block b0, Block b1) {
        if (isInMidAir(playG, ignoreSamePice, b0) && isInMidAir(playG, ignoreSamePice, b1)) {
            b0.setY(b0.getY() + 1);
            b1.setY(b1.getY() + 1);
            return true;
        } else {
            return false;
        }
    }
    public static boolean down3Blocks(int[][] playG, boolean ignoreSamePice, Block b0, Block b1, Block b2) {
        if (isInMidAir(playG, ignoreSamePice, b0) && isInMidAir(playG, ignoreSamePice, b1) && isInMidAir(playG, ignoreSamePice, b2)) {
            b0.setY(b0.getY() + 1);
            b1.setY(b1.getY() + 1);
            b2.setY(b2.getY() + 1);
            return true;
        } else {
            return false;
        }
    }
    public static boolean down4Blocks(int[][] playG, Block b0, Block b1, Block b2, Block b3) {
        if (isInMidAir(playG, true, b0) && isInMidAir(playG, true, b1)
                && isInMidAir(playG, true, b2) && isInMidAir(playG, true, b3)) {
            b0.setY(b0.getY() + 1);
            b1.setY(b1.getY() + 1);
            b2.setY(b2.getY() + 1);
            b3.setY(b3.getY() + 1);
            return true;
        } else {
            return false;
        }
    }

    public static void left4Blocks(int[][] playG, Block[] b) {
        if (isFreeGroundByBlock(playG, -1, 0, true, b[0]) && isFreeGroundByBlock(playG, -1, 0, true, b[1])
                && isFreeGroundByBlock(playG, -1, 0, true, b[2]) && isFreeGroundByBlock(playG, -1, 0, true, b[3])) {
            for (int i = 0; i <= 3; i++) {
                b[i].setX(b[i].getX() - 1);
            }
        }
    }
    public static void right4Blocks(int[][] playG, Block[] b) {

        if (isFreeGroundByBlock(playG, +1, 0, true, b[0]) && isFreeGroundByBlock(playG, +1, 0, true, b[1])
                && isFreeGroundByBlock(playG, +1, 0, true, b[2]) && isFreeGroundByBlock(playG, +1, 0, true, b[3])) {
            for (int i = 0; i <= 3; i++) {
                b[i].setX(b[i].getX() + 1);
            }
        }
    }
}
