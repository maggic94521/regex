package com.machangzhe;

import com.machangzhe.entities.*;
import com.machangzhe.parser.Parser;

public class Main {
    public static void main(String[] args) {
        String regex = "ab(jkj*|de(fg|h*i))*";

        NFA nfa = Parser.regexToNfa(regex);
        for (NFANode node: nfa.nodeList) {
            for (NFAPath path: node.paths) {
                System.out.println(node.id + " --- " + (path.allowChar != '\0' ? path.allowChar : " ") + " ---> " + path.targetNode.id);
            }
        }

        DFA dfa = Parser.nfaToDfa(nfa);

        for (DFANode node : dfa.nodeList) {
            for (DFAPath path: node.paths) {
                System.out.println(node.getNodesString() + " --- " + (path.allowChar != '\0' ? path.allowChar : " ") + " ---> " + path.targetNode.getNodesString());
            }
        }

        String[] testStrs = new String[10];
        testStrs[0] = "ab";
        testStrs[1] = "ccc";
        testStrs[2] = "abcbbbaba";
        testStrs[3] = "abjkjjjjdehhhij";
        testStrs[4] = "bcc";
        testStrs[5] = "abcc";
        testStrs[6] = "aabbcc";
        testStrs[7] = "abbcd";
        testStrs[8] = "ababc";
        testStrs[9] = "aaabcaab";

        for (String str: testStrs) {
//            System.out.println(str + " -------- " + dfa.goThroughString(str) + " --------- correct is    " + str.matches(regex));
            System.out.println(dfa.goThroughString(str) == str.matches(regex));
        }
    }
}
