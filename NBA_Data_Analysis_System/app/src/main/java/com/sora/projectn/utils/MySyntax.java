package com.sora.projectn.utils;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by lenovo on 2016/4/26.
 */
public class MySyntax {

    //建立分析表，输入横纵坐标，输出对应的字符串
    private String parsing(String N, String T){
        String result = new String("");
        String[][] parsingTable = new String[6][9];
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j<9; j++){
                parsingTable[i][j]="ERROR";
            }
        }
        parsingTable[0][4]="S->A";
        parsingTable[0][6]="S->A";
        parsingTable[0][7]="S->A";
        parsingTable[0][8]="S->ε";
        parsingTable[1][4]="A->CB";
        parsingTable[1][6]="A->CB";
        parsingTable[1][7]="A->CB";
        parsingTable[2][0]="B->+CB";
        parsingTable[2][1]="B->-CB";
        parsingTable[2][5]="B->ε";
        parsingTable[2][8]="B->ε";
        parsingTable[3][4]="C->ED";
        parsingTable[3][6]="C->ED";
        parsingTable[3][7]="C->ED";
        parsingTable[4][0]="D->ε";
        parsingTable[4][1]="D->ε";
        parsingTable[4][2]="D->*ED";
        parsingTable[4][3]="D->/ED";
        parsingTable[4][5]="D->ε";
        parsingTable[4][8]="D->ε";
        parsingTable[5][4]="E->(A)";
        parsingTable[5][6]="E->id";
        parsingTable[5][7]="E->num";
        //将输入转换成坐标
        int x = define(N);
        int y = define(T);
        if(x>=0&&y>=0){
            result=parsingTable[define(N)][define(T)];
        }
        else{
            result="ERROR";
        }
        return result;
    }
    //将待分析的终结符、非终结符转换成数字，方便分析表的建立
    private int define(String S){
        int index = -1;
        if(S.equals("S")||S.equals("+")){
            index = 0;
        }
        else if(S.equals("A")||S.equals("-")){
            index = 1;
        }
        else if(S.equals("B")||S.equals("*")){
            index = 2;
        }
        else if(S.equals("C")||S.equals("/")){
            index = 3;
        }
        else if(S.equals("D")||S.equals("(")){
            index = 4;
        }
        else if(S.equals("E")||S.equals(")")){
            index = 5;
        }
        else if(S.equals("id")){
            index = 6;
        }
        else if(S.equals("num")){
            index = 7;
        }
        else if(S.equals("$")){
            index = 8;
        }
        else{//出现不可识别的符号，但不可能发生
        }

        return index;
    }


    //预处理
    private ArrayList<String> pretreatment(String syntax_in){
        System.out.println("syntax_in："+syntax_in);
        ArrayList <String> sentence = new ArrayList<String>();
        String[] temp1;
        temp1 = syntax_in.split(";");//每行分别保存到单独字符串中
        System.out.println("temp1的length："+temp1.length);
        for(int i=0; i<temp1.length; i++){
            System.out.println(temp1[i]+" ");
        }
        //提取token中有用部分
        for(int i=0; i<temp1.length; i++){
            String str = new String("");

            char c=temp1[i].charAt(1);
            if(c=='o'){//如果是操作符 则取第四个
                str=""+temp1[i].charAt(4);
            }
            else if(c=='n'){//如果是数字，则返回num
                str="num";
            }
            else if(c=='i'){
                str="id";
            }
            else if(c=='e'){
                //输入通不过词法分析
                return null;
            }
            else{
                //应该不会有其他情况了
            }
            sentence.add(str);

        }

        return sentence;
    }

    //进行语法分析，输出最左推导
    private  String parse(ArrayList<String> sentence){
        String result = new String("");
        //在末尾加$
        if(sentence.get(0).equals("")){
            sentence.set(0, "$");
        }
        else{
            sentence.add("$");
        }
        System.out.print("待分析的句子：");
        for(int k = 0; k<sentence.size(); k++ ){
            System.out.print(sentence.get(k)+" ");
        }
        System.out.println();
        //建立分析栈
        Stack<String> stack = new Stack<String>();
        stack.push("$");
        stack.push("S");
        //栈未见底时，循环分析每个输入元素
        int i = 0;
        String T = new String("");
        String N = new String("");
        while(!(N=stack.peek()).equals("$")){
            T=sentence.get(i);
            System.out.println("输入指针："+i+","+"栈顶："+N+","+"输入："+T);
            if(N.equals(T)){//匹配
                System.out.println("匹配："+N+" "+T);
                stack.pop();//出栈
                i++;//指针后移
                continue;
            }
            else{//不匹配
                String currentResult = parsing(N,T);
                System.out.println("输入指针："+i+","+"推导:"+currentResult);
                result = result+ currentResult+";";
                //ERROR出现 停止分析
                if(currentResult.equals("ERROR")){
                    result=result+"ERROR";
                    return result;
                }
                else{//未出现ERROR，输出产生式，栈顶出栈，产生式右部入栈
                    stack.pop();
                    String[] temp = currentResult.split("->");
                    String str = temp[1];
                    if(!str.equals("ε")){//产生式右部不为空
                        for(int j=str.length()-1; j>=0; j--){
                            String s = new String("");
                            char c;
                            c=str.charAt(j);
                            if(c=='d'){//若为id，则id一起进栈
                                s="id";
                                j--;
                            }
                            else if(c=='m'){//若为num，则num一起进栈
                                s="num";
                                j=j-2;
                            }
                            else{
                                s=s+c;//普通情况
                            }
                            stack.push(s);
                        }
                    }
                    else{
                        //产生式右部为空
                    }
                }
            }
        }
        //若栈已见底，但输入仍有未分析的部分
        if(!sentence.get(i).equals("$")){
            System.out.println("输入指针："+i+","+"推导:ERROR");
            result=result+"ERROR";
        }

        return result;
    }

    //开始分析句子
    public String parseSentence(String syntax_in){
        String result = "";

        //预处理
        ArrayList<String> sentence=new ArrayList<String>();
        sentence=pretreatment(syntax_in);
        result=parse(sentence);

        return result;
    }

}
