package miniMaxPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        int tempMove = connect3(board, ai);
        if (tempMove!=-1)
        {
            move = tempMove;
        }
        else
        {
            tempMove = connect3(board, human);
            if (tempMove==-1)
            {
                minimax(this.board, -Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 10);
            }
            else
            {
                move = tempMove;
            }
        }
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
    private int ai = 5;//2;

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

        /*for(int i=0; i<columns; i++)
        {
            for (int j=0; j<rows; j++)
            {
                if (board[j][i]==human)
                {
                    if (i==0)
                    {
                        if (!validCols.contains(i))
                            validCols.add(i);
                        if (!validCols.contains(i+1))
                            validCols.add(i+1);
                    }

                    if (i==6)
                    {
                        if (!validCols.contains(i))
                            validCols.add(i);
                        if (!validCols.contains(i-1))
                            validCols.add(i-1);
                    }
                    validCols.add(i);
                    break;
                }
            }
        }*/
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
            return 1*depth;
        }
        if (win(board,human))
        {
            //System.out.println("jitse");
            return -1*depth;
        }

        List<Integer> validCols = new ArrayList<>();

        calculateValidCols(board, validCols);

        //System.out.println("---"+validCols+"---");

        if (turn == 1)
        {
            Random rand = new Random();
            int kut = rand.nextInt(validCols.size());
            move = validCols.get(kut);

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

    public int connect3 (int [][] board, int player)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]+board[i+1][j+1]+board[i+2][j+2]+board[i+3][j+3]==3*player)
                {
                    for (int k=0; k<4; k++)
                    {
                        if (board[i+k][j+k]==0)
                        {
                            if ((i+k-calculateRow(board, j+k))==0)
                                return j+k;
                        }
                    }
                }
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]+board[i-1][j+1]+board[i-2][j+2]+board[i-3][j+3]==3*player)
                {
                    for (int k=0; k<4; k++)
                    {
                        if (board[i-k][j+k]==0)
                        {
                            if ((i-k-calculateRow(board, j+k))==0)
                                return j+k;
                        }
                    }
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]+board[i][j+1]+board[i][j+2]+board[i][j+3]==3*player)
                {
                    for (int k=0; k<4; k++)
                    {
                        if (board[i][j+k]==0)
                        {
                            if ((i-calculateRow(board, j+k))==0)
                                return j+k;
                        }
                    }
                }
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]+board[i+1][j]+board[i+2][j]+board[i+3][j]==3*player)
                {
                    for (int k=0; k<4; k++)
                    {
                        if (board[i+k][j]==0)
                        {
                            if ((i+k-calculateRow(board, j))==0)
                                return j;
                        }
                    }
                }
            }
        }

        return -1;
    }

    /*public int connect3 (int [][] board, int player)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i+1][j+1]==player&&board[i+2][j+2]==player)
                {
                    if (board[i+3][j+3]==0)
                    {
                        if ((i+3-calculateRow(board, j+3))==0)
                            return j+3;
                    }
                }
            }
        }

        for(int i=1; i<4; i++)
        {
            for(int j=1; j<5; j++)
            {
                if(board[i][j]==player&&board[i+1][j+1]==player&&board[i+2][j+2]==player)
                {
                    if (board[i-1][j-1]==0)
                    {
                        if ((i-1-calculateRow(board, j-1))==0)
                            return j-1;
                    }
                }
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i-1][j+1]==player&&board[i-2][j+2]==player)
                {
                    if (board[i-3][j+3]==0)
                    {
                        if ((i-3-calculateRow(board, j+3))==0)
                            return j+3;
                    }
                }
            }
        }

        for(int i=2; i<5; i++)
        {
            for(int j=1; j<5; j++)
            {
                if(board[i][j]==player&&board[i-1][j+1]==player&&board[i-2][j+2]==player)
                {
                    if (board[i+1][j-1]==0)
                    {
                        if ((i+1-calculateRow(board, j-1))==0)
                            return j-1;
                    }
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j]==player&&board[i][j+1]==player&&board[i][j+2]==player)
                {
                    if (board[i][j+3]==0)
                    {
                        if ((i-calculateRow(board, j+3))==0)
                            return j+3;
                    }
                }
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=1; j<5; j++)
            {
                if(board[i][j]==player&&board[i][j+1]==player&&board[i][j+2]==player)
                {
                    if (board[i][j-1]==0)
                    {
                        if ((i-calculateRow(board, j-1))==0)
                            return j-1;
                    }
                }
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]==player&&board[i+1][j]==player&&board[i+2][j]==player)
                {
                    if (board[i+3][j]==0)
                    {
                        if ((i+3-calculateRow(board, j))==0)
                            return j;
                    }
                }
            }
        }

        for(int i=1; i<4; i++)
        {
            for(int j=0; j<7; j++)
            {
                if(board[i][j]==player&&board[i+1][j]==player&&board[i+2][j]==player)
                {
                    if (board[i-1][j]==0)
                    {
                        if ((i-1-calculateRow(board, j))==0)
                            return j;
                    }
                }
            }
        }

        return -1;
    }*/
}
