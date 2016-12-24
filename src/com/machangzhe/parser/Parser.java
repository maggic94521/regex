package com.machangzhe.parser;


import com.machangzhe.entities.NFA;
import com.machangzhe.entities.NFANode;
import com.machangzhe.entities.Path;
import com.machangzhe.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MaChangzhe on 2016/12/09.
 */
public class Parser {
    public static NFA parseRegex(String regex) {
        NFA nfa = new NFA();
        NFANode startNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.START, null);
        nfa.startNode = startNode;
        nfa.nodeList.add(startNode);
        NFANode currentNode = startNode;

        Path tempPath = new Path(null, Constants.EPSON);
        currentNode.paths.add(tempPath);

        currentNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.MIDDLE, null);
        nfa.nodeList.add(currentNode);
        tempPath.targetNode = currentNode;

        List<NFANode> headNodes = new ArrayList<>();
        Map<Integer, NFANode> tailNodes = new HashMap<>();
        List<NFANode> preNodes = new ArrayList<>();
        headNodes.add(currentNode);
        preNodes.add(startNode);
        
        for(int i = 0; i < regex.length(); i++) {
            char currentChar = regex.charAt(i);
            if (currentChar == '*') {
                tempPath = new Path(preNodes.get(preNodes.size() - 1), Constants.EPSON);
                currentNode.paths.add(tempPath);

                tempPath = new Path(currentNode, Constants.EPSON);
                preNodes.get(preNodes.size() - 1).paths.add(tempPath);
            } else if (currentChar == '|') {
                tempPath = new Path(null, Constants.EPSON);
                currentNode.paths.add(tempPath);
                if (tailNodes.get(headNodes.get(headNodes.size() - 1).id) == null) {
                    currentNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.MIDDLE, null);
                    nfa.nodeList.add(currentNode);
                    tempPath.targetNode = currentNode;
                    tailNodes.put(headNodes.get(headNodes.size() - 1).id, currentNode);
                } else {
                    tempPath.targetNode = tailNodes.get(headNodes.get(headNodes.size() - 1).id);
                }
                currentNode = headNodes.get(headNodes.size() - 1);
            } else if (currentChar == '(') {
                headNodes.add(currentNode);
                preNodes.remove(preNodes.size() - 1);
                preNodes.add(currentNode);
                preNodes.add(NFANode.fakeNode());
            } else if (currentChar == ')') {
                tempPath = new Path(null, Constants.EPSON);
                currentNode.paths.add(tempPath);
                if (tailNodes.get(headNodes.get(headNodes.size() - 1).id) == null) {
                    currentNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.MIDDLE, null);
                    nfa.nodeList.add(currentNode);
                    tempPath.targetNode = currentNode;
                } else {
                    currentNode = tailNodes.get(headNodes.get(headNodes.size() - 1).id);
                    tailNodes.remove(headNodes.get(headNodes.size() - 1).id);
                    tempPath.targetNode = currentNode;
                }
                headNodes.remove(headNodes.size() - 1);
                preNodes.remove(preNodes.size() - 1);
            } else if ((currentChar >= 'A' && currentChar <= 'z') || (currentChar >= '0' && currentChar <= '9')) {
                preNodes.remove(preNodes.size() - 1);
                preNodes.add(currentNode);

                tempPath = new Path(null, currentChar);
                currentNode.paths.add(tempPath);
                currentNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.MIDDLE, null);
                nfa.nodeList.add(currentNode);
                tempPath.targetNode = currentNode;
            }
        }

        if (tailNodes.get(headNodes.get(headNodes.size() - 1).id) != null) {
            tempPath = new Path(tailNodes.get(headNodes.get(headNodes.size() - 1).id), Constants.EPSON);
            currentNode.paths.add(tempPath);
            currentNode = tempPath.targetNode;
        }

        tempPath = new Path(null, Constants.EPSON);
        currentNode.paths.add(tempPath);

        NFANode endNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.END, null);
        nfa.nodeList.add(endNode);
        tempPath.targetNode = endNode;
        return nfa;
        
    }
}
