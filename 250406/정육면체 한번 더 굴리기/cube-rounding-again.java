import java.util.*;
import java.io.*;

public class Main {

    static int n, m;
    static int[][] matrix;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        matrix = new int[n][];
        for(int i = 0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            matrix[i] = new int[n];
            for(int j = 0; j<n; j++){
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        Dice dice = new Dice();
        //System.out.println(dice);
        int result = 0;
        for(int i = 0; i< m; i++){
            //rollDice();
            dice.roll();
            
            int score = getScore(dice);
            dice.rotate(matrix[dice.x][dice.y]);
            //System.out.println(score);
            result += score;
        }
        //dice.roll();
        //System.out.println(dice);
        System.out.println(result);
    }

    static int getScore(Dice dice){
        int x = dice.x;
        int y = dice.y;
        
        visited= new boolean[n][];
        for(int i = 0; i<n; i++){
            visited[i] = new boolean[n];
        }

        int score = dfs(x, y, matrix[x][y]);

        return score;

    }

    static boolean[][] visited;
    static int dfs(int x, int y, int num){

        if(x < 0 || x >= n || y < 0 || y >= n){
            return 0;
        }

        if(matrix[x][y] != num){
            visited[x][y] = true;
            return 0;
        }

        if(visited[x][y]){
            return 0;
        }

        visited[x][y] = true;

        int sum = matrix[x][y];

        return sum + dfs(x-1, y, num) + dfs(x+1, y, num) + dfs(x, y-1, num) + dfs(x, y+1, num);

    }

    static class Dice{
        int x, y;
        int[] vertical;
        int left, right;
        int dir;

        Dice(){
            x = 0; y = 0;
            vertical = new int[]{1, 2, 6, 5};
            left = 4;
            right = 3;
            dir = 1;
        }

        public String toString(){
            return "{" +  x + ", " +y +"} : " + dir + Arrays.toString(vertical);
        }

        int getBot(){
            return this.vertical[2];
        }

        void rotate(int matrixNum){
            
            if(getBot() > matrixNum){
                dir = dir + 1 > 4 ? 1 : dir + 1;
            }else if (getBot() < matrixNum){
                dir = dir - 1 == 0 ? 4 : dir - 1;
            }else{

            }
        }

        void move(){
            int righty = y + 1;
            int lefty = y - 1;
            int upx = x - 1;
            int downx = x + 1;
            if(dir == 1){
                if(isValid(x, righty)){
                    this.x = x;
                    this.y = righty;
                }else{
                    dir = 3;
                    move();
                }
            }else if(dir == 2){
                if(isValid(downx, y)){
                    this.x = downx;
                    this.y = y;
                }else{
                    dir = 4;
                    move();
                }
            }else if(dir == 3){
                if(isValid(x, lefty)){
                    this.x = x;
                    this.y = lefty;
                }else{
                    dir = 1;
                    move();
                }
            }else if(dir == 4){
                if(isValid(upx, y)){
                    this.x = upx;
                    this.y = y;
                }else{
                    dir = 2;
                    move();
                }
            }
        }

        boolean isValid(int x, int y){
            if( x < 0 || x >= n || y < 0 || y >= n){
                return false;
            }
            return true;
        }

        void roll(){
            move();

            if(dir == 1){ // 오른쪽
                int top = vertical[0];
                int bot = vertical[2];
                vertical[0] = left;
                vertical[2] = right;
                left = bot;
                right = top;
            }else if(dir == 2){ // 아래
                vertical = new int[] {vertical[3], vertical[0], vertical[1], vertical[2]};
            }else if(dir == 3){ // 왼쪽
                int top = vertical[0];
                int bot = vertical[2];
                vertical[0] = right;
                vertical[2] = left;
                right = bot;
                left = top;
            }else if(dir == 4){ // 위쪽
                vertical = new int[] {vertical[1], vertical[2], vertical[3], vertical[0]};
            }
        }
    }
}