package com.company;

import org.opencv.core.Point;
import org.opencv.core.Point3;

public class MathTransform {
    public static double matrixWidth = 8;
    public static double matrixHeight = 6;
    public static double focalLength = 6;
    public static double height;

    public static double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    public static double convertMmToM(double mm) {
        return mm / 1000;
    }

    public static double distance(Point3 p1, Point3 p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z));
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static double[][] multiply(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length;
        int m2RowLength = m2.length;

        if (m1ColLength != m2RowLength)
            return null;

        int mRRowLength = m1.length;
        int mRColLength = m2[0].length;

        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {
            for (int j = 0; j < mRColLength; j++) {
                for (int k = 0; k < m1ColLength; k++) {
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

    public static Point3 getDirectingVector(double[][] pointMatrix, double[][] focus) {
        Point3 vector = new Point3();

        vector.x = pointMatrix[0][0] - focus[0][0];
        vector.y = pointMatrix[1][0] - focus[1][0];
        vector.z = pointMatrix[2][0] - focus[2][0];

        return vector;
    }

    public static Point3 getDirectingVector(Point3 pointMatrix, Point3 focus) {
        Point3 vector = new Point3();

        vector.x = pointMatrix.x - focus.x;
        vector.y = pointMatrix.y - focus.y;
        vector.z = pointMatrix.z - focus.z;

        return vector;
    }

    public static Point3 getMatrixPointOnGround(Point3 centerMatrix, String point) {
        Point3 groundMatrix = new Point3();
        groundMatrix.z = 0;

        double[][] resultMatrix = multiply(Matrices.yawMatrix, multiply(Matrices.rollMatrix, Matrices.pitchMatrix));

        double[][] focusMatrix = multiply(resultMatrix, getMatrixPointOnAir(centerMatrix, "focus"));
        double[][] airMatrix = multiply(resultMatrix, getMatrixPointOnAir(centerMatrix, point));

        Point3 directVector = getDirectingVector(airMatrix, focusMatrix);

        double param = (height - focusMatrix[2][0]) / directVector.z;
        groundMatrix.x = focusMatrix[0][0] + directVector.x * param;
        groundMatrix.y = focusMatrix[1][0] + directVector.y * param;

        return groundMatrix;
    }

    public static double[][] getMatrixPointOnAir(Point3 center, String point) {
        double[][] cornerMatrix = new double[4][1];

        if (point == "rightTopCorner") {
            cornerMatrix[0][0] = center.x + convertMmToM(matrixWidth) / 2;
            cornerMatrix[1][0] = center.y + convertMmToM(matrixHeight) / 2;
            cornerMatrix[2][0] = center.z;
            cornerMatrix[3][0] = 1;
        }

        if (point == "leftTopCorner") {
            cornerMatrix[0][0] = center.x - convertMmToM(matrixWidth) / 2;
            cornerMatrix[1][0] = center.y + convertMmToM(matrixHeight) / 2;
            cornerMatrix[2][0] = center.z;
            cornerMatrix[3][0] = 1;
        }

        if (point == "rightBottomCorner") {
            cornerMatrix[0][0] = center.x + convertMmToM(matrixWidth) / 2;
            cornerMatrix[1][0] = center.y - convertMmToM(matrixHeight) / 2;
            cornerMatrix[2][0] = center.z;
            cornerMatrix[3][0] = 1;
        }

        if (point == "leftBottomCorner") {
            cornerMatrix[0][0] = center.x - convertMmToM(matrixWidth) / 2;
            cornerMatrix[1][0] = center.y - convertMmToM(matrixHeight) / 2;
            cornerMatrix[2][0] = center.z;
            cornerMatrix[3][0] = 1;
        }

        if (point == "focus") {
            cornerMatrix[0][0] = center.x;
            cornerMatrix[1][0] = center.y;
            cornerMatrix[2][0] = center.z - convertMmToM(focalLength);
            cornerMatrix[3][0] = 1;
        }

        return cornerMatrix;
    }

    public static double[][] getMatrixPointOnAir(Point3 center, double i, double j) {
        double[][] cornerMatrix = new double[4][1];
        cornerMatrix[0][0] = center.x + convertMmToM(matrixWidth) * i;
        cornerMatrix[1][0] = center.y + convertMmToM(matrixHeight) * j;
        cornerMatrix[2][0] = center.z;
        cornerMatrix[3][0] = 1;
        return cornerMatrix;
    }

    public static Plane getPlane(Point3 m0, Point3 m1, Point3 m2) {
        double X1 = m1.x - m0.x;
        double X2 = m2.x - m0.x;
        double Y1 = m1.y - m0.y;
        double Y2 = m2.y - m0.y;
        double Z1 = m1.z - m0.z;
        double Z2 = m2.z - m0.z;

        Plane plane = new Plane();

        plane.A = Y1 * Z2 - Z1 * Y2;
        plane.B = Z1 * X2 - X1 * Z2;
        plane.C = X1 * Y2 - Y1 * X2;
        plane.D = -m0.x * plane.A - m0.y * plane.B - m0.z * plane.C;

        return plane;
    }

    public static Point3 getIntersect(Point3 p1, Point3 p2, Plane plane) {
        Point3 directVector = getDirectingVector(p1, p2);

        double t = -(plane.D + plane.A * p1.x + plane.B * p1.y + plane.C * p1.z) /
                (plane.A * directVector.x + plane.B * directVector.y + plane.C * directVector.z);

        Point3 intersect = new Point3();

        intersect.x = p1.x + t * directVector.x;
        intersect.y = p1.y + t * directVector.y;
        intersect.z = p1.z + t * directVector.z;

        return intersect;
    }

    public static double distanceFromPointToLine(Point3 from, Point3 p1, Point3 p2) {
        Point3 directVector = getDirectingVector(p1, p2);
        double l1 = directVector.x;
        double m1 = directVector.y;
        double n1 = directVector.z;

        double X = from.x - p1.x;
        double Y = from.y - p1.y;
        double Z = from.z - p1.z;

        double dist = (Math.sqrt((X * m1 - Y * l1) * (X * m1 - Y * l1) + (Y * n1 - Z * m1) * (Y * n1 - Z * m1) + (Z * l1 - X * n1) * (Z * l1 - X * n1)))
                / (Math.sqrt(l1 * l1 + m1 * m1 + n1 * n1));

        return dist;
    }
}
