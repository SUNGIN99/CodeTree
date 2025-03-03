import java.util.*;
import java.io.*;

public class Main {

    static class Appendent{
        int num;
        int x, y;
        int moveCount = 0;
        boolean out;

        Appendent(int num, int x, int y){
            this.num = num;
            this.x = x;
            this.y = y;
            this.out = false;
        }

        public void moved(int x, int y, boolean m){
            this.x = x;
            this.y = y;
            if(m){
                this.moveCount ++;
            }
            
        }

        public String toString(){
            return num + " =>" +  "(" + x + ", " + y +") :" + moveCount + "_" + out;
        }
    }

    static int N, M, K;
    static int[][] miro;
    static int exitX, exitY;
    static Map<Integer, Appendent> appendent = new HashMap<>();
    static ArrayList<Integer>[][] whereAt;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        miro = new int[N][];
        whereAt = new ArrayList[N][];
        for(int i = 0; i<N; i++){
            miro[i] = new int[N];
            whereAt[i] = new ArrayList[N];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<N; j++){
                miro[i][j] = Integer.parseInt(st.nextToken());
                whereAt[i][j] = new ArrayList<Integer>();
            }
        }

        for(int i = 0; i<M; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;

            appendent.put(i+1, new Appendent(i+1, x, y));
            whereAt[x][y].add(i+1);
        }

        
        st = new StringTokenizer(br.readLine());
        exitX = Integer.parseInt(st.nextToken()) - 1;
        exitY = Integer.parseInt(st.nextToken()) - 1;
        miro[exitX][exitY] = -99;
        
        int time = 0;
        while(time < K){
            
            //System.out.println("exit : " + exitX + " " + exitY);
            //System.out.println(appendent);
            for(int key : appendent.keySet()){
                Appendent a = appendent.get(key);
                if(a.out){
                    continue;
                }

                int curX = a.x;
                int curY = a.y;
                int upX = a.x - 1;
                int downX = a.x + 1;
                int leftY = a.y - 1;
                int rightY = a.y + 1;

                int newX = curX , newY = curY;
                int min = Math.abs(curX - exitX) + Math.abs(curY - exitY);
                if(isValid(upX, curY) ){
                    if(min > Math.abs(upX - exitX) + Math.abs(curY - exitY)){
                        newX = upX;
                        min = Math.abs(upX - exitX) + Math.abs(curY - exitY);
                    }
                }
                if(isValid(downX, curY)){
                    if(min > Math.abs(downX - exitX) + Math.abs(curY - exitY)){
                        newX = downX;
                        min = Math.abs(downX - exitX) + Math.abs(curY - exitY);
                    }
                }
                if(isValid(curX, leftY)){
                    if(min > Math.abs(curX - exitX) + Math.abs(leftY - exitY)){
                        newY = leftY;
                        min = Math.abs(curX - exitX) + Math.abs(leftY - exitY);
                    }
                }
                if(isValid(curX, rightY)){
                    if(min > Math.abs(curX - exitX) + Math.abs(rightY - exitY)){
                        newY = rightY;
                        min = Math.abs(curX - exitX) + Math.abs(rightY - exitY);
                    }
                }

                if(newX == curX && newY == curY){

                }else{
                    
                    //System.out.println("from : " + a);
                    a.moved(newX, newY, true);
                    whereAt[curX][curY].remove(Integer.valueOf(a.num));
                    if(a.x == exitX && a.y == exitY){
                        a.out = true;
                    }else{
                        whereAt[newX][newY].add(a.num);
                        //System.out.println(time + ": " + "move ! -> " + a);
                    }
                    //System.out.println(time + "(" + exitX + ", " + exitY + ")" + ": " + a);
                }
            }

            /*
            for(int i = 0; i<N; i++){
                System.out.println(Arrays.toString(whereAt[i]));
            }
            System.out.println("---");
            for(int i = 0; i<N; i++){
                System.out.println(Arrays.toString(miro[i]));
            }
            */
            

            int[] range = getSquare();

            if(range == null){
                break;
            }

            spin(range);

            time ++;
            
        }

        int sum = 0;
        for(int key : appendent.keySet()){
            Appendent a = appendent.get(key);
            sum += a.moveCount;
        }
        System.out.println(sum);
        System.out.println((exitX+1) + " " + (exitY+1));
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= N || y < 0 || y >= N){
            return false;
        }

        if(miro[x][y] > 0){
            return false;
        }

        return true;
    }

    static void spin(int[] range){
        int size = Math.abs(range[0] - range[2]) + 1;
        int[][] newMiro = new int[size][];
        ArrayList<Integer>[][] newWhereAt = new ArrayList[size][];

        for(int i = 0; i<size; i++){
            newMiro[i] = new int[size];
            newWhereAt[i] = new ArrayList[size];
            for(int j = 0; j<size; j++){
                newWhereAt[i][j] = new ArrayList<>();
            }
        }   

        int x1 = range[0]; //1
        int y1 = range[1]; // 0
        int x2 = range[2]; // 2
        int y2 = range[3];  // 1
        for(int i = 0; i<size; i++){
            for(int j = 0; j<size; j++){
                newMiro[j][size-i-1] = miro[x1+i][y1+j];
                newWhereAt[j][size-i-1] = whereAt[x1+i][y1+j];

                if(newMiro[j][size-i-1] == -99){
                    exitX = x1+j;
                    exitY = y1+size-i-1;
                    newMiro[j][size-i-1] = -99;
                }
                else if(newMiro[j][size-i-1] > 0){
                    newMiro[j][size-i-1] -= 1;
                }

                for(int index : newWhereAt[j][size-i-1]){
                    Appendent a = appendent.get(index);
                    a.moved(x1+j, y1+size-i-1, false);
                    //System.out.println(a);
                }
            }
        }

        for(int i = 0; i<size; i++){
            //System.out.println(Arrays.toString(newMiro[i]));
            System.arraycopy(newMiro[i], 0, miro[x1+i], y1, size);
            System.arraycopy(newWhereAt[i], 0, whereAt[x1+i], y1, size);
        }
        //System.out.println();
    }

    static int[] getSquare(){
        for(int s = 1; s<N; s++){
            for(int i = 0; i < N-s; i++){
                for(int j = 0; j < N-s; j++){
                    int x1 = i;
                    int x2 = i + s;
                    int y1 = j;
                    int y2 = j + s;
                    //System.out.println(x1 + ", " + y1 + ", " + x2 + ", " + y2);

                    if(exitX >= x1 && exitX <= x2 && exitY >= y1 && exitY <= y2){
                        for(int key : appendent.keySet()){
                            Appendent a = appendent.get(key);

                            if(!a.out && a.x >= x1 && a.x <= x2 && a.y >= y1 && a.y <= y2){
                                return new int[]{x1, y1, x2, y2};
                            }
                        }
                    }
                }
            }
            //System.out.println();
        }
        return null;
        
    }
}