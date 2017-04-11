# About
This project was developed for the [Introduction to Algorithms](https://www.coursera.org/learn/introduction-to-algorithms) course created by __Princeton University__ and provided __through Coursera__. After studying the [Balanced Search Tree](http://algs4.cs.princeton.edu/33balanced/) __data structure__ for effective searches, we worked on __a geometric application__ of it, to support efficient __range search__ and __nearest neighbor search__ operations.

## The project
For this project we had to implement 2 classes as per requirements, each of which __represents a set of points in the unit square__. The `PointSET` class is a mutable brute-force implementation, whereas the `KdTree` class makes use of a 2d-tree to enable more efficient searching operations.

### The KdTree class
This implementation represents a set of points by __building a Balanced Search Tree__ in which __nodes are represented
by the points__ of the set, whose x- and y-coordinates are used as key in a strictly alternating sequence.

Each node corresponds to an axis-aligned rectangle (x-axis or y-axis) in the unit square, which encloses all of the points in its subtree, supporting __efficient implementation of range search and nearest neighbor search__.