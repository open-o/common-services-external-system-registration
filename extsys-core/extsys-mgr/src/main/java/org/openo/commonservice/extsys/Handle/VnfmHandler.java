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
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.util.HqlFactory;
import org.openo.commonservice.extsys.entity.db.VnfmData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 ** @author 10159474
 */
public class VnfmHandler extends BaseHandler<VnfmData> {
    private static final Logger logger = LoggerFactory.getLogger(VnfmHandler.class);

    @Override
    public boolean validity(VnfmData data) throws ExtsysException {
        return true;
    }

    public List<VnfmData> getAll() throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        return query(query, ExtSysResuorceType.VNFM.name());
    }

    public List<VnfmData> getVnfmById(String id) throws ExtsysException {
        Map<String, String> query = new HashMap<String, String>();
        query.put(Parameters.id.name(), id);
        return query(query, ExtSysResuorceType.VNFM.name());
    }

    public void update(VnfmData vnfmData, String id) throws ExtsysException {
        update(vnfmData, HqlFactory.getOidFilter(Parameters.id.name(), id),
                ExtSysResuorceType.VNFM.name());
    }

    public VnfmData add(VnfmData vnfmData) throws ExtsysException {
        return create(vnfmData, ExtSysResuorceType.VNFM.name());
    }

    public void delete(String id) throws ExtsysException {
        VnfmData vnfm = new VnfmData();
        vnfm.setId(id);
        delete(vnfm, ExtSysResuorceType.VNFM.name());
    }
}
