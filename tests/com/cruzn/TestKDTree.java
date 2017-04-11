package com.cruzn;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class TestKDTree {

    KdTree horizontal8 = new KdTree();
    List<Point2D> pointsHorizontal8;

    @Before
    public void setUp() {
        pointsHorizontal8 = new ArrayList<>(Arrays.asList(
                new Point2D(0.9, 0.5),
                new Point2D(0.2, 0.5),
                new Point2D(0.3, 0.5),
                new Point2D(0.4, 0.5),
                new Point2D(0.1, 0.5),
                new Point2D(0.6, 0.5),
                new Point2D(0.5, 0.5),
                new Point2D(0.7, 0.5)
        ));

        horizontal8 = new KdTree();
        for (Point2D p : pointsHorizontal8)
            horizontal8.insert(p);
    }

    @Test
    public void testSizeOnEmpty(){
        KdTree t = new KdTree();
        assertEquals(0, t.size());
    }

    @Test
    public void testSize1(){
        KdTree t = new KdTree();
        t.insert(new Point2D(0.500000, 0.000000));
        assertEquals(1, t.size());
    }

    @Test
    public void testSizeWithDuplicateInput(){
        KdTree t = new KdTree();
        t.insert(new Point2D(0.500000, 0.000000));
        t.insert(new Point2D(0.500000, 0.000000));
        t.insert(new Point2D(0.500000, 0.000000));
        assertEquals(1, t.size());
    }

    @Test
    public void testSize4(){
        KdTree t = new KdTree();
        t.insert(new Point2D(0.500000, 0.000000));
        t.insert(new Point2D(0.500000, 1.000000));
        t.insert(new Point2D(0.500000, 0.300000));
        t.insert(new Point2D(1.000000, 0.500000));
        assertEquals(4, t.size());
    }

    @Test
    public void testIsEmptyTrue(){
        KdTree t = new KdTree();
        assertTrue(t.isEmpty());
    }

    @Test
    public void testIsEmptyFalse(){
        KdTree t = new KdTree();
        t.insert(new Point2D(0.500000, 0.000000));
        assertFalse(t.isEmpty());
    }

    @Test
    public void testIsEmptyForEveryInsert(){
        KdTree t = new KdTree();
        assertTrue(t.isEmpty());
        t.insert(new Point2D(0.5, 0.0));
        assertFalse(t.isEmpty());
        t.insert(new Point2D(0.0, 0.5));
        assertFalse(t.isEmpty());
        t.insert(new Point2D(0.2, 0.5));
        assertFalse(t.isEmpty());
    }

    @Test
    public void testInsert1(){
        KdTree t = new KdTree();
        t.insert(new Point2D(0.000000, 0.500000));
        int c = 0;
        for (KdTree.Node node : t.it()) {
            assertTrue(node.point().equals(new Point2D(0.000000, 0.500000)));
            c++;
        }
        assertEquals(1, c);
    }

    @Test
    public void testInsert4(){
        KdTree t = new KdTree();
        List<Point2D> points = new ArrayList<>(Arrays.asList(
                new Point2D(0.000000, 0.500000),
                new Point2D(0.500000, 1.000000),
                new Point2D(0.500000, 0.000000),
                new Point2D(1.000000, 0.500000)));
        for (Point2D p : points)
            t.insert(p);

        for (KdTree.Node node : t.it())
            points.remove(node.point());
        assertEquals(0, points.size());
    }

    @Test
    public void testContainsNothing() {
        KdTree t = new KdTree();
        assertFalse(t.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void testContainsFalse() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.2, 0.2));
        assertFalse(t.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void testContainsTrue() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.1, 0.1));
        assertTrue(t.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void testContainsOffTest7b_2() {
        KdTree t = new KdTree();
        t.insert(new Point2D(1.0, 0.0));
        assertFalse(t.isEmpty());
        t.insert(new Point2D(0.0, 1.0));
        // t.range([1.0, 1.0] x [1.0, 1.0])
        assertFalse(t.isEmpty());
        assertFalse(t.contains(new Point2D(1.0, 1.0)));
    }

    @Test
    public void testContainsOffTest7b() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.4, 0.9));
        assertFalse(t.contains(new Point2D(0.8, 0.4)));
        //set.range([0.0, 0.8] x [0.1, 0.5])
        assertFalse(t.contains(new Point2D(0.5, 0.5)));
        // set.range([0.2, 0.3] x [0.6, 0.9])
        assertEquals(1, t.size());
        assertFalse(t.contains(new Point2D(0.4, 0.3)));
    }

    @Test
    public void testContainsDistinctPoints() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.2, 0.2));

        t.insert(new Point2D(0.2, 0.5));
        t.insert(new Point2D(0.5, 0.2));

        t.insert(new Point2D(0.5, 0.5));

        assertTrue(t.contains(new Point2D(0.5,0.2))); //
        assertTrue(t.contains(new Point2D(0.2,0.2)));
        assertTrue(t.contains(new Point2D(0.2,0.5)));
        assertTrue(t.contains(new Point2D(0.5,0.5)));
        assertFalse(t.contains(new Point2D(0.2, 0.3)));
    }

    @Test
    public void testNearestInMiddleOf2Points(){
        Point2D nearest = horizontal8.nearest(new Point2D(0.15, 0.4));
        assertEquals(new Point2D(0.1, 0.5), nearest); // or should it be (0.2, 0.5) ?
    }

    @Test
    public void testNearestIsMiddle(){
        Point2D nearest = horizontal8.nearest(new Point2D(0.4, 0.4));
        assertEquals(new Point2D(0.4, 0.5), nearest);
    }

    @Test
    public void testNearestAfterLast(){
        Point2D nearest = horizontal8.nearest(new Point2D(0.95, 0.5));
        assertEquals(new Point2D(0.9, 0.5), nearest);
    }

    @Test
    public void testNearestBeforeFirst(){
        Point2D nearest = horizontal8.nearest(new Point2D(0.05, 0.5));
        assertEquals(new Point2D(0.1, 0.5), nearest);
    }

    @Test
    public void testNearestFromTests7a() {
        KdTree t = new KdTree();

        t.insert(new Point2D(0.93338, 0.09783));
        t.insert(new Point2D(0.1225, 0.35019));
        t.insert(new Point2D(0.6883, 0.54082));
        t.insert(new Point2D(0.65903, 0.30141));

        t.range(new RectHV(0.11991, 0.44724, 0.52424, 0.59513)); //set.range([0.11991, 0.52424] x [0.44724, 0.59513])
        t.insert(new Point2D(0.80117, 0.52405));
        t.range(new RectHV(0.53512, 0.14803, 0.72832, 0.22334)); // set.range([0.53512, 0.72832] x [0.14803, 0.22334])
        t.insert(new Point2D(0.50423, 0.08418));

        Point2D p = t.nearest(new Point2D(0.73574, 0.74091)); //   ==>  (0.65903, 0.30141)
        assertEquals(new Point2D(0.6883, 0.54082), p);
    }

    @Test
    public void testNearestFromTests7b() {
        KdTree t = new KdTree();

        t.insert(new Point2D(0.08, 0.94));
        t.insert(new Point2D(0.97, 0.03));
        t.insert(new Point2D(0.94, 0.62));

        Point2D nearest = t.nearest(new Point2D(0.95, 0.77)); //  ==>  (0.94, 0.62)
        assertEquals(new Point2D(0.94, 0.62), nearest);

        nearest = t.nearest(new Point2D(0.3, 0.26)); // ==>  (0.97, 0.03)
        assertEquals(new Point2D(0.97, 0.03), nearest);

        nearest = t.nearest(new Point2D(0.26, 0.68)); //  ==>  (0.08, 0.94)
        assertEquals(new Point2D(0.08, 0.94), nearest);

        nearest = t.nearest(new Point2D(0.76, 0.77)); // ==>  (0.08, 0.94)
        assertEquals(new Point2D(0.94, 0.62), nearest);
    }

    @Test
    public void testNearestFromTests7c() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.7, 0.7));
        t.range(new RectHV(0.1, 0.9, 0.8, 1.0));

        boolean contains = t.contains(new Point2D(0.9, 0.7));//  ==>  false
        assertFalse(contains);

        t.insert(new Point2D(0.7, 0.5));
        contains = t.contains(new Point2D(1.0, 0.8));//  ==>  false
        assertFalse(contains);

        t.insert(new Point2D(0.8, 0.2));
        t.insert(new Point2D(0.3, 0.9));
        t.insert(new Point2D(1.0, 0.0));

        contains = t.contains(new Point2D(0.0, 0.5)); //  ==>  false
        assertFalse(contains);

        Point2D nearest = t.nearest(new Point2D(0.1, 0.6));//  ==>  (0.7, 0.5)
        assertEquals(new Point2D(0.3, 0.9), nearest);
    }
    /*
    * Test base on 7a
    * Intermixed sequence of calls to insert(), isEmpty(), size(), contains(), range(), and nearest() with probabilities
    * */

    @Test
    public void testNearest() {
        KdTree t = new KdTree();
        t.insert(new Point2D(0.2, 0.2));

        t.insert(new Point2D(0.2, 0.5));
        t.insert(new Point2D(0.5, 0.2));

        t.insert(new Point2D(0.5, 0.5));
        assertEquals(new Point2D(0.5, 0.5), t.nearest(new Point2D(0.5, 0.4)));
        assertEquals(new Point2D(0.2, 0.2), t.nearest(new Point2D(0.2, 0.3)));
        assertEquals(new Point2D(0.2, 0.5), t.nearest(new Point2D(0.2, 0.4)));
        assertEquals(new Point2D(0.5, 0.2), t.nearest(new Point2D(0.5, 0.3)));
    }

    @Test
    public void testNearNullPointerException() {
        //
        KdTree t = new KdTree();
        t.insert(new Point2D(0.324, 0.91));

        t.range(new RectHV(0.082, 0.148, 0.534, 0.82)); // set.range([0.082, 0.534] x [0.148, 0.82])
        t.range(new RectHV(0.313, 0.203, 0.518, 0.301)); // set.range([0.313, 0.518] x [0.203, 0.301])

        t.insert(new Point2D(0.283, 0.495));
        Point2D nearest324 = t.nearest(new Point2D(0.495, 0.796)); //  ==>  (0.324, 0.91)
        assertEquals(new Point2D(0.324, 0.91), nearest324);

        t.insert(new Point2D(0.913, 0.057));
        boolean containsFalse = t.contains(new Point2D(0.531, 0.009)); //  ==>  false
        assertFalse(containsFalse);

        t.range(new RectHV(0.128, 0.9, 0.223, 0.916)); //set.range([0.128, 0.223] x [0.9, 0.916])
        Point2D nearest283 = t.nearest(new Point2D(0.159, 0.468)); // exception?
        assertEquals(new Point2D(0.283, 0.495), nearest283);

    }

    @Test
    public void testNearestNull() {
        KdTree t = new KdTree();
        assertNull(t.nearest(new Point2D(0.5, 0.3)));
    }

    @Test
    public void testRangeEmpty() {
        KdTree t = new KdTree();
        Iterable<Point2D> range = t.range(new RectHV(0, 0, 1, 1));
        assertFalse(range.iterator().hasNext());
    }

    @Test
    public void testRangeAllHorizontal() {
        Iterable<Point2D> range = horizontal8.range(new RectHV(0, 0, 1, 1));
        for (Point2D p : range)
            pointsHorizontal8.remove(p);
        assertTrue(pointsHorizontal8.isEmpty());
    }

    @Test
    public void testRange1() {
        List<Point2D> points = new ArrayList<>(Arrays.asList(
                new Point2D(0.6, 0.5), new Point2D(0.2, 0.2), new Point2D(0.9, 0.7),
                new Point2D(0.35, 0.8), new Point2D(0.8, 0.45)));

        KdTree t = new KdTree();
        for (Point2D p : points)
            t.insert(p);

        Iterable<Point2D> range = t.range(new RectHV(0.4, 0.4, 0.7, 0.6));

        int pointsInRange = 0;
        for (Point2D p : range) {
            pointsInRange++;
            assertTrue(p.equals(new Point2D(0.6, 0.5)));
        }
        assertEquals(1, pointsInRange);
    }



}
