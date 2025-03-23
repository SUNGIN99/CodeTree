import java.util.*;
import java.io.*;

public class Main {
    
    static class Player{
        int num;
        int x, y;
        int dir;
        int power;
        int gunPower;
        int point;

        Player(int num, int x, int y, int d, int p, int g){
            this.num = num;
            this.x = x;
            this.y = y;
            this.dir = d;
            this.power = p;
            this.gunPower = g;
        }

        public String toString(){
            return   "[" + num + "] " + x + ", " + y + " : " + dir + ", " + power + "(" + gunPower + ")";
        }

        int getAttkP(){
            return this.power + gunPower;
        }
    }

    static int n, m, k;
    static Player[] players;
    static PriorityQueue<Integer> guns[][];
    static int[][] battleGround;
    static int[] scores;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        guns = new PriorityQueue[n][];
        battleGround = new int[n][];
        for(int i = 0; i<n; i++){
            guns[i] = new PriorityQueue[n];
            battleGround[i] = new int[n];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                guns[i][j] = new PriorityQueue<>(Collections.reverseOrder());
                guns[i][j].add(Integer.parseInt(st.nextToken()));
            }
            //System.out.println(Arrays.toString(guns[i]));
        }

        players = new Player[m+1];
        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            int d = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());

            players[i+1] = new Player(i+1, x, y, d, s, 0);
            battleGround[x][y] = i+1;
        }

        int round = 0;
        scores = new int[m+1];
        while(round < k){
            for(int i = 1; i<= m; i++){
                Player p = players[i];
                // 1 플레이어 한 칸 이동
                Player moved = playerMove(p);
                
                battleGround[p.x][p.y] = 0;
                if(battleGround[moved.x][moved.y] == 0){
                    // 2-1 총줍기
                    getGun(moved);
                    players[i] = moved;
                    battleGround[moved.x][moved.y] = i;
                }else{
                    // 이동해서 싸우기
                    int vIndex = battleGround[moved.x][moved.y];
                    Player versus = players[vIndex];

                    fight(round, moved, versus);

                }

                /*
                if(round >=1){
                    System.out.println(i);
                    for(int j = 0; j<n; j++){
                        System.out.println(Arrays.toString(battleGround[j]));
                    }
                    System.out.println();
                    for(int j = 0; j<n; j++){
                        System.out.println(Arrays.toString(guns[j]));
                    }
                    System.out.println("\n --- \n ");
                }
                */
                
                
            }
            
            round++;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for(int i = 1; i<=m; i++){
            bw.write(scores[i] + " ");
        }
        bw.flush();
        bw.close();
    }

    static void fight(int round, Player moved, Player versus){
        Player winner, loser;
        
        if(moved.getAttkP() > versus.getAttkP()){
            winner = moved;
            loser = versus;
        }else if(moved.getAttkP() < versus.getAttkP()){
            winner = versus;
            loser = moved;
        }else{ //moved.getAttkP() == versus.getAttkP()
            if(moved.power > versus.power){
                winner = moved;
                loser = versus;
            }else{
                winner = versus;
                loser = moved;
            }
        }

        scores[winner.num] += Math.abs(moved.getAttkP() - versus.getAttkP());

        //System.out.println(loser);
        //System.out.println(winner);

        playLoser(loser);
        playWinner(winner);
        
        players[winner.num] = winner;
        players[loser.num] = loser;
        battleGround[winner.x][winner.y] = winner.num;
        battleGround[loser.x][loser.y] = loser.num;

    }

    static void playWinner(Player winner){
        getGun(winner);

    }

    static void playLoser(Player loser){
        int x = loser.x;
        int y = loser.y;
        battleGround[x][y] = 0;

        // 총을 내려놓고
        if(loser.gunPower != 0){
            guns[x][y].add(loser.gunPower);
            loser.gunPower = 0;
        }

        // 방향대로 이동 또는 90 도 회전 이동
        int[] moveIndex = loserMove(x, y, loser.dir);
        loser.x = moveIndex[0];
        loser.y = moveIndex[1];
        loser.dir = moveIndex[2];

        // 총 줍기
        getGun(loser);
    }

    static int[] loserMove(int x, int y, int dir){
        
        int nextX = x, nextY = y, nextD = dir;
        if(dir == 0){ // 상
            nextX = x - 1;
            if(nextX < 0 || battleGround[nextX][nextY] != 0){
                nextD = dir + 1;
                return loserMove(x, y, nextD);
            }
        }else if(dir == 1){ // 우
            nextY = y + 1;
            if(nextY >= n || battleGround[nextX][nextY] != 0){
                nextD = dir + 1;
                return loserMove(x, y, nextD);
            }
        }else if(dir == 2){ // 하
            nextX = x+1;
            if(nextX >= n || battleGround[nextX][nextY] != 0){
                nextD = dir + 1;
                return loserMove(x, y, nextD);
            }
        }else if(dir == 3){ // 좌   
            nextY = y-1;
            if(nextY < 0 || battleGround[nextX][nextY] != 0){
                nextD = 0;
                return loserMove(x, y, nextD);
            }
        }

        return new int[] { nextX, nextY, nextD};
    }

    static void getGun(Player p){        
        int x = p.x;
        int y = p.y;
        int gp = p.gunPower;

        if(!guns[x][y].isEmpty()){
            int maxPower = guns[x][y].peek();
            if(maxPower > gp){
                maxPower = guns[x][y].poll();
                if(gp != 0){
                    guns[x][y].add(gp);
                }
                p.gunPower = maxPower;
            }
        }else{
            
        }
    }

    static Player playerMove(Player p){
        int x = p.x;
        int y = p.y;
        int dir = p.dir;
        
        int nextX = x, nextY = y, nextD = dir;
        if(dir == 0){ // 상
            nextX = x - 1;
            if(nextX < 0){
                nextX = x + 1;
                nextD = 2;
            }
        }else if(dir == 1){ // 우
            nextY = y + 1;
            if(nextY >= n){
                nextY = y - 1;
                nextD = 3;
            }
        }else if(dir == 2){ // 하
            nextX = x+1;
            if(nextX >= n){
                nextX = x - 1;
                nextD = 0;
            }
        }else if(dir == 3){ // 좌   
            nextY = y-1;
            if(nextY < 0){
                nextY = y + 1;
                nextD = 1;
            }
        }

        return new Player(p.num, nextX, nextY, nextD, p.power, p.gunPower);
    }


}