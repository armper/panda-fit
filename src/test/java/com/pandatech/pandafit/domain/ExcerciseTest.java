package com.pandatech.pandafit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pandatech.pandafit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Excercise.class);
        Excercise excercise1 = new Excercise();
        excercise1.setId("id1");
        Excercise excercise2 = new Excercise();
        excercise2.setId(excercise1.getId());
        assertThat(excercise1).isEqualTo(excercise2);
        excercise2.setId("id2");
        assertThat(excercise1).isNotEqualTo(excercise2);
        excercise1.setId(null);
        assertThat(excercise1).isNotEqualTo(excercise2);
    }
}
