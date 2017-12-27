package com.dcdt.doctorstation.service;

import com.dcdt.doctorstation.preIntervenation.IntervenationManager;
import com.dcdt.doctorstation.preIntervenation.IntervenationState;
import com.dcdt.doctorstation.preIntervenation.IntervenedDoctor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by LiRong on 2017/12/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class IntervenationManagerTest {
    @Autowired
    private IntervenationManager intervenationManager;

    @Test
    public void testRegisterDoctor() {
        String id = "abcd";
        intervenationManager.registerDoctor(new IntervenedDoctor(id));
        IntervenedDoctor doctor = intervenationManager.findDoctorById(id);
        assertTrue(doctor.getId().equals(id));
    }

    @Test
    public void testChangeState() throws InterruptedException {
        String id = "abcd";
        intervenationManager.registerDoctor(new IntervenedDoctor(id));
        intervenationManager.changeDoctorState(id, 2);
        assertTrue(intervenationManager.checkDoctorState(id) == 2);

        intervenationManager.registerDoctor(new IntervenedDoctor(id));
        TimeUnit.SECONDS.sleep(2);

        assertTrue(IntervenationState.OUT_TIME.getState() == intervenationManager.checkDoctorState(id));
        assertNull(intervenationManager.findDoctorById(id));
    }
}
