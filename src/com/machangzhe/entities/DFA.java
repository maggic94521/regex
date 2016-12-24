package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by MaChangzhe on 2016/12/24.
 */
public class DFA {
    public List<DFANode> nodeList;
    public DFANode startNode;

    public DFA() {
        nodeList = new ArrayList<>();
    }

    public DFANode findDFANode(Set<NFANode> charClosure) {
        DFANode fakeDfa = new DFANode(0);
        fakeDfa.nodes = charClosure;
        for (DFANode currentNode: nodeList) {
            if (currentNode.isSame(fakeDfa)) return currentNode;
        }
        return null;
    }

    public boolean goThroughString(String str) {
        DFANode currentNode = startNode;


        for (int i = 0; i < str.length(); i++) {

            if (currentNode == null) return false;
            if (currentNode.paths == null) return false;

            List<DFAPath> allowPath = currentNode.paths;
            currentNode = null;
            char c = str.charAt(i);
            for (DFAPath path: allowPath) {
                if (path.allowChar == c) {
                    currentNode = path.targetNode;
                    break;
                }
            }
        }

        if (currentNode != null && currentNode.isEndNode()) return true;

        return false;
    }
}
