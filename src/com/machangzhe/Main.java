package com.machangzhe;

import com.machangzhe.entities.NFA;
import com.machangzhe.entities.NFANode;
import com.machangzhe.entities.Path;
import com.machangzhe.parser.Parser;

public class Main {
    public static void main(String[] args) {
        NFA nfa = Parser.parseRegex("(a|a*bc)*ab");
        for (NFANode node: nfa.nodeList) {
            for (Path path: node.paths) {
                for(Character character: path.allowChars) {
                    System.out.println(node.id + " --- " + (character != '\0' ? character : " ") + " ---> " + path.targetNode.id);
                }
            }
        }
    }
}
