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

package org.openo.commonservice.extsys.Handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.commonservice.extsys.entity.db.EmsData;
import org.openo.commonservice.extsys.util.HqlFactory;
import org.openo.commonservice.extsys.common.ExtSysResuorceType;
import org.openo.commonservice.extsys.common.Parameters;
import org.openo.commonservice.extsys.exception.ExtsysException;

/**
 *
 ** @author 10159474
 */
public class EmsHandler extends BaseHandler<EmsData> {

    @Override
    public boolean validity(EmsData data) throws ExtsysException {
        return true;
    }

    public List<EmsData> getAll() throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        return query(query, ExtSysResuorceType.EMS.name());
    }

    public List<EmsData> getEmsById(String id) throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        query.put(Parameters.id.name(), id);
        return query(query, ExtSysResuorceType.EMS.name());
    }

    public void update(EmsData emsData, String id) throws ExtsysException {
        update(emsData, HqlFactory.getOidFilter(Parameters.id.name(), id),
                ExtSysResuorceType.EMS.name());
    }

    public EmsData add(EmsData emsData) throws ExtsysException {

        return create(emsData, ExtSysResuorceType.EMS.name());
    }

    public void delete(String id) throws ExtsysException {
        EmsData ems = new EmsData();
        ems.setId(id);
        delete(ems, ExtSysResuorceType.EMS.name());
    }
}
