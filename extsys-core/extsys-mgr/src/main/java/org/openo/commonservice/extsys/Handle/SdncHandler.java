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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.commonservice.extsys.common.ExtSysResuorceType;
import org.openo.commonservice.extsys.common.Parameters;
import org.openo.commonservice.extsys.entity.db.SdncData;
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.util.HqlFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 ** @author 10159474
 */
public class SdncHandler extends BaseHandler<SdncData> {
    private static final Logger logger = LoggerFactory.getLogger(SdncHandler.class);

    @Override
    public boolean validity(SdncData data) throws ExtsysException {
        return true;
    }

    public List<SdncData> getAll() throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        return query(query, ExtSysResuorceType.SDNC.name());
    }

    public List<SdncData> getSdncById(String id) throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        query.put(Parameters.id.name(), id);
        return query(query, ExtSysResuorceType.SDNC.name());
    }

    public void update(SdncData SdncData, String id) throws ExtsysException {
        update(SdncData, HqlFactory.getOidFilter(Parameters.id.name(), id),
                ExtSysResuorceType.SDNC.name());
    }

    public SdncData add(SdncData sdncData) throws ExtsysException {
        return create(sdncData, ExtSysResuorceType.SDNC.name());
    }

    public void delete(String id) throws ExtsysException {
        SdncData sdnc = new SdncData();
        sdnc.setId(id);
        delete(sdnc, ExtSysResuorceType.SDNC.name());
    }
}
