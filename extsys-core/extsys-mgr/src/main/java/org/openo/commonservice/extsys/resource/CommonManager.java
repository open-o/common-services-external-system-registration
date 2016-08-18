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

package org.openo.commonservice.extsys.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.openo.commonservice.extsys.Handle.CommonHandler;
import org.openo.commonservice.extsys.entity.db.BaseData;
import org.openo.commonservice.extsys.entity.rest.BaseRestData;
import org.openo.commonservice.extsys.exception.ExtsysException;
import org.openo.commonservice.extsys.util.ExtsysDbUtil;
import org.openo.commonservice.extsys.util.RestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/common")
@Api(tags = {" common Management "})
public class CommonManager {

    CommonHandler handler = new CommonHandler();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonManager.class);

    @Path("")
    @GET
    @ApiOperation(value = "get  all instance ")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response queryInstanceList() {
        LOGGER.info("start query all instance!");
        List<BaseData> list;
        try {
            list = handler.getAll();
        } catch (ExtsysException e) {
            LOGGER.error("query all instance failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        if (list == null || list.size() <= 0) {
            LOGGER.info("query all instance end.no match condition record");
            return RestResponseUtil.getSuccessResponse(null);
        } else {
            LOGGER.info("query all instance end.size:" + list.size());
            ArrayList<BaseRestData> restList = new ArrayList<BaseRestData>();
            for (int i = 0; i < list.size(); i++) {
                restList.add(new BaseRestData(list.get(i)));
            }
            return RestResponseUtil.getSuccessResponse(restList);
        }

    }

    @Path("/{instanceId}")
    @GET
    @ApiOperation(value = "get instance by id")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response queryInstanceById(
            @ApiParam(value = "instanceId") @PathParam("instanceId") String instanceId) {
        LOGGER.info("start query  instance by id." + instanceId);
        List<BaseData> list;
        try {
            list = handler.getInstanceById(instanceId);
        } catch (ExtsysException e) {
            LOGGER.error("query  instance failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        if (list == null || list.size() <= 0) {
            LOGGER.info("query  instance end.no match condition record");
            return RestResponseUtil.getSuccessResponse(null);
        } else {
            LOGGER.info("query  ems end.info:" + ExtsysDbUtil.objectToString(list));
            return RestResponseUtil.getSuccessResponse(new BaseRestData(list.get(0)));
        }
    }


}
