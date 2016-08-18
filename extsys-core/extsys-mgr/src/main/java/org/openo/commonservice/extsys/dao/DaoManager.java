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
 * DAO manager class
 * 
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

    public synchronized static DaoManager getInstance() {
        return instance;
    }

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

    public VimDao getVimDao() {
        if (vimDao == null) {
            vimDao = new VimDao(sessionFactory);
        }
        return vimDao;
    }

    public void setVimDao(VimDao vimDao) {
        this.vimDao = vimDao;
    }

    public EmsDao getEmsDao() {
        if (emsDao == null) {
            emsDao = new EmsDao(sessionFactory);
        }
        return emsDao;
    }

    public void setEmsDao(EmsDao emsDao) {
        this.emsDao = emsDao;
    }

    public VnfmDao getVnfmDao() {
        if (vnfmDao == null) {
            vnfmDao = new VnfmDao(sessionFactory);
        }
        return vnfmDao;
    }

    public void setVnfmDao(VnfmDao vnfmDao) {
        this.vnfmDao = vnfmDao;
    }

    public SdncDao getSdncDao() {
        if (sdncDao == null) {
            sdncDao = new SdncDao(sessionFactory);
        }
        return sdncDao;
    }

    public void setSdncDao(SdncDao sdncDao) {
        this.sdncDao = sdncDao;
    }

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
