import java.util.*;
import java.io.*;

public class Main {

    
    static class Vertex implements Comparable<Vertex>{
        int from, to;
        int weight;

        Vertex(int f, int t, int w){
            this.from = f;
            this.to = t;
            this.weight = w;
        }

        public String toString(){
            return "(" + from + "-> " + to + " : " + weight + ")";
        }

        public int compareTo(Vertex v){
            return this.weight - v.weight;
        }
    }

    static class Travle implements Comparable<Travle>{
        int id;
        int revenue;
        int dest;

        int profit = 0;

        Travle(int i, int r, int d){
            id = i;
            revenue = r;
            dest = d;
        }

        public int compareTo(Travle t){
            if(this.profit == t.profit){
                return this.id - t.id;
            }else{
                return t.profit - this.profit;
            }
        }

        public String toString(){
            return this.id + " : " + profit;
        }

    }

    static int[] dijkstra;
    static HashMap<Integer, ArrayList<Vertex>> graphs;
    static HashMap<Integer, Travle> travles;
    static int n, m;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int q= Integer.parseInt(st.nextToken());
        int start = 0;
        for(int qq= 0; qq < q; qq++){
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if(op == 100){
                graphs = new HashMap<>();
                travles = new HashMap<>();
                dijkstra = new int[n];

                n = Integer.parseInt(st.nextToken());
                m = Integer.parseInt(st.nextToken());
                for(int i = 0; i<n; i++){
                    graphs.put(i, new ArrayList<>());
                }

                for(int i = 0; i<m; i++){
                    int v = Integer.parseInt(st.nextToken());
                    int u = Integer.parseInt(st.nextToken());
                    int w = Integer.parseInt(st.nextToken());

                    Vertex v1 = new Vertex(v, u, w);
                    Vertex v2 = new Vertex(u, v, w);

                    graphs.get(v).add(v1);
                    if(v != u){
                        graphs.get(u).add(v2);
                    }
                    
                }
                
                makeDist(0);
                bestTravle = new PriorityQueue<>();
                //System.out.println(graphs);

            }else if(op == 200){
                int id = Integer.parseInt(st.nextToken());
                int revenue = Integer.parseInt(st.nextToken());
                int dest = Integer.parseInt(st.nextToken());

                Travle t = new Travle(id, revenue, dest);
                travles.put(id, t);

                int profit = t.revenue - dijkstra[dest] ;
                if(dijkstra[dest] == -1 || profit < 0){
                }else{
                    t.profit = profit;
                    bestTravle.add(t);
                }

            }else if(op == 300){
                int id = Integer.parseInt(st.nextToken());
                travles.remove(id);

            }else if(op == 400){
                boolean canTravle = true;
                while(true){
                    if(bestTravle.isEmpty()){
                        canTravle = false;
                        break;
                    }

                    Travle bestT = bestTravle.poll();
                    if(travles.containsKey(bestT.id)){
                        bw.write(bestT.id + "\n");
                        //System.out.println(id);
                        travles.remove(bestT.id);
                        break;
                    }
                }

                if(!canTravle){
                    bw.write(-1 + "\n");
                    //System.out.println(-1);
                }

            }else if(op == 500){
                start = Integer.parseInt(st.nextToken());
                makeDist(start);
                makeTravle(start);
            }
        }

        bw.flush();
        bw.close();
    }

    static PriorityQueue<Travle> bestTravle;
    static void makeTravle(int start){
        bestTravle = new PriorityQueue<>();

        for(int id : travles.keySet()){
            Travle t = travles.get(id);
            int rev = t.revenue; // 수익
            int dest = t.dest; // 목적지

            int cost = dijkstra[dest];
            int profit = rev - cost;
            if(cost == -1 || profit < 0){
                
            }else{
                t.profit = profit;
                bestTravle.add(t);
            }
        }

        //System.out.println(bestTravle);

    }

    static int[] visited;
    static void makeDist(int start){
        dijkstra = new int[n];
        visited =new int[n];

        for(int i = 0; i<n; i++){
            dijkstra[i] = -1;
        }

        visited[start] = -1;
        dijkstra[start] = 0;

        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        queue.add(new Vertex(start, start, 0));

        while(!queue.isEmpty()){
            Vertex v = queue.poll();

            int from = v.from;
            int weight = v. weight;

            ArrayList<Vertex> vex = graphs.get(from);

            for(Vertex nv : vex){
                int to = nv.to;
                int tow = nv.weight;
                if(visited[to] == 0 || visited[to] > weight + tow){
                    Vertex next = new Vertex(to, to, weight + tow);
                    dijkstra[to] = weight + tow;
                    visited[to] = weight + tow;

                    queue.add(next);
                }
            }

        }

        //System.out.println(Arrays.toString(dijkstra));
        //System.out.println(Arrays.toString(visited));

    }
}