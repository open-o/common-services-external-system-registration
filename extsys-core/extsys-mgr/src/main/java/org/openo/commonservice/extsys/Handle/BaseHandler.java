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
package org.openo.commonservice.extsys.Handle;

import java.util.List;
import java.util.Map;

import org.openo.commonservice.extsys.dao.BaseDao;
import org.openo.commonservice.extsys.entity.db.BaseData;
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.util.ExtsysDbUtil;
import org.openo.commonservice.extsys.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * an abstract class for  wrapper class
 * 
 * provide the common methods to process the CRUD rest request
 * 
 * *@author 10159474
 *
 * @param <T>
 */
public abstract class BaseHandler<T extends BaseData> {
    private final static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    public Gson gson = new Gson();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T create(T data, String resouceType) throws ExtsysException {
        T rtnData = null;
        boolean check;
        logger.info("BaseHandler:start create data.info:" + ExtsysDbUtil.objectToString(data));
        try {
            check = validity(data);
            if (check)
                logger.error("validity check success!");
            else {
                logger.error("validity check failed");
                throw new ExtsysException("", "validity check failed");
            }
        } catch (ExtsysException e) {
            logger.error("validity check failed,errorMsg:" + e.getErrorMsg());
            throw e;
        }
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            data.setId(ExtsysDbUtil.generateId());
            data.setCreateTime(ExtsysDbUtil.getNowTime());
            data.setCategory(resouceType);
            rtnData = (T) dao.create(data);
        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while creating " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler:create data end.info:" + ExtsysDbUtil.objectToString(data));
        return rtnData;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void delete(T data, String resouceType) throws ExtsysException {
        logger.info("BaseHandler:start delete data.info:" + ExtsysDbUtil.objectToString(data));
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            dao.delete(data);
        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while deleting " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler:delete data end");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void delete(Map<String, String> queryParam, String resouceType) throws ExtsysException {
        logger.info("BaseHandler:start delete data by condition.info:"
                + ExtsysDbUtil.objectToString(queryParam));
        List<T> datas;
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            datas = dao.query(queryParam);
            for (T data : datas) {
                delete(data, resouceType);
            }
        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while deleting " + resouceType, e);
            throw e;
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void update(T data, String filter, String resouceType) throws ExtsysException {
        logger.info("BaseHandler:start update data .info:" + ExtsysDbUtil.objectToString(data)
                + " filter:" + filter);
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            dao.update(data, filter);

        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while updating " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler:update data end ");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> query(Map<String, String> queryParam, String resouceType)
            throws ExtsysException {
        logger.info(
                "BaseHandler:start query data .info:" + ExtsysDbUtil.objectToString(queryParam));
        List<T> datas = null;
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            datas = dao.query(queryParam);

        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while querying " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler: query data end .info:" + ExtsysDbUtil.objectToString(datas));
        return datas;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> unionQuery(String filter, String resouceType) throws ExtsysException {
        logger.info("BaseHandler:start union query data.fliter:" + filter);
        List<T> datas = null;
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            datas = dao.unionQuery(filter);

        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while union querying " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler:union query data end .info:" + ExtsysDbUtil.objectToString(datas));
        return datas;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public int unionDelete(String filter, String resouceType) throws ExtsysException {
        logger.info("BaseHandler:start delete query data.fliter:" + filter);
        int num;
        try {
            BaseDao dao = DaoManager.getInstance().getDao(resouceType);
            num = dao.unionDelete(filter);

        } catch (ExtsysException e) {
            logger.error("BaseHandler:error while union delete " + resouceType, e);
            throw e;
        }
        logger.info("BaseHandler:union delete data end .num:" + num);
        return num;
    }

    /**
     * check if the related object id exists in the system
     * 
     * @param data
     * @throws ExtsysException
     */
    public abstract boolean validity(T data) throws ExtsysException;

}
