import java.util.*;
import java.io.*;

public class Main {

    static class Warrior{
        int x, y;
        boolean stoned;

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
    static ArrayList<Warrior>[][] warrior;
    
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        for(int q= 0; q < 1; q++){
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());

            matrix = new int[n][];
            warrior = new ArrayList[n][];
            for(int i = 0; i<n; i++){
                matrix[i] = new int[n];
                warrior[i] = new ArrayList[n];
                for(int j = 0; j<n; j++){
                    warrior[i][j] = new ArrayList<>();
                }
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
                Warrior war = new Warrior(x, y);
                warrior[x][y].add(war);
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

                warriorMove();
                pcount++;

               // break;
            }
        }
    }
    static int movedWarriorCount, stonedWarriorCount, attackedWarriorCount;
    static void warriorMove(){
        movedWarriorCount = stonedWarriorCount = attackedWarriorCount = 0;

        ArrayList<Warrior>[][] newWarrior = new ArrayList[n][];
        for(int i = 0; i<n; i++){
            newWarrior[i] = new ArrayList[n];
            for(int j = 0; j<n; j++){
                newWarrior[i][j] = new ArrayList<>();
            }
            //System.out.println(Arrays.toString(stonedArea[i]));
        }
        

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(stonedWarrior.contains(i + " " + j)){
                    stonedWarriorCount += warrior[i][j].size();
                    newWarrior[i][j].addAll(warrior[i][j]);
                }else{
                    if(warrior[i][j].size() > 0){
                        int[] first = firstMove(i, j);
                        //System.out.println(i+" " + j + " -> " + Arrays.toString(first));
                        if(first[0] == sr && first[1] == sc){
                            attackedWarriorCount += warrior[i][j].size();
                            movedWarriorCount += warrior[i][j].size();
                            continue;
                        }

                        int[] second = secondMove(first[0], first[1]);
                        //System.out.println(Arrays.toString(second) + "\n");

                        int dist2 = Math.abs(i - second[0]) + Math.abs(j - second[1]);

                        if(first[2] < second[2]){
                            movedWarriorCount += warrior[i][j].size();
                            newWarrior[first[0]][first[1]].addAll(warrior[i][j]);
                            continue;
                        }else{
                            movedWarriorCount += warrior[i][j].size() * dist2;
                        }

                        if(second[0] == sr && second[1] == sc){
                            attackedWarriorCount += warrior[i][j].size();
                        }else{
                            newWarrior[second[0]][second[1]].addAll(warrior[i][j]);
                        }
                    }
                }
            }
        }
        warrior = newWarrior;
        System.out.println(movedWarriorCount + " " + stonedWarriorCount + " " + attackedWarriorCount);
    }

    static int[] secondMove(int x, int y){
        int upx = x-1;
        int downx = x + 1;
        int lefty = y - 1;
        int righty = y + 1;

        int[][] dist = new int[4][3];
        dist[2] = new int[]{upx, y, Math.abs(sr - upx) + Math.abs(sc - y)};
        dist[3] = new int[]{downx, y, Math.abs(sr - downx) + Math.abs(sc - y)};
        dist[0] = new int[]{x, lefty, Math.abs(sr - x) + Math.abs(sc - lefty)};
        dist[1] = new int[]{x, righty, Math.abs(sr - x) + Math.abs(sc - righty)};

        int far = -1;
        int[] toward = new int[3];
        for(int i = 0; i<4; i++){
            if(isValid2(dist[i][0], dist[i][1]) && (far == -1 || far > dist[i][2])){
                far = dist[i][2];
                toward = dist[i];
            }
        }

        if(far == -1){
            return new int[]{x, y, 0};
        }
        return toward;
    }

    static int[] firstMove(int x, int y){
        int upx = x-1;
        int downx = x + 1;
        int lefty = y - 1;
        int righty = y + 1;

        int[][] dist = new int[4][3];
        dist[0] = new int[]{upx, y, Math.abs(sr - upx) + Math.abs(sc - y)};
        dist[1] = new int[]{downx, y, Math.abs(sr - downx) + Math.abs(sc - y)};
        dist[2] = new int[]{x, lefty, Math.abs(sr - x) + Math.abs(sc - lefty)};
        dist[3] = new int[]{x, righty, Math.abs(sr - x) + Math.abs(sc - righty)};

        int far = -1;
        int[] toward = new int[3];
        for(int i = 0; i<4; i++){
            if(isValid2(dist[i][0], dist[i][1]) && (far == -1 || far > dist[i][2])){
                far = dist[i][2];
                toward = dist[i];
            }
        }

        if(far == -1){
            return new int[]{x, y, 0};
        }
        return toward;
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
            uCount += warrior[x][y].size();
        }
        for(Warrior w : lookD){
            int x = w.x;
            int y = w.y;
            dCount += warrior[x][y].size();
        }
        for(Warrior w : lookL){
            int x = w.x;
            int y = w.y;
            lCount += warrior[x][y].size();
        }
        for(Warrior w : lookR){
            int x = w.x;
            int y = w.y;
            rCount += warrior[x][y].size();
        }

        int count = uCount;
        choosed = lookU;
        medusaSee = medusaSeeU;

        if(count < dCount){
            count = dCount;
            choosed = lookD;
            medusaSee = medusaSeeD;
        }
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
        System.out.println(count + ": " + choosed);
        */
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
            if(warrior[sr][j].size() > 0){
                looked.add(warrior[sr][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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
            if(warrior[sr][j].size() > 0){
                looked.add(warrior[sr][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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
            if(warrior[i][sc].size() > 0){
                looked.add(warrior[i][sc].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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
            if(warrior[i][sc].size() > 0){
                looked.add(warrior[i][sc].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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

                if(warrior[i][j].size() > 0){
                    looked.add(warrior[i][j].get(0));
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

        if(warrior[sr][sc].size() > 0){
            warrior[sr][sc] = new ArrayList<>();
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

            if(isValid(upx, y) && (visited[upx][y] == 0 || visited[upx][y] >= dist + 1)){
                Move next = new Move(upx, y);
                next.dist = dist + 1;
                next.path = path + "1";
                visited[upx][y] = dist + 1;
                queue.add(next);
            }
            if(isValid(downx, y) && (visited[downx][y] == 0 || visited[downx][y] >= dist + 1)){
                Move next = new Move(downx, y);
                next.dist = dist + 1;
                next.path = path + "2";
                visited[downx][y] = dist + 1;
                queue.add(next);
            }if(isValid(x, lefty) && (visited[x][lefty] == 0 || visited[x][lefty] >= dist + 1)){
                Move next = new Move(x, lefty);
                next.dist = dist + 1;
                next.path = path + "3";
                visited[x][lefty] = dist + 1;
                queue.add(next);
            }if(isValid(x, righty) && (visited[x][righty] == 0 || visited[x][righty] >= dist + 1)){
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