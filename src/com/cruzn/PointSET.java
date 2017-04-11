/******************************************************************************
 * BRUTE-FORCE IMPLEMENTATION
 * PointSET is a mutable data type that represents a SET of points
 * in the unit square (all points have x- and y-coordinates between 0 and 1).
 *
 * https://www.coursera.org/learn/algorithms-part1/programming/wuF0a/kd-trees
 *
 ******************************************************************************/
package com.cruzn;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private SET<Point2D> points;

    /** construct an empty set of points */
    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    /**
     * Add the point to the set, if it is not already in the set
     * O(N) = number of points in the set
     * @throws java.lang.NullPointerException if argument is null
     * */
    public void insert(Point2D p) {
        points.add(p);
    }

    /**
     * @return true if set contain point p
     * O(N) = logarithm of the number of points in the set
     * @throws java.lang.NullPointerException if argument is null
     * */
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    /** draw all points to standard draw */
    public void draw() {
        for (Point2D p : points)
            StdDraw.point(p.x(), p.y());
    }

    /**
     * @return an iterable containing all points that are inside the rectangle
     * O(N) = number of points in the set.
     * @throws java.lang.NullPointerException if argument is null
     * */
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> iterable = new SET<Point2D>();
        for (Point2D p : points)
            if (rect.contains(p))
                iterable.add(p);
        return iterable;
    }

    /**
     * @return a nearest neighbor in the set to point p; null if the set is empty
     * O(N) = number of points in the set.
     * @throws java.lang.NullPointerException if argument is null
     * */
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;
        for (Point2D point : points)
            if (nearest == null || p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest))
                nearest = point;
        return nearest;
    }

    public static void main(String[] args) {

    }

}