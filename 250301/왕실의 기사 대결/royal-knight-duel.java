import java.util.*;
import java.io.*;

public class Main {
    static class Soldier{
        int num;
        int x1, y1;
        int x2, y2;
        int k;
        int damaged = 0;

        Soldier(int num, int r, int c, int h, int w, int k){
            this.num = num;
            this.x1 = r;
            this.y1 = c;
            this.x2 = r + h - 1;
            this.y2 = c + w - 1;
            this.k = k;
        }

        public String toString(){
            return num + "(" + x1 + ", " + y2 + "" + ") life:" + (k-damaged);
        }
    }

    static int[][] board;
    static int[][] located;
    static Map<Integer, Soldier> survive = new HashMap<>();
    static int L, N, Q;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[L][];
        located = new int[L][];

        for(int i = 0; i<L; i++){
            st = new StringTokenizer(br.readLine());
            board[i] = new int[L];
            located[i] = new int[L];
            for(int j = 0 ;j<L; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i = 0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) -1;
            int c = Integer.parseInt(st.nextToken()) -1;
            int h = Integer.parseInt(st.nextToken()) ;
            int w = Integer.parseInt(st.nextToken()) ;
            int k = Integer.parseInt(st.nextToken());

            Soldier s = new Soldier(i+1, r, c, h, w, k);
            survive.put(i+1, s);

            charge(i+1, s);
            
        }

        for(int i = 0; i<Q; i++){
            st = new StringTokenizer(br.readLine());
            int is = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            // 0 : 위, 1: 오른, 2: 아래, 3: 왼 
            order(is, d);

            for(int x = 0; x<L; x++){
                for(int y = 0; y<L; y++){
                    if(located[x][y] > 0){
                        Soldier s = survive.get(located[x][y]);
                        if(s.k <= s.damaged){
                            located[x][y] = 0;
                        }
                    }
                    
                }
            }


            //System.out.println(survive);
            //for(int  j = 0; j< L; j++){
            //    System.out.println(Arrays.toString(located[j]));
            //}
        }


        int result = 0;
        for(int key : survive.keySet()){
            Soldier s = survive.get(key);
            if(s.k > s.damaged){
                result += s.damaged;
            }
        }

        System.out.println(result);
    }

    static void charge(int num, Soldier s){
        for(int x = s.x1; x<= s.x2; x++){
                for(int y = s.y1; y<=s.y2; y++){
                    located[x][y] = num;
                }
            }
    }

    static void charge2(Soldier s, int dir, boolean attacked){
        int nextX1 = s.x1, nextX2 = s.x2, nextY1 = s.y1, nextY2 = s.y2;

        if(dir == 0){
            nextX1 = s.x1-1;
            nextX2 = s.x2-1;
            for(int y = s.y1; y <= s.y2; y++){
                located[s.x2][y] = 0;
            }
        }else if(dir == 1){
            nextY1 = s.y1+1;
            nextY2 = s.y2+1;
            for(int x = s.x1; x <= s.x2; x++){
                located[x][s.y1] = 0;
            }
        }else if(dir == 2){
            nextX1 = s.x1+1;
            nextX2 = s.x2+1;
            for(int y = s.y1; y <= s.y2; y++){
                located[s.x1][y] = 0;
            }
        }else { // if(dir == 3)
            nextY1 = s.y1-1;
            nextY2 = s.y2-1;
            for(int x = s.x1; x <= s.x2; x++){
                located[x][s.y2] = 0;
            }
        }

        int damage = 0;
        for(int x = nextX1; x <= nextX2; x++){
            for(int y = nextY1; y <= nextY2; y++){
                located[x][y] = s.num;
                if(board[x][y] == 1){   
                    damage ++;
                }
            }
        }

        s.x1 = nextX1;
        s.x2 = nextX2;
        s.y1 = nextY1;
        s.y2 = nextY2;

        if(attacked){
            s.damaged += damage;
        }else{

        }
    }

    static void order(int is, int dir){
        Soldier s = survive.get(is);
        if(s.damaged >= s.k){
            return;
        }

        Deque<Soldier> moveList = canMove(is, dir);
        //System.out.println(moveList);
        if(moveList.size() == 0){
            return;
        }

        while(moveList.size() > 1){
            Soldier pop = moveList.pollLast();
            charge2(pop, dir, true);
        }


        Soldier pop = moveList.pollLast();
        charge2(pop, dir, false);


    }

    static Deque<Soldier> canMove(int is, int dir){
        Soldier s = survive.get(is);
        
        Deque<Soldier> moveList = new LinkedList<>();
        Queue<Soldier> queue = new LinkedList<>();
        queue.add(s);
        while(!queue.isEmpty()){
            Soldier cur = queue.poll();
            //System.out.println(cur);

            moveList.add(cur);
            int x1 = cur.x1;
            int x2 = cur.x2;
            int y1 = cur.y1;
            int y2 = cur.y2;

            HashSet<Integer> set = new HashSet<>();

            if(dir == 0){
                for(int y = y1; y <= y2; y++){
                    if(!isValid(x1 - 1, y)){
                        return new LinkedList<>();
                    }

                    if(located[x1-1][y] > 0){
                        if(!set.contains(located[x1-1][y])){
                            set.add(located[x1-1][y]);
                            queue.add(survive.get(located[x1-1][y]));
                        }
                    }
                }
            }else if(dir == 1){
                for(int x = x1; x <= x2; x++){
                    if(!isValid(x, y2+1)){
                        return new LinkedList<>();
                    }

                    if(located[x][y2+1] > 0){
                        if(!set.contains(located[x][y2+1])){
                            set.add(located[x][y2+1]);
                            queue.add(survive.get(located[x][y2+1]));
                        }
                    }
                }
            }else if(dir == 2){
                for(int y = y1; y <= y2; y++){
                    if(!isValid(x2 + 1, y)){
                        return new LinkedList<>();
                    }

                    if(located[x2+1][y] > 0){
                        if(!set.contains(located[x2+1][y])){
                            set.add(located[x2+1][y]);
                            queue.add(survive.get(located[x2+1][y]));
                        }
                    }
                }
            }else { // if (dir == 3)
                for(int x = x1; x <= x2; x++){
                    if(!isValid(x, y1-1)){
                        return new LinkedList<>();
                    }

                    if(located[x][y1-1] > 0){
                        if(!set.contains(located[x][y1-1])){
                            set.add(located[x][y1-1]);
                            queue.add(survive.get(located[x][y1-1]));
                        }
                    }
                }
            }
        }

        return moveList;
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= L || y < 0 || y >= L){
            return false;
        }

        if(board[x][y] == 2){
            return false;
        }

        return true;
    }
}