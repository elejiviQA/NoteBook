package org.noBrains;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NoteBookServiceTest extends TestCase {
    public NoteBookServiceTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(NoteBookServiceTest.class);
    }

    public void testApp() {
        assertTrue(true);
    }
}
