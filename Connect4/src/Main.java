import java.util.Scanner;

public class Main {
    public static void main(String []argc){
        int r;
        int c;
        int player=1;
        int AI=2;
        double pInfinite = Double.POSITIVE_INFINITY;
        double nInfinite = Double.NEGATIVE_INFINITY;
        Scanner s = new Scanner(System.in);
        int [][]board = new int[6][7];
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                board[i][j]=0;

                System.out.print(board[i][j]+"   ");
            }
            System.out.println();
        }
        while(true) {
            System.out.println("enter row number:");
            System.out.println("enter col number:");
            r = -(s.nextInt() - 6);
            c = s.nextInt() - 1;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    if (i == r && j == c) {
                        board[i][j] = 1;
                    }

                    System.out.print(board[i][j] + "   ");
                }
                System.out.println();
            }

            maxAlgo m = new maxAlgo(board);
            int aiC = m.getMove();
            System.out.println(aiC);

            int aiR = -(m.calculateRow(board, aiC) - 5);
            System.out.println(aiR);
            board[aiR][aiC] = 10;
            System.out.println(board[aiR][aiC]);

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {

                    System.out.print(board[i][j] + "   ");
                }
                System.out.println();
            }

        }
    }
}
