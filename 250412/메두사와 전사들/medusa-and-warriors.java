import java.util.*;
import java.io.*;

public class Main {

    
    static class Warrior{
        int x, y;

        Warrior(int x, int y){
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return "{"+ x + ", " + y + "}";
        }
    }

    static int n, m;
    static int sr, sc, er, ec;
    static int[][] matrix;
    static int[][] warrior;
    
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        for(int q= 0; q < 1; q++){
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());

            matrix = new int[n][];
            warrior = new int[n][];
            for(int i = 0; i<n; i++){
                matrix[i] = new int[n];
                warrior[i] = new int[n];
            }

            st = new StringTokenizer(br.readLine());
            sr = Integer.parseInt(st.nextToken());
            sc = Integer.parseInt(st.nextToken());
            er = Integer.parseInt(st.nextToken());
            ec = Integer.parseInt(st.nextToken());

            st = new StringTokenizer(br.readLine());
            for(int i = 0; i<m; i++){
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                warrior[x][y]++;
            }

            for(int i = 0; i<n; i++){
                st = new StringTokenizer(br.readLine());
                for(int j = 0; j<n; j++){
                    matrix[i][j] = Integer.parseInt(st.nextToken());
                }
                //System.out.println(Arrays.toString(matrix[i]));
            }

            /*
            for(int i = 0; i<n; i++){
                for(int j = 0; j<n; j++){
                    System.out.print(warrior[i][j].size() + " ");
                }
                System.out.println();
            }
            */
            
            bfs();
            //System.out.println(route);
            if(route.equals("")){
                System.out.println(-1);
                break;
            }

            String[] dir = route.split("");
            int pcount = 0;
            for(String d : dir){
                
                medusaMove(d);
                if(pcount == dir.length - 1){
                    
                    break;
                }

                medusaLook();
                //System.out.println(stonedWarrior);

                warriorMove();
                pcount++;
                /*
                for(int i = 0; i<n; i++){
                    for(int j = 0; j<n; j++){
                        System.out.print(warrior[i][j].size());
                    }
                    System.out.println();
                }
                */

                //break;
            }
        }
    }
    static int movedWarriorCount, stonedWarriorCount, attackedWarriorCount;
    static void warriorMove(){
        movedWarriorCount = stonedWarriorCount = attackedWarriorCount = 0;

        int[][] newWarrior = new int[n][];
        for(int i = 0; i<n; i++){
            newWarrior[i] = new int[n];
            //System.out.println(Arrays.toString(stonedArea[i]));
        }
        
        //System.out.println(stonedWarrior);

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(warrior[i][j] == 0){
                    continue;
                }

                if(stonedWarrior.contains(i + " " + j)){
                    newWarrior[i][j] += warrior[i][j];
                    stonedWarriorCount += warrior[i][j];
                }else{
                    int up1 = i - 1, up2 = i - 2;
                    int down1 = i + 1, down2 = i + 2;
                    int left1 = j - 1, left2 = j-2;
                    int right1 = j + 1, right2 = j + 2;
                    int dist = Math.abs(sr - i) + Math.abs(sc - j);

                    int nextx = i, nexty = j;
                    int can = 0;
                    if(isValid2(up1, j)){
                        if(dist > Math.abs(sr - up1) + Math.abs(sc - j)){
                            dist = Math.abs(sr - up1) + Math.abs(sc - j);
                            nextx = up1;
                            nexty = j;
                            can = 1;
                            if(isValid2(up1, left1)){
                                if(dist > Math.abs(sr - up1) + Math.abs(sc - left1)){
                                    dist = Math.abs(sr - up1) + Math.abs(sc - left1);
                                    nextx = up1;
                                    nexty = left1;
                                    can = 2;
                                }
                            }
                            if(isValid2(up1, right1)){
                                if(dist > Math.abs(sr - up1) + Math.abs(sc - right1)){
                                    dist = Math.abs(sr - up1) + Math.abs(sc - right1);
                                    nextx = up1;
                                    nexty = right1;
                                    can = 2;
                                }
                            }
                            if(isValid2(up2, j)){
                                if(dist > Math.abs(sr - up2) + Math.abs(sc - j)){
                                    dist = Math.abs(sr - up2) + Math.abs(sc - j);
                                    nextx = up2;
                                    nexty = j;
                                    can = 2;
                                }
                            }
                        }
                    }

                    if(isValid2(down1, j)){
                        if(dist > Math.abs(sr - down1) + Math.abs(sc - j)){
                            dist = Math.abs(sr - down1) + Math.abs(sc - j);
                            nextx = down1;
                            nexty = j;
                            can = 1;
                            if(isValid2(down1, left1)){
                                if(dist > Math.abs(sr - down1) + Math.abs(sc - left1)){
                                    dist = Math.abs(sr - down1) + Math.abs(sc - left1);
                                    nextx = down1;
                                    nexty = left1;
                                    can = 2;
                                }
                            }
                            if(isValid2(down1, right1)){
                                if(dist > Math.abs(sr - down1) + Math.abs(sc - right1)){
                                    dist = Math.abs(sr - down1) + Math.abs(sc - right1);
                                    nextx = down1;
                                    nexty = right1;
                                    can = 2;
                                }
                            }
                            if(isValid2(down2, j)){
                                if(dist > Math.abs(sr - down2) + Math.abs(sc - j)){
                                    dist = Math.abs(sr - down2) + Math.abs(sc - j);
                                    nextx = down2;
                                    nexty = j;
                                    can = 2;
                                }
                            }
                        }
                    }

                    if(isValid2(i, left1)){
                        if(dist > Math.abs(sr - i) + Math.abs(sc - left1)){
                            dist = Math.abs(sr - i) + Math.abs(sc - left1);
                            nextx = i;
                            nexty = left1;
                            can = 1;
                            if(isValid2(i, left2)){
                                if(dist > Math.abs(sr - i) + Math.abs(sc - left2)){
                                    dist = Math.abs(sr - i) + Math.abs(sc - left2);
                                    nextx = i;
                                    nexty = left2;
                                    can = 2;
                                }
                            }
                            if(isValid2(up1, left1)){
                                if(dist > Math.abs(sr - up1) + Math.abs(sc - left1)){
                                    dist = Math.abs(sr - up1) + Math.abs(sc - left1);
                                    nextx = up1;
                                    nexty = left1;
                                    can = 2;
                                }
                            }
                            if(isValid2(down1, left1)){
                                if(dist > Math.abs(sr - down1) + Math.abs(sc - left1)){
                                    dist = Math.abs(sr - down1) + Math.abs(sc - left1);
                                    nextx = down1;
                                    nexty = left1;
                                    can = 2;
                                }
                            }
                        }
                    }

                    if(isValid2(i, right1)){
                        if(dist > Math.abs(sr - i) + Math.abs(sc - right1)){
                            dist = Math.abs(sr - i) + Math.abs(sc - right1);
                            nextx = i;
                            nexty = right1;
                            can = 1;
                            if(isValid2(i, right2)){
                                if(dist > Math.abs(sr - i) + Math.abs(sc - right2)){
                                    dist = Math.abs(sr - i) + Math.abs(sc - right2);
                                    nextx = i;
                                    nexty = right2;
                                    can = 2;
                                }
                            }
                            if(isValid2(up1, right1)){
                                if(dist > Math.abs(sr - up1) + Math.abs(sc - right1)){
                                    dist = Math.abs(sr - up1) + Math.abs(sc - right1);
                                    nextx = up1;
                                    nexty = right1;
                                    can = 2;
                                }
                            }
                            if(isValid2(down1, right1)){
                                if(dist > Math.abs(sr - down1) + Math.abs(sc - right1)){
                                    dist = Math.abs(sr - down1) + Math.abs(sc - right1);
                                    nextx = down1;
                                    nexty = right1;
                                    can = 2;
                                }
                            }
                        }
                    }

                    if(nextx == sr && nexty == sc){
                        attackedWarriorCount += warrior[i][j];
                        movedWarriorCount += warrior[i][j] * can;
                    }else{
                        movedWarriorCount += warrior[i][j] * can;
                        
                        newWarrior[nextx][nexty] +=  warrior[i][j];
                    }
                    //System.out.println (i + ", " + j);
                    //System.out.println(dist + ", " + nextx + ", " + nexty + ", " + can);
                }
            }
        }
        warrior = newWarrior;
        System.out.println(movedWarriorCount + " " + stonedWarriorCount + " " + attackedWarriorCount);
    }

    static boolean isValid2(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return false;
        }

        if(stonedArea[x][y]){
            return false;
        }

        return true;
    }

    static boolean[][] stonedArea;
    static HashSet<String> stonedWarrior;
    static boolean[][] medusaSeeU, medusaSeeD, medusaSeeL, medusaSeeR;
    static void medusaLook(){
        medusaSeeU = new boolean[n][];
        medusaSeeD = new boolean[n][];
        medusaSeeL = new boolean[n][];
        medusaSeeR = new boolean[n][];
        for(int i = 0; i<n; i++){
            medusaSeeU[i] = new boolean[n];
            medusaSeeD[i] = new boolean[n];
            medusaSeeL[i] = new boolean[n];
            medusaSeeR[i] = new boolean[n];
        }

        ArrayList<Warrior> lookU, lookD, lookL, lookR;
        ArrayList<Warrior> choosed = new ArrayList<>();
        boolean[][] medusaSee = new boolean[n][];

        lookU = lookUp();
        lookD = lookDown();
        lookL = lookLeft();
        lookR = lookRight();

        int uCount = 0, dCount = 0, lCount = 0, rCount = 0;
        for(Warrior w : lookU){
            int x = w.x;
            int y = w.y;
            uCount += warrior[x][y];
        }
        for(Warrior w : lookD){
            int x = w.x;
            int y = w.y;

            //System.out.println(warrior[x][y]);
            dCount += warrior[x][y];
        }
        for(Warrior w : lookL){
            int x = w.x;
            int y = w.y;
            lCount += warrior[x][y];
        }
        for(Warrior w : lookR){
            int x = w.x;
            int y = w.y;
            rCount += warrior[x][y];
        }

        int count = uCount;
        choosed = lookU;
        medusaSee = medusaSeeU;

        if(count < dCount){
            count = dCount;
            choosed = lookD;
            medusaSee = medusaSeeD;
        }
        //System.out.println(count + ": " + choosed);
        if(count < lCount){
            count = lCount;
            choosed = lookL;
            medusaSee = medusaSeeL;
        }
        if(count < rCount){
            count = rCount;
            choosed = lookR;
            medusaSee = medusaSeeR;
        }

        /*
        for(int i = 0; i<n; i++){
            System.out.println(Arrays.toString(medusaSeeR[i]));
        }
        */
        //System.out.println(count + ": " + choosed);
        stonedArea = medusaSee;
        stonedWarrior = new HashSet<>();
        for(Warrior w : choosed){
            stonedWarrior.add(w.x + " " + w.y);
        }

    }

    static ArrayList<Warrior> lookRight(){
        ArrayList<Warrior> looked = new ArrayList<>();

        // sr, sc 
        //왼
        for(int j = sc + 1; j < n; j++){
            medusaSeeR[sr][j] = true;
            if(warrior[sr][j] > 0){
                looked.add(new Warrior(sr, j));
                break;
            }
        }
        
        
        int range = 0;
        int wx = sr, wy = sc;
        for(int i = sr - 1; i>= 0; i--){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int j = sc + 1 + range; j < n; j++){
                if(wx == i && wy == j){
                    wx--;
                    wy++;
                    break;
                }
                medusaSeeR[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i-1;
                    wy = j+1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);

        range = 0;
        wx = sr;
        wy = sc;
        for(int i = sr + 1; i< n; i++){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int j = sc + 1 + range; j < n; j++){
                if(wx == i && wy == j){
                    wx++;
                    wy++;
                    break;
                }

                medusaSeeR[i][j] = true;
               // System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i+1;
                    wy = j+1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);
        //System.out.println(looked);

        return looked;
    }

    static ArrayList<Warrior> lookLeft(){
        ArrayList<Warrior> looked = new ArrayList<>();

        // sr, sc 
        //왼
        for(int j = sc - 1; j>= 0; j--){
            medusaSeeL[sr][j] = true;
            if(warrior[sr][j] > 0){
                looked.add(new Warrior(sr, j));
                break;
            }
        }
        
        
        int range = 0;
        int wx = sr, wy = sc;
        for(int i = sr - 1; i>= 0; i--){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int j = sc - 1 - range; j >= 0; j--){
                if(wx == i && wy == j){
                    wx--;
                    wy--;
                    break;
                }

                medusaSeeL[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i-1;
                    wy = j-1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);

        range = 0;
        wx = sr;
        wy = sc;
        for(int i = sr + 1; i< n; i++){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int j = sc - 1 - range; j >= 0; j--){
                if(wx == i && wy == j){
                    wx++;
                    wy--;
                    break;
                }
                medusaSeeL[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i+1;
                    wy = j-1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);
        //System.out.println(looked);

        return looked;
    }

    static ArrayList<Warrior> lookDown(){
        ArrayList<Warrior> looked = new ArrayList<>();

        // sr, sc 
        //위
        for(int i = sr+1 ; i < n; i++){
            medusaSeeD[i][sc] = true;
            if(warrior[i][sc] > 0){
                looked.add(new Warrior(i, sc));
                break;
            }
        }
        
        int range = 0;
        int wx = sr, wy = sc;
        for(int j = sc - 1; j>= 0; j--){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int i = sr + 1 + range; i < n; i++){
                if(wx == i && wy == j){
                    wx++;
                    wy--;
                    break;
                }

                medusaSeeD[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i+1;
                    wy = j-1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);

        range = 0;
        wx = sr;
        wy = sc;
        for(int j = sc + 1; j < n; j++){
            for(int i = sr + 1 + range; i < n; i++){
                if(wx == i && wy == j){
                    wx++;
                    wy++;
                    break;
                }

                medusaSeeD[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i+1;
                    wy = j+1;
                    break;
                }
            }
            range ++;
        }
        //System.out.println(wx + " ,, " + wy);
        //System.out.println(looked);

        return looked;
    }

    static ArrayList<Warrior> lookUp(){
        ArrayList<Warrior> looked = new ArrayList<>();

        // sr, sc 
        //위
        for(int i = sr-1 ; i>= 0; i--){
            medusaSeeU[i][sc] = true;
            if(warrior[i][sc] > 0){
                looked.add(new Warrior(i, sc));
                break;
            }
        }
        
        
        int range = 0;
        int wx = sr, wy = sc;
        for(int j = sc - 1; j>= 0; j--){
            //System.out.println("(" + (sr-1-range) + ", " + j + ")");
            for(int i = sr - 1 - range; i >= 0; i--){
                if(wx == i && wy == j){
                    wx--;
                    wy--;
                    break;
                }

                medusaSeeU[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i-1;
                    wy = j-1;
                    break;
                }
            }
            range ++;
        }

        range = 0;
        wx = sr;
        wy = sc;
        for(int j = sc + 1; j < n; j++){
            for(int i = sr - 1 - range; i >= 0; i--){
                if(wx == i && wy == j){
                    wx--;
                    wy++;
                    break;
                }

                medusaSeeU[i][j] = true;
                //System.out.println(i + ", " + j);

                if(warrior[i][j] > 0){
                    looked.add(new Warrior(i, j));
                    wx = i-1;
                    wy = j+1;
                    break;
                }
            }
            range ++;
        }

        return looked;

    }

    static void medusaMove(String d){
        if(d.equals("1")){
            sr--;
        }else if(d.equals("2")){
            sr++;
        }else if(d.equals("3")){
            sc--;
        }else if(d.equals("4")){
            sc++;
        }

        if(warrior[sr][sc] > 0){
            warrior[sr][sc] = 0;
        }

        if(sr == er && sc == ec){
            System.out.println(0);
        }
    }

    static String route = "";
    static class Move implements Comparable<Move>{
        int x, y;
        int dist;
        String path;

        public int compareTo(Move m){
            if(this.dist == m.dist){
                return path.compareTo(m.path);
            }else{
                return this.dist - m.dist;
            }
        }

        Move(int x, int y){
            this.x = x;
            this.y = y;
            this.dist = 0;
            this.path = "";
        }
        
    }

    static int[][] visited;
    static void bfs(){
        visited = new int[n][];
        for(int i = 0; i<n; i++){
            visited[i] = new int[n];
        }
        route = "";

        Move move = new Move(sr, sc);
        visited[sr][sc] = -1;
        PriorityQueue<Move> queue =new PriorityQueue<>();
        queue.add(move);

        while(!queue.isEmpty()){
            Move curr = queue.poll();
            
            int x = curr.x;
            int y = curr.y;
            int dist = curr.dist;
            String path = curr.path;

            if(x == er && y == ec){
                //System.out.println(path);
                if(route.equals("") || path.compareTo(route) < 0){
                    route = path;
                }
                continue;
            }

            int upx = x - 1;
            int downx = x + 1;
            int lefty = y - 1;
            int righty = y + 1;

            if(isValid(upx, y) && (visited[upx][y] == 0 || visited[upx][y] > dist + 1)){
                Move next = new Move(upx, y);
                next.dist = dist + 1;
                next.path = path + "1";
                visited[upx][y] = dist + 1;
                queue.add(next);
            }
            if(isValid(downx, y) && (visited[downx][y] == 0 || visited[downx][y] > dist + 1)){
                Move next = new Move(downx, y);
                next.dist = dist + 1;
                next.path = path + "2";
                visited[downx][y] = dist + 1;
                queue.add(next);
            }if(isValid(x, lefty) && (visited[x][lefty] == 0 || visited[x][lefty] > dist + 1)){
                Move next = new Move(x, lefty);
                next.dist = dist + 1;
                next.path = path + "3";
                visited[x][lefty] = dist + 1;
                queue.add(next);
            }if(isValid(x, righty) && (visited[x][righty] == 0 || visited[x][righty] > dist + 1)){
                Move next = new Move(x, righty);
                next.dist = dist + 1;
                next.path = path + "4";
                visited[x][righty] = dist + 1;
                queue.add(next);
            }
        }

        //System.out.println(route);
    }
    static boolean isValid(int x, int y){
            if(x < 0 || x >= n || y < 0 || y >= n){
                return false;
            }

            if(matrix[x][y] == 1){
                return false;
            }

            return true;
        }
}