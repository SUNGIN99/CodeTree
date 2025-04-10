import java.util.*;
import java.io.*;

public class Main {

    static class Node{
        int mid;
        int pid;
        int color;
        int mdepth;
        //boolean[] values = new boolean[6];

        List<Node> childs = new ArrayList<>();

        Node(int m, int p, int c, int d){
            this.mid = m;
            this.pid = p;
            this.color = c;
            this.mdepth = d;

            //values[c] = true;
        }

        void add(Node no){
            int deep = 2;

            Node curr = this;
            boolean canAdd = true;
            while(curr.pid != -1){
                if(curr.mdepth >= deep){
                    curr = nodes.get(curr.pid);
                }else{
                    canAdd = false;
                    break;
                }
                deep++;
            }

            if(canAdd && curr.mdepth >= deep){
                this.childs.add(no);
            }
        }

        void colored(int color){
            this.color = color;

            for(Node ch : this.childs){
                ch.color = color;
                dfsCol(ch, color);
            }
        }

        void dfsCol(Node no , int color){
            for(Node ch : no.childs){
                ch.color = color;
                dfsCol(ch, color);
            }
        }

    }

    static Map<Integer, Node> nodes = new HashMap<>();
    static List<Node> parents = new ArrayList<>();
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int q = Integer.parseInt(st.nextToken());
        
        for(int qq= 0; qq<q; qq++){
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if(op == 100){
                int mid = Integer.parseInt(st.nextToken());
                int pid = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                int mdepth = Integer.parseInt(st.nextToken());

                Node nod = new Node(mid, pid, color, mdepth);
                nodes.put(mid, nod);
                if(pid == -1){
                    parents.add(nod);
                }else{
                    Node par = nodes.get(pid);
                    par.add(nod);
                }
            }else if(op == 200){
                int mid = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());

                Node nod = nodes.get(mid);
                nod.colored(color);
        
            }else if(op == 300){
                int mid = Integer.parseInt(st.nextToken());
                System.out.println(nodes.get(mid).color);
            }else if(op == 400){
                result = 0;
                for(Node par : parents){
                    int[] a = getScore(par);
                }

                System.out.println(result);
            }
        }

        //print(parents.get(0));
    }

    static int result;
    static int[] getScore(Node root){
        int[] colors = new int[6];
        for(Node child : root.childs){
            int[] a = getScore(child);

            for(int i = 1; i<=5; i++){
                if(a[i] == 1){
                    colors[i] = a[i];
                }
            }
        }

        colors[root.color] = 1;
        int count = 0;
        for(int i = 1; i<=5; i++){
            if(colors[i] == 1){
                count++;
            }
        }

        //System.out.println(root.mid + " : " + Arrays.toString(colors));

        result += count * count;

        return colors;
    }

    static void print(Node no){
        Queue<Node> queue = new LinkedList<>();
        queue.add(no);

        while(!queue.isEmpty()){
            Node node = queue.poll();
            System.out.println(node.mid + "(" + node.pid + ")");
            for(Node a : node.childs){
                queue.add(a);
            }
        }
    }
}