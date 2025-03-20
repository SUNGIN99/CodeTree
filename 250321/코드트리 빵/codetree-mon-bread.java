import java.util.*;
import java.io.*;

public class Main {

    static class Person{
        int num;
        int x, y;
        int time;
        
        Person (int num, int x, int y, int t){
            this.num = num;
            this.x = x;
            this.y = y;
            this.time = t;
        }

        public String toString(){
            return num + "(" + x + ", " + y + ")";
        }

        public boolean equals(Object o){
            if(o == null){
                return false;
            }
            if(getClass() != o.getClass()){
                return false;
            }
            if(this == o){
                return true;
            }
            
            Person po = (Person) o;
            
            return this.num == po.num;
            
        }

        public int hashCode(){
            return Objects.hash(this.num);
        }
    }

    static int n, m;
    static int[][] matrix;
    static ArrayList<Person>[][] people;
    static HashMap<Integer, int[]> store = new HashMap<>();
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        matrix = new int[n][];
        people = new ArrayList[n][];
        for(int i = 0; i<n; i++){
            matrix[i] = new int[n];
            people[i] = new ArrayList[n];

            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                matrix[i][j] = Integer.parseInt(st.nextToken());
                people[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i< m; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;

            matrix[x][y] = 2;
            store.put(i+1, new int[]{x, y});
        }

        int time = 1;
        while(true){
            if(time > 1){
                // 1. 사람들 움직임
                personMove(time);

                checkArrived();
            }
            
            /*
            System.out.println(store);
            for(int i = 0; i<n; i++){
                for(int j = 0; j<n; j++){
                    System.out.print(people[i][j] + ", ");
                }
                System.out.println();
            }
            System.out.println();
            
            if(time == 7){
                break;
            }*/

            if(store.size() == 0){
                break;
            }


            // 3. 베이스캠프 들어가기
            if(time <= m){
                int[] baseXY = baseCamp(time);
                int baseX = baseXY[0];
                int baseY = baseXY[1];
                //System.out.println(Arrays.toString(baseXY));
                matrix[baseX][baseY] = -1;
                people[baseX][baseY].add(new Person(time, baseX, baseY, time));
            }

            time ++;
        }

        System.out.println(time);
    }

    static void checkArrived(){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(people[i][j].size() > 0){
                    ArrayList<Person> temp = new ArrayList<>();
                    for(Person p : people[i][j]){
                        int[] storeXY = store.get(p.num);
                        int storex = storeXY[0];
                        int storey = storeXY[1];

                        if(p.x == storex && p.y == storey){
                            matrix[p.x][p.y] = -1;
                            //people[i][j].remove(p);
                            temp.add(p);

                            //System.out.println(p.num + " : " + i + ", " + j + temp);
                        }
                    }

                    for(Person p : temp){
                        people[i][j].remove(p);
                        store.remove(p.num);
                        //System.out.println(p.num + " : " + i + ", " + j + people[i][j]);
                    }
                }
            }
        }
    }

    static void personMove(int time){
        ArrayList<Person>[][] newPeople = new ArrayList[n][];

        // 새 이동 후 사람들 초기화
        for(int i = 0; i<n; i++){
            newPeople[i] = new ArrayList[n];
            for(int j = 0; j<n; j++){
                newPeople[i][j] = new ArrayList<>();
            }
        }

        // 사람 있는지 체크
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(people[i][j].size() > 0){
                    for(Person p : people[i][j]){
                        int tt = 1;
                        int count = personRoute(p.num, p.x-1, p.y);
                        int px = p.x - 1;
                        int py = p.y;

                        int routeCount= personRoute(p.num, p.x, p.y-1);
                        if(count > routeCount){
                            px = p.x;
                            py = p.y - 1;
                            tt = 2;
                            count = routeCount;

                        }
                        routeCount = personRoute(p.num, p.x, p.y+1);
                        if(count > routeCount){
                            px = p.x;
                            py = p.y + 1;
                            tt= 3;
                            count = routeCount;
                        }
                        routeCount = personRoute(p.num, p.x+1, p.y);
                        if(count > routeCount){
                            px = p.x + 1;
                            py = p.y;
                            tt=3;
                            count = routeCount;
                        }

                        //System.out.println(tt  + " | "+ p.num + " , " + time + " : " + px + ", " + py + ", " + count);
                        newPeople[px][py].add(new Person(p.num, px, py, time));
                    }
                }
            }
        }

        people = newPeople;
    }

    static int personRoute(int pn, int px, int py){
        visited = new int[n][];
        for(int i = 0; i<n; i++){
            visited[i] = new int[n];
        }

        int[] storesAt = store.get(pn);

        if(!isValid(px, py)){
            return Integer.MAX_VALUE;
        }

        visited[px][py] = -1;

        // 편의점에서 가장 가까운 베이스캠프찾기
        Location init = new Location(px, py, 0);
        PriorityQueue<Location> queue = new PriorityQueue<>();
        queue.add(init);

        while(!queue.isEmpty()){
            Location cur =  queue.poll();

            int x = cur.x;
            int y = cur.y;
            int dist = cur.dist;

            if(x == storesAt[0] && y == storesAt[1]){
                return dist;
            }

            int upx = x-1;
            int downx = x+1;
            int lefty = y-1;
            int righty = y+1;

            if(isValid(upx, y)){
                if (visited[upx][y] == 0 || visited[upx][y] > dist + 1){
                    visited[upx][y] = dist + 1;
                    queue.add(new Location(upx, y, dist + 1));
                }
            }
            if(isValid(x, lefty)){
                if (visited[x][lefty] == 0 || visited[x][lefty] > dist + 1){
                    visited[x][lefty] = dist + 1;
                    queue.add(new Location(x, lefty, dist + 1));
                }
            }
            if(isValid(x, righty)){
                if (visited[x][righty] == 0 || visited[x][righty] > dist + 1){
                    visited[x][righty] = dist + 1;
                    queue.add(new Location(x, righty, dist + 1));
                }
            }
            if(isValid(downx, y)){
                if (visited[downx][y] == 0 || visited[downx][y] > dist + 1){
                    visited[downx][y] = dist + 1;
                    queue.add(new Location(downx, y, dist + 1));
                }
            }
        }

        return Integer.MAX_VALUE;
    }
    

    static class Location implements Comparable<Location>{
        int x, y;
        int dist;

        Location(int x, int y, int d){
            this.x = x;
            this.y = y;
            this.dist = d;
        }

        public int compareTo(Location l){
            if(this.dist == l.dist){
                if(this.x == l.x){
                    return this.y - l.y;
                }else{
                    return this.x - l.x;
                }
            }else{
                return this.dist - l.dist;
            }
        }

        public String toString(){
            return "{" + x + "," + y  + "}";
        }
    }

    static int[][] visited;
    static int[] baseCamp(int time){
        visited = new int[n][];
        for(int i = 0; i<n; i++){
            visited[i] = new int[n];
        }


        int[] storesAt = store.get(time);
        visited[storesAt[0]][storesAt[1]] = -1;

        // 편의점에서 가장 가까운 베이스캠프찾기
        Location init = new Location(storesAt[0], storesAt[1], 0);
        PriorityQueue<Location> queue = new PriorityQueue<>();
        queue.add(init);

        while(!queue.isEmpty()){
            Location cur =  queue.poll();

            int x = cur.x;
            int y = cur.y;
            int dist = cur.dist;

            if(matrix[x][y] == 1){
                return new int[]{x, y};
            }

            int upx = x-1;
            int downx = x+1;
            int lefty = y-1;
            int righty = y+1;

            if(isValid(upx, y)){
                if (visited[upx][y] == 0 || visited[upx][y] > dist + 1){
                    visited[upx][y] = dist + 1;
                    queue.add(new Location(upx, y, dist + 1));
                }
            }
            if(isValid(x, lefty)){
                if (visited[x][lefty] == 0 || visited[x][lefty] > dist + 1){
                    visited[x][lefty] = dist + 1;
                    queue.add(new Location(x, lefty, dist + 1));
                }
            }
            if(isValid(x, righty)){
                if (visited[x][righty] == 0 || visited[x][righty] > dist + 1){
                    visited[x][righty] = dist + 1;
                    queue.add(new Location(x, righty, dist + 1));
                }
            }
            if(isValid(downx, y)){
                if (visited[downx][y] == 0 || visited[downx][y] > dist + 1){
                    visited[downx][y] = dist + 1;
                    queue.add(new Location(downx, y, dist + 1));
                }
            }
        }

        return new int[]{-1, -1};
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return false;
        }
        if(matrix[x][y] == -1){
            return false;
        }

        return true;
    }
}