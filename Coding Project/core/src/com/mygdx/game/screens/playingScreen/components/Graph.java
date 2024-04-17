package com.mygdx.game.screens.playingScreen.components;

public class Graph {
    public GraphNode[][] nodeSystem;
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    Graph () {
        nodeSystem = new GraphNode[7][5];
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 5; y++) {
                nodeSystem[x][y] = new GraphNode(-1, -1,
                        ((y-1) == -1 ? -1 : nodeSystem[x][y-1].upCost),
                        ((x-1) == -1 ? -1 : nodeSystem[x-1][y].rightCost));
            }
        }
    }
}
