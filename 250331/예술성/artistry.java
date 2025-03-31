import java.util.*;
import java.io.*;

public class Main {

    static int n;

    static class XY {
        int x, y;

        XY (int x, int y){
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object o){
            if(o instanceof XY){
                XY xy = (XY) o;
                return this.x == xy.x && this.y == xy.y;
            }
            return false;
        }

        public int hashCode(){
            return Objects.hash(x + " " + y);
        }

        public String toString(){
            return "{" + x + ", " + y + "}";
        }
    }

    static class Group{
        int groupNum;
        int num;
        int count;
        List<XY> touched;

        Group(int gn, int num, int count){
            this.groupNum = gn;
            this.num = num;
            this.count = count;

            this.touched = new ArrayList<>();
        }

        public String toString(){
            return groupNum + " : " +  num + ", " + count;
        }
    }

    static int[][] matrix;
    static int[][] temp;
    static Map<Integer, Group> groups;
    static Map<XY, Integer> target;
    static int score = 0;
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());

        matrix = new int[n][];
        temp = new int[n][];
        for(int i = 0; i<n; i++){
            matrix[i] = new int[n];
            temp[i] = new int[n];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                matrix[i][j] = temp[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int r = 0; r<=3; r++){

            int gn = 1;
            groups = new HashMap<>();
            target = new HashMap<>();
            for(int i = 0; i<n; i++){
                for(int j = 0; j<n; j++){
                    if(matrix[i][j] != 0){
                        groups.put(gn, new Group(gn, matrix[i][j], 0));
                        grouping(gn, i, j);
                        gn++;
                    }
                }
            }
            
            for(int i = 0; i<n; i++){
                System.arraycopy(temp[i], 0, matrix[i], 0 , n);
            }

            /*
            for(int i = 0; i<n; i++){
                System.out.println(Arrays.toString(matrix[i]));
            }
            System.out.println();
                */

            getScore();

            rotate();

            for(int i = 0; i<n; i++){
                System.arraycopy(newMatrix[i], 0, matrix[i], 0 , n);
                System.arraycopy(newMatrix[i], 0, temp[i], 0 , n);
            }

        }

        //System.out.println(target.size());

        /*
        for(Integer key : groups.keySet()){
            Group gr = groups.get(key);
            System.out.println(gr.groupNum + " : " + gr.touched.keySet().toString());
        }*/

        System.out.println(score);
    }

    static int[][] newMatrix;
    static void rotate(){
        int mid = n / 2;

        newMatrix = new int[n][];
        for(int i = 0; i<n; i++){
            newMatrix[i] = new int[n];
        }

        for(int i = mid + 1; i< n; i++){
            newMatrix[n - i - 1][mid] = matrix[mid][i];
        }

        for(int i = 0; i< mid; i++){
            newMatrix[mid][i] = matrix[i][mid];
        }

        for(int i = 0; i< mid; i++){
            newMatrix[n - i - 1][mid] = matrix[mid][i];
        }

        for(int i = mid + 1; i< n; i++){
            newMatrix[mid][i] = matrix[i][mid];
        }

        newMatrix[mid][mid] = matrix[mid][mid];

        quarter1(0, 0, mid-1, mid -1);
        quarter2(0, mid+1, mid-1, n-1);
        quarter3(mid + 1, 0, n-1, mid -1);
        quarter4(mid + 1, mid + 1, n-1, n -1);

        /*
        for(int i = 0; i<n; i++){
            System.out.println(Arrays.toString(newMatrix[i]));
        }
        */

    }

    static void quarter1(int x1, int y1, int x2, int y2){
        int mid = n / 2;
        for(int i = x1 ; i <= x2; i++){
            for(int j = y1; j <= y2; j++){
                newMatrix[j][mid - i - 1] = matrix[i][j];

                //System.out.println(j + ", " + (mid - i) + " => "  + i + ", " + j );
            }
        }
    }

    static void quarter2(int x1, int y1, int x2, int y2){
        int mid = n / 2;
        for(int i = x1 ; i <= x2; i++){
            for(int j = y1; j <= y2; j++){
                newMatrix[j - mid - 1][n - i - 1] = matrix[i][j];
            }
        }
    }

    static void quarter3(int x1, int y1, int x2, int y2){
        int mid = n / 2;
        for(int i = x1 ; i <= x2; i++){
            for(int j = y1; j <= y2; j++){
                newMatrix[mid +j + 1][n - i - 1] = matrix[i][j];
            }
        }
    }

    static void quarter4(int x1, int y1, int x2, int y2){
        int mid = n / 2;
        for(int i = x1 ; i <= x2; i++){
            for(int j = y1; j <= y2; j++){
                newMatrix[j][n + mid - i] = matrix[i][j];
            }
        }
    }
    

    static class Contact{
        int bGroup;
        int bCount;
        int bNum;
        int bTouched;

        Contact(int g, int c, int nu, int t){
            this.bGroup = g;
            this.bCount = c;
            this.bNum = nu;
            this.bTouched = t;
        }

        public String toString(){
            return bGroup + " : " + "(" + bNum + ", "  + bTouched + ")";
        }
    }

    static HashMap<Integer, HashMap<Integer, Contact>> johwa;
    static void getScore(){
        johwa = new HashMap<>();
        for(Integer key : groups.keySet()){
            // 현재 그룹에서
            Group gr = groups.get(key);

            // 맞닿아 있는 다른 그룹을 조회
            List<XY> contact = gr.touched;

            if(contact.size() > 0){
                // 결과 값을 구하기 위한 HashMap
                HashMap<Integer, Contact> jojo = johwa.getOrDefault(key, new HashMap<>());

                for(XY kxy : contact){
                    // 맞닿아있는 행렬 구하기
                    int tg = target.get(kxy);
                    Group ggg = groups.get(tg);
                    Contact joc = jojo.getOrDefault(tg, new Contact(tg, ggg.count, ggg.num, 0));

                    joc.bTouched++;
                    jojo.put(tg, joc);
                }
                johwa.put(key, jojo);
            }
        }

        int rotateScore = 0;
        for(int key : johwa.keySet()){
            Group a = groups.get(key);
            HashMap<Integer, Contact> contacts = johwa.get(key);
            for(int tkey : contacts.keySet()){
                Group b = groups.get(tkey);
                Contact con = contacts.get(tkey);
                rotateScore += (a.count + b.count) * a.num * b.num * con.bTouched;

                //System.out.println((a.count + b.count) * a.num * b.num * con.bTouched);
            }
        }
        
        //System.out.println(johwa);

        score = score + rotateScore;
        //System.out.println(rotateScore);
    }

    static void grouping(int gn, int x, int y){

        int upx = x - 1;
        int downx = x + 1;
        int lefty = y - 1;
        int righty = y + 1;

        int valid;

        Group gr = groups.get(gn);
        gr.count++;
        matrix[x][y] = 0;
        target.put(new XY(x, y), gn);

        // 위
        valid = isValid(gr.num, upx, y);
        if(valid == 0){
            // 주변에 맞닿은거 
            gr.touched.add(new XY(upx, y));
        }else if(valid == 1){
            grouping(gn, upx, y);
        }

        // 아래
        valid = isValid(gr.num, downx, y);
        if(valid == 0){
            // 주변에 맞닿은거 
            gr.touched.add(new XY(downx, y));
        }else if(valid == 1){
            grouping(gn, downx, y);
        }

        // 왼쪽
        valid = isValid(gr.num, x, lefty);
        if(valid == 0){
            // 주변에 맞닿은거 
            gr.touched.add(new XY(x, lefty));
        }else if(valid == 1){
            grouping(gn, x, lefty);
        }

        // 오른쪽
        valid = isValid(gr.num, x, righty);
        if(valid == 0){
            // 주변에 맞닿은거 
            gr.touched.add(new XY(x, righty));
        }else if(valid == 1){
            grouping(gn, x, righty);
        }
    }

    static int isValid(int val, int x, int y){
        if(x < 0 || x >= n || y < 0 || y >= n){
            return -1;
        }

        if(matrix[x][y] == 0){
            return -1;
        }

        if(matrix[x][y] != val){
            return 0;
        }

        return 1;
    }
}