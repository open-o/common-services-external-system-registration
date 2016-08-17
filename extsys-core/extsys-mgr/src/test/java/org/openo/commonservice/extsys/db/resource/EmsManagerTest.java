/**
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.commonservice.extsys.db.resource;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.commonservice.extsys.Handle.EmsHandler;
import org.openo.commonservice.extsys.db.util.H2DbServer;
import org.openo.commonservice.extsys.db.util.HibernateSession;
import org.openo.commonservice.extsys.entity.db.EmsData;
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.dao.DaoManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EmsHandler.class})
public class EmsManagerTest {
    private EmsHandler handler;
    private static String id = "0000000000000000";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        H2DbServer.startUp();

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        try {
            HibernateSession.destory();
            H2DbServer.shutDown();
        } catch (Exception e) {
            Assert.fail("Exception" + e.getMessage());
        }
    }

    @Before
    public void setUp() throws Exception {
        DaoManager.getInstance().setSessionFactory(HibernateSession.init());
        EmsData data = new EmsData();
        data.setName("ems");
        handler = PowerMockito.spy(new EmsHandler());
        PowerMockito.doReturn(true).when(handler, "validity", data);
        try {
            id = handler.add(data).getId();
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            handler.delete(id);
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
        }
    }

    @Test
    public void testQueryEmsById_exist() {
        List<EmsData> list = null;
        try {
            list = handler.getEmsById(id);
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testAddEmsInstance_validity_false() throws Exception {
        EmsData data = new EmsData();
        data.setName("ems2");
        PowerMockito.doReturn(false).when(handler, "validity", data);
        try {
            handler.add(data);
        } catch (ExtsysException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail("not Exception");
    }

    @Test
    public void testAddEmsInstance_validity_throw_ExtsysException() throws Exception {
        EmsData data = new EmsData();
        data.setName("ems2");
        PowerMockito.doReturn(false).when(handler, "validity", data);
        PowerMockito.doThrow(new ExtsysException()).when(handler, "validity", data);
        try {
            handler.add(data);
        } catch (ExtsysException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail("not Exception");
    }

    @Test
    public void testQueryEmsById_not_exist() {
        List<EmsData> list = null;
        try {
            list = handler.getEmsById("123456");
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void testUpdateEms() {
        EmsData data = new EmsData();
        data.setName("ems_new");
        try {
            handler.update(data, id);
        } catch (ExtsysException e1) {
            Assert.fail("Exception" + e1.getMessage());
            return;
        }
        List<EmsData> list = null;
        try {
            list = handler.getEmsById(id);
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        assertTrue(list.size() > 0 && list.get(0).getName().equals("ems_new"));
    }

}
