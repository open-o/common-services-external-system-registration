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
package org.openo.orchestrator.nfv.extsys.db.resource;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.orchestrator.nfv.extsys.Handle.SdncHandler;
import org.openo.orchestrator.nfv.extsys.dao.DaoManager;
import org.openo.orchestrator.nfv.extsys.db.util.H2DbServer;
import org.openo.orchestrator.nfv.extsys.db.util.HibernateSession;
import org.openo.orchestrator.nfv.extsys.entity.db.SdncData;
import org.openo.orchestrator.nfv.extsys.exception.ExtsysException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SdncHandler.class})
public class SdncManagerTest {
    private SdncHandler handler;
    private String id = "0000000000000000";

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
        SdncData data = new SdncData();
        data.setName("sdnc");
        handler = PowerMockito.spy(new SdncHandler());
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
    public void testAddSdncInstance_validity_false() throws Exception {
        SdncData data = new SdncData();
        data.setName("sdnc");
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
    public void testAddSdncInstance_validity_throw_ExtsysException() throws Exception {
        SdncData data = new SdncData();
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
    public void testQuerySdncById_exist() {
        List<SdncData> list = null;
        try {
            list = handler.getSdncById(id);
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testQuerySdncById_not_exist() {
        List<SdncData> list = null;
        try {
            list = handler.getSdncById("100001");
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void testUpdateSdnc() {
        SdncData data = new SdncData();
        data.setName("Sdnc_new");
        try {
            handler.update(data, id);
        } catch (ExtsysException e1) {
            Assert.fail("Exception" + e1.getMessage());
            return;
        }
        List<SdncData> list = null;
        try {
            list = handler.getSdncById(id);
        } catch (ExtsysException e) {
            Assert.fail("Exception" + e.getMessage());
            return;
        }
        assertTrue(list.size() > 0 && list.get(0).getName().equals("Sdnc_new"));
    }

}
