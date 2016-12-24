package com.machangzhe.parser;


import com.machangzhe.entities.*;
import com.machangzhe.utils.Constants;

import java.util.*;

/**
 * Created by MaChangzhe on 2016/12/09.
 */
public class Parser {
    public static NFA regexToNfa(String regex) {
        NFA nfa = new NFA();
        NFANode startNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.START, null);
        nfa.startNode = startNode;
        nfa.nodeList.add(startNode);
        NFANode currentNode = startNode;

        NFAPath tempPath = new NFAPath(null, Constants.EPSON);
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
                tempPath = new NFAPath(preNodes.get(preNodes.size() - 1), Constants.EPSON);
                currentNode.paths.add(tempPath);

                tempPath = new NFAPath(currentNode, Constants.EPSON);
                preNodes.get(preNodes.size() - 1).paths.add(tempPath);
            } else if (currentChar == '|') {
                tempPath = new NFAPath(null, Constants.EPSON);
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
                tempPath = new NFAPath(null, Constants.EPSON);
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

                tempPath = new NFAPath(null, currentChar);
                currentNode.paths.add(tempPath);
                currentNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.MIDDLE, null);
                nfa.nodeList.add(currentNode);
                tempPath.targetNode = currentNode;
            }
        }

        if (tailNodes.get(headNodes.get(headNodes.size() - 1).id) != null) {
            tempPath = new NFAPath(tailNodes.get(headNodes.get(headNodes.size() - 1).id), Constants.EPSON);
            currentNode.paths.add(tempPath);
            currentNode = tempPath.targetNode;
        }

        tempPath = new NFAPath(null, Constants.EPSON);
        currentNode.paths.add(tempPath);

        NFANode endNode = new NFANode(nfa.nodeList.size(), Constants.NodeType.END, null);
        nfa.nodeList.add(endNode);
        tempPath.targetNode = endNode;
        return nfa;
        
    }

    public static DFA nfaToDfa(NFA nfa) {
        DFA dfa = new DFA();
        DFANode currentNode = new DFANode(dfa.nodeList.size());
        dfa.nodeList.add(currentNode);
        dfa.startNode = currentNode;
        currentNode.nodes = getEpsonClosure(nfa, 0);

        while(currentNode != null) {
            Set<Character> allowChars = getAllowChars(currentNode.nodes);
            for (Character character: allowChars) {
                Set<NFANode> charClosure = getCharClosure(nfa, currentNode.nodes, character);
                DFANode targetDfaNode = dfa.findDFANode(charClosure);
                if (targetDfaNode == null) {
                    targetDfaNode = new DFANode(dfa.nodeList.size());
                    dfa.nodeList.add(targetDfaNode);
                    targetDfaNode.nodes = charClosure;
                }
                currentNode.paths.add(new DFAPath(targetDfaNode, character));
            }
            currentNode = findNextDFANode(dfa, currentNode);
        }

        return dfa;
    }

    private static DFANode findNextDFANode(DFA dfa, DFANode currentNode) {
        for (DFANode node: dfa.nodeList) {
            if (node.id == currentNode.id + 1) {
                return node;
            }
        }
        return null;
    }

    private static Set<NFANode> getEpsonClosure(NFA nfa, int id) {
        Set<NFANode> closureSet = new HashSet<>();

        NFANode sourceNode = null;
        for (NFANode node: nfa.nodeList) {
            if (node.id == id) sourceNode = node;
        }

        if (sourceNode == null) return closureSet;

        closureSet.add(sourceNode);

        int preClosureSize = 0;
        Set<NFANode> tempNfaNodeSet = new HashSet<>();
        do {
            preClosureSize = closureSet.size();
            tempNfaNodeSet.clear();
            for (NFANode node: closureSet) {
                for(NFAPath path: node.paths) {
                    if (path.allowChar == Constants.EPSON) tempNfaNodeSet.add(path.targetNode);
                }
            }
            closureSet.addAll(tempNfaNodeSet);
        } while (preClosureSize != closureSet.size());

        return closureSet;
    }

    private static Set<NFANode> getCharClosure(NFA nfa, Set<NFANode> nodes, char c) { //nodes should be a epson closure
        Set<NFANode> closureSet = new HashSet<>();

        for (NFANode node: nodes) {
            for (NFAPath path: node.paths) {
                if (path.allowChar == c) {
                    closureSet.addAll(getEpsonClosure(nfa, path.targetNode.id));
                }
            }
        }

        return closureSet;
    }

    private static Set<Character> getAllowChars(Set<NFANode> nodes) {
        Set<Character> charSet = new HashSet<>();

        for (NFANode node: nodes) {
            for (NFAPath path: node.paths) {
                if (path.allowChar != Constants.EPSON) {
                    charSet.add(path.allowChar);
                }
            }
        }

        return charSet;
    }
}
