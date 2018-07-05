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

import com.googlecode.junittoolbox.ParallelRunner;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelSuite extends Suite {

    public ParallelSuite(Class<?> type, RunnerBuilder builder) throws Throwable {
        super(type, getParallelRunners(type));
        setScheduler(new ParallelScheduler());
    }

    private static List<Runner> getParallelRunners(Class<?> type) {
        return getInnerTestClasses(type).stream()
                                        .map(innerClass -> {
                                            try {
                                                if (innerClass.isAnnotationPresent(IgnoreParallel.class)) {
                                                    return new BlockJUnit4ClassRunner(innerClass);
                                                } else {
                                                    return new ParallelRunner(innerClass);
                                                }
                                            } catch (InitializationError e) {
                                                return null;
                                            }
                                        })
                                        .collect(Collectors.toList());
    }

    private static List<Class> getInnerTestClasses(Class type) {
        List<Class> innerClasses = new ArrayList<>();
        Arrays.asList(type.getDeclaredClasses())
              .forEach(innerClass -> {
                  boolean classContainsTests = Arrays.stream(innerClass.getDeclaredMethods())
                                                     .anyMatch(method -> method.isAnnotationPresent(Test.class));
                  if (classContainsTests) {
                      innerClasses.add(innerClass);
                  }
                  innerClasses.addAll(getInnerTestClasses(innerClass));
              });
        return innerClasses;
    }
}
