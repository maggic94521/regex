package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaChangzhe on 2016/12/20.
 */
public class NFA {
    public List<Node> nodeList;
    public Node startNode;
    public Node endNode;

    public NFA() {
        nodeList = new ArrayList<>();
    }
}
