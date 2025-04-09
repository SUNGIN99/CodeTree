import java.util.*;
import java.io.*;

public class Main {

    static int n, m, k, c;
    static int[][] forest;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        forest = new int[n][];
        spray = new int[n][];
        for(int i = 0; i<n; i++){
            forest[i] = new int[n];
            spray[i] = new int[n];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                forest[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        simulate(m);
    }

    static void simulate(int year){

        int y = 1; 
        int result = 0;
        while(y <= year){
            growTree();

            child();

            int killed = killer(y);
            result += killed;
            gone(y);

            /*
            System.out.println(killed);
            for(int i = 0; i<n; i++){
                System.out.println(Arrays.toString(forest[i]));
            }
            System.out.println();
            for(int i = 0; i<n; i++){
                System.out.println(Arrays.toString(spray[i]));
            }
            System.out.println();
            System.out.println();*/
            y++;

        }

        System.out.println(result);
    }


    static int[][] spray;

    static void gone(int t){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(spray[i][j] != 0 && t - spray[i][j] >= c){
                    spray[i][j] = 0;
                }
            }
        }
    }

    static int killer(int t){
        int maxKill = 0;
        int maxx = 0, maxy= 0;
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(forest[i][j] == -1 || forest[i][j] == 0){
                    continue;
                }

                int killed = forest[i][j];

                int x = i, y= j;
                for(int a = 0; a< k; a++){
                    if(isValid3(--x, --y) == 1){
                        killed += forest[x][y];
                    }else{
                        break;
                    }
                }

                x = i; y = j;
                for(int a = 0; a< k; a++){
                    if(isValid3(--x, ++y) == 1){
                        killed += forest[x][y];
                    }else{
                        break;
                    }
                }

                x = i; y = j;
                for(int a = 0; a< k; a++){
                    if(isValid3(++x, --y) == 1){
                        killed += forest[x][y];
                    }else{
                        break;
                    }
                }

                x = i; y = j;
                for(int a = 0; a< k; a++){
                    if(isValid3(++x, ++y) == 1){
                        killed += forest[x][y];
                    }else{
                        break;
                    }
                }

                if(killed > maxKill){
                    maxKill = killed;
                    maxx = i;
                    maxy = j;
                }else if(killed == maxKill){
                    if(maxx == i){
                        if(maxy > j){
                            maxx = i;
                            maxy = j;
                        }
                    }else if(maxx > i){
                        maxx = i;
                        maxy = j;
                    }
                }
            }
        }
        
        //System.out.println(maxKill + ", " + maxx + ", " + maxy);
        if(forest[maxx][maxy] == 0){
            spray[maxx][maxy]= t;
            return 0;
        }

        forest[maxx][maxy] = 0;
        spray[maxx][maxy]= t;

        int lux, luy, rux, ruy, ldx, ldy, rdx, rdy;
        lux = rux = ldx = rdx = maxx;
        luy = ruy = ldy = rdy = maxy;
        boolean leftup = true, rightup = true, leftdown = true, rightdown = true;
        for(int i = 0; i<k; i++){
            lux--;
            luy--;
            int valid = isValid3(lux, luy);
            if(leftup && valid >= 0){
                if(valid == 0 || valid == 2){
                    leftup = false;
                }
                forest[lux][luy] = 0;
                spray[lux][luy] = t;
            }

            rux--;
            ruy++;
            valid = isValid3(rux, ruy);
            if(rightup && valid >= 0){
                if(valid == 0 || valid == 2){
                    rightup = false;
                }
                forest[rux][ruy] = 0;
                spray[rux][ruy] = t;
            }

            ldx++;
            ldy--;
            valid = isValid3(ldx, ldy);
            if(leftdown && valid >= 0){
                if(valid == 0 || valid == 2){
                    leftdown = false;
                }
                forest[ldx][ldy] = 0;
                spray[ldx][ldy] = t;

            }

            rdx++;
            rdy++;
            valid = isValid3(rdx, rdy);
            if(rightdown && valid >= 0){
                if(valid == 0 || valid == 2){
                    rightdown = false;
                }
                forest[rdx][rdy] = 0;
                spray[rdx][rdy] = t;
            }
        }
        return maxKill;
    }

    static int isValid3(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return -1;
        }

        if(forest[x][y] == -1){
            return 2;
        }

        if(forest[x][y] == 0){
            return 0;
        }

        return 1;


    }

    static void child(){
        int[][] grow = new int[n][];
        for(int i = 0; i<n; i++){
            grow[i] = new int[n];
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(forest[i][j] >= 1){
                    int split = 0;
                    int up = 0, down = 0, left = 0, right = 0;
                    if(isValid2(i-1, j)){
                        split++;
                        up = 1;
                    }
                    if(isValid2(i+1, j)){
                        split++;
                        down = 1;
                    }
                    if(isValid2(i, j-1)){
                        split++;
                        left = 1;
                    }
                    if(isValid2(i, j+1)){
                        split++;
                        right = 1;
                    }

                    if(up == 1){
                        grow[i-1][j] += forest[i][j] / split;
                    }
                    if(down == 1){
                        grow[i+1][j] += forest[i][j] / split;
                    }
                    if(left == 1){
                        grow[i][j-1] += forest[i][j] / split;
                    }
                    if(right == 1){
                        grow[i][j+1] += forest[i][j] / split;
                    }
                }
            }
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(grow[i][j] != 0){
                    forest[i][j] = grow[i][j];
                }
            }
        }
    }

    static boolean isValid2(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return false;
        }

        if(forest[x][y] == 0 && spray[x][y] == 0){
            return true;
        }

        return false;
    }

    static void growTree(){
        int[][] grow = new int[n][];
        
        for(int i = 0; i<n; i++){
            grow[i] = new int[n];
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(forest[i][j] >= 1){

                    if(isValid(i-1, j)){
                        forest[i][j]++;
                    }
                    if(isValid(i+1, j)){
                        forest[i][j]++;
                    }
                    if(isValid(i, j-1)){
                        forest[i][j]++;
                    }
                    if(isValid(i, j+1)){
                        forest[i][j]++;
                    }
                }
            }
        }
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return false;
        }

        if(forest[x][y] == 0 || forest[x][y] == -1){
            return false;
        }

        return true;
    }
}