/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2014, Enno Gottschalk <mrmaffen@googlemail.com>
 *
 *   Tomahawk is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Tomahawk is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Tomahawk. If not, see <http://www.gnu.org/licenses/>.
 */
package org.tomahawk.tomahawk_android.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor mPipeLineThreadPool;

    private ThreadPoolExecutor mPlaybackThreadPool;

    public ThreadManager() {
        mPipeLineThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, new PriorityBlockingQueue<Runnable>());
        mPlaybackThreadPool = new ThreadPoolExecutor(1, 1, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, new LinkedBlockingQueue<Runnable>());
    }

    public void executePipeLineRunnable(TomahawkRunnable r) {
        mPipeLineThreadPool.execute(r);
    }

    public void executePlaybackRunnable(Runnable r) {
        mPlaybackThreadPool.execute(r);
    }

    public boolean isActive() {
        return mPipeLineThreadPool.getActiveCount() > 0
                || mPipeLineThreadPool.getQueue().size() > 0
                || mPlaybackThreadPool.getActiveCount() > 0
                || mPlaybackThreadPool.getQueue().size() > 0;
    }
}
