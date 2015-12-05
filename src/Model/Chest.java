
package Model;

/**
 * Chest class
 */
public class Chest {
    
    /**
     *    n = 4
     * ===========
     * 00 01 02 03
     * 04 05 06 07
     * 08 09 10 11
     * 12 13 14 15
     */
    
    public static final int N_DEFAULT = 4;
    public static final int QUEEN_NOT_PLACED = -1;
    
    protected int[] queens;
    protected int n;
    
    public void display() {
        /*
        * 00 01 XX 03
        * XX 05 06 07
        * 08 09 10 XX
        * 12 XX 14 15
        */
        int maxPerColumn = this.n - 1;
        int max = this.n * this.n;
        int numberOfNumberToDisplay = String.valueOf(max - 1).length();
        int queensDisplayed = 0;
        boolean moreQueensToDisplay = true;
        for (int i = 0; i < max; ++i) {
            if (moreQueensToDisplay && this.isQueen(i)) {
                for (int j = 0; j < numberOfNumberToDisplay; ++j) {
                    System.out.print("X"); // display queen
                }
                queensDisplayed++;
                if (queensDisplayed == this.n) {
                    moreQueensToDisplay = false; // small opt. to avoid checking queen position
                }
            }
            else {
                int numberInI = String.valueOf(i).length();
                for (int j = numberInI; j < numberOfNumberToDisplay; ++j) {
                    System.out.print("0"); // display 0s ==> filling with 0
                }
                System.out.print(i); // display i
            }
            
            // new line or space ?
            if (i%n == maxPerColumn) {
                System.out.print("\n");
            }
            else {
                System.out.print(" ");
            }
        }
    }
    
    public String serialize() {
        String serial = "";
        for (int i = 0; i < this.n; ++i) {
            serial += this.queens[i] + ", ";
        }
        
        if (serial.length() > 2) {
            serial = serial.substring(0, serial.length()-2);
        }
        return serial;
    }
    
    public Chest(int n) {
        this.n = n;
        
        if (this.n <= 2) {
            this.n = N_DEFAULT;
            System.err.println("[CHEST] n must be greater than 2. Default value sets to " + N_DEFAULT + ".");
        }
        
        this.queens = new int[this.n];
        for (int i = 0; i < this.n; ++i) {
            this.queens[i] = QUEEN_NOT_PLACED;
        }
    }
    
    public static Chest fromSerial(String serial, int n) {
        String[] queens_str = serial.split(", ");
        int size = queens_str.length;
        int[] queens = new int[size];
        
        for (int i = 0; i < size; ++i) {
            try {
                queens[i] = Integer.parseInt(queens_str[i]);
            } catch (Exception e) {
                System.err.println("[CHEST] Impossible to retrieve a serial value!");
                queens[i] = QUEEN_NOT_PLACED;
            }
        }
        return new Chest(queens, n);
    }
    
    public Chest(int[] queens, int n) {
        this.n = n;
        if (this.n <= 2) {
            this.n = N_DEFAULT;
            System.err.println("[CHEST] n must be greater than 2. Default value sets to " + N_DEFAULT + ".");
        }
        
        this.queens = new int[n];
        boolean possible = queens.length <= this.n; // too many queens ?
        for (int i = 0; i < this.n; ++i) {
            if (possible && i < queens.length) {
                this.queens[i] = queens[i];
            }
            else {
                this.queens[i] = QUEEN_NOT_PLACED;
            }
        }
    }
    
    public void setQueen(int index, int position) {
        if (this.checkPosition(position)) {
            this.queens[index] = position;
        }
    }
    
    public int findPlace(int column) {
        int start = column;
        int stop = start + this.n*(this.n - 1) + 1;
        
        for (int i = start; i < stop; i += this.n) {
            if (this.possible(i)) {
                return i;
            }
        }
        return QUEEN_NOT_PLACED;
    }
    
    public int findPlaceWithoutPrevious(int column) {
        int previousPosition = this.queens[column]; // save previous position
        this.queens[column] = QUEEN_NOT_PLACED; // remove queen
        int start = column;
        int stop = start + this.n*(this.n - 1) + 1;
        
        for (int i = start; i < stop; i += this.n) {
            if (i != previousPosition && this.possible(i)) {
                return i;
            }
        }
        return QUEEN_NOT_PLACED;
    }
    
    public int findPlaceFromPrevious(int column, int previous) {
        int previousPosition = this.queens[column]; // save previous position
        this.queens[column] = QUEEN_NOT_PLACED; // remove queen
        int start = (previous == QUEEN_NOT_PLACED) ? column : previous;
        int stop = start + this.n*(this.n - 1) + 1;
        
        for (int i = start; i < stop; i += this.n) {
            if (i != previousPosition && this.possible(i)) {
                return i;
            }
        }
        return QUEEN_NOT_PLACED;
    }
    
    public boolean possible(int position) {
        if (!this.checkPosition(position)) {
            return false;
        }
        
        // check if we can put a queen at `position`
        for (int i = 0; i < this.n; ++i) {
            int queenPosition = this.queens[i];
            if (queenPosition == QUEEN_NOT_PLACED) {
                continue;
            }
            
            if(!this.check(queenPosition, position)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean check(int queen, int position) {
        int queenRow = queen/this.n;
        int queenColumn = queen%this.n;
        
        int positionRow = position/this.n;
        int positionColumn = position%this.n;
        
        // check row
        if (queenRow == positionRow) {
            //System.out.println("POSITION " + position + " FALSE BECAUSE OF QUEEN " + queen + " (same row)");
            return false;
        }
        
        // check column
        if (queenColumn == positionColumn) {
            //System.out.println("POSITION " + position + " FALSE BECAUSE OF QUEEN " + queen + " (same column)");
            return false;
        }
        
        // check diagonals        
        boolean checkDiag = (Math.abs(queenColumn - positionColumn) == Math.abs(queenRow - positionRow)); // |deltaRow| == |deltaColumn|
        
        if (checkDiag) {
            //System.out.println("POSITION " + position + " FALSE BECAUSE OF QUEEN " + queen + " (same diag)");
            return false;
        }
        return true;
    }
    
    private boolean checkPosition(int position) {
        return !(position < 0 || position > (this.n*this.n - 1));
    }
    
    private boolean isQueen(int position) {
        for (int i = 0; i < this.n; ++i) {
            if (this.queens[i] == position) {
                return true;
            }
        }
        return false;
    }
}
