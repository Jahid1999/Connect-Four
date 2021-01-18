package minimaxPackage;

import java.util.ArrayList;
import java.util.List;

public class Minimax
{
    private int [][] board = new int [6] [7];

    private int rows = 6;
    private int columns = 7;

    private List<Integer> validCols = new ArrayList<>();

    public void calculateValidCols (int [][] board)
    {
        validCols.clear();
        for(int i=0; i<columns; i++)
        {
            if(board[rows-1][i]==0)
            {
                validCols.add(i);
            }
        }
    }

    public int calculateRow (int [][] board, int col)
    {
        for(int i=0; i<rows; i++)
        {
            if(board[i][col]==0)
            {
                return i;
            }
        }

        return -1;
    }

    public void minimax (int [][] board, int a, int b, int turn, int depth)
    {
        calculateValidCols(board);

        if (turn == 0)
        {
            /*choose = random.choice(validPositions)
            tempBeta = negInf

            for col in validPositions:
        tempBoard = board.copy()
            row = checkForFreeRow(board, col)

            putPiece(tempBoard, row, col, player_2)

            newValue = minimax(tempBoard, depth-1, alpha, beta, False)[1]

            if newValue > tempBeta:
            tempBeta = newValue
            choose = col

            if alpha>tempBeta:
            tempBeta=alpha
			else:
            alpha=alpha
            print(alpha)
            if alpha >= beta:
            print("pruning done- 1")
            break
            return choose, tempBeta*/
        }
        else
        {

        }
    }
}
