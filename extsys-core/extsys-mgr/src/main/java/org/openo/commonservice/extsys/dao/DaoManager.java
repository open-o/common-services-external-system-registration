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

package org.openo.commonservice.extsys.dao;

import org.hibernate.SessionFactory;
import org.openo.commonservice.extsys.common.ExtSysResuorceType;

/**
 * DAO manager class.<br>
 * a class to store DAO instances and provide methods to get these instances
 * 
 *
 */
public class DaoManager {
  private static DaoManager instance = new DaoManager();

  private VimDao vimDao;
  private EmsDao emsDao;
  private VnfmDao vnfmDao;
  private SdncDao sdncDao;
  private CommonDao commonDao;
  private SessionFactory sessionFactory;

  private DaoManager() {}

  public static synchronized DaoManager getInstance() {
    return instance;
  }

  /**
   * according to resource type and return proper DAO.
   * 
   * @param type resource Type
   * @return DAO
   */
  public BaseDao<?> getDao(String type) {
    switch (ExtSysResuorceType.getType(type)) {
      case VIM:
        return getVimDao();
      case SDNC:
        return getSdncDao();
      case VNFM:
        return getVnfmDao();
      case EMS:
        return getEmsDao();
      default:
        return getCommonDao();
    }
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * @return Returns the vim dao.
   */
  public VimDao getVimDao() {
    if (vimDao == null) {
      vimDao = new VimDao(sessionFactory);
    }
    return vimDao;
  }

  public void setVimDao(VimDao vimDao) {
    this.vimDao = vimDao;
  }

  /**
   * @return Returns the ems dao.
   */
  public EmsDao getEmsDao() {
    if (emsDao == null) {
      emsDao = new EmsDao(sessionFactory);
    }
    return emsDao;
  }

  public void setEmsDao(EmsDao emsDao) {
    this.emsDao = emsDao;
  }

  /**
   * @return Returns the vnfm dao.
   */
  public VnfmDao getVnfmDao() {
    if (vnfmDao == null) {
      vnfmDao = new VnfmDao(sessionFactory);
    }
    return vnfmDao;
  }

  public void setVnfmDao(VnfmDao vnfmDao) {
    this.vnfmDao = vnfmDao;
  }

  /**
   * @return Returns the sdnc dao.
   */
  public SdncDao getSdncDao() {
    if (sdncDao == null) {
      sdncDao = new SdncDao(sessionFactory);
    }
    return sdncDao;
  }

  public void setSdncDao(SdncDao sdncDao) {
    this.sdncDao = sdncDao;
  }

  /**
   * @return Returns the common dao.
   */
  public CommonDao getCommonDao() {
    if (commonDao == null) {
      commonDao = new CommonDao(sessionFactory);
    }
    return commonDao;
  }

  public void setCommonDao(CommonDao commonDao) {
    this.commonDao = commonDao;
  }

}
