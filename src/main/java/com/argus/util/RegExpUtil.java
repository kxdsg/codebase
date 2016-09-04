package com.argus.util;

import org.apache.oro.text.regex.*;

import java.util.List;
import java.util.ArrayList;


/**
 * 正则表达式工具类
 */
public class RegExpUtil {

    static PatternCompiler compiler = new Perl5Compiler();
    static PatternMatcher matcher = new Perl5Matcher();
    static Pattern pattern = null;

    public static boolean matches(String input, String regex){
        try {
            pattern = compiler.compile(regex);
        } catch (MalformedPatternException e) {
            e.printStackTrace();
        }
        return matcher.matches(input, pattern);

    }

    public static List<String> getExpStr(String input, String regex){

        List<String> resultList = new ArrayList<String>();
        try {
            pattern = compiler.compile(regex);
        } catch (MalformedPatternException e) {
            e.printStackTrace();
        }

        if(matcher.contains(input, pattern)){
            MatchResult result = matcher.getMatch();
            int count = result.groups();
            for(int i=1;i<count;i++){
                resultList.add(result.group(i));
            }
        }

        return resultList;
    }

    public static void main(String[] args){
        //get all font tag attributes
        String input = "<font face=\"Arial,Serif\" size=\"2\" color=\"blue\">";
        String fontReg = "<\\s*font\\s+([^>]*)\\s*>";
        String attributeReg = "([a-z]+)\\s*=\"([^\"]+)\"";

        Pattern fontPattern = null;
        Pattern attributePattern = null;

        try {
            fontPattern = compiler.compile(fontReg,Perl5Compiler.CASE_INSENSITIVE_MASK);
            attributePattern = compiler.compile(attributeReg,Perl5Compiler.CASE_INSENSITIVE_MASK);
            if(matcher.contains(input,fontPattern)){
                MatchResult result = matcher.getMatch();
                String attributes = result.group(1);
                PatternMatcherInput in = new PatternMatcherInput(attributes);
                while(matcher.contains(in,attributePattern)){
                    result = matcher.getMatch();
                    System.out.println(result.group(1) + ":" + result.group(2));
                }
            }


        } catch (MalformedPatternException e) {
            e.printStackTrace();
        }


    }


}
