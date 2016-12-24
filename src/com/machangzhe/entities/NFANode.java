package com.machangzhe.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaChangzhe on 2016/12/19.
 */
public class NFANode {
    public int id;
    public String type;
    public List<Path> paths;

    public NFANode(int id, String type, List<Path> path) {
        this.type = type;
        if (path != null) {
            this.paths = path;
        } else {
            this.paths = new ArrayList<>();
        }
        this.id = id;
    }

    public static NFANode fakeNode() {
        return new NFANode(0, null, null);
    }
}
