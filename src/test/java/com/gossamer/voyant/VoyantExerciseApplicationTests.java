package com.gossamer.voyant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoyantExerciseApplicationTests {

    @Test
    public void applicationStart() {
        VoyantJavaInterview.main(new String[] {});
    }
}
