
import java.util.Scanner;
import java.util.Stack;

class ExpressionSolver{
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        String s=scan.next();
        String post=postfix(s);
        calculate(post);
    }


    static String postfix(String s){
          Stack<Character> st=new Stack<>();
          String ans="";
          for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))){
                while(i<s.length()&&Character.isDigit(s.charAt(i))){
                    ans+=s.charAt(i);
                    i++;
                }
                ans+=' ';
                i--;
            }
            else{
                if(st.size()>0&&st.peek()<=s.charAt(i)){
                    ans+=st.pop();
                    i--;
                }
                else
                st.push(s.charAt(i));
            }
          }
          while(st.size()!=0){
            ans+=st.pop();
        }
        System.out.println(ans);
          return ans;
    }



    static void calculate(String s){
        Stack<Double> st=new Stack<>();
        for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))){
                double ans=0;
                while(i<s.length()&&Character.isDigit(s.charAt(i))){
                    ans=ans*10+val(s.charAt(i));
                    i++;
                } 
                st.push(ans);
                i--;
            }
            else if(s.charAt(i)!=' '){
               try{ double a1=st.pop();
                double a2=st.pop();
                double ans=solve(a1,a2,s.charAt(i));
                st.push(ans);}
                catch(Exception e){
                    System.out.println("-----INVALID  INPUT-----");
                    return;
                }
            }
           // System.out.println(st);
        }
        System.out.println("ANS  :"+st.peek());
    }



    static double val(char a){
        return a-'0';
    }



    static double solve(double b,double a,char c){
        switch (c) {
            case '+':
              return (double)a+(double)b;
              case '-':
              return (double)a-(double)b;
              case '*':
              return (double)a*(double)b;
              case '/':
              return (double)a/(double)b;
            default:
                return -1;
        }
    }
}