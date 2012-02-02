/**
 * Test the GPXcalculator class which contains a calculateDistanceTraveled
 * method that calculates the total distance traveled over
 * track segments contained in a GPXtrk object.
 */
package edu.upenn.cis350.gpx;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author hoconnie
 *
 */
public class GPXcalculatorTest {
	GPXtrkpt pos1, pos2, pos3, pos4;
	GPXtrkpt neg1, neg2, neg3, neg4;
	GPXtrkpt bound1, bound2, bound3, bound4;
	GPXtrkpt greater90, lessNeg90, greater180, lessNeg180;

	GPXtrkseg posTrkPtsSeg;
	GPXtrkseg negTrkPtsSeg;
	GPXtrkseg mixTrkPtsSeg;
	GPXtrkseg boundaryPtsSeg;
	GPXtrkseg rightOffBoundaryPtsSeg;
	GPXtrkseg zeroDistanceSeg;
	GPXtrkseg circularPathSeg;
	GPXtrkseg onePtSeg;
	GPXtrkseg invalid90Seg;
	GPXtrkseg invalid180Seg;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		/* Track points */
		pos1 = new GPXtrkpt(9.0, 12.0, new Date(157400));
		pos2 = new GPXtrkpt(3.0, 4.0, new Date(157415));
		pos3 = new GPXtrkpt(6.0, 8.0, new Date(158010));
		pos4 = new GPXtrkpt(9.0, 12.0, new Date(164233));

		neg1 = new GPXtrkpt(-9.0, -12.0, new Date(200423));
		neg2 = new GPXtrkpt(-6.0, -8.0, new Date(201345));
		neg3 = new GPXtrkpt(-12.0, -16.0, new Date(202410));
		neg4 = new GPXtrkpt(-9.0, -12.0, new Date(203233));

		bound1 = new GPXtrkpt(90.0, 45.0, new Date(234243)); // boundary condition
		bound2 = new GPXtrkpt(90.0, 47.0, new Date(235454)); // boundary condition

		bound3 = new GPXtrkpt(91.0, 36.0, new Date(236948)); // right off boundary condition
		bound4 = new GPXtrkpt(89.0, 36.0, new Date(237424)); // right off boundary condition

		greater90 = new GPXtrkpt(93.0, 12.0, new Date(357400));
		lessNeg90 = new GPXtrkpt(-98.0, -13.0, new Date(358415));
		greater180 = new GPXtrkpt(-12.0, 189.0, new Date(359010));
		lessNeg180 = new GPXtrkpt(35.0, -186.0, new Date(364233));

		/* ArrayLists of track points */
		ArrayList<GPXtrkpt> posTrkPts = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> negTrkPts = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> mixTrkPts = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> onePt = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> boundaryPts = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> rightOffBoundaryPts = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> zeroDistance = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> circularPath = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> invalid90 = new ArrayList<GPXtrkpt>();
		ArrayList<GPXtrkpt> invalid180 = new ArrayList<GPXtrkpt>();

		// Distance of 10 + 5 + 5 = 20
		posTrkPts.add(pos1);
		posTrkPts.add(pos2);
		posTrkPts.add(pos3);
		posTrkPts.add(pos4);

		// Distance of 5 + 10 + 5 = 20
		negTrkPts.add(neg1);
		negTrkPts.add(neg2);
		negTrkPts.add(neg3);
		negTrkPts.add(neg4);

		// Distance of 10 + 20 + 5 = 35
		mixTrkPts.add(pos1);
		mixTrkPts.add(pos2);
		mixTrkPts.add(neg1);
		mixTrkPts.add(neg2);

		// Distance of 2
		boundaryPts.add(bound1);
		boundaryPts.add(bound2);

		// Distance of 2
		rightOffBoundaryPts.add(bound3);
		rightOffBoundaryPts.add(bound4);

		// Distance of 0
		zeroDistance.add(pos1);
		zeroDistance.add(pos1);
		zeroDistance.add(pos1);

		// Distance of 10 + 5 + 5 = 20
		circularPath.add(pos1);
		circularPath.add(pos2);
		circularPath.add(pos3);
		circularPath.add(pos1);

		// Distance of 0
		onePt.add(pos1);

		// Distance of 0
		invalid90.add(greater90);
		invalid90.add(lessNeg90);
		invalid90.add(pos1);
		invalid90.add(pos2);

		// Distance of 0
		invalid180.add(greater180);
		invalid180.add(lessNeg180);
		invalid180.add(neg1);
		invalid180.add(neg2);

		/* Track segments */
		posTrkPtsSeg = new GPXtrkseg(posTrkPts);
		negTrkPtsSeg = new GPXtrkseg(negTrkPts);
		mixTrkPtsSeg = new GPXtrkseg(mixTrkPts);
		boundaryPtsSeg = new GPXtrkseg(boundaryPts);
		rightOffBoundaryPtsSeg = new GPXtrkseg(rightOffBoundaryPts);
		zeroDistanceSeg = new GPXtrkseg(zeroDistance);
		circularPathSeg = new GPXtrkseg(circularPath);
		onePtSeg = new GPXtrkseg(onePt);
		invalid90Seg = new GPXtrkseg(invalid90);
		invalid180Seg = new GPXtrkseg(invalid180);
	}

	/* Test the calculateDistanceTraveled method with all valid points of all
	 * positive coordinates. */
	@Test
	public void testCalculationPositiveCoordinates() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(posTrkPtsSeg);
		GPXtrk trk = new GPXtrk("All positives", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(20, result, 0);
	}

	/* Test the calculateDistanceTraveled method with all valid points of all
	 * negative coordinates. */
	@Test
	public void testCalculationNegativeCoordinates() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(negTrkPtsSeg);
		GPXtrk trk = new GPXtrk("All negatives", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(20, result, 0);
	}

	/* Test the calculateDistanceTraveled method with all valid points of a mix of positive
	 * and negative coordinates. */
	@Test
	public void testCalculationMixedCoordinates() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(mixTrkPtsSeg);
		GPXtrk trk = new GPXtrk("Both positive and negative coordinates", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(35, result, 0);
	}

	/* Test the calculateDistanceTraveled method with multiple track
	 * segments in it, which should return the sum of the distances
	 * of the track segments.
	 */
	@Test
	public void testCalculationMultipleSegments() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(posTrkPtsSeg);
		segs.add(negTrkPtsSeg);
		segs.add(mixTrkPtsSeg);
		GPXtrk trk = new GPXtrk("Only one point", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(75, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a boundary condition:
	 * valid points right on the edge of a valid latitude (90, since past 90
	 * should return 0 distance. */
	@Test
	public void testCalculationBoundary() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(boundaryPtsSeg);
		GPXtrk trk = new GPXtrk("Boundary points", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(2, result, 0);
	}

	/* Test the calculateDistanceTraveled method with latitudes
	 * right around a boundary condition: valid points one over and one 
	 * under the edge valid latitude of 90. */
	@Test
	public void testCalculationRightOffBoundary() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(rightOffBoundaryPtsSeg);
		GPXtrk trk = new GPXtrk("Right off boundary points", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(2, result, 0);
	}

	/* Test the calculateDistanceTraveled method with the same track
	 * point repeated in a track segment, for no distance traveled.
	 */
	@Test
	public void testCalculationZeroDistance() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(zeroDistanceSeg);
		GPXtrk trk = new GPXtrk("Zero distance", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(0, result, 0);
	}

	/* Test the calculateDistanceTraveled method with the a circular
	 * path that starts and ends on the same track point
	 * but has a distance traveled in between.
	 */
	@Test
	public void testCalculationCircularPath() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(circularPathSeg);
		GPXtrk trk = new GPXtrk("Circular path", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(20, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a null GPXtrk object,
	 * which should return -1.
	 */
	@Test
	public void testNullGPXtrk() {
		double result = GPXcalculator.calculateDistanceTraveled(null);
		assertEquals(-1, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a GPXtrk object
	 * that contains no track segments, which should return -1.
	 */
	@Test
	public void testNoGPXtrkseg() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		GPXtrk trk = new GPXtrk("No track segments", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(-1, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a GPXtrk object
	 * that contains a null track segment, which should just consider
	 * the distance for that segment 0 but still return the sum of the
	 * distances from other valid segments.
	 */
	@Test
	public void testNullGPXtrkseg() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(posTrkPtsSeg);
		segs.add(negTrkPtsSeg);
		segs.add(null);
		GPXtrk trk = new GPXtrk("Contains a null track segment", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(40, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a track segment
	 * that has no track points in it, which should just count this distance
	 * as 0 but still return the sum of distances of the other
	 * valid track segments.
	 */
	@Test
	public void testNoGPXtrkpt() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(posTrkPtsSeg);
		segs.add(negTrkPtsSeg);
		// Track segment with no track points
		ArrayList<GPXtrkpt> noTrkPts = new ArrayList<GPXtrkpt>();
		GPXtrkseg noTrkPtsSeg = new GPXtrkseg(noTrkPts);
		segs.add(noTrkPtsSeg);
		GPXtrk trk = new GPXtrk("Contains a track segment with no track points", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(40, result, 0);
	}

	/* Test the calculateDistanceTraveled method with one valid point, which should
	 * return 0 distance traveled. */
	@Test
	public void testOneGPXtrkpt() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(onePtSeg);
		GPXtrk trk = new GPXtrk("Only one point", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(0, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a track segment
	 * that contains a null track point, which should just consider
	 * the distance for that segment 0 but still return the sum of the
	 * distances from other valid segments.
	 */
	@Test
	public void testNullGPXtrkpt() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(posTrkPtsSeg);
		segs.add(negTrkPtsSeg);
		// Make a track segment with a null track point
		ArrayList<GPXtrkpt> hasNullTrkPt = new ArrayList<GPXtrkpt>();
		hasNullTrkPt.add(pos1);
		hasNullTrkPt.add(pos2);
		hasNullTrkPt.add(null);
		// Distance should be 0 for this segment
		GPXtrkseg hasNullTrkPtSeg = new GPXtrkseg(hasNullTrkPt);
		segs.add(hasNullTrkPtSeg);
		GPXtrk trk = new GPXtrk("Contains a track segment with a null track point", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(40, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a track segment
	 * that contains invalid track points (latitude > 90 or < -90), should consider
	 * the distance for that segment 0 but still return the sum of the
	 * distances from other valid segments.
	 */
	@Test
	public void testLatGreater90() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(mixTrkPtsSeg);
		segs.add(invalid90Seg); // distance should be 0
		GPXtrk trk = new GPXtrk("Track segment with invalid latitudes", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(35, result, 0);
	}

	/* Test the calculateDistanceTraveled method with a track segment
	 * that contains invalid track points (longitude > 180 or < -180), should consider
	 * the distance for that segment 0 but still return the sum of the
	 * distances from other valid segments.
	 */
	@Test
	public void testLongGreater180() {
		ArrayList<GPXtrkseg> segs = new ArrayList<GPXtrkseg>();
		segs.add(mixTrkPtsSeg);
		segs.add(invalid180Seg); // distance should be 0
		GPXtrk trk = new GPXtrk("Track segment with invalid longitudes", segs);
		double result = GPXcalculator.calculateDistanceTraveled(trk);
		assertEquals(35, result, 0);
	}

}
