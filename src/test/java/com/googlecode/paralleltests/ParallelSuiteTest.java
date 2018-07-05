package com.googlecode.paralleltests;

import org.junit.Test;
import org.junit.runner.RunWith;
import testhelpers.TestHelper;

@RunWith(ParallelSuite.class)
public class ParallelSuiteTest {

    public static class HttpRequests {
        public static class GoogleRequests {
            @Test
            public void request1() {

            }

            @Test
            public void request2() {

            }

            @Test
            public void request3() {

            }

            @Test
            public void request4() {

            }

            @Test
            public void request5() {

            }

            @Test
            public void request6() {

            }

            @Test
            public void request7() {

            }

            @Test
            public void request8() {

            }

            @Test
            public void request9() {

            }

            @Test
            public void request10() {

            }
        }

        public static class OtherRequests {
            @Test
            public void request1() {

            }

            @Test
            public void request2() {

            }

            @Test
            public void request3() {

            }

            @Test
            public void request4() {

            }

            @Test
            public void request5() {

            }

            @Test
            public void request6() {

            }

            @Test
            public void request7() {

            }

            @Test
            public void request8() {

            }

            @Test
            public void request9() {

            }

            @Test
            public void request10() {

            }
        }
    }

    public static class SleepTests {
        @Test
        public void test1() {
            TestHelper.sleep(3000);
        }

        @Test
        public void test2() {
            TestHelper.sleep(3000);
        }

        @Test
        public void test3() {
            TestHelper.sleep(3000);
        }

        @Test
        public void test4() {
            TestHelper.sleep(3000);
        }

        @Test
        public void test5() {
            TestHelper.sleep(3000);
        }

        @Test
        public void test6() {

        }

        @Test
        public void test7() {

        }

        @Test
        public void test8() {

        }

        @Test
        public void test9() {

        }

        @Test
        public void test10() {

        }
    }
}
