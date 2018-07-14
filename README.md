parallel-junit
==============

This library provides a new JUnit Runner called `ParallelSuite`.

If a class is annotated with `@RunWith(ParallelSuite.class)`, all of this class' tests and inner static test classes will run in parallel.

The runner will recursively search through all inner static classes to collect the set of tests that will be run in parallel.
