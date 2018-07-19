import java.util.*;
import java.lang.*;
import java.io.*;

public class smith_waterman{
    private String A,B;
    private char[][] scoring_scheme;
    private float penalty;
    private float[][] scoring_dataSheet;
    private String[] answer;
    private int count=0;

    public smith_waterman(){
        A=null;
        B=null;
        scoring_scheme=null;
        penalty=0;
        scoring_dataSheet=null;
        answer=new String[2];
    }
    private void scoring(boolean isGlobal){
        scoring_dataSheet_initial();
        scoring_calculation(isGlobal);

        if(isGlobal)traceBack_global(scoring_dataSheet.length,scoring_dataSheet[0].length,null);
        else traceBack_local();
    }

    private boolean scoring_dataSheet_initial(){
        scoring_dataSheet=new float[A.length()+1][B.length()+1];
        try{
            scoring_dataSheet[0][0]=0;
            for(int i=1;i<scoring_dataSheet[0].length;i++){
                scoring_dataSheet[0][i]=0;
            }
            for(int j=1;j<scoring_dataSheet.length;j++){
                scoring_dataSheet[j][0]=0;
            }

        }catch(Exception e){
            return false;
        }
        return true;
    }
    public void scoring_calculation(boolean isGlobal){
        for(int i=1;i<scoring_dataSheet.length;i++){
            for(int j=1;j<scoring_dataSheet[0].length;j++){
                scoring_dataSheet[i][j]=maximun(isGlobal,i,j);
            }
        }
    }
    private boolean traceBack_global(int i,int j,String traceBack_direction){
        try{
            count++;
            if(traceBack_direction.equals("reverse_diagonal")){
                answer[0]+=A.charAt(i);
                answer[1]+=B.charAt(j);
            }else if(traceBack_direction.equals("leftward")){
                answer[0]+='-';
                answer[1]+=B.charAt(j);
            }else if(traceBack_direction.equals("upward")){
                answer[0]+=A.charAt(i);
                answer[1]+='-';
            }else{
                if(scoring_dataSheet[i-1][j]==scoring_dataSheet[i][j]+matching(i,j,"downward"))
                    traceBack_global(i,j,"upward");
                if(scoring_dataSheet[i][j-1]==scoring_dataSheet[i][j]+matching(i,j,"rightward"))
                    traceBack_global(i,j,"leftward");
                if(scoring_dataSheet[i-1][j-1]==scoring_dataSheet[i][j]+matching(i,j,"diagonal"))
                    traceBack_global(i,j,"reverse_diagonal");
            }
            if(i==0&&j==0){
                System.out.println("the length of the matched seq:"+count);
                System.out.println(answer[0]);
                System.out.println(answer[1]);
                answer[0]="";
                answer[1]="";
                count=0;
                return false;
            }else if(i==0||j==0){

                return false;
            }else{
                if(scoring_dataSheet[i-1][j]==scoring_dataSheet[i][j]+matching(i,j,"downward"))
                    traceBack_global(i-1,j,"upward");
                if(scoring_dataSheet[i][j-1]==scoring_dataSheet[i][j]+matching(i,j,"rightward"))
                    traceBack_global(i,j-1,"leftward");
                if(scoring_dataSheet[i-1][j-1]==scoring_dataSheet[i][j]+matching(i,j,"diagonal"))
                    traceBack_global(i-1,j-1,"reverse_diagonal");
            }
        }catch(Exception e){
        if(e.getMessage().equals("cannot analyze direction"))
            System.out.println("cannot trace back because of unknown direction during tracing");
        if(i<0||j<0||i>=scoring_dataSheet.length||j>=scoring_dataSheet[0].length)
            System.out.println("IndexOutOfBondException");
        }finally{
            return false;
        }
    }
    private void traceBack_local(){
        answer[0]+=A.charAt(i);
        answer[1]+=B.charAt(j);
        count++;
        if()
    }
    private float maximun(boolean isGlobal,int i,int j){
        float temp_socre=0;
        try{
            if(isGlobal){
                temp_socre=scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal");
                if(scoring_dataSheet[i-1][j]+matching(i,j,"downward")>temp_socre)
                    temp_socre=scoring_dataSheet[i-1][j]+matching(i,j,"downward");
                if(scoring_dataSheet[i][j-1]+matching(i,j,"rightward")>temp_socre)
                    temp_socre=scoring_dataSheet[i][j-1]+matching(i,j,"rightward");
            }else if(!isGlobal){
                if(scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal")>temp_socre)
                    temp_socre=scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal");
                if(scoring_dataSheet[i-1][j]+matching(i,j,"downward")>temp_socre)
                    temp_socre=scoring_dataSheet[i-1][j]+matching(i,j,"downward");
                if(scoring_dataSheet[i][j-1]+matching(i,j,"rightward")>temp_socre)
                    temp_socre=scoring_dataSheet[i][j-1]+matching(i,j,"rightward");
            }
        }catch (Exception e){
            if(e.getMessage().equals("cannot find character")) System.out.println("cannot find character in arrays");
            if(e.getMessage().equals("cannot analyze direction")) System.out.println("cannot analyze direction");
        }
        finally{
            return temp_socre;
        }
    }
    private float matching(int i,int j,String direction)throws Exception{
        int a_pos=0,b_pos=0;
        switch (direction){
            case "diagonal":{
                try{
                    char a=A.charAt(i-1),b=B.charAt(j-1);
                    for(int k=0;k<scoring_scheme.length;k++) if(a==scoring_scheme[k][0]) a_pos=k;
                    for(int m=0;m<scoring_scheme[0].length;m++) if(b==scoring_scheme[0][m]) b_pos=m;
                    if(a_pos==0||b_pos==0) throw new Exception("cannot find character");
                }finally {
                    return Integer.parseInt(String.valueOf(scoring_scheme[a_pos][b_pos]));
                }
            }
            case "rightward": return penalty;
            case  "downward": return penalty;
            default: {
                throw new Exception("cannot analyze direction");
            }
        }
    }


    /**
     * get methods
     */
    private String getA(){
        return this.A;
    }
    private String getB(){
        return this.B;
    }
    private String[] getAnswer(){
        return this.answer;
    }
    private float getPenalty(){
        return this.penalty;
    }
    private char[][] getScoring_scheme(){
        return this.scoring_scheme;
    }
    private float[][] getScoring_dataSheet(){
        return this.scoring_dataSheet;
    }

    /**
     * set methods
     */

    private void setA(String A){
        this.A=A;
    }
    private void setB(String B){
        this.B=B;
    }
    private void setScoring_scheme(char[][] scoring_scheme){
        this.scoring_scheme=scoring_scheme;
    }
    private void setPenalty(float penalty){
        this.penalty=penalty;
    }
    private void setScoring_dataSheet(float[][] scoring_dataSheet){
        this.scoring_dataSheet=scoring_dataSheet;
    }
    public smith_waterman(String types){
        switch (types){
            case "default": new smith_waterman();
            case "Settings from files": new smith_waterman();
        }
    }
}
