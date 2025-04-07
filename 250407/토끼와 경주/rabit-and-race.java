import java.util.*;
import java.io.*;

public class Main {

    static int n, m, p;
    static ArrayList<Rabbit>[][] matrix;
    static HashMap<Integer, Rabbit> rabbits = new HashMap<>();
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int qq = Integer.parseInt(st.nextToken());
        for(int q = 0; q<qq; q++){
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if(op == 100){
                n = Integer.parseInt(st.nextToken());
                m = Integer.parseInt(st.nextToken());
                p = Integer.parseInt(st.nextToken());
                matrix = new ArrayList[n][];
                for(int i = 0; i<n; i++){
                    matrix[i] = new ArrayList[m];
                    for(int j = 0; j<m; j++){
                        matrix[i][j] = new ArrayList<>();
                    }
                }

                for(int i = 0; i<p; i++){
                    int pid = Integer.parseInt(st.nextToken());
                    int d = Integer.parseInt(st.nextToken());

                    Rabbit rab = new Rabbit(0, 0, 0, pid, d);
                    rabbits.put(pid, rab);
                    matrix[0][0].add(rab);
                }
                
            }else if(op == 200){
                int k = Integer.parseInt(st.nextToken());
                int s = Integer.parseInt(st.nextToken());
                race(k, s);
            }else if(op == 300){
                int pid = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());

                Rabbit rab = rabbits.get(pid);
                rab.d *= d;

            }else if(op == 400){
                int maxScore = 0;
                //System.out.println(rabbits);
                //System.out.println();
                for(int key: rabbits.keySet()){
                    Rabbit rab = rabbits.get(key);

                    for(int scoKey : makeScore.keySet()){
                        if(rab.pid != scoKey){
                            rab.score += makeScore.get(scoKey);
                        }
                    }
                    maxScore = Math.max(rab.score, maxScore);
                }
                //System.out.println(rabbits);
                //System.out.println();
                System.out.println(maxScore);
            }
        }

    }

    static HashMap<Integer, Integer> makeScore = new HashMap<>();
    static void race(int k, int s){
        PriorityQueue<Rabbit> queue = new PriorityQueue<>();
        for(int key : rabbits.keySet()){
            queue.add(rabbits.get(key));
        }

        int i = 0;
        Rabbit max = null;
        while(i < k){
            Rabbit rab = queue.poll();
            int x = rab.x;
            int y = rab.y;
            int d = rab.d;
            int jump = rab.jump;
            int pid = rab.pid;

            Spot left = toLeft(x, y, d);
            Spot right = toRight(x, y, d);
            Spot up = toUp(x, y, d);
            Spot down = toDown(x, y, d);

            //System.out.println(left+ ", " + right + ", " + up + ", " + down);
            PriorityQueue<Spot> spot = new PriorityQueue<>();
            spot.add(left);
            spot.add(right);
            spot.add(up);
            spot.add(down);

            Spot jumped = spot.poll();
            rab.x = jumped.x;
            rab.y = jumped.y;
            rab.jump ++;

            int soccur = makeScore.getOrDefault(pid, 0);
            soccur += (rab.x + + rab.y + 2);
            makeScore.put(pid, soccur);
            
            queue.add(rab);

            if(max == null || max.equals(null)){
                max = rab;
            }else{
                int mxy = max.x + max.y;
                int rxy = rab.x + rab.y;
                if(mxy < rxy){
                    max = rab;
                }else if (mxy == rxy){
                    if(max.x < rab.x){
                        max = rab;
                    }else if(max.x == rab.x){
                        if(max.y < rab.y){
                            max = rab;
                        }else if(max.y == rab.y){
                            if(max.pid < rab.pid){
                                max = rab;
                            }
                        }
                    }
                }
            }

            i++;
        }

        max.score += s;
    }

    static Spot toRight(int x, int y, int d){
        int end = m - 1;
        int go = (y + d) % (2*end);

        int movey = -1;
        if (go > end){
            movey =  (end) - (go % (end));
        }else{ // go <= end
            movey = go;
        }

        return new Spot(x, movey);
    }

    static Spot toLeft(int x, int y, int d){
        int end = m - 1;
        int go = (y - d);

        int movey = -1;
        if(go < 0){
            int remainDist = -1 * go;
            return toRight(x, 0, remainDist);
        }else{
            movey = go;
        }
        return new Spot(x, movey);
    }

    static Spot toDown (int x, int y, int d){
        int end = n - 1;
        int go = (x + d) % (2 * end);

        int movex = -1;
        if(go > end){
            movex = (end) - (go % (end));
        }else{
            movex = go;
        }

        return new Spot(movex, y);
    }

    static Spot toUp(int x, int y, int d){
        int end = n - 1;
        int go = x - d;
        
        int movex = -1;
        if(go < 0){
            int remainDist = -1 * go;
            return toDown(0, y, remainDist);
        }else{
            movex = go;
        }
        return new Spot(movex, y);
    }

    static class Spot implements Comparable<Spot>{
        int x, y;

        Spot(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int compareTo(Spot s){
            if(this.x + this.y == s.x + s.y){
                if(this.x == s.x){
                    return s.y - this.y;
                }else{
                    return s.x - this.x;
                }
            }else{
                return (s.x + s.y) - (this.x + this.y);
            }
        }

        public String toString(){
            return "{" + x + ", " + y +"}";
        }

    }
    
    static class Rabbit implements Comparable<Rabbit>{
        int jump;
        int x, y;
        int pid;
        int d;
        int score;

        Rabbit(int j, int x, int y, int nu, int d){
            this.jump = j;
            this.x = x;
            this.y = y;
            this.pid = nu;
            this.d = d;
            this.score = 0;
        }

        public int compareTo(Rabbit r){
            if(this.jump == r.jump){
                if(this.x + this.y == r.x + r.y){
                    if(this.x == r.x){
                        if(this.y == r.y){
                            return this.pid - r.pid;
                        }else{
                            return this.y - r.y;
                        }
                    }else{
                        return this.x - r.x;
                    }
                }else{
                    return (this.x + this.y) - (r.x + r.y);
                }
            }else{  
                return this.jump - r.jump;
            }
        }

        public String toString(){
            return pid + " : " + "{" + x + ", " + y + "(" + jump + "," + d + ")" + "score : " + score +"}";
        }

    }

    
}