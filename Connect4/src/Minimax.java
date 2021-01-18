import java.util.Random;

public class Minimax {
    int [][]board = new int[6][7];
    int r;
    int c;
    int player=1;
    int AI=2;
    int value;
    double pInfinite = Double.POSITIVE_INFINITY;
    double nInfinite = Double.NEGATIVE_INFINITY;
    boolean max=true;
    public Minimax(int [][]board,int row,int col){
        board = board;
        r=row;
        c=col;
    }

    public void putValue(int r,int c,int value){
        board[r][c] = value;
    }
    public int checkForFreeRow(int[][]board,int col){
        for(int i=0;i<6;i++){
            if(board[i][col]==0){
                return i;
            }
        }
        return -1;
    }

    public void minimax(int[][]board,int depth,double alpha,double beta,boolean max){
        int []valid_locations=[0,1,2,3,4,5,6];
        if (max==true){

            double tempBeta = nInfinite;
            int choice = new Random().nextInt(valid_locations.length);
            System.out.println(choice);

            for (int i=0;i<valid_locations.length;i++) {
                int[][] tempBoard = board.clone();

                int row = checkForFreeRow(board, i);
                if(row==-1){
                    System.out.print("There is no free row");
                    break;
                }

                putValue(row, i, player);

                newValue = minimax(tempBoard, depth - 1, alpha, beta, False)[1]
                print(newValue)

                if newValue > tempBeta:
                tempBeta = newValue
                choose = col

                if alpha<tempBeta:
                alpha = tempBeta
			else:
                alpha = alpha
				#print(alpha)

                if alpha >= beta:
                print("pruning done- 1")
                break
                return choose,tempBeta
            }

        }
    }

}
