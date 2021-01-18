package minimaxPackage;

import java.util.ArrayList;
import java.util.List;

public class Minimax
{
    private int rows = 6;
    private int columns = 7;

    private int human = 1;
    private int ai = 2;

    private int move;

    public int getMove() {
        return move;
    }

    public void calculateValidCols (int [][] board, List<Integer> validCols)
    {
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

    public int minimax (int [][] board, int a, int b, int turn, int depth)
    {
        if (depth==0)
        {
            return 0;
        }

        if (win(board, ai))
        {
            return 1;
        }
        else if (win(board,human))
        {
            return -1;
        }

        List<Integer> validCols = new ArrayList<>();

        calculateValidCols(board, validCols);

        if (turn == 1)
        {
            move = validCols.get(0);

            for(int col: validCols)
            {
                int [][] tempBoard = board.clone();

                int row = calculateRow(tempBoard, col);

                tempBoard[row][col] = ai;

                int value = minimax(tempBoard, a, b, 0, depth-1);

                if (value>a)
                {
                    a = value;
                    move = col;
                }

                if (a>=b)
                {
                    break;
                }
            }
            return a;
        }
        else
        {
            for(int col: validCols)
            {
                int [][] tempBoard = board.clone();

                int row = calculateRow(tempBoard, col);

                tempBoard[row][col] = human;

                int value = minimax(tempBoard, a, b, 1, depth-1);

                if (value<b)
                {
                    b = value;
                }

                if (a>=b)
                {
                    break;
                }
            }
            return b;
        }
    }

    public boolean win (int [][] board, int player)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i+1][j+1]==0&&board[i+2][j+2]==0&&board[i+3][j+3]==0)
                {
                    return true;
                }
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i-1][j+1]==0&&board[i-2][j+2]==0&&board[i-3][j+3]==0)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i][j+1]==0&&board[i][j+2]==0&&board[i][j+3]==0)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]==player&&board[i+1][j]==0&&board[i+2][j]==0&&board[i+3][j]==0)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
