package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaChangzhe on 2016/12/19.
 */
public class Path {
    public List<Character> allowChars;
    public NFANode targetNode;

    public Path(NFANode target, Character... allowChar) {
        this.targetNode = target;
        this.allowChars = new ArrayList<>();
        if (allowChar != null) {
            for (int i = 0; i < allowChar.length; i++)
                allowChars.add(allowChar[i]);
        }
    }
}
