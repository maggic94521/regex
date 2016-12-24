package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaChangzhe on 2016/12/20.
 */
public class NFA {
    public List<NFANode> nodeList;
    public NFANode startNode;
    public NFANode endNode;

    public NFA() {
        nodeList = new ArrayList<>();
    }
}
