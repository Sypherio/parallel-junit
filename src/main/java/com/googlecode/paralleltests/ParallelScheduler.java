package com.googlecode.paralleltests;

/**
 * Copyright 2018 Steven Teplica
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.runners.model.RunnerScheduler;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;

class ParallelScheduler implements RunnerScheduler {

    private static ForkJoinPool forkJoinPool = setUpForkJoinPool();

    private final Deque<ForkJoinTask<?>> asyncTasks = new LinkedList<>();
    private Runnable lastScheduledChild;

    private static ForkJoinPool setUpForkJoinPool() {
        int numThreads = Math.max(2, Runtime.getRuntime().availableProcessors());
        ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = pool -> {
            if (pool.getPoolSize() >= pool.getParallelism()) {
                return null;
            } else {
                ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                thread.setName("JUnit-" + thread.getName());
                return thread;
            }
        };
        return new ForkJoinPool(numThreads, threadFactory, null, false);
    }

    @Override
    public void schedule(Runnable childStatement) {
        if (lastScheduledChild != null) {
            if (ForkJoinTask.inForkJoinPool()) {
                asyncTasks.addFirst(ForkJoinTask.adapt(lastScheduledChild).fork());
            } else {
                asyncTasks.addFirst(forkJoinPool.submit(lastScheduledChild));
            }
        }
        lastScheduledChild = childStatement;
    }

    @Override
    public void finished() {
        List<Throwable> throwables = new ArrayList<>();
        if (lastScheduledChild != null) {
            if (ForkJoinTask.inForkJoinPool()) {
                try {
                    lastScheduledChild.run();
                } catch (Throwable t) {
                    throwables.add(t);
                }
            } else {
                asyncTasks.addFirst(forkJoinPool.submit(lastScheduledChild));
            }
            for (ForkJoinTask<?> task : asyncTasks) {
                try {
                    task.join();
                } catch (Throwable t) {
                    throwables.add(t);
                }
            }
            throwables.forEach(t -> {
                throw (Error) t;
            });
        }
    }
}
