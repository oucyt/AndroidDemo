package com.ty.unittest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @Before
    public void before() {
        System.out.println("before");
    }


    @After
    public void after() {
        System.out.println("after");

    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");

    }

    @Test
    public void addition_isCorrect() {
        Maths maths = new Maths();
        assertEquals(4, maths.add(2, 2));
    }
    @Test
    public void testMock() {
        Maths maths = mock(Maths.class);
        when(maths.add(2,2)).thenReturn(6);
        assertEquals(6, maths.add(2, 2));
    }
}