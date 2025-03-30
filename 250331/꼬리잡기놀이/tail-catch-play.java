import java.util.*;
import java.io.*;

public class Main {

    static class Route{
        int x, y ;

        Route(int x, int y){
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return "(" + x + ", " + y + ")";
        }
    }

    static class Train{
        int x, y;

        int seq;
        int reverse;

        Train(int x, int y, int s){
            this.x = x;
            this.y = y;
            this.seq = s;
        }

        public String toString(){
            return "(" + x + ", " + y +  ", " + seq +")";
        }
    }

    static int n, m, k;
    static int[][] matrix;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        matrix = new int[n][];
        newMatrix = new int[n][];
        team = new int[n][];
        seqValue = new int[n][][];
        for(int i = 0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            matrix[i] = new int[n];
            newMatrix[i] = new int[n];
            team[i] = new int[n];
            seqValue[i] = new int[n][];
            for(int j = 0; j<n; j++){
                matrix[i][j] = Integer.parseInt(st.nextToken()); 
                seqValue[i][j] = new int[2];
            }
        }

        initRoute();
        //System.out.println(Arrays.toString(heads));
        //System.out.println(Arrays.toString(tails));
        //

        seqMatrix();        

        matrix = newMatrix;
        //System.out.println(Arrays.toString(heads));
        //System.out.println(Arrays.toString(tails));

        for(int i = 0; i<k; i++){
            // 팀 이동
            moveTrain();
            

            // 공 던짐
            throwBall();

        }

        System.out.println(score);

        /*
        for(int i = 0; i<n; i++){
            System.out.println(Arrays.toString(matrix[i]));
        }
        */
        /*
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                System.out.print(Arrays.toString(seqValue[i][j]) + ", ");
            }
            System.out.println();
        }
        */
        
        
        //System.out.println(Arrays.toString(heads));
        //System.out.println(Arrays.toString(tails));
    }

    static int round = 1;
    static int quarter = 0;
    static int ballRow = 0, ballCol = 0;
    static int score = 0;

    static void throwBall(){

        int teamNum = -1;
        int seq = -1;
        if(quarter == 0){
            for(int i = 0; i<n; i++){
                if(matrix[ballRow][i] > 0){ // 만나면
                    teamNum = team[ballRow][i] - 1;
                    seq = clockDir[teamNum] ? seqValue[ballRow][i][0] : seqValue[ballRow][i][1];

                    //System.out.println(ballRow + ", " + i + " : " + seq);
                    break;
                }
            }
            ballRow = ballRow + 1 < n ? ballRow + 1 : ballRow;

        }else if(quarter == 1){
            for(int i = 0; i<n; i++){
                if(matrix[ballRow - i][ballCol] > 0){ // 만나면
                    teamNum = team[ballRow - i][ballCol] - 1;
                    seq = clockDir[teamNum] ? seqValue[ballRow - i][ballCol][0] : seqValue[ballRow - i][ballCol][1];
                    break;
                }
            }
            ballCol = ballCol + 1 < n ? ballCol + 1 : ballCol;

        }else if(quarter == 2){
            for(int i = 0; i<n; i++){
                if(matrix[ballRow][ballCol - i] > 0){ // 만나면
                    teamNum = team[ballRow][ballCol - i]  - 1;
                    seq = clockDir[teamNum] ? seqValue[ballRow][ballCol - i][0]  : seqValue[ballRow][ballCol - i][1] ;
                    break;
                }
            }
            ballRow = ballRow - 1 >= 0 ? ballRow - 1 : 0;

        }else if(quarter == 3){
            for(int i = 0; i<n; i++){
                if(matrix[ballRow+i][ballCol] > 0){ // 만나면
                    teamNum = team[ballRow+i][ballCol] - 1;
                    seq = clockDir[teamNum] ? seqValue[ballRow+i][ballCol][0] : seqValue[ballRow+i][ballCol][1];
                    break;
                }
            }
            ballCol = ballCol - 1 >= 0 ? ballCol - 1 :  0;
        }

        round ++;

        if(round == n+1){
            quarter++;
        }
        if(round == 2 * n + 1){
            quarter ++;
        }
        if(round == 3* n + 1){
            quarter++;
        }
        if(round == 4 * n + 1){
            quarter = 0;
            round = 1;
        }

        if(seq != -1){
            score += seq * seq;
            clockDir[teamNum] = !clockDir[teamNum];
            
            Train temp = heads[teamNum];
            heads[teamNum] = tails[teamNum];
            tails[teamNum] = temp;
        }

    }

    static void moveTrain(){
        for(int i = 0; i<m; i++){
            Train he = heads[i];
            boolean isClockDir = clockDir[i];

            int j = 0;
            int seq = he.seq;
            Route edge = routes[i].get(seq);
            int headNum = matrix[edge.x][edge.y];
            int[] headSeq = seqValue[edge.x][edge.y];
            int nextSeq = seq;
            while(j < routes[i].size()){

                if(isClockDir){
                    nextSeq = seq - 1;
                    nextSeq = nextSeq < 0 ? routes[i].size() - 1 : nextSeq;
                }else{ // == 2
                    // 2라면 반시계방향이고,
                    nextSeq = seq + 1;
                    nextSeq %= routes[i].size();
                }
                
                Route nextR = routes[i].get(nextSeq);
                Route curR = routes[i].get(seq);
                matrix[curR.x][curR.y] = matrix[nextR.x][nextR.y];
                seqValue[curR.x][curR.y] = seqValue[nextR.x][nextR.y];

                if(j == routes[i].size() - 1){
                    matrix[curR.x][curR.y] = headNum;
                    heads[i] = new Train(curR.x, curR.y, seq);

                    seqValue[curR.x][curR.y] = headSeq;
                }

                if(clockDir[i]){
                    if(seqValue[curR.x][curR.y][0] == teamCount[i]){
                        tails[i] = new Train(curR.x, curR.y, seq);
                    }
                }else{
                    if(seqValue[curR.x][curR.y][1] == teamCount[i]){
                        tails[i] = new Train(curR.x, curR.y, seq);
                    }
                }
                

                seq = nextSeq;
                //System.out.println(nextR + " " + curR);

                j++;
            }

            
        }
    }

    static int[] teamCount;
    static boolean[] clockDir; // clockDir[i] = true 라면 시계방향이라서, index를 -1로해야함.
    static int[][] newMatrix;
    static int[][] team;
    static int[][][] seqValue;
    static void seqMatrix(){

        teamCount = new int[m];
        clockDir = new boolean[m];
        for(int i = 0; i<m; i++){
            Train curHead = heads[i];
            
            int curIndex = curHead.seq;

            Route rotate = routes[i].get( (curIndex + 1) % routes[i].size() );
            
            int j = 1;
            int num = 1;
            while(j <= routes[i].size()){
                Route next = routes[i].get(curIndex);

                if(matrix[next.x][next.y] >= 1 && matrix[next.x][next.y] <= 3){
                    newMatrix[next.x][next.y] = num++;
                }else{
                    newMatrix[next.x][next.y] = -1;
                }

                if(matrix[rotate.x][rotate.y] != 2){
                    // 3, 4 라면 시계방향
                    clockDir[i] = true;
                    curIndex = curIndex - 1;
                    curIndex = curIndex < 0 ? routes[i].size() - 1 : curIndex;
                }else{ // == 2
                    // 2라면 반시계방향이고,
                    curIndex = curIndex + 1;
                    curIndex %= routes[i].size();
                }

                j++;
            }

            teamCount[i] = num - 1;
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(newMatrix[i][j] >= 1){
                    if(clockDir[team[i][j] - 1]){
                        seqValue[i][j][0] = newMatrix[i][j];
                        seqValue[i][j][1] = teamCount[team[i][j]-1] - newMatrix[i][j] + 1;
                    }else{
                        seqValue[i][j][0] = teamCount[team[i][j]-1] - newMatrix[i][j] + 1;
                        seqValue[i][j][1] = newMatrix[i][j];
                    }
                }
            }
            
        }
    }


    static List<Route>[] routes;
    static Train[] heads, tails;
    static boolean[][] visited;
    static int teamI = 0;

    static void initRoute(){
        // 초기화 
        heads = new Train[m];
        tails = new Train[m];
        routes = new ArrayList[m];
        for(int i = 0; i<m; i++){
            routes[i] = new ArrayList<>();
        }

        visited = new boolean[n][];
        for(int i = 0; i<n; i++){
            
            visited[i] = new boolean[n];
        }
        // 초기화 끝

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(!visited[i][j] && matrix[i][j] >= 1){
                    // 경로만들기
                    dfs(i, j);
                    teamI ++;
                }
            }
        }

        /*
        for(int i = 0 ; i<m; i++){
            System.out.println(routes[i]);
        }*/
    }
    static void dfs(int x, int y){
        if(visited[x][y]){
            return;
        }

        visited[x][y] = true;
        routes[teamI].add(new Route(x, y));
        team[x][y] = teamI + 1;

        if(matrix[x][y] == 1){
            heads[teamI] = new Train(x, y, routes[teamI].size() - 1);
        }
        if(matrix[x][y] == 3){
            tails[teamI] = new Train(x, y, routes[teamI].size() - 1);
        }

        int upx = x - 1;
        int downx = x + 1;
        int lefty = y - 1;
        int righty = y + 1;

        if(isValid(upx, y)){
            dfs(upx, y);
        }else if(isValid(x, righty)){
            dfs(x, righty);
        }else if(isValid(downx, y)){
            dfs(downx, y);
        }else if(isValid(x, lefty)){
            dfs(x, lefty);
        }else{
            return;
        }

    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return false;
        }

        if(matrix[x][y] == 0){
            return false;
        }

        if(visited[x][y]){
            return false;
        }

        return true;
    }



}