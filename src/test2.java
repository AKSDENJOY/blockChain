import java.util.Scanner;


public class test2 {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while (sc.hasNext()){
            String line1=sc.nextLine().trim();
            String line2=sc.nextLine().trim();
            System.out.println(filter(line1,line2));
        }
        sc.close();
    }

    private static int filter(String source, String patter) {
        int i=0;
        for (int j=0;i<patter.length();){
            if (patter.charAt(j)=='?'){
                if (j==(patter.length()-1)){
                    return 1;
                }
                i++;
                j++;
                continue;
            }
            if (patter.charAt(j)=='*'){
                if (j==(patter.length()-1)){
                    return 1;
                }
                i=findNextI(patter.charAt(j+1),i,source);
                if (i==-1){
                    return 0;
                }
                j++;
                continue;
            }
            if (patter.charAt(j)==source.charAt(i)){
                i++;
                j++;
                continue;
            }
            else {
                return 0;
            }
        }
        if (i!=(source.length()-1)) {
            return 0;
        }
        return 1;
    }

    private static int findNextI(char c, int index, String source) {
        if (c=='?'){
            return index+1;
        }
        for (int i=index;i<source.length();i++){
            if (c==source.charAt(i)){
                return i;
            }
        }
        return -1;
    }
}
