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
package org.openo.orchestrator.nfv.extsys.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openo.orchestrator.nfv.extsys.exception.ExtsysException;
import org.openo.orchestrator.nfv.extsys.util.HqlFactory;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.util.Generics;

/**
 * a base class for Hibernate DAO classes
 * 
 * provide the common methods to create,delete,update and query data
 * 
 * *@author 10159474
 *
 * @param <T>
 */
public class BaseDao<T> extends AbstractDAO<T> {

    public BaseDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
        this.entityClass = Generics.getTypeParameter(getClass());
    }

    public String[] excludeProperties;
    private SessionFactory sessionFactory;
    protected Session session;
    private final Class<?> entityClass;

    @Override
    protected Session currentSession() {
        return this.session;
    }

    /**
     * @param data the object to update
     * @throws ExtsysException
     */
    public void update(T data, String filter) throws ExtsysException {
        try {
            String hql = HqlFactory.getUpdateHql(data, excludeProperties, filter);
            beginTransaction();
            Query query = this.session.createQuery(hql);
            query.executeUpdate();
            closeTransaction();
        } catch (Exception e) {
            transactionRollBack();
            throw new ExtsysException("", "error while updating data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
    }

    /**
     * @param data the object to delete
     * @throws ExtsysException
     */
    public void delete(T data) throws ExtsysException {
        try {
            beginTransaction();
            this.session.delete(data);
            closeTransaction();
        } catch (Exception e) {
            transactionRollBack();
            throw new ExtsysException("", "error while deleting data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
    }

    /**
     * @param data the object to create
     * @return
     * @throws ExtsysException
     */
    public T create(T data) throws ExtsysException {
        try {
            beginTransaction();
            session.save(data);
            closeTransaction();
        } catch (HibernateException e) {
            transactionRollBack();
            throw new ExtsysException("", "error while creating data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
        return data;
    }

    public List<T> unionQuery(String unionHql) throws ExtsysException {
        List<T> data;
        try {
            beginTransaction();
            Query query = this.session.createQuery(unionHql);
            data = query.list();
            closeTransaction();
        } catch (Exception e) {
            transactionRollBack();
            throw new ExtsysException("",
                    "error while union query data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
        return data;
    }

    public int unionDelete(String unionHql) throws ExtsysException {
        int num = 0;
        try {
            beginTransaction();
            Query query = this.session.createQuery(unionHql);
            num = query.executeUpdate();
            closeTransaction();
        } catch (Exception e) {
            transactionRollBack();
            throw new ExtsysException("",
                    "error while union query data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
        return num;
    }

    /**
     * @param queryParams the condition map used to query objects
     * @return
     * @throws ExtsysException
     */
    @SuppressWarnings("unchecked")
    public List<T> query(Map<String, String> queryParams) throws ExtsysException {
        List<T> result = null;
        try {
            beginTransaction();
            Criteria criteria = this.session.createCriteria(entityClass);
            for (String key : queryParams.keySet()) {
                criteria.add(Restrictions.eq(key, queryParams.get(key)));
            }
            result = (List<T>) criteria.list();
            closeTransaction();
        } catch (HibernateException e) {
            throw new ExtsysException("", "error while querying data.errorMsg:" + e.getMessage());
        } finally {
            closeSession();
        }
        return result;
    }

    protected void beginTransaction() {
        this.session = this.sessionFactory.openSession();
        this.session.beginTransaction();
    }

    protected void closeTransaction() {
        this.session.getTransaction().commit();
    }

    protected void closeSession() {
        this.session.close();
    }

    protected void transactionRollBack() {
        this.session.getTransaction().rollback();
    }

}
