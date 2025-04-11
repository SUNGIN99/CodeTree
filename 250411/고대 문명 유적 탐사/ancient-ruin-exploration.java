import java.util.*;
import java.io.*;

public class Main {

    static int k, m;
    static int[][] matrix;
    static Queue<Integer> legacy = new LinkedList<>();
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        k = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        matrix = new int[5][];
        for(int i = 0; i<5; i++){
            matrix[i] = new int[5];
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<5; j++){
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }   
        }

        st = new StringTokenizer(br.readLine());
        legacy = new LinkedList<>();
        for(int i = 0; i<m; i++){
            legacy.add(Integer.parseInt(st.nextToken()));
        }

        int turn = 0;
        while(turn < k){
            int sum = 0;
            List<int[]> targeted = checkSpot();

            if(targeted.size() == 0){
                break;
            }

            for(int[] tar : targeted){
                int x = tar[0];
                int y = tar[1];
                //System.out.println(x + ", " + y);
                sum += island2(x, y, matrix[x][y]);
            }

            //print(matrix);

            fillBlock();

            //print(matrix);

            while(true){
                List<int[]> tar1 = new ArrayList<>();
                int result1 = getBlock(matrix, tar1);

                if(result1 == 0){
                    break;
                }

                for(int[] tar : tar1){
                    int x = tar[0];
                    int y = tar[1];

                    //System.out.println(x + ", " + y);
                    sum += island2(x, y, matrix[x][y]);
                }

                //print(matrix);
                fillBlock();
            }

            turn++;
            
            System.out.print(sum + " ");
        }


    }

    static void fillBlock(){
        for(int j = 0; j<5; j++){
            for(int i = 4; i>= 0; i--){
                if(matrix[i][j] == 0){
                    matrix[i][j] = legacy.poll();
                }
            }
        }
    }

    
    static int island2(int x, int y, int val){
        if(!isValid(x, y) || matrix[x][y] == 0 || matrix[x][y] != val){
            //System.out.println(x + ", " + y + ", " + val + ", " + temp[x][y]);
            return 0;
        }   

        int result = 1;
        matrix[x][y] = 0;

        result += island2(x-1, y, val);
        result += island2(x+1, y, val);
        result += island2(x, y-1, val);
        result += island2(x, y+1, val);

        return result;
    }

    static List<int[]> checkSpot(){

        int maxValue = 0;
        int degree = 270;
        int x = 1, y = 1;
        List<int[]> target = new ArrayList<>();
        int[][] newRot = new int[5][];

        for(int i = 1; i<=3; i++){
            for(int j = 1; j<=3; j++){
                List<int[]> tar1 = new ArrayList<>();
                int[][] rot1 = rotated(i, j, matrix);
                int result1 = getBlock(rot1, tar1);

                if(maxValue < result1){
                    maxValue = result1;
                    x = i;
                    y = j;
                    degree = 90;
                    newRot = rot1;
                    target = tar1;
                }else if(maxValue == result1){
                    if(degree > 90){
                        maxValue = result1;
                        x = i;
                        y = j;
                        degree = 90;
                        newRot = rot1;
                        target = tar1;
                    }else if(degree == 90){
                        if(y > j){
                            maxValue = result1;
                            x = i;
                            y = j;
                            degree = 90;
                            newRot = rot1;
                            target = tar1;
                        }else if(y == j){
                            if(x > i){
                                maxValue = result1;
                                x = i;
                                y = j;
                                degree = 90;
                                newRot = rot1;
                                target = tar1;
                            }
                        }
                    }
                }

                
                List<int[]> tar2 = new ArrayList<>();
                int[][] rot2 = rotated(i, j, rot1);
                int result2 = getBlock(rot2, tar2);

                if(maxValue < result2){
                    maxValue = result2;
                    x = i;
                    y = j;
                    degree = 180;
                    newRot = rot2;
                    target = tar2;
                }else if(maxValue == result2){
                    if(degree > 180){
                        maxValue = result2;
                        x = i;
                        y = j;
                        degree = 180;
                        newRot = rot2;
                        target = tar2;
                    }else if(degree == 180){
                        if(y > j){
                            maxValue = result2;
                            x = i;
                            y = j;
                            degree = 180;
                            newRot = rot2;
                            target = tar2;
                        }else if(y == j){
                            if(x > i){
                                maxValue = result2;
                                x = i;
                                y = j;
                                degree = 180;
                                newRot = rot2;
                                target = tar2;
                            }
                        }
                    }
                }

                
                List<int[]> tar3 = new ArrayList<>();
                int[][] rot3 = rotated(i, j, rot2);
                int result3 = getBlock(rot3, tar3);

                if(maxValue < result3){
                    maxValue = result3;
                    x = i;
                    y = j;
                    degree = 270;
                    newRot = rot3;
                    target = tar3;
                }else if(maxValue == result3){
                    if(degree == 270){
                        if(y > j){
                            maxValue = result3;
                            x = i;
                            y = j;
                            degree = 270;
                            newRot = rot3;
                            target = tar3;
                        }else if(y == j){
                            if(x > i){
                                maxValue = result3;
                                x = i;
                                y = j;
                                degree = 270;
                                newRot = rot3;
                                target = tar3;
                            }
                        }
                    }
                }
            }
        }


        matrix = newRot;
        return target;
        /*
        System.out.println(maxValue + ", " + x + ", " + y + ", " + degree);
        for(int[] a : target){
            System.out.println(a[0] + ", " + a[1]);
        }
        */
    }

    static int[][] temp;
    static int getBlock(int[][] mat, List<int[]> tar){
        
        int[][] rot = copyMat(mat);
        temp = rot;
        int value = 0;
        for(int i = 0; i<5; i++){
            for(int j = 0; j<5; j++){
                if(temp[i][j] != 0){
                    int result = island(i, j, temp[i][j]);
                    if(result >= 3){
                        tar.add(new int[]{i, j});
                        value += result;
                    }
                }
            }
        }

        return value;
    }

    static int island(int x, int y, int val){
        if(!isValid(x, y) || temp[x][y] == 0 || temp[x][y] != val){
            //System.out.println(x + ", " + y + ", " + val + ", " + temp[x][y]);
            return 0;
        }   

        int result = 1;
        temp[x][y] = 0;

        result += island(x-1, y, val);
        result += island(x+1, y, val);
        result += island(x, y-1, val);
        result += island(x, y+1, val);

        return result;
    }

    static boolean isValid(int x, int y){
        if(x < 0 || x >= 5 || y < 0 || y >= 5){
            return false;
        }

        return true;
    }


    static int[][] rotated(int x, int y, int[][] mat){
        int[][] rot = copyMat(mat);
        x = x-1;
        y = y-1;

        for(int i = 0; i<3; i++){
            rot[x][y+i] = mat[x+2-i][y];
        }

        for(int i = 0; i<3; i++){
            rot[x+i][y+2] = mat[x][y+i];
        }

        for(int i = 0; i<3; i++){
            rot[x+2][y+i] = mat[x+2-i][y+2];
        }

        for(int i = 0; i<3; i++){
            rot[x+i][y] = mat[x+2][y+i];
        }

        return rot;
    }

    static int[][] copyMat(int[][] mat){
        int[][] rot = new int[5][];
        for(int i = 0; i<5; i++){
            rot[i] = new int[5];
            System.arraycopy(mat[i], 0, rot[i], 0, 5);

        }

        return rot;
    }

    static void print(int[][] mat){
        for(int i = 0; i<5; i++){
            System.out.println(Arrays.toString(mat[i]));
        }
    }
}