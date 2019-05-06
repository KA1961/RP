import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe_Minimax 
{

	static int[][] board = new int[3][3];
	static final int GAMENOTOVER = -2;
    static final int WIN = 1;
	static final int LOST = -1;
	static final int DRAW = 0;
	static final int Player_1 = 1;
    static final int Player_2 = -1;
     
     

    public static int find(int[][] state)
    {
        for (int i = 0; i < 3; i++) 
        {
         
            if (state[i][0] != 0 && state[i][0] == state[i][1] && state[i][0] == state[i][2])
            {
                return state[i][0];
            }
            
            if (state[0][i] != 0 && state[0][i] == state[1][i] && state[0][i] == state[2][i])
            {
                return state[0][i];
            }
        }
        if (state[0][0] != 0 && state[0][0] == state[1][1] && state[0][0] == state[2][2]) 
        {
            return state[0][0];
        }

        if (state[0][2] != 0 && state[0][2] == state[1][1] && state[0][2] == state[2][0])
        {
            return state[0][2];
        }
        // Checking if the it is draw
        boolean Full_flag = true;
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 3; j++) 
            {
                if (state[i][j] == 0)
                {
                	Full_flag = false;
                    break;
                }
            }
            if (!Full_flag) break;
        }
        if (Full_flag) return DRAW;

        return GAMENOTOVER;
    }

    

    public static String insertMark(int mrk) 
    {
        if (mrk == Player_1)
        {
            return "O";
        } else if (mrk == Player_2) 
        {
            return "X";
        } else {
            return " ";
        }
    }

    public static void displayBoard(int[][] state)
    {
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 3; j++) 
            {
                System.out.print("[" + insertMark(state[i][j]) + "]");
            }
            System.out.println();
        }
    }

    public static String printResult(int score) 
    {
        if (score == WIN) {
            return "WIN";
        } else if (score == DRAW) {
            return "DRAW";
        } else {
            return "LOST";
        }
    }
    private static ArrayList<ArrayList<Integer>> getEmptyCells(int[][] board)
    {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (board[i][j] == 0) 
                {
                    ArrayList<Integer> empty = new ArrayList<>();
                    empty.add(i);
                    empty.add(j);
                    result.add(empty);
                }
            }
        }
        return result;
    }
    public static int[] minimax(int[][] board, int player,int depth) 
    {
    	int[] bestMove = new int[3];
        bestMove[0] = -1;
        bestMove[1] = -1;
        if (player == Player_1)
        {
            bestMove[2] = Integer.MIN_VALUE;
        } else 
        {
            bestMove[2] = Integer.MAX_VALUE;
        }
        // Returning the score if the game is over
        if (find(board) != GAMENOTOVER || depth ==0)
        {
            bestMove[2] = find(board);
            return bestMove;
        }
        // Simulating all possible moves and then pick the best one
        ArrayList<ArrayList<Integer>> emptyCells = getEmptyCells(board);
        for (ArrayList<Integer> empty : emptyCells) 
        {
            board[empty.get(0)][empty.get(1)] = player;
            int[] result = minimax(board, -player,depth-1);
           board[empty.get(0)][empty.get(1)] = 0;

            if (player == Player_1 && result[2] > bestMove[2]) 
            {
            	bestMove[0] = empty.get(0);
                bestMove[1] = empty.get(1);
                bestMove[2] = result[2];
              
            }
            else if (player == Player_2 && result[2] < bestMove[2])
            {
                bestMove[0] = empty.get(0);
                bestMove[1] = empty.get(1);
                bestMove[2] = result[2];
            }
        }
        return bestMove;
    }

    public static void main(String[] args) 
    {  	
        int c = 0;
        int depth=5;
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 3; j++)
            {
                board[i][j] = 0;
            }
        }
        while (find(board) == GAMENOTOVER) 
        {
            if (c % 2 == 0)
            {
            	int[] nextMove = minimax(board, Player_1,depth);
                board[nextMove[0]][nextMove[1]] = Player_1;
                System.out.println("Player O: " + nextMove[0] + ", " + nextMove[1] + ".");
            } 
            else
            {
                System.out.print("Enter a row number");
                int row = input.nextInt();
                System.out.print("Enter a column number ");
                int col = input.nextInt();
                board[row][col] = Player_2;
                System.out.println("Player X: " + row + ", " + col);
            }
            displayBoard(board);
            c++;
        }
        System.out.println(" Result for player(Computer): " + printResult(find(board)));
        input.close();
    }

}