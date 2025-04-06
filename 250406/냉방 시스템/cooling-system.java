import java.util.*;
import java.io.*;

public class Main {
    static class Block{
        boolean left, right, top, bottom;
        
    }

    static int n, m, k;
    static int[][] matrix;
    static Block[][] blocked;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        blocked = new Block[n][];

        matrix = new int[n][];
        for(int i = 0; i<n; i++){
            matrix[i] = new int[n];
            blocked[i] = new Block[n];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                blocked[i][j] = new Block();
                matrix[i][j] = Integer.parseInt(st.nextToken());
                if(matrix[i][j] >= 2){
                    List<int[]> air = aircon.getOrDefault(matrix[i][j], new ArrayList<>());
                    air.add(new int[]{i, j});
                    aircon.put(matrix[i][j], air);
                }
                if(matrix[i][j] == 1){
                    office.add(new int[]{i, j});
                }
            }
        }

        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int s = Integer.parseInt(st.nextToken());

            if(s == 0){
                blocked[x-1][y].bottom = true;
                blocked[x][y].top = true;
            }else if(s == 1){
                blocked[x][y-1].right = true;
                blocked[x][y].left = true;
            }
            //System.out.println(blocked[x][y].left + ", "  +blocked[x][y].top);
        }


        cool = new int[n][];
        for(int i = 0; i<n; i++){
            cool[i] = new int[n];
        }

        int minute = 0;
        boolean isCool = false;
        while(true){
            wind();

            mix();

            outWall();

            minute++;

            isCool = checkCool();
            
            if(isCool){
                break;
            }
            //break;
        }

        System.out.println(minute);
    }

    static HashMap<String, boolean[]> wall = new HashMap<>();
    static HashMap<Integer, List<int[]>> aircon = new HashMap<>();
    static ArrayList<int[]> office = new ArrayList<>();
    static int[][] cool;

    static boolean checkCool(){
        boolean result = true;
        for(int[] off : office){    
            if(cool[off[0]][off[1]] < k){
                result = false;
            }
        }

        return result;
    }

    static void outWall(){
        for(int j = 0; j<n; j++){
            if(cool[0][j] != 0 ){
                cool[0][j] --;
            }
            if(cool[n-1][j] != 0){
                cool[n-1][j] --;
            }
        }   

        for(int i = 0; i<n; i++){
            if(cool[i][0] != 0){
                cool[i][0] --;
            }
            if(cool[i][n-1] != 0){
                cool[i][n-1] --;
            }
        }
    }

    static void mix(){
        int[][] mixCool = new int[n][];
        for(int i = 0; i<n; i++){
            mixCool[i] = new int[n];
        }

        for(int i = 0; i<n - 1; i++){
            for(int j = 0; j < n; j++){
                Block b = blocked[i][j];
                if(!b.bottom){
                    int dif = Math.abs(cool[i+1][j] - cool[i][j]) / 4;
                    if(cool[i][j] < cool[i+1][j]){
                        mixCool[i][j] += dif;
                        mixCool[i+1][j] -= dif;
                    }else if(cool[i][j] > cool[i+1][j]){
                        mixCool[i][j] -= dif;
                        mixCool[i+1][j] += dif;
                    }
                }
                if(j == n -1){

                }else{
                    if(!b.right){
                        int dif = Math.abs(cool[i][j+1] - cool[i][j]) / 4;
                        if(cool[i][j] < cool[i][j+1]){
                            mixCool[i][j] += dif;
                            mixCool[i][j+1] -= dif;
                        }else if(cool[i][j] > cool[i][j+1]){
                            mixCool[i][j] -= dif;
                            mixCool[i][j+1] += dif;
                        }
                    }
                }
            }
        }
        

        for(int j = 0; j<n - 1; j++){
            Block b = blocked[n-1][j];
            if(!b.right){
                int dif = Math.abs(cool[n-1][j+1] - cool[n-1][j]) / 4;
                if(cool[n-1][j] < cool[n-1][j+1]){
                    mixCool[n-1][j] += dif;
                    mixCool[n-1][j+1] -= dif;
                }else if(cool[n-1][j] > cool[n-1][j+1]){
                    mixCool[n-1][j] -= dif;
                    mixCool[n-1][j+1] += dif;
                }
            }
        }


        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                cool[i][j] += mixCool[i][j];
            }
            //System.out.println(Arrays.toString(cool[i]));
        }
    }

    
    static void wind(){
        for(int key : aircon.keySet()){
            for(int[] b : aircon.get(key)){
                int x = b[0];
                int y = b[1];

                blow(key, x, y);
            }
        }
    }

    static int[][] newCool;
    static void blow(int dir, int x, int y){
        newCool = new int[n][];
        for(int i = 0; i<n; i++){
            newCool[i] = new int[n];
        }

        int curx= x;
        int cury = y;
        if(dir == 2){ // 왼
            cury = y -1;
            newCool[curx][cury] += 5;

            int row = 0;
            int col = 0;

            for(int i = 0; i<7; i+=2){
                for(int j = 0; j<=i; j++){
                    int r = curx - row + j;
                    int c = cury - col;

                    if(isValid(r, c) && newCool[r][c] != 0){
                        Block curB = blocked[r][c];

                        if(isValid(r, c-1) && !curB.left){
                            newCool[r][c-1] = newCool[r][c] - 1;
                        }
                        
                        if(isValid(r-1, c) && isValid(r-1, c-1)){
                            Block sideB1 = blocked[r-1][c];
                            if(!sideB1.top && !sideB1.left){
                                newCool[r-1][c-1] = newCool[r][c] - 1;
                            }
                        }

                        
                        if(isValid(r+1, c) && isValid(r+1, c-1)){
                            Block sideB2 = blocked[r+1][c];
                            if(!sideB2.bottom && !sideB2.left){
                                newCool[r+1][c-1] = newCool[r][c] - 1;
                            }
                        }
                    }
                }
                //System.out.println();
                row++;
                col++;
            }

        }else if(dir == 3){ // 위
            curx = x - 1;
            newCool[curx][cury] += 5;

            int row = 0;
            int col = 0;
            for(int i = 0; i<7; i+=2){
                for(int j = 0; j<=i; j++){
                    int r = curx - col;
                    int c = cury - row + j;
                    //System.out.print(r + " " + c + ", ");
                    if(isValid(r, c) && newCool[r][c] != 0){
                        //System.out.println(r + " " + c);
                        Block curB = blocked[r][c];
                        if(isValid(r-1, c) && !curB.top){
                            newCool[r-1][c] = newCool[r][c] - 1;
                        }

                        if(isValid(r, c-1) && isValid(r-1, c-1)){
                            Block sideB1 = blocked[r][c-1];
                            if(!sideB1.right && !sideB1.top){
                                newCool[r-1][c-1] = newCool[r][c] - 1;
                            }
                        }
                        
                        if(isValid(r, c+1) && isValid(r-1, c+1)){
                            Block sideB2 = blocked[r][c+1];
                            if(!sideB2.left && !sideB2.top){
                                newCool[r-1][c+1] = newCool[r][c] - 1;
                            }
                        }
                    }
                }
                //System.out.println();
                row++;
                col++;
            }
        }else if(dir == 4){ //오른
            cury = y + 1;
            newCool[curx][cury] += 5;

            int row = 0;
            int col = 0;
            for(int i = 0; i<7; i+=2){
                for(int j = 0; j<=i; j++){
                    int r = curx - row + j;
                    int c = cury + col;
                    //System.out.print(r + " " + c + ", ");
                    if(isValid(r, c) && newCool[r][c] != 0){
                        //System.out.println(r + " " + c);
                        Block curB = blocked[r][c];
                        if(isValid(r, c+1) && !curB.right){
                            newCool[r][c+1] = newCool[r][c] - 1;
                        }

                        if(isValid(r-1, c) && isValid(r-1, c+1)){
                            Block sideB1 = blocked[r-1][c];
                            if(!sideB1.bottom && !sideB1.right){
                                newCool[r-1][c+1] = newCool[r][c] - 1;
                            }
                        }

                        if(isValid(r+1, c) && isValid(r+1, c+1)){
                            Block sideB2 = blocked[r+1][c];
                            if(!sideB2.top && !sideB2.right){
                                newCool[r+1][c+1] = newCool[r][c] - 1;
                            }
                        }
                    }
                }
                //System.out.println();
                row++;
                col++;
            }
        }else if(dir == 5){ // 아래
            curx = x + 1;
            newCool[curx][cury] += 5;

            int row = 0;
            int col = 0;
            for(int i = 0; i<7; i+=2){
                for(int j = 0; j<=i; j++){
                    int r = curx + col;
                    int c = cury - row + j;
                    //System.out.print(r + " " + c + ", ");
                    if(isValid(r, c) && newCool[r][c] != 0){
                        //System.out.println(r + " " + c);
                        Block curB = blocked[r][c];
                        if(isValid(r+1, c) && !curB.bottom){
                            newCool[r+1][c] = newCool[r][c] - 1;
                        }

                        if(isValid(r, c-1) && isValid(r+1, c-1)){
                            Block sideB1 = blocked[r][c-1];
                            if(!sideB1.right && !sideB1.bottom){
                                newCool[r+1][c-1] = newCool[r][c] - 1;
                            }
                        }

                        if(isValid(r, c+1) && isValid(r+1, c+1)){
                            Block sideB2 = blocked[r][c+1];
                            if(!sideB2.left && !sideB2.bottom){
                                newCool[r+1][c+1] = newCool[r][c] - 1;
                            }
                        }
                    }
                }
                //System.out.println();
                row++;
                col++;
            }
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                cool[i][j] += newCool[i][j];
            }
            //System.out.println(Arrays.toString(newCool[i]));
        }
        //System.out.println();
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= n|| y < 0 || y >= n){
            return false;
        }

        return true;
    }

    
}