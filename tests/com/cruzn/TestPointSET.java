package com.cruzn;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPointSET {

    private List<Point2D> pointsHorizontal8 = new ArrayList<>(Arrays.asList(
            new Point2D(0.9, 0.5),
            new Point2D(0.2, 0.5),
            new Point2D(0.3, 0.5),
            new Point2D(0.4, 0.5),
            new Point2D(0.1, 0.5),
            new Point2D(0.6, 0.5),
            new Point2D(0.5, 0.5),
            new Point2D(0.7, 0.5)
    ));

    private PointSET setHorizontal8;

    @Before
    public void setUp(){
        setHorizontal8 = new PointSET();
        for (Point2D p : pointsHorizontal8)
            setHorizontal8.insert(p);
    }

    @Test
    public void testIsEmptyFalse(){
        assertFalse(setHorizontal8.isEmpty());
    }

    @Test
    public void testIsEmptyTrue(){
        PointSET emptySet = new PointSET();
        assertTrue(emptySet.isEmpty());
        emptySet.insert(new Point2D(0.1, 0.5));
        assertFalse(emptySet.isEmpty());
    }

    @Test
    public void testSize8(){
        Assert.assertEquals(8, setHorizontal8.size());
    }

    @Test
    public void testSize0(){
        PointSET emptySet = new PointSET();
        Assert.assertEquals(0, emptySet.size());
        emptySet.insert(new Point2D(0.1, 0.5));
        Assert.assertEquals(1, emptySet.size());
    }

    @Test
    public void testInsertThroughContains(){
        assertTrue(setHorizontal8.contains(new Point2D(0.9, 0.5)));
    }

    @Test
    public void testContainsTrue(){
        assertTrue(setHorizontal8.contains(new Point2D(0.9, 0.5)));
    }

    @Test
    public void testContainsFalse(){
        assertFalse(setHorizontal8.contains(new Point2D(0.9, 0.8)));
    }

    @Test
    public void testRangeNone(){
        RectHV searchRange = new RectHV(0.75, 0.4, 0.85, 0.6);
        Iterable<Point2D> result = setHorizontal8.range(searchRange);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testRangeAll(){
        RectHV searchRange = new RectHV(0.05, 0.4, 0.95, 0.6);
        Iterable<Point2D> result = setHorizontal8.range(searchRange);
        int resultSize = 0;
        for (Point2D p : result) {
            assertTrue(pointsHorizontal8.contains(p));
            resultSize++;
        }
        assertEquals(8, resultSize);
    }

    @Test
    public void testRangeLeftPoints(){
        RectHV searchRange = new RectHV(0.05, 0.4, 0.45, 0.6);
        Iterable<Point2D> result = setHorizontal8.range(searchRange);
        int resultSize = 0;
        for (Point2D p : result) {
            assertTrue(pointsHorizontal8.contains(p));
            resultSize++;
        }
        assertEquals(4, resultSize);

    }

    @Test
    public void testRangeRightPoints(){
        RectHV searchRange = new RectHV(0.45, 0.4, 0.95, 0.6);
        Iterable<Point2D> result = setHorizontal8.range(searchRange);
        int resultSize = 0;
        for (Point2D p : result) {
            assertTrue(pointsHorizontal8.contains(p));
            resultSize++;
        }
        assertEquals(4, resultSize);
    }


    @Test
    public void testNearestInMiddleOf2Points(){
        Point2D nearest = setHorizontal8.nearest(new Point2D(0.15, 0.4));
        assertEquals(new Point2D(0.1, 0.5), nearest); // or should it be (0.2, 0.5) ?
    }

    @Test
    public void testNearestIsMiddle(){
        Point2D nearest = setHorizontal8.nearest(new Point2D(0.4, 0.4));
        assertEquals(new Point2D(0.4, 0.5), nearest);
    }

    @Test
    public void testNearestAfterLast(){
        Point2D nearest = setHorizontal8.nearest(new Point2D(0.95, 0.5));
        assertEquals(new Point2D(0.9, 0.5), nearest);
    }

    @Test
    public void testNearestBeforeFirst(){
        Point2D nearest = setHorizontal8.nearest(new Point2D(0.05, 0.5));
        assertEquals(new Point2D(0.1, 0.5), nearest);
    }
}
