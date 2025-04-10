import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());


        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        forest = new int[r][];
        for(int i = 0; i<r; i++){
            forest[i] = new int[c];
        }

        result  = 0;
        for(int i = 0; i<k; i++){
            st = new StringTokenizer(br.readLine());
            int col = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken()); // 0 1 2 3 북동남서 

            godown(i+1, col, d);
            //System.out.println();
        }

        System.out.println(result);

    }

    static int r, c, k;
    static int[][] forest;

    static class Golem{
        int ux, dx;
        int ly, ry;

        int x, y;
        int dir;
         
        int num;

        public void info(){
            System.out.println("[" + dir +"]");
            System.out.println("  " + ux + "  ");
            System.out.println(ly +" (" + x + ", " + y + ") "+ ry);
            System.out.println("  " + dx + "  ");
        }
        Golem(int num, int col, int d){
            this.num = num;
            x = -2;
            y = col;

            ux = -3;
            dx = -1;

            ly = y - 1;
            ry = y + 1;

            dir = d;
        }

        void stop(){
            // dir 0123 북동남서
            forest[x][y] = num ;
            forest[ux][y] = num * (dir==0 ? -1 : 1);
            forest[dx][y] = num * (dir==2 ? -1 : 1);
            forest[x][ly] = num * (dir==3 ? -1 : 1);
            forest[x][ry] = num * (dir==1 ? -1 : 1);
        }

        void goDown(){
            x++;
            ux++;
            dx++;
        }

        void goLeft(){
            x++;
            y--;
            ux++;
            dx++;
            ly--;
            ry--;
            dir = dir == 0 ? 3 : dir - 1;
        }

        void goRight(){
            x++;
            y++;
            ux++;
            dx++;
            ly++;
            ry++;
            dir = dir== 3 ? 0 : dir + 1;
        }

        boolean canDown(){
            if(x == -2){
                return isValid(dx + 1, y);
            }else{
                return isValid(dx+1, y) && isValid(dx, ly) && isValid(dx, ry);
            }
        }

        boolean canLeft(){
            if(x == -2){
                return isValid(dx + 1, ly);
            }else if(x == -1){
                return isValid(dx + 1, ly) && isValid(dx, ly - 1) && isValid(dx, ly);
            }else if(x == 0){
                return isValid(dx + 1, ly) && isValid(dx, ly - 1) && isValid(dx, ly) && isValid(x, ly - 1);
            }

            return isValid(dx + 1, ly) && isValid(dx, ly - 1) && isValid(dx, ly) && isValid(x, ly - 1) && isValid(ux, ly);
        }

        boolean canRight(){
            if(x == -2){
                return isValid(dx + 1, ry);
            }else if(x == -1){
                return isValid(dx + 1, ry) && isValid(dx, ry + 1) && isValid(dx, ry);
            }else if(x == 0){
                return isValid(dx + 1, ry) && isValid(dx, ry + 1) && isValid(dx, ry) && isValid(x, ry + 1);
            }

            return isValid(dx + 1, ry) && isValid(dx, ry + 1) && isValid(dx, ry) && isValid(x, ry + 1) && isValid(ux, ry);

        }

        boolean isValid(int x, int y){
            if(x < 0 || x >= r || y < 0 || y >= c){
                return false;
            }

            if(forest[x][y] != 0){
                return false;
            }

            return true;
        }
    }
    static void godown(int num, int y, int dir){

        Golem gol = new Golem(num, y, dir);
        //gol.info();
        while(true){
            if(gol.canDown()){
                gol.goDown();
            }else{
                if(gol.canLeft()){
                    gol.goLeft();
                }else{
                    if(gol.canRight()){
                        gol.goRight();
                    }else{
                        break;
                    }
                }
            }
        }

        //gol.info();

        if(gol.ux < 0){
            //System.out.println("bomb");
            forest = new int[r][];
            for(int i = 0; i<r; i++){
                forest[i] = new int[c];
            }
        }else{
            gol.stop();

            /*
            for(int i = 0; i<r; i++){
                System.out.println(Arrays.toString(forest[i]));
            }
            */
            deep(gol);
        }
    }

    static int result = 0;
    static boolean[][] visited;
    static void deep(Golem gol){
        visited= new boolean[r][];
        for(int i = 0; i<r; i++){
            visited[i] = new boolean[c];
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{gol.x, gol.y, gol.num});

        int deepx = 0;

        while(!queue.isEmpty()){
            int[] xy = queue.poll();
            int x = xy[0];
            int y = xy[1];
            int num = xy[2];

            deepx = Math.max(deepx, x);

            if(visited[x][y]){
                continue;
            }

            visited[x][y] = true;

            if(isValid2(x-1, y, num)){
                queue.add(new int[]{x-1, y, forest[x-1][y]});
            }

            if(isValid2(x+1, y, num)){
                queue.add(new int[]{x+1, y, forest[x+1][y]});
            }

            if(isValid2(x, y-1, num)){
                queue.add(new int[]{x, y-1, forest[x][y-1]});
            }

            if(isValid2(x, y+1, num)){
                queue.add(new int[]{x, y+1, forest[x][y+1]});
            }
        }

        //System.out.println(deepx + 1);
        result += (deepx + 1);
    }    
    

    static boolean isValid2(int x, int y, int num){
        if(x < 0 || x >= r || y < 0 || y >= c){
            return false;
        }

        if(visited[x][y]){
            return false;
        }

        if(num < 0 && forest[x][y] != 0){
            return true;
        }
        
        if(forest[x][y] == num || forest[x][y] * -1 == num){
            return true;
        }else{
            return false;
        }

        // if(forest[x][y] != num){
        //     return false;
        // }


    }
}