/**
 * Copyright 2016 [ZTE] and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.commonservice.extsys.db.resource;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.openo.commonservice.extsys.dao.DaoManager;
import org.openo.commonservice.extsys.db.util.H2DbServer;
import org.openo.commonservice.extsys.db.util.HibernateSession;
import org.openo.commonservice.extsys.entity.db.VnfmData;
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.handle.VnfmHandler;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VnfmHandler.class})
public class VnfmManagerTest {
  private VnfmHandler handler;
  private String id = "0000000000000000";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    H2DbServer.startUp();
  }

  /**
   * shut down db.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    try {
      HibernateSession.destory();
      H2DbServer.shutDown();
    } catch (Exception error) {
      Assert.fail("Exception" + error.getMessage());
    }
  }

  /**
   * init db data.
   */
  @Before
  public void setUp() throws Exception {
    DaoManager.getInstance().setSessionFactory(HibernateSession.init());
    VnfmData data = new VnfmData();
    data.setName("vnfm");
    handler = PowerMockito.spy(new VnfmHandler());
    PowerMockito.doReturn(true).when(handler, "validity", data);
    try {
      id = handler.add(data).getId();
    } catch (ExtsysException error) {
      Assert.fail("Exception" + error.getMessage());
    }
  }

  /**
   * clear db data.
   */
  @After
  public void tearDown() {
    try {
      handler.delete(id);
    } catch (ExtsysException error) {
      Assert.fail("Exception" + error.getMessage());
    }
  }


  @Test
  public void testQueryVnfmById_exist() {
    List<VnfmData> list = null;
    try {
      list = handler.getVnfmById(id);
    } catch (ExtsysException error) {
      Assert.fail("Exception" + error.getMessage());
      return;
    }
    Assert.assertTrue(list.size() > 0);
  }

  @Test
  public void testQueryVnfmById_not_exist() {
    List<VnfmData> list = null;
    try {
      list = handler.getVnfmById("100001");
    } catch (ExtsysException error) {
      Assert.fail("Exception" + error.getMessage());
      return;
    }
    Assert.assertTrue(list.size() == 0);
  }

  @Test
  public void testUpdateVnfm() {
    VnfmData data = new VnfmData();
    data.setName("vnfm_new");
    try {
      handler.update(data, id);
    } catch (ExtsysException error1) {
      Assert.fail("Exception" + error1.getMessage());
      return;
    }
    List<VnfmData> list = null;
    try {
      list = handler.getVnfmById(id);
    } catch (ExtsysException error) {
      Assert.fail("Exception" + error.getMessage());
      return;
    }
    assertTrue(list.size() > 0 && list.get(0).getName().equals("vnfm_new"));
  }

  @Test
  public void testAddVnfmInstance_validity_false() throws Exception {
    VnfmData data = new VnfmData();
    data.setName("Vnfm");
    PowerMockito.doReturn(false).when(handler, "validity", data);
    try {
      handler.add(data);
    } catch (ExtsysException error) {
      Assert.assertTrue(true);
      return;
    }
    Assert.fail("not Exception");
  }

  @Test
  public void testAddVnfmInstance_validity_throw_ExtsysException() throws Exception {
    VnfmData data = new VnfmData();
    data.setName("vnfm2");
    PowerMockito.doReturn(false).when(handler, "validity", data);
    PowerMockito.doThrow(new ExtsysException()).when(handler, "validity", data);
    try {
      handler.add(data);
    } catch (ExtsysException error) {
      Assert.assertTrue(true);
      return;
    }
    Assert.fail("not Exception");
  }
}
