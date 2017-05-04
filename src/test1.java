import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


/**
 * Created by EnjoyD on 2017/4/26.
 */
public class test1 {
    public static void main(String[] args) {

        ArrayList<Integer> inputs = new ArrayList<Integer>();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        if (line != null && !line.isEmpty()) {
            int res = resolve(line.trim());
            System.out.println(String.valueOf(res));
        }
    }

    // write your code here
    public static int resolve(String expr) {
        Stack<Integer> stack=new Stack<>();
        char [] exprTem=expr.toCharArray();
        for (int i=0;i<exprTem.length;){
            if (exprTem[i]<='9' && exprTem[i]>='0'){
                StringBuilder sb=new StringBuilder();
                int j=findNum(exprTem,i,sb);//找到下一个数并且返回下标好让循环继续
                i=j;
                stack.push(Integer.parseInt(sb.toString()));
                if (stack.size()>16){
                    return -2;
                }
                continue;
            }
            if (exprTem[i]==' '){
                i++;
                continue;
            }
            if (exprTem[i]=='^'){
                if (stack.size()==0){
                    return -1;
                }
                int tem=stack.pop();
                tem++;
                stack.push(tem);
                i++;
                continue;
            }
            if (exprTem[i]=='+' || exprTem[i]=='*'){
                if (stack.size()<2){
                    return -1;
                }
                int num1=stack.pop();
                int num2=stack.pop();
                if (exprTem[i]=='+'){
                    stack.push(num1+num2);
                }
                else {
                    stack.push(num1*num2);
                }
                i++;
                continue;
            }
        }
        return stack.pop();


    }

    private static int findNum(char[] exprTem, int start, StringBuilder sb) {
        for (int i=start;i<exprTem.length;i++){
            if (exprTem[i]>='0'&&exprTem[i]<'9'){
                sb.append(exprTem[i]);
            }else {
                return i;
            }
        }
        return exprTem.length;
    }
}

