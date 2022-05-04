package com.example.myapplication

import android.util.Log

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/5/4 7:28 下午
 */
object MusicData {

    val list = mutableListOf<Music>()

    init {
        list.add(Music().apply {
            timestamps = arrayOf(
                0.27, 1.95, 3.63, 5.31, 6.98, 8.66, 10.34, 12.02, 13.7, 15.37, 17.05, 18.73, 20.41, 22.09, 23.77, 25.45, 27.12, 28.8, 30.48, 32.16, 33.84, 35.51, 37.2, 38.87, 40.55, 42.23, 43.91, 45.59, 47.26
            )
            fileName = "kadian_2.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.97, 2.58, 4.18, 5.77, 7.38, 8.98, 10.58, 12.18, 13.78, 15.38, 16.98, 18.58, 20.18, 21.79, 23.38, 24.97, 26.57, 28.17
            )
            fileName = "kadian_4.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.59, 2.25, 3.9, 5.59, 7.25, 8.91, 10.59, 12.25, 13.92, 15.59, 17.25, 18.92, 20.59, 22.25, 23.92, 25.58, 27.25, 28.92, 30.58
            )
            fileName = "kadian_14.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.02, 2.4, 4.78, 7.16, 9.55, 11.92, 14.31, 16.69, 19.06, 21.44, 23.82, 26.2, 28.59, 30.95, 33.34, 35.72, 38.1, 40.48, 42.86, 45.23, 47.61, 49.99, 52.37, 54.75, 57.13
            )
            fileName = "kadian_15.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.65, 3.19, 5.76, 8.34, 10.9, 13.45, 16.05, 18.64, 21.19, 23.73, 26.22, 28.72, 31.19, 33.68, 36.22, 38.72, 41.2, 43.67, 46.13, 48.59, 51.06, 53.56,
                56.06
            )
            fileName = "kadian_16.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                1.52, 3.52, 5.52, 7.52, 9.52, 11.52, 13.52, 15.52, 17.52, 19.52, 21.52, 23.52, 25.52, 27.52, 29.52
            )
            fileName = "kadian_17.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.02, 2.86, 5.67, 8.48, 11.31, 14.14, 16.97, 19.79, 22.61, 25.44, 28.26
            )
            fileName = "kadian_20.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.77, 3.44, 6.11, 8.78, 11.45, 14.11, 16.78, 19.44, 22.11, 24.78, 27.44
            )
            fileName = "kadian_21.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                2.08, 3.43, 4.84, 6.2, 7.54, 8.87, 10.19, 11.52, 12.83, 14.15, 15.44, 16.9, 18.36, 19.75, 21.11, 22.52, 23.86, 25.19, 26.53, 27.92, 29.27, 30.69, 32.12, 33.48, 34.81, 36.09, 37.42, 38.76, 40.19, 41.56, 42.94, 44.24, 45.57, 46.87, 48.2, 49.47, 50.78, 52.09, 53.38, 54.7, 56.02, 57.33, 58.62, 59.91, 61.22, 62.58, 63.84, 65.08, 66.32, 67.56
            )
            fileName = "kadian_22.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.07, 1.83, 3.57, 5.32, 7.05, 8.8, 10.53
            )
            fileName = "kadian_24.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                1.55, 3.5, 5.45, 7.41, 9.35, 11.31, 13.25, 15.21, 17.15, 19.1, 21.06, 22.99, 24.97, 26.94, 28.87
            )
            fileName = "kadian_25.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                1.06, 2.61, 4.17, 5.73, 7.28, 8.84, 10.4, 11.96, 13.52, 15.08, 16.64, 18.19, 19.74, 21.3, 22.87
            )
            fileName = "kadian_26.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.78, 2.52, 4.25, 6.0, 7.74, 9.47, 11.22, 12.95, 14.69, 16.43
            )
            fileName = "kadian_30.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                0.43, 2.38, 4.32, 6.26, 8.19, 10.13, 12.06, 14.0, 15.92, 17.86, 19.79, 21.74, 23.67, 25.61, 27.55, 29.48, 31.42, 33.35, 35.27, 37.22, 39.15, 41.09, 43.03, 44.96, 46.9, 48.84, 50.77, 52.7, 54.64, 56.58, 58.51, 60.45
            )
            fileName = "kadian_31.m4a"
        })
        list.add(Music().apply {
            timestamps = arrayOf(
                1.5, 3.5, 5.5, 7.5, 9.5, 11.5, 13.5, 15.5, 17.5, 19.5, 21.5, 23.5, 25.5, 27.5, 29.5, 31.49, 33.49, 35.5, 37.5, 39.49, 41.48, 43.5, 45.5, 47.49, 49.5, 51.5, 53.5, 55.5, 57.49
            )
            fileName = "kadian_32.m4a"
        })
        Log.e("zhaoleihe", "init done", )
        for (music in list) {
            Log.e("zhaoleihe", "${music.fileName}  ${music.timestamps.size}", )
        }
    }
}