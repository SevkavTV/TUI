package com.company;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Task4 {
    public static double[] listOfCoords;
    public static boolean ok;
    public static List<Image> lsImages;
    public static String pathToDocument = "";
    public static List<Integer> listOfIndex = new ArrayList<>();
    public static boolean check = false;

    public static void solveTask4() throws Exception {
        check = true;
        int n = PhotosInfo.photos.size();
        List<Area> areaList = new ArrayList<>();
        Image[] arrayOfImages = new Image[n];
        int currIndex = 0;

        for (int i = 0; i < PhotosInfo.photos.size(); i++) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(PhotosInfo.photos.get(i).getLeftTopCorner().x, PhotosInfo.photos.get(i).getLeftTopCorner().y);
            path.lineTo(PhotosInfo.photos.get(i).getRightTopCorner().x, PhotosInfo.photos.get(i).getRightTopCorner().y);
            path.lineTo(PhotosInfo.photos.get(i).getRightBottomCorner().x, PhotosInfo.photos.get(i).getRightBottomCorner().y);
            path.lineTo(PhotosInfo.photos.get(i).getLeftBottomCorner().x, PhotosInfo.photos.get(i).getLeftBottomCorner().y);
            path.closePath();

            Shape temp = path;
            areaList.add(new Area(temp));
        }

        boolean[][] matrix = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (check(areaList.get(i), areaList.get(j))) {
                    matrix[i][j] = true;
                    matrix[j][i] = true;
                } else {
                    matrix[i][j] = false;
                    matrix[j][i] = false;
                }
            }
        }
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = i;
        }
        int[] path = PlanPath.init(matrix);
        ok = true;
        for (int i = 0; i + 1 < n; i++) {
            if (!matrix[path[i]][path[i + 1]]) {
                ok = false;
                break;
            }
        }
        a = path;
        if (ok) {
            listOfCoords = new double[n * 8];
            for (int i = 0; i < PhotosInfo.photos.size(); ++i) {
                listOfIndex.add(a[i]);
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftBottomCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftBottomCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightBottomCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightBottomCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightTopCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightTopCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftTopCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftTopCorner().y);
                currIndex++;
            }
        } else {
            listOfCoords = new double[n * 8];
            for (int i = 0; i < PhotosInfo.photos.size(); ++i) {
                listOfIndex.add(a[i]);
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftBottomCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftBottomCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightBottomCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightBottomCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightTopCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getRightTopCorner().y);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftTopCorner().x);
                currIndex++;
                listOfCoords[currIndex] = (PhotosInfo.photos.get(a[i]).getLeftTopCorner().y);
                currIndex++;
            }

        }
        try (FileWriter writer = new FileWriter("notes.txt", false)) {
            for (int i = 0; i < listOfCoords.length; ++i) {
                writer.write(listOfCoords[i] + " ");
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean check(Area a, Area b) {
        Area c = (Area) a.clone();
        Area d = (Area) b.clone();
        c.intersect(d);
        return !c.getPathIterator(null).isDone();
    }
}