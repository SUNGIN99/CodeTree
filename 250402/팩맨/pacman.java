import java.io.*;
import java.util.*;

public class Main {

    public static class Monster{
        int x, y;
        int dir;
        
        int died;


        Monster(int x, int y, int d){
            this.x = x;
            this.y = y;
            this.dir = d;
        }

        Monster(int x, int y, int d, int de){
            this.x = x;
            this.y = y;
            this.dir = d;
            this.died = de;
        }

        public String toString(){
            return "{" + x + ", " + y +  ", " + dir +"}";
        }
    }
    
    static ArrayList<Monster>[][] monsters;
    static ArrayList<Monster>[][] eggs;
    static ArrayList<Monster> [][] deads;

    static int m, t;
    static int pacx, pacy;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());   

        st = new StringTokenizer(br.readLine());
        pacx = Integer.parseInt(st.nextToken()) - 1;
        pacy = Integer.parseInt(st.nextToken()) - 1;

        monsters = new ArrayList[4][];
        eggs = new ArrayList[4][];
        deads = new ArrayList[4][];
        for(int i = 0; i<4; i++){
            monsters[i] = new ArrayList[4];
            eggs[i] = new ArrayList[4];
            deads[i] = new ArrayList[4];

            for(int j = 0; j<4; j++){
                monsters[i][j] = new ArrayList<>();
                eggs[i][j] = new ArrayList<>();
                deads[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken());

            monsters[r][c].add(new Monster(r, c, d));
        }

        int turn = 1;
        while(turn <= t){

            monsterCopy();

            monsterMove();

            String toward = pacmanRoute();
            //System.out.println(toward);
            pacmanMove(toward, turn);

            rip(turn);

            bornCopy();

            turn ++;
        }


        int sum = 0;
        for(int i = 0; i<4; i++){
            //System.out.print(Arrays.toString(monsters[i]));
            //System.out.println();
            for(int j = 0; j<4; j++){
                sum += monsters[i][j].size();
            }
        }
        System.out.println(sum);
        
        
    }

    static void bornCopy(){
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                for(Monster m : eggs[i][j]){
                    monsters[i][j].add(m);
                }
                eggs[i][j] = new ArrayList<>();
            }
        }
    }

    static void rip(int turn){
        ArrayList<Monster>[][] newDead = new ArrayList[4][];
        for(int i = 0; i<4; i++){
            newDead[i] = new ArrayList[4];
            for(int j = 0; j<4; j++){
                newDead[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                for(Monster m : deads[i][j]){
                    int deadTurn = m.died;
                    if(turn - deadTurn >= 2){

                    }else{
                        newDead[i][j].add(m);
                    }
                }
            }
        }

        deads = newDead;
    }

    static void pacmanMove(String route, int turn){
        int curx = pacx;
        int cury = pacy;
        for(String r : route.split("")){
            if(r.equals("1")){
                curx = curx-1;
            }else if(r.equals("2")){
                cury = cury-1;
            }else if(r.equals("3")){
                curx = curx+1;
            }else if(r.equals("4")){
                cury = cury+1;
            }

            for(Monster m : monsters[curx][cury]){
                deads[curx][cury].add(new Monster(curx, cury, -1, turn));
            }
            monsters[curx][cury] = new ArrayList<>();
        }

        pacx = curx;
        pacy = cury;
    }

    static class Route implements Comparable<Route>{
        int x;
        int y;
        int count;
        String priority;

        HashSet<String> visited = new HashSet<>();

        Route(int x, int y, int c, String p){
            this.x = x;
            this.y= y;
            this.count = c;
            this.priority = p;
        }

        public int compareTo(Route r){
            if(this.count != r.count){
                return r.count - this.count;
            }else{
                return this.priority.compareTo(r.priority);
            }
        }

        public void set(HashSet<String> sets){
            for(String s : sets){
                this.visited.add(s);
            }
        }
    }

    static String pacmanRoute(){
        int curX = pacx;
        int curY = pacy;

        int maxCount = 0;
        String maxRoute = "444";

        PriorityQueue<Route> queue = new PriorityQueue<>();
        queue.add (new Route(curX, curY, 0, ""));
        while(!queue.isEmpty()){
            Route route = queue.poll();
            int x = route.x;
            int y = route.y;
            int count = route.count;
            String prior = route.priority;
            HashSet<String> visited = route.visited;

            if(prior.length() == 3){
                if(maxCount < count){
                    maxCount = count;
                    maxRoute = prior;
                }else{
                    if(maxCount == count && maxRoute.compareTo(prior) > 0){
                        maxRoute = prior;
                    }
                }

                continue;
            }

            // 상 1, 좌 2, 하 3, 우 4
            int upx = x-1;
            int downx = x+1;
            int lefty = y-1;
            int righty = y+1;

            if(isValid2(upx, y)){
                Route newr = new Route(upx, y, count, prior + "1");
                newr.set(visited);
                if(!newr.visited.contains(upx + " " + y)){
                    newr.count += monsters[upx][y].size();
                    newr.visited.add(upx + " " + y);
                }
                queue.add(newr);
            }
            if(isValid2(x, lefty)){
                Route newr = new Route(x, lefty, count, prior + "2");
                newr.set(visited);
                if(!newr.visited.contains(x + " " + lefty)){
                    newr.count += monsters[x][lefty].size();
                    newr.visited.add(x + " " + lefty);
                }
                queue.add(newr);
            }
            if(isValid2(downx, y)){
                Route newr = new Route(downx, y, count, prior + "3");
                newr.set(visited);
                if(!newr.visited.contains(downx + " " + y)){
                    newr.count += monsters[downx][y].size();
                    newr.visited.add(downx + " " + y);
                }
                queue.add(newr);
            }
            if(isValid2(x, righty)){
                Route newr = new Route(x, righty, count, prior + "4");
                newr.set(visited);
                if(!newr.visited.contains(x + " " + righty)){
                    newr.count += monsters[x][righty].size();
                    newr.visited.add(x + " " + righty);
                }
                queue.add(newr);
            }

        }

        return maxRoute;


        // 최종 최신화 해주기
        //pacx = -1;
        //pacy = -1;
    }

    static boolean isValid2(int x, int y){
        if(x < 0 || x >= 4 || y < 0 || y >= 4){
            return false;
        }
        return true;
    }

    static void monsterMove(){
        ArrayList<Monster>[][] newMon = new ArrayList[4][];
        for(int i = 0; i<4; i++){
            newMon[i] = new ArrayList[4];
            for(int j = 0; j<4; j++){
                newMon[i][j] = new ArrayList<>();
            }
        }


        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                if(monsters[i][j].size() > 0){
                    for(Monster m : monsters[i][j]){
                        Monster moved = getDir(m, m.dir, m.dir);
                        newMon[moved.x][moved.y].add(moved);
                    }
                }
            }
        }

        monsters = newMon;
    }

    static Monster getDir(Monster mon, int curD, int firstD){
        int x = mon.x;
        int y = mon.y;

        int nextDir = curD + 1 > 8 ? 1 : curD + 1;

        int[] nextXY = nextDir(x, y, curD);
        int nextX = nextXY[0];
        int nextY = nextXY[1];

        if(isValid(nextX, nextY)){
            return new Monster(nextX, nextY, curD);
        }else{
            if(nextDir != firstD){
                return getDir(mon, nextDir, firstD);
            }else{
                return mon;
            }
            
        }


    }

    static int[] nextDir(int x, int y, int dir){
        
        // 1: 위
        // 2: 왼위
        // 3: 왼
        // 4: 왼 아래
        // 5: 아래
        // 6: 오른 아래
        // 7: 오른
        // 8: 오른 위
        if(dir == 1){
            return new int[]{x-1, y};
        }else if(dir == 2){
            return new int[]{x-1, y-1};
        }else if(dir == 3){
            return new int[]{x, y -1};
        }else if(dir == 4){
            return new int[]{x+1, y-1};
        }else if(dir == 5){
            return new int[]{x+1, y};
        }else if(dir == 6){
            return new int[]{x+1, y+1};
        }else if(dir == 7){
            return new int[]{x, y+1};
        }else{// if(dir == 8){
            return new int[]{x-1, y+1};
        }
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= 4 || y < 0 || y >= 4){
            return false;
        }

        if(deads[x][y].size() > 0 ){
            return false;
        }

        if(x == pacx && y == pacy){
            return false;
        }

        return true;
    }

    static void monsterCopy(){

        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                if(monsters[i][j].size() > 0){
                    for(Monster m : monsters[i][j]){
                        eggs[i][j].add(new Monster(i, j, m.dir));
                    }
                    
                }

            }
        }
    }
}