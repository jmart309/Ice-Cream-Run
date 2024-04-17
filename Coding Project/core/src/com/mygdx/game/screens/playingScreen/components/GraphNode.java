package com.mygdx.game.screens.playingScreen.components;

public class GraphNode {
    public final int upCost;
    public final int downCost;
    public final int rightCost;
    public final int leftCost;

    public GraphNode(int up, int right, int down, int left) {
        if (up == -1)
            upCost = (int) (Math.random() * 7) + 1;
        else
            upCost = up;

        if (right == -1)
            rightCost = (int) (Math.random() * 7) + 1;
        else
            rightCost = right;

        if (down == -1)
            downCost = (int) (Math.random() * 7) + 1;
        else
            downCost = down;

        if (left == -1)
            leftCost = (int) (Math.random() * 7) + 1;
        else
            leftCost = left;
    }
}
