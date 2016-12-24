package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaChangzhe on 2016/12/19.
 */
public class NFAPath {
    public char allowChar;
    public NFANode targetNode;

    public NFAPath(NFANode target, char allowChar) {
        this.targetNode = target;
        this.allowChar = allowChar;
    }
}
