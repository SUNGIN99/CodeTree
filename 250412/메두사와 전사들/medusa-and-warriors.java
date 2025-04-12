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
            for(String d : dir){
                medusaMove(d);

                medusaLook();
                break;
            }
        }
    }

    static int[][] medusaSeeU, medusaSeeD, medusaSeeL, medusaSeeR
    static void medusaLook(){
        medusaSeeU = new int[n][];
        medusaSeeU = new int[n][];
        v
        ArrayList<Warrior> lookU, lookD, lookL, lookR;
        ArrayList<Warrior> choosed = new ArrayList<>();

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

        if(count < dCount){
            count = dCount;
            choosed = lookD;
        }
        if(count < lCount){
            count = lCount;
            choosed = lookL;
        }
        if(count < rCount){
            count = rCount;
            choosed = lookR;
        }

        System.out.println(count + ": " + choosed);
    }

    static ArrayList<Warrior> lookRight(){
        ArrayList<Warrior> looked = new ArrayList<>();

        // sr, sc 
        //왼
        for(int j = sc + 1; j < n; j++){
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