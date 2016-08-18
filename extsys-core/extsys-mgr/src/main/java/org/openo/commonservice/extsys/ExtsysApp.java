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

package org.openo.commonservice.extsys;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.openo.commonservice.extsys.common.Config;
import org.openo.commonservice.extsys.entity.db.BaseData;
import org.openo.commonservice.extsys.entity.db.EmsData;
import org.openo.commonservice.extsys.entity.db.SdncData;
import org.openo.commonservice.extsys.entity.db.VimData;
import org.openo.commonservice.extsys.dao.DaoManager;
import org.openo.commonservice.extsys.entity.db.VnfmData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

public class ExtsysApp extends Application<ExtsysAppConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtsysApp.class);

    public static void main(String[] args) throws Exception {
        new ExtsysApp().run(args);
    }

    @Override
    public String getName() {
        return "OPENO-Extsys";
    }

    private final HibernateBundle<ExtsysAppConfiguration> bundle =
            new HibernateBundle<ExtsysAppConfiguration>(EmsData.class, BaseData.class,
                    VimData.class, VnfmData.class, SdncData.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(
                        ExtsysAppConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public void initialize(Bootstrap<ExtsysAppConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
        initDB(bootstrap);
    }

    private void initDao() {
        DaoManager.getInstance().setSessionFactory(bundle.getSessionFactory());
    }

    private void initDB(Bootstrap<ExtsysAppConfiguration> bootstrap) {
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(ExtsysAppConfiguration configuration, Environment environment) {
        LOGGER.info("Start to initialize catalogue.");
        initDao();
        environment.jersey().packages("org.openo.orchestrator.nfv.extsys.resource");
        environment.jersey().register(MultiPartFeature.class);
        initSwaggerConfig(environment, configuration);
        Config.setConfigration(configuration);
        LOGGER.info("Initialize catalogue finished.");
    }

    /**
     * initialize swagger configuration.
     * 
     * @param environment environment information
     * @param configuration configuration
     */
    private void initSwaggerConfig(Environment environment, ExtsysAppConfiguration configuration) {
        environment.jersey().register(new ApiListingResource());
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        BeanConfig config = new BeanConfig();
        config.setTitle("Open-o ExtSys Service rest API");
        config.setVersion("1.0.0");
        config.setResourcePackage("org.openo.orchestrator.nfv.extsys.resource");
        // set rest api basepath in swagger
        SimpleServerFactory simpleServerFactory =
                (SimpleServerFactory) configuration.getServerFactory();
        String basePath = simpleServerFactory.getApplicationContextPath();
        String rootPath = simpleServerFactory.getJerseyRootPath();
        rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
        basePath = basePath.equals("/") ? rootPath
                : (new StringBuilder()).append(basePath).append(rootPath).toString();
        config.setBasePath(basePath);
        config.setScan(true);
    }

}
