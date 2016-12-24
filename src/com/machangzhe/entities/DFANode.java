package com.machangzhe.entities;

import com.machangzhe.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by MaChangzhe on 2016/12/19.
 */
public class DFANode {
    public int id;
    public List<DFAPath> paths;
    public Set<NFANode> nodes;

    public DFANode(int id) {
        this.id = id;
        this.paths = new ArrayList<>();
        this.nodes = new HashSet<>();
    }

    public String getNodesString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(" + id + ")");
        stringBuilder.append("(");
        for (NFANode node: nodes) {
            stringBuilder.append(node.id + ",");
        }
        stringBuilder.append(")");

        if (getType().contains(Constants.NodeType.START)) {
            stringBuilder.append("S");
        }
        if (getType().contains(Constants.NodeType.END)) {
            stringBuilder.append("E");
        }

        return stringBuilder.toString();
    }

    public boolean isSame(DFANode node) {
        if (this.nodes == null && node.nodes == null) return true;

        if (this.nodes == null || node.nodes == null ) return false;

        if (this.nodes.size() != node.nodes.size()) return false;

        for (NFANode nfaNode: this.nodes) {
            if (!node.nodes.contains(nfaNode)) {
                return false;
            }
        }
        return true;
    }

    public String getType() {
        boolean isStart = false;
        boolean isEnd = false;

        for (NFANode nfaNode: nodes) {
            if (Constants.NodeType.START.equals(nfaNode.type)) isStart = true;
            if (Constants.NodeType.END.equals(nfaNode.type)) isEnd = true;
        }

        if (isStart && isEnd) return Constants.NodeType.START_AND_END;
        if (isStart) return Constants.NodeType.START;
        if (isEnd) return Constants.NodeType.END;
        return Constants.NodeType.MIDDLE;
    }

    public boolean isEndNode() {
        return getType().equals(Constants.NodeType.END) || getType().equals(Constants.NodeType.START_AND_END);
    }
}
