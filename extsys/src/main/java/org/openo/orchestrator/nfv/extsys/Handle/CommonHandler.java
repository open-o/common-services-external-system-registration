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
package org.openo.orchestrator.nfv.extsys.Handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.extsys.common.ExtSysResuorceType;
import org.openo.orchestrator.nfv.extsys.common.Parameters;
import org.openo.orchestrator.nfv.extsys.entity.db.BaseData;
import org.openo.orchestrator.nfv.extsys.exception.ExtsysException;

/**
 *
 ** @author 10159474
 */
public class CommonHandler extends BaseHandler<BaseData> {

    public List<BaseData> getAll() throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        return query(query, ExtSysResuorceType.BASE.name());
    }

    public List<BaseData> getInstanceById(String id) throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        query.put(Parameters.id.name(), id);
        return query(query, ExtSysResuorceType.BASE.name());
    }

    @Override
    public boolean validity(BaseData data) throws ExtsysException {
        // TODO Auto-generated method stub
        return false;
    }

}
