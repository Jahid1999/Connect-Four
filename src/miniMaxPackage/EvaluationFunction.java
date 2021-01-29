package miniMaxPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvaluationFunction
{
    int human = 1;
    int ai = 5;

    public int calculatePieces (List<Integer> listOf4, int player)
    {
        int score = 0;

        int opponent;

        if (player == human)
        {
            opponent = ai;
        }
        else
        {
            opponent = human;
        }

        if (Collections.frequency(listOf4, player)==3&&Collections.frequency(listOf4, 0)==1)
        {
            score = score + evaluate (3);
        }

        if (Collections.frequency(listOf4, player)==2&&Collections.frequency(listOf4, 0)==2)
        {
            score = score + evaluate (2);
        }

        if (Collections.frequency(listOf4, player)==1&&Collections.frequency(listOf4, 0)==3)
        {
            score = score + evaluate (1);
        }

        if (Collections.frequency(listOf4, opponent)==3&&Collections.frequency(listOf4, 0)==1)
        {
            score = score - evaluate (3);
        }

        if (Collections.frequency(listOf4, opponent)==2&&Collections.frequency(listOf4, 0)==2)
        {
            score = score - evaluate (2);
        }

        if (Collections.frequency(listOf4, opponent)==1&&Collections.frequency(listOf4, 0)==3)
        {
            score = score - evaluate (1);
        }

        return score;
    }

    private int evaluate(int i)
    {
        return (int) Math.pow(i, i);
    }

    public int getValue (int [][] board, int player)
    {
        int value = 0;
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(board[i][j]);
                tempList.add(board[i+1][j+1]);
                tempList.add(board[i+2][j+2]);
                tempList.add(board[i+3][j+3]);

                value = value + calculatePieces(tempList, player);
            }
        }

        for(int i=3; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(board[i][j]);
                tempList.add(board[i-1][j+1]);
                tempList.add(board[i-2][j+2]);
                tempList.add(board[i-3][j+3]);

                value = value + calculatePieces(tempList, player);
            }
        }

        for(int i=0; i<6; i++)
        {
            for(int j=0; j<4; j++)
            {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(board[i][j]);
                tempList.add(board[i][j+1]);
                tempList.add(board[i][j+2]);
                tempList.add(board[i][j+3]);

                value = value + calculatePieces(tempList, player);
            }
        }

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<7; j++)
            {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(board[i][j]);
                tempList.add(board[i+1][j]);
                tempList.add(board[i+2][j]);
                tempList.add(board[i+3][j]);

                value = value + calculatePieces(tempList, player);
            }
        }

        return value;
    }
}

