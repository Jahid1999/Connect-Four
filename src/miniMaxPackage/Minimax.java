package miniMaxPackage;

import java.util.ArrayList;
import java.util.List;

public class Minimax
{
    private int [][] board = new int[6][7];

    public Minimax(int[][] board) {
        for(int i=0; i<6; i++)
        {
            for(int j=0; j<7; j++)
            {
                this.board[i][j] = board[i][j];
            }
        }
        minimax(this.board, -Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 5);
        /*for(int i=0; i<6; i++)
        {
            for(int j=0; j<7; j++)
            {
                System.out.print(board[i][j]+"-------");
            }
            System.out.println();
        }*/
    }

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
            for (int j=0; j<rows; j++)
            {
                if (board[j][i]==0)
                {
                    validCols.add(i);
                    break;
                }
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
            //System.out.println("ami asi");
            return 0;
        }

        if (win(board, ai))
        {
            //System.out.println("jitse");
            return 1;
        }
        if (win(board,human))
        {
            //System.out.println("jitse");
            return -1;
        }

        List<Integer> validCols = new ArrayList<>();

        calculateValidCols(board, validCols);

        //System.out.println("---"+validCols+"---");

        if (turn == 1)
        {
            move = validCols.get(0);

            for(int col: validCols)
            {
                int [][] tempBoard = new int [6][7]; // = board.clone();

                for(int i=0; i<6; i++)
                {
                    for(int j=0; j<7; j++)
                    {
                        tempBoard[i][j] = board[i][j];
                    }
                }

                int row = calculateRow(tempBoard, col);

                tempBoard[row][col] = ai;

                int value = minimax(tempBoard, a, b, 0, depth-1);

                if (value>a)
                {
                    a = value;
                    move = col;
                    //System.out.println("----------" + move);
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
                int [][] tempBoard = new int [6][7]; // = board.clone();

                for(int i=0; i<6; i++)
                {
                    for(int j=0; j<7; j++)
                    {
                        tempBoard[i][j] = board[i][j];
                    }
                }

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
                if(board[i][j]==player&&board[i+1][j+1]==player&&board[i+2][j+2]==player&&board[i+3][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i-1][j+1]==player&&board[i-2][j+2]==player&&board[i-3][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i][j+1]==player&&board[i][j+2]==player&&board[i][j+3]==player)
                {
                    return true;
                }
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]==player&&board[i+1][j]==player&&board[i+2][j]==player&&board[i+3][j]==player)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
