package org.example.spring;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoApplication.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    @Ignore
    public void contextLoads() {
        Assert.isNull(null, "null object");
    }

    @Test
    @Ignore
    public void contextLoadsOK() {
        Assert.notNull(new String());
    }

}
