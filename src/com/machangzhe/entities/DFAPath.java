package com.machangzhe.entities;

/**
 * Created by MaChangzhe on 2016/12/19.
 */
public class DFAPath {
    public char allowChar;
    public DFANode targetNode;

    public DFAPath(DFANode target, char allowChar) {
        this.targetNode = target;
        this.allowChar = allowChar;
    }
}
