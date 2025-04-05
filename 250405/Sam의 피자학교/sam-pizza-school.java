import java.io.*;
import java.util.*;

public class Main {
    static int n, k;
    static int[] mil;

    static int minDow;
    static List<Integer> least;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        mil = new int[n];
        minDow = Integer.MAX_VALUE;
        for(int i = 0; i<n; i++){
            mil[i] = Integer.parseInt(st.nextToken());

            if(minDow > mil[i]){
                minDow = mil[i];
                least = new ArrayList<>();
                least.add(i);
            }else if(minDow == mil[i]){
                least.add(i);
            }

        }

        int replica = 0;
        while(true){
            // 최소값 올리기
            for(Integer index : least){
                mil[index] ++;
            }

            // 피자 말기
            int[] bt = rolling();

            int bot = bt[0];
            int top = bt[1];
            int col = bt[2];

            push(bot, top+1, col);

            int[] tw = twice();
            bot = tw[0];
            top = tw[1];
            col = tw[2];

            least = new ArrayList<>();
            push(bot, top, col);

            //System.out.println(minD + " " + maxD);

            replica ++;
            if(maxD - minD <= k){
                System.out.println(replica);
                return;
            }

            //break;
            
        }
    }

    static int[] twice(){
        int[] head = new int[n/2];
        int[] tail = new int[n/2];

        for(int i = 0; i<n/2; i++){
            head[i] = mil[n/2 - i - 1];
            tail[i] = mil[i + n/2];
        }

        //System.out.println(Arrays.toString(head));
        //System.out.println(Arrays.toString(tail));

        int[][] fu = new int[4][n/4];
        for(int i = 0; i<4; i++){
            fu[i] = new int[n/4];
        }

        for(int j = 0; j<n/4; j++){
            fu[0][n/4 - j - 1] = tail[j];
            fu[1][n/4 - j - 1] = head[j];
        }

        int a = 0;
        for(int j = n/4; j < n/2; j++){
            fu[2][a] = head[j];
            fu[3][a++] = tail[j];
        }

        /*
        for(int j = 0; j<4; j++){
            System.out.println(Arrays.toString(fu[j]));
        }
        */


        rollDow = fu;
        colLimit = n/4;
        return new int[]{n/4, 4, n/4};

    }


    static int maxD = Integer.MIN_VALUE;
    static int minD = Integer.MAX_VALUE;
    static int[][] rollDow = null;
    static void push(int bot, int top, int col){
        int[][] pushedDow = new int[n][];
        for(int i = 0; i<n; i++){
            pushedDow[i] = new int[n];
        }

        for(int i = 0; i<top; i++){
            for(int j = 0; j<bot; j++){
                if(i < top - 1 && j >= col){
                    continue;
                }
                int result = 0;
                if(i == top -1){
                    result = ab(i, j, top, bot);
                }else{
                    result = ab(i, j, top, col);
                }
                pushedDow[i][j] = result + rollDow[i][j];
            }
        }

        /*
        System.out.println(bot + ", " + top + ", " + col);
        for(int i = 0; i<top; i++){
            System.out.println(Arrays.toString(pushedDow[i])); 
        }*/
        

        minD = Integer.MAX_VALUE;
        maxD = Integer.MIN_VALUE;
        int m = 0;
        for(int j = 0; j<col; j++){
            for(int i = top-1; i>=0; i--){
                if(minD > pushedDow[i][j]){
                    minD = pushedDow[i][j];
                    least = new ArrayList<>();
                    least.add(m);
                }else if(minD == pushedDow[i][j]){
                    least.add(m);
                }

                maxD = Math.max(maxD, pushedDow[i][j]);
                mil[m++] = pushedDow[i][j];
            }
        }

        for(int j = col; j<bot; j++){
            if(minD > pushedDow[top-1][j]){
                minD = pushedDow[top-1][j];
                least = new ArrayList<>();
                least.add(m);
            }else if(minD == pushedDow[top-1][j]){
                least.add(m);
            }
            maxD = Math.max(maxD, pushedDow[top-1][j]);

            mil[m++] = pushedDow[top-1][j];
        }
        //System.out.println(Arrays.toString(mil));
    }

    static int colLimit = 0;
    static int ab(int x, int y, int top, int col){
        int upx = x-1;
        int downx = x + 1;
        int lefty = y - 1;
        int righty = y + 1;

        int result = 0;
        int gap = 0;
        if(isValid(upx, y, top, col)){
            if(x != top -1 || (y < colLimit)){
                gap = Math.abs(rollDow[x][y] - rollDow[upx][y]) / 5;
                if(rollDow[x][y] > rollDow[upx][y]){
                    result -= gap;
                }else if(rollDow[x][y] < rollDow[upx][y]){
                    result += gap;
                }
            }
        }

        if(isValid(downx, y, top, col)){
            gap = Math.abs(rollDow[x][y] - rollDow[downx][y]) / 5;
            if(rollDow[x][y] > rollDow[downx][y]){
                result -= gap;
            }else if(rollDow[x][y] < rollDow[downx][y]){
                result += gap;
            }
        }

        if(isValid(x, lefty, top, col)){
            gap = Math.abs(rollDow[x][y] - rollDow[x][lefty]) / 5;
            if(rollDow[x][y] > rollDow[x][lefty]){
                result -= gap;
            }else if(rollDow[x][y] < rollDow[x][lefty]){
                result += gap;
            }
        }

        if(isValid(x, righty, top, col)){
            gap = Math.abs(rollDow[x][y] - rollDow[x][righty]) / 5;
            if(rollDow[x][y] > rollDow[x][righty]){
                result -= gap;
            }else if(rollDow[x][y] < rollDow[x][righty]){
                result += gap;
            }
        }

        return result;
    }

    static boolean isValid(int x, int y, int top, int col){
        if(x < 0 || x >= top || y < 0 || y >= col){
            return false;
        }
        return true;
    }

    static int[] rolling(){
        int[][] rolls = new int[n][];
        for(int i = 0; i<n; i++){
            rolls[i] = new int[n];
        }

        System.arraycopy(mil, 0, rolls[0], 0, n);

        int bottom = n;
        int top = 0, topP = 1;
        int col = 1, colP = 0;
        int[][] rolled = null;
        while(top + 1 <= bottom - col){
            rolled = new int[n][];
            for(int i = 0; i<n; i++){
                rolled[i] = new int[n];
            }

            for(int i = col; i < bottom; i++){
                rolled[col][i-col] = rolls[top][i];
            }
            
            for(int i = 0; i<= top; i++){
                for(int j = 0; j<col; j++){
                    rolled[j][top-i] = rolls[i][j];
                }
            }

            topP = (topP + 1) % 2;
            colP = (colP + 1) % 2;

            if(topP == 0){
                top ++;
            }

            if(colP == 0){
                col++;
            }

            bottom = bottom - top;
            rolls = rolled;

            /*
            System.out.println(bottom + " " + top + " " + col);
            for(int i = 0; i< top + 1; i++){
                System.out.println(Arrays.toString(rolls[i]));
            }
            System.out.println();
            */
        }

        rollDow = rolls;
        colLimit = col;

        return new int[]{bottom, top, col};
        
    }
}