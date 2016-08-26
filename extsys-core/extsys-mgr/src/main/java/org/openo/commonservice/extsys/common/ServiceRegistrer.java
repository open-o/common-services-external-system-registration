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
package org.openo.commonservice.extsys.common;

import org.openo.commonservice.extsys.externalservice.entity.ServiceRegisterEntity;
import org.openo.commonservice.extsys.externalservice.msb.MicroserviceBusConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 10159474
 *
 */
public class ServiceRegistrer implements Runnable {
    private final ServiceRegisterEntity extsysEntity = new ServiceRegisterEntity();
    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistrer.class);

    public ServiceRegistrer() {
        initServiceEntity();
    }

    @Override
    public void run() {
        LOG.info("start extsys microservice register");
        boolean flag = false;
        int retry = 0;
        while (!flag && retry < 1000) {
            LOG.info("extsys microservice register.retry:" + retry);
            retry++;
            flag = MicroserviceBusConsumer.registerService(extsysEntity);
            if (flag == false) {
                LOG.warn("microservice register failed, sleep 30S and try again.");
                ThreadSleep(30000);
            } else {
                LOG.info("microservice register success!");
                break;
            }
        }
        LOG.info("extsys microservice register end.");
    }

    private void ThreadSleep(int second) {
        LOG.info("start sleep ....");
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            LOG.error("thread sleep error.errorMsg:" + e.getMessage());
        }
        LOG.info("sleep end .");
    }

    private void initServiceEntity() {
        extsysEntity.setServiceName("extsys");
        extsysEntity.setProtocol("REST");
        extsysEntity.setVersion("v1");
        extsysEntity.setUrl("/openoapi/extsys/v1");
        extsysEntity.setSingleNode(null, "8100", 0);
        extsysEntity.setVisualRange("1");
    }
}