/******************************************************************************
 * 2D-TREE IMPLEMENTATION
 *
 * KdTree is a mutable data type that uses a 2d-tree to represent a SET of points
 * (sames as PointSET).
 *
 * A 2d-tree is a generalization of a BST to two-dimensional keys.
 * The idea is to build a BST with points in the nodes,
 * using the x- and y-coordinates of the points as keys in strictly alternating sequence.
 *
 * The prime advantage of a 2d-tree over a BST
 * is that it supports efficient implementation of range search and nearest neighbor search.
 * Each node corresponds to an axis-aligned rectangle in the unit square,
 * which encloses all of the points in its subtree.
 * The root corresponds to the unit square;
 * the left and right children of the root corresponds
 * to the two rectangles split by the x-coordinate of the point at the root; and so forth.
 ******************************************************************************/
package com.cruzn;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class KdTree {

    private Node root;
    private int size;

    /** construct an empty set of points */
    public KdTree() {
        size = 0;
    }

    /** @return true if tree is empty */
    public boolean isEmpty() {
//        return root == null;
        return size() == 0;
    }

    /** @return number of points in the tree */
    public int size() {
        return size;
    }

    /**
     * Adds the point to the set (if it is not already in the set)
     *
     * The algorithms for insert is similar to this for BSTs, but at the root we use the x-coordinate
     * (if the point to be inserted has a smaller x-coordinate than the point at the root, go left;
     *  otherwise go right);
     * then at the next level, we use the y-coordinate
     * (if the point to be inserted has a smaller y-coordinate than the point in the node, go left;
     *  otherwise go right);
     * then at the next level the x-coordinate, and so forth.
     *
     * should run in time proportional to the logarithm of the number of points in the set
     * in the worst case;
     *
     * Throw a java.lang.NullPointerException if any argument is null.*
     * */
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        root = insertByX(root, p);
    }

    private Node insertByX(Node r, Point2D p) {
        if (r == null) {
            size++;
            return new Node(p);
        }
        if (p.compareTo(r.point()) == 0) {
            return r;
        }
        if (Point2D.X_ORDER.compare(p, r.point()) < 0) {
            r.lb = insertByY(r.lb, p);
            r.lb.setRect(r.rect().xmin(), r.rect().ymin(), r.point().x(), r.rect().ymax());
            r.lb.orient(!r.orientation);
        }
        else {
            r.rt = insertByY(r.rt, p);
            r.rt.setRect(r.point().x(), r.rect().ymin(), r.rect().xmax(), r.rect().ymax());
            r.rt.orient(!r.orientation);
        }
        return r;
    }

    private Node insertByY(Node r, Point2D p) {
        if (r == null) {
            size++;
            return new Node(p);
        }
        if (p.compareTo(r.point()) == 0) {
            return r;
        }
        if (Point2D.Y_ORDER.compare(p, r.point()) < 0) {
            r.lb = insertByX(r.lb, p);
            r.lb.setRect(r.rect().xmin(), r.rect().ymin(), r.rect().xmax(), r.point().y());
            r.lb.orient(!r.orientation);
        }
        else { // if (Point2D.Y_ORDER.compare(p, r.p) > 0) {
            r.rt = insertByX(r.rt, p);
            r.rt.setRect(r.rect().xmin(), r.point().y(), r.rect().xmax(), r.rect().ymax());
            r.rt.orient(!r.orientation);
        }
        return r;
    }

    /**
     * @return true if the set contains point p
     * @throws NullPointerException if p is null
     *
     * The algorithms for search is similar to this for BSTs,
     * but at the root we use the x-coordinate to choose whether to move left or right
     * then at the next level, we use the y-coordinate, and so forth.
     *
     * O(N) should be proportional to the logarithm of the number of points in the set;
     * */
    public boolean contains(Point2D p) {
        if (isEmpty())
            return false;
        return contains(root, p, Point2D.X_ORDER);
    }

    private boolean contains(Node current, Point2D p, Comparator<Point2D> c) {
        if (current == null)
            return false;
        if (p.equals(current.point()))
            return true;
        if (c.compare(p, current.point()) < 0)
            return contains(current.lb, p, current.orientation ? Point2D.Y_ORDER : Point2D.X_ORDER);
        else
            return contains(current.rt, p, current.orientation ? Point2D.Y_ORDER : Point2D.X_ORDER);
    }

    /**
     * @return an iterable containing all points that are inside the rectangle or empty
     * if there are no points in the range.
     *
     * To find all points contained in a given query rectangle,
     * start at the root and recursively search for points in both subtrees using the following pruning rule:
     * if the query rectangle does not intersect the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     *
     * O(N) should be proportional to the number of points in the set.
     * */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> iterable = new ArrayList<>();

        range(root, rect, iterable);

        return iterable;
    }

    private void range(Node current, RectHV rect, List<Point2D> iterable) {
        if (current == null)
            return;
        if (current.rect().intersects(rect)) {
            if (rect.contains(current.point()))
                iterable.add(current.point());
            range(current.lb, rect, iterable);
            range(current.rt, rect, iterable);
        }
    }

    /**
     * @return a nearest neighbor in the set to point p; null if the set is empty
     *
     * should run in time proportional to the number of points in the set.
     *
     * Nearest neighbor search.
     * To find a closest point to a given query point,
     * start at the root and recursively search in both subtrees using the following pruning rule:
     *
     *  if the closest point discovered so far is closer than
     *  the distance between the query point
     *  and the rectangle corresponding to a node,
     *  there is no need to explore that node (or its subtrees).
     *
     *  That is, a node is searched only if it might contain a point that is closer than the best one found so far.
     *  The effectiveness of the pruning rule depends on quickly finding a nearby point.
     *
     *  To do this, organize your recursive method so that when there are two possible subtrees to go down,
     *  you always choose the subtree that is on the same side of the splitting line as the query point
     *  as the first subtree to explore—
     *  the closest point found while exploring the first subtree may enable pruning of the second subtree.
     *
     * */
    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;
        return nearest(p, null, Double.POSITIVE_INFINITY, root);
    }

    private Point2D nearest(Point2D p, Point2D nearest, double dist, Node currentNode) {
        if (currentNode == null)
            return nearest;
        if (currentNode.point().equals(p))
            return currentNode.point();
        if (p.distanceSquaredTo(currentNode.point()) < dist) {
            nearest = currentNode.point();
            dist = p.distanceSquaredTo(nearest);
        }
        if (currentNode.lb != null && currentNode.lb.rect().contains(p))
            nearest = nearest(p, nearest, dist, currentNode, true);
        else
            nearest = nearest(p, nearest, dist, currentNode, false);

        return nearest;
    }

    private Point2D nearest(Point2D p, Point2D nearest, double dist, Node current, boolean goLeft) {
        Point2D newNearest = nearest(p, nearest, dist, goLeft ? current.lb : current.rt);
        nearest = closerPointTo(p, nearest, newNearest);
        dist = p.distanceSquaredTo(nearest);
        // If there's a chance to find something in the other branch...
        Node siblingNode = goLeft ? current.rt : current.lb;
        if (siblingNode != null && siblingNode.rect().distanceSquaredTo(p) < dist)
            newNearest = nearest(p, nearest, dist, siblingNode);
        return closerPointTo(p, nearest, newNearest);
    }

    private Point2D closerPointTo(Point2D p, Point2D nearest, Point2D newNearest) {
        return (p.distanceSquaredTo(newNearest) < p.distanceSquaredTo(nearest)) ?
                newNearest : nearest;
    }

    Iterable<Node> it() { // TO DO: private
        List<Node> iterable = new ArrayList<>();
        iterable.add(root);
        for (int i = 0; i < size; i++) {
            Node current = iterable.get(i);
            if (current.rt != null)
                iterable.add(current.rt);
            if (current.lb != null)
                iterable.add(current.lb);
        }
        return iterable;
    }

    /**
     * Draw all points to standard draw in black, vertical splits in red and horizontal splits in blue
     *
     * A 2d-tree divides the unit square in a simple way:
     * all the points to the left of the root go in the left subtree;
     * all those to the right go in the right subtree; and so forth, recursively.
     *
     * O(N) not relevant —it is primarily for debugging.
     * */
    public void draw() {
        for (Node n : it()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(n.point().x(), n.point().y());

            StdDraw.setPenRadius();
            if (n.orientation) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.point().x(), n.rect().ymin(), n.point().x(), n.rect().ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect().xmin(), n.point().y(), n.rect().xmax(), n.point().y());
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }

    static class Node {
        private Point2D p;          // the point
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree
        private boolean orientation;// true if oriented on x-axis; false if vertically oriented.
                                    // 'oriented on x-axis' means its children are located to right and left
                                    // 'oriented on y-axis' means its children are located on top and bottom
        public Node(Point2D p) {
            this.p = p;
            this.rect = new RectHV(0, 0, 1, 1);
            this.orientation = true;
        }

        public Point2D point() {
            return p;
        }

        private void setRect(double xMin, double yMin, double xMax, double yMax) {
            this.rect = new RectHV(xMin, yMin, xMax, yMax);
        }

        /** @param orientation: true if horizontal; false if vertical */
        private void orient(boolean orientation) {
            this.orientation = orientation;
        }

        public RectHV rect() {
            return this.rect;
        }
    }
}
