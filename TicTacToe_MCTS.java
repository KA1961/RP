import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe_MCTS {

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
        if (score == WIN) 
        {
            return "WIN";
        } else if (score == DRAW) 
        {
            return "DRAW";
        } else {
            return "LOST";
        }
    }
  public static int randomrunsimulate(int[][] state, int player) 
  {
        while (find(state) == GAMENOTOVER)
        {
            ArrayList<ArrayList<Integer>> emptyCells = getEmptyCells(state);
            ArrayList<Integer> empty = emptyCells.get((int)(Math.random() * emptyCells.size()));
    
            state[empty.get(0)][empty.get(1)] = player;
            player = -player;
        }

        return find(state);
    }
  
  public static ArrayList<ArrayList<Integer>> getEmptyCells(int[][] board) 
  {
      ArrayList<ArrayList<Integer>> result = new ArrayList<>();
      for (int i = 0; i < 3; i++) 
      {
          for (int j = 0; j < 3; j++)
          {
              if (board[i][j] == 0) {
                  ArrayList<Integer> empty_cell = new ArrayList<>();
                  empty_cell.add(i);
                  empty_cell.add(j);
                  result.add(empty_cell);
              }
          }
      }
      return result;
  }

    public static ArrayList<Node_MCTS> createchildren(Node_MCTS parent) 
    {
        
        ArrayList<ArrayList<Integer>> emptyCells = getEmptyCells(parent.board);
        ArrayList<Node_MCTS> children = new ArrayList<>();
        if (emptyCells.isEmpty()) return null;
        for (ArrayList<Integer> empty: emptyCells) 
        {
            int[][] boardCopy = new int[3][3];
            for (int i = 0; i < parent.board.length; i++) 
            {
            	
                boardCopy[i] = parent.board[i].clone();
            }
            
            boardCopy[empty.get(0)][empty.get(1)] = -parent.player;

            Node_MCTS child = new Node_MCTS();
            child.row = empty.get(0);
            child.col = empty.get(1);
            child.values = 0;
            child.num_simulations = 0;
            child.player = -parent.player;
            child.parent = parent;
            child.board = boardCopy;
            child.children = null;
            children.add(child);
        }
       return children;
    }

    public static int[] mcts(int[][] board, int player)
    {
    	Node_MCTS root = new Node_MCTS();
        root.row = -1;
        root.col = -1;
        root.values = 0;
        root.num_simulations = 0;
        root.player = -player;
        root.parent = null;
        root.board = board;       
        root.children = createchildren(root);    
        
        for (int i = 0; i < 10000; i++) 
        {
        	Node_MCTS node = root; //Selection Step
            while (node.children != null)
            {
                double bestValue = Integer.MIN_VALUE;
                Node_MCTS bestNode = null;
                for (Node_MCTS child : node.children)
                {
                    if (getUCB(child) > bestValue)
                    {
                        bestValue = getUCB(child);
                        bestNode = child;
                    }
                } 
                node = bestNode;
            }
            // Expansion step
            node.children = createchildren(node);
            // Simulation step
            int score = randomrunsimulate(node.board, -node.player);
            if (player == Player_2)
            {
            	score = -score;
            }      
            // BackPropagation Step
            while (node != null) 
            {
                if (score == WIN)
                {
                	node.values +=100;
                } else if (score == LOST) 
                {
                	node.values -=100;
                }
                node.num_simulations++;
                node = node.parent;
            }
        }
        // Returning best move
      double bestValue = Integer.MIN_VALUE;
      Node_MCTS bestNode = null;
        for (Node_MCTS child : root.children) 
        {
            if ((child.values / child.num_simulations) > bestValue) 
            {
                bestValue = (child.values / child.num_simulations);
                bestNode = child;
            }
        }  
        int[] Move = new int[2];
        Move[0] = bestNode.row;
        Move[1] = bestNode.col;
        return Move;
    }    
    public static double getUCB(Node_MCTS node) 
    {
        if (node.num_simulations == 0) return Integer.MAX_VALUE;
        return (node.values / node.num_simulations) + Math.sqrt( 2 *Math.log(node.parent.num_simulations) 
        		/ node.num_simulations);
    }
    public static void main(String[] args) 
    {  	
        int c = 0;
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        while (find(board) == GAMENOTOVER) 
        {
            if (c % 2== 0)
            {
                int[] nextMove = mcts(board, Player_1);
                board[nextMove[0]][nextMove[1]] = Player_1;
                System.out.println("Player O: " + nextMove[0] + ", " + nextMove[1]);
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
        System.out.println(" Result for player (Computer): " + printResult(find(board)));
        input.close();
    }

}
class Node_MCTS 
{
    int row;
    int col;
    double values;
    int num_simulations;
    int player;
    Node_MCTS parent;
    int[][] board;
    ArrayList<Node_MCTS> children;
}

