package com.pandatech.pandafit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pandatech.pandafit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = new ApplicationUser();
        applicationUser1.setId("id1");
        ApplicationUser applicationUser2 = new ApplicationUser();
        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);
        applicationUser2.setId("id2");
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
        applicationUser1.setId(null);
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }
}
