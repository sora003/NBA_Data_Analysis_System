package com.sora.projectn.utils;

/**
 * Created by lenovo on 2016/4/26.
 */
public class MyLexical {
    public String getToken(String in)
    {
        String token="";
        String token_type="";
        String token_content="";
        String theHoleToken="";
        int state=0;
        char c;
        for(int i=0; i<in.length(); i++)
        {
            //((c>='0'&&c<='9')||((c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='+')||(c=='-')||(c=='*')||(c=='×')||(c=='/')||(c=='÷')||(c=='%')||(c=='(')||(c==')')
            c=in.charAt(i);

            switch(state){

                case 0:

                    token="";
                    token_type="";
                    token_content="";

                    if(c>='0'&&c<='9'){
                        state = 1;
                        token_content=token_content+c;
                    }
                    else if((c>='A'&&c<='Z')||(c>='a'&&c<='z')){
                        state = 4;
                        token_content=token_content+c;
                    }
//                    else if((c=='<')){
//                        state = 5;
//                        token_content=token_content+c;
//                    }
//                    else if((c=='>')){
//                        state = 7;
//                        token_content=token_content+c;
//                    }
                    else if((c=='+')){
                        state = 5;
                        token_content=token_content+c;
                    }
                    else if((c=='-')){
                        state = 6;
                        token_content=token_content+c;
                    }
                    else if((c=='*')){
                        state =  7;
                        token_content=token_content+c;
                    }
                    else if((c=='×')){
                        state = 8;
                        token_content=token_content+c;
                    }
                    else if((c=='/')){
                        state = 9;
                        token_content=token_content+c;
                    }
                    else if((c=='÷')){
                        state = 10;
                        token_content=token_content+c;
                    }
                    else if((c=='%')){
                        state = 11;
                        token_content=token_content+c;
                    }
                    else if((c=='(')){
                        state = 12;
                        token_content=token_content+c;
                    }
                    else if((c==')')){
                        state = 13;
                        token_content=token_content+c;
                    }

                    else if(c==' '){
                        state = 0;//空格
                    }
                    else{
                        state = 20;
                        token_content=token_content+c;//非法输入
                    }

                    break;

                case 1:
                    System.out.println("case1");
                    if(c>='0'&&c<='9'){
                        state= 1;
                        token_content=token_content+c;
                    }
                    else if(c=='.'){
                        state = 2;
                        token_content=token_content+c;
                    }
                    else if(c==' '){
                        state = 0;
                        token_type="num";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                    }
                    else if(((c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='+')||(c=='-')||(c=='*')||(c=='×')||(c=='/')||(c=='÷')||(c=='%')||(c=='(')||(c==')')){
                        state = 0;
                        token_type="num";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                        i--;
                    }
                    else{
                        state = 20;
                        token_content=token_content+c;
                    }

                    break;

                case 2:
                    System.out.println("case2");
                    if(c>='0'&&c<='9'){
                        state = 3;
                        token_content=token_content+c;
                    }
                    else if(c==' '){
                        state = 0;
                        token_type="error";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                    }
                    else if(((c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='+')||(c=='-')||(c=='*')||(c=='×')||(c=='/')||(c=='÷')||(c=='%')||(c=='(')||(c==')')){
                        state = 20;
                        token_content=token_content+c;
                    }
                    else{
                        state = 20;
                        token_content=token_content+c;
                    }

                    break;

                case 3:
                    System.out.println("case3");
                    if(c>='0'&&c<='9'){
                        state = 3;
                        token_content=token_content+c;
                    }
                    else if(c==' '){
                        state = 0;
                        token_type="num";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                    }
                    else if(((c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='+')||(c=='-')||(c=='*')||(c=='×')||(c=='/')||(c=='÷')||(c=='%')||(c=='(')||(c==')')){
                        state = 0;
                        token_type="num";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                        i--;
                    }
                    else{
                        state = 20;
                        token_content=token_content+c;
                    }

                    break;

                case 4:
                    System.out.println("case4");
                    if((c>='0'&&c<='9')||(c>='A'&&c<='Z')||(c>='a'&&c<='z')){
                        state = 4;
                        token_content=token_content+c;
                    }
                    else if(c==' '){
                        state = 0;
                        token_type="id";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                    }
                    else if((c=='+')||(c=='-')||(c=='*')||(c=='×')||(c=='/')||(c=='÷')||(c=='%')||(c=='(')||(c==')')){
                        state = 0;
                        token_type="id";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                        i--;
                    }
                    else{
                        state = 20;
                        token_content=token_content+c;
                    }

                    break;

//                case 5:
//                    System.out.println("case5");
//                    if(c=='='){
//                        state = 6;
//                        token_content=token_content+c;
//                    }
//                    else if(c==' '){
//                        state = 0;
//                        token_type="op_<";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                    }
//                    else if(((c>='0'&&c<='9')||(c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='<')||(c=='>')||(c=='+')||(c=='-')||(c=='*')||(c=='/')||(c=='%')||(c=='!')||(c=='(')||(c==')')||(c==';')){
//                        state = 0;
//                        token_type="op_<";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                        i--;
//                    }
//                    else{
//                        state = 21;
//                        token_content=token_content+c;
//                    }
//
//                    break;

//                case 6:
//                    System.out.println("case6");
//                    state = 0;
//                    token_type="op_<=";
//                    token = "<"+token_type +"   ,   "+token_content+">";
//                    theHoleToken+=token+"\r\n";
//                    i--;
//                    break;

//                case 7:
//                    System.out.println("case7");
//                    if(c=='='){
//                        state = 8;
//                        token_content=token_content+c;
//                    }
//                    else if(c==' '){
//                        state = 0;
//                        token_type="op_>";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                    }
//                    else if(((c>='0'&&c<='9')||(c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='<')||(c=='>')||(c=='+')||(c=='-')||(c=='*')||(c=='/')||(c=='%')||(c=='!')||(c=='(')||(c==')')||(c==';')){
//                        state = 0;
//                        token_type="op_>";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                        i--;
//                    }
//                    else{
//                        state = 21;
//                        token_content=token_content+c;
//                    }
//
//                    break;

//                case 8:
//                    state = 0;
//                    token_type="op_>=";
//                    token = "<"+token_type +"   ,   "+token_content+">";
//                    theHoleToken+=token+"\r\n";
//                    i--;
//                    break;

                case 5:
                    System.out.println("case5");
                    state = 0;
                    token_type="op_+";
                    token = "<"+token_type +"   ,   "+token_content+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 6:
                    state = 0;
                    token_type="op_-";
                    token = "<"+token_type +"   ,   "+token_content+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 7:
                    state = 0;
                    token_type="op_*";
                    token = "<"+token_type +"   ,   "+"*"+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 8:
                    state = 0;
                    token_type="op_*";
                    token = "<"+token_type +"   ,   "+"*"+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 9:
                    state = 0;
                    token_type="op_/";
                    token = "<"+token_type +"   ,   "+"/"+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 10:
                    state = 0;
                    token_type="op_/";
                    token = "<"+token_type +"   ,   "+"/"+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 11:
                    state = 0;
                    token_type="op_%";
                    token = "<"+token_type +"   ,   "+token_content+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

//                case 14:
//                    if(c=='='){
//                        token_content=token_content+c;
//                        state =15;
//                    }
//                    else if(c==' '){
//                        state = 0;
//                        token_type="op_=";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                    }
//                    else if(((c>='0'&&c<='9')||(c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='<')||(c=='>')||(c=='+')||(c=='-')||(c=='*')||(c=='/')||(c=='%')||(c=='!')||(c=='(')||(c==')')||(c==';')){
//                        state = 0;
//                        token_type="op_=";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                        i--;
//                    }
//                    else{
//                        state = 21;
//                        token_content=token_content+c;
//                    }
//
//                    break;
//
//                case 15:
//                    state = 0;
//                    token_type="op_==";
//                    token = "<"+token_type +"   ,   "+token_content+">";
//                    theHoleToken+=token+"\r\n";
//                    i--;
//                    break;

//                case 16:
//                    if(c=='='){
//                        token_content=token_content+c;
//                        state =17;
//                    }
//                    else if(c==' '){
//                        state = 0;
//                        token_type="op_!";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                    }
//                    else if(((c>='0'&&c<='9')||(c>='A'&&c<='Z')||(c>='a'&&c<='z'))||(c=='<')||(c=='>')||(c=='+')||(c=='-')||(c=='*')||(c=='/')||(c=='%')||(c=='!')||(c=='(')||(c==')')||(c==';')){
//                        state = 0;
//                        token_type="op_!";
//                        token = "<"+token_type +"   ,   "+token_content+">";
//                        theHoleToken+=token+"\r\n";
//                        i--;
//                    }
//                    else{
//                        state =21;
//                        token_content=token_content+c;
//                    }
//
//                    break;

//                case 17:
//                    state = 0;
//                    token_type="op_!=";
//                    token = "<"+token_type +"   ,   "+token_content+">";
//                    theHoleToken+=token+"\r\n";
//                    i--;
//                    break;

                case 12:
                    state = 0;
                    token_type="op_(";
                    token = "<"+token_type +"   ,   "+token_content+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

                case 13:
                    state = 0;
                    token_type="op_)";
                    token = "<"+token_type +"   ,   "+token_content+">";
                    theHoleToken+=token+";";
                    i--;
                    break;

//                case 20:
//                    state = 0;
//                    token_type="delim_;";
//                    token = "<"+token_type +"   ,   "+token_content+">";
//                    theHoleToken+=token+"\r\n";
//                    i--;
//                    break;

                case 20:
                    if(c!=' '){
                        state = 20;
                        token_content=token_content+c;
                    }
                    else{
                        state = 0;
                        token_type = "error";
                        token = "<"+token_type +"   ,   "+token_content+">";
                        theHoleToken+=token+";";
                    }

                    break;

            }
        }


        return theHoleToken;
    }
}
