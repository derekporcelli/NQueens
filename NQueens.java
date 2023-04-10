import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NQueens {
    private Set<int[]> allSolutions;
    private final int[][] map;
    private int[] queens;
    private final int size;
    private int positiveDiagonal;
    private int negativeDiagonal;

    public NQueens(int size) {
        this.size = size;
        this.allSolutions = new HashSet<>();
        this.map = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                map[row][column] |= (row << 24);
                map[row][column] |= (column << 16);
                map[row][column] |= ((row + column) << 8);
                map[row][column] |= row - column + size;
            }
        }
        this.queens = new int[size];
        tryColumn();
    }

    public void tryColumn() {
        tryColumn(0);
    }

    public void tryColumn(int column) {
        if (column == size) {
            allSolutions.add(queens.clone());
            return;
        }
        
        outerLoop:
        for (int row = 0; row < size; row++) {
            if (column == 0) {
                System.out.print(size >= 10 && row + 1 < 10 ? 
                "\nCalculating " + (row + 1) + " of " + size + "...  [" : 
                "\nCalculating " + (row + 1) + " of " + size + "... [");
                for (int j = 0; j < size; j++) {
                    System.out.print("_");
                }
                System.out.print("]");
                for (int j = 0; j < size + 1; j++) {
                    System.out.print("\b");
                }
            }
            if (column == 1) {
                System.out.print("#");
            }

            positiveDiagonal = (map[row][column] >> 8) & 0xFF;
            negativeDiagonal = map[row][column] & 0xFF;

            for (int i = 0; i < column; i++) {
                if (row == ((queens[i] >> 24) & 0xFF)) {
                    continue outerLoop;
                }
                if (positiveDiagonal == ((queens[i] >> 8) & 0xFF)) {
                    continue outerLoop;
                }
                if (negativeDiagonal == (queens[i] & 0xFF)) {
                    continue outerLoop;
                }
            } 

            queens[column] = map[row][column];
            for (int i = column + 1; i < size; i++) {
                queens[i] = 0;
            } 
            
            tryColumn(column + 1);
        }

        if (column == 1) {
            System.out.print("]");
        }
    }

    public Set<int[]> getAllSolutions() {
        return allSolutions;
    }

    public static String queenToString(int queen) {
        int row = (queen >> 24) & 0xFF;
        int column = (queen >> 16) & 0xFF;
        int positiveDiagonal = (queen >> 8) & 0xFF;
        int negativeDiagonal = (queen & 0xFF);
        return row + "." + column + "." + positiveDiagonal + "." + negativeDiagonal;
    }

    public static void main(String[] args) {
        Set<int[]> solutions;
        int size = 13;

        double timer0 = System.currentTimeMillis();
        NQueens NQueens = new NQueens(size);
        solutions = NQueens.getAllSolutions();
        double timer1 = System.currentTimeMillis();

        System.out.println("\n\nBoard of size " + size + "x" + size + " has " + solutions.size() + " solutions given " + size + " queens");
        System.out.println("Calculation took " + ((timer1 - timer0) / 1000.0) + " seconds\n");

        Scanner scanner = new Scanner(System.in);
        try {
            while(true){
                System.out.print(">> ");
                String input = scanner.nextLine();
                if(input.equals("exit")){
                    scanner.close();
                    break;
                } else if(input.equals("print")){
                    for (int[] solution : solutions) {
                        System.out.print("[");
                        for (int queen : solution) {
                            System.out.print(queenToString(queen));
                            if (queen != solution[solution.length - 1]){
                                System.out.print(", ");
                            }
                        }
                        System.out.println("]");
                        String[][] board = new String[size][size];
                        for(int i = 0; i < size; i++){for(int j = 0; j < size; j++){
                                board[i][j] = "_ ";
                        }}
                        for (Integer queen : solution) {
                            board[((queen >> 24) & 0xFF)][((queen >> 16) & 0xFF)] = "Q ";
                        }
                        for (String[] row : board) {
                            for (String element : row) {
                                System.out.print(element);
                            }
                            System.out.println();
                        }
                        System.out.println();
                    }
                } else if(input.equals("help")){
                    System.out.println("Type \"print\" to print all solutions\nType \"exit\" to exit the program\n");
                } else if(input.equals("")){
                } else {
                    System.out.println("Invalid input, try again or type \"exit\" to exit the program\n");
                }
            }
        } catch (Exception e) {
            scanner.close();
        }
    }
}

//Code by: Derek Porcelli