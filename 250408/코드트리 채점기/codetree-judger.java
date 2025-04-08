import java.util.*;
import java.io.*;

public class Main {

    static int n;

    static class Domain implements Comparable<Domain>{
        int priority;
        int start;
        int end;
        String url;
        String domain;
        int judge;

        Domain(int p, int s, int e, String u, String d){
            this.priority = p;
            this.start = s;
            this.end = e;
            this.url = u;
            this.domain = d;

            this.judge = -1;
        }

        public int compareTo(Domain d){
            if(this.priority != d.priority){
                return this.priority - d.priority;
            }else{
                return this.start - d.start;
            }
        }

        public String toString(){
            return "{" + url+ ", " +priority + "}";
        }
    }

    static HashMap<String, Domain> urls = new HashMap<>();
    static HashMap<String, Domain> judging = new HashMap<>();
    static HashMap<Integer, String> judgingJudge = new HashMap<>();
    static HashMap<String, Domain> history = new HashMap<>();
    static PriorityQueue<Integer> waitingJudge = new PriorityQueue<>();
    static PriorityQueue<Domain> waiting = new PriorityQueue<>();
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int qq = Integer.parseInt(st.nextToken());
        for(int q = 0; q<qq; q++){
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if(op == 100){
                n = Integer.parseInt(st.nextToken());
                String url = st.nextToken();
                String[] u = url.split("/");

                Domain dom = new Domain(1, 0, -1, url, u[0]); 
                waiting.add(dom);
                urls.put(url, dom);

                for(int i = 0; i<n; i++){
                    waitingJudge.add(i+1);
                }
            }else if(op == 200){
                int t = Integer.parseInt(st.nextToken());
                int p = Integer.parseInt(st.nextToken());
                String url = st.nextToken();
                String[] u = url.split("/");

                Domain dom = new Domain(p, t, -1, url, u[0]); 

                if(!urls.containsKey(url)){
                    waiting.add(dom);
                    urls.put(url, dom);
                }
                //System.out.println(waiting);

            }else if(op == 300){
                int t = Integer.parseInt(st.nextToken());
                List<Domain> out = new ArrayList<>();

                while(!waiting.isEmpty()){
                    Domain high = waiting.poll();
                    //System.out.println(high);
                    if(judging.containsKey(high.domain)){
                        out.add(high);
                    }else{
                        Domain hist = history.get(high.domain);

                        if(hist != null){
                            //System.out.println(t + ": " +  high + ", " + hist);
                            //System.out.println(hist.start + 3 * (hist.end - hist.start));
                            //System.out.println(hist.start + " , " + hist.end);
                        }
                        
                        if (history.containsKey(high.domain) && t < hist.start + 3 * (hist.end - hist.start)){
                            out.add(high);
                        }else{
                            if(waitingJudge.isEmpty()){
                                out.add(high);
                                break;
                            }else{
                                int wj = waitingJudge.poll();
                                high.judge = wj;

                                high.start = t;
                                judging.put(high.domain, high);
                                judgingJudge.put(wj, high.domain);

                                urls.remove(high.url);

                                break;
                            }
                        }
                    }
                }

                for(Domain d : out){
                    waiting.add(d);
                }
                //System.out.println(waiting);
                //System.out.println(t + "" + judging);
                //System.out.println();

            }else if(op == 400){
                int t = Integer.parseInt(st.nextToken());
                int jid = Integer.parseInt(st.nextToken());
                if(judgingJudge.containsKey(jid)){
                    String domain = judgingJudge.get(jid);
                    Domain dom = judging.get(domain);
                    dom.end = t;

                    waitingJudge.add(jid);
                    history.put(domain, dom);

                    judgingJudge.remove(jid);
                    judging.remove(domain);
                }

            }else if(op == 500){
                int t = Integer.parseInt(st.nextToken());
                //System.out.println(waiting.size());
                bw.write(waiting.size()+"\n");
            }
        }

        bw.flush();
        bw.close();
    }
}