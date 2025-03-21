import java.util.*;
import java.io.*;

public class Main {

    static class Stuck{
        int num;
        Stuck left, right;

        Stuck (int num){
            this.num = num;
        }

        public void addFront(Stuck s){
            s.left = this.left;
            s.right = this;
            this.left.right = s;
            this.left = s;
        }
    }

    static Stuck[] beltHead, beltRear;
    static int[] stuckCount;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int q = Integer.parseInt(st.nextToken());

        int n = 0, m = 0;

        for(int qi = 0; qi<q; qi++){
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if (op == 100){
                n = Integer.parseInt(st.nextToken());
                m = Integer.parseInt(st.nextToken());

                stuckCount = new int[n+1];

                beltHead = new Stuck[n+1];
                beltRear = new Stuck[n+1];
                for(int i = 1; i<=n; i++){
                    Stuck front = new Stuck(-1);
                    Stuck back = new Stuck(-1);

                    front.right= back;
                    back.left = front;

                    beltHead[i] = front;
                    beltRear[i] = back;
                }

                for(int i = 0; i<m; i++){
                    Stuck stuck = new Stuck(i+1);
                    int beltNum = Integer.parseInt(st.nextToken());

                    Stuck back = beltRear[beltNum];
                    back.addFront(stuck);
                    stuckCount[beltNum] ++;
                }

                /*
                for(int i = 1; i<=n; i++){
                    Stuck cur = beltHead[i];
                    while(cur.right != null){
                        System.out.print(cur.num + ", " );
                        cur = cur.right;
                    }
                    System.out.println();
                }*/
            }else if(op == 200){
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());

                stuckCount[to] += stuckCount[from];
                stuckCount[from] = 0;

                Stuck last = beltRear[from].left;
                Stuck first = beltHead[from].right;

                beltRear[from].left = beltHead[from];
                beltHead[from].right = beltRear[from];

                Stuck dstFirst = beltHead[to].right;
                dstFirst.left = last;
                last.right = dstFirst;

                beltHead[to].right = first;
                first.left= beltHead[to];
            }else if(op == 300){
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());

                Stuck srcFirst = beltHead[from].right;
                Stuck dstFirst = beltHead[to].right;

                if(srcFirst.num == -1 && dstFirst.num == -1){
                    // 둘다 비었다면?
                }else if(srcFirst.num != -1 && dstFirst.num == -1){
                    beltHead[from].right = srcFirst.right;
                    srcFirst.right.left = beltHead[from];

                    dstFirst.addFront(srcFirst);
                    
                    stuckCount[from] --;
                    stuckCount[to] ++;
                }else if(srcFirst.num == -1 && dstFirst.num != -1){
                    beltHead[to].right = dstFirst.right;
                    dstFirst.right.left = beltHead[to];

                    srcFirst.addFront(dstFirst);
                    
                }else{
                    beltHead[from].right = srcFirst.right;
                    srcFirst.right.left = beltHead[from];

                    beltHead[to].right = dstFirst.right;
                    dstFirst.right.left = beltHead[to];

                    beltHead[from].right.addFront(dstFirst);
                    beltHead[to].right.addFront(srcFirst);
                }
            }else if(op == 400){
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());

            }else if(op == 500){

            }else if(op == 600){

            }
        }
    }
}