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
package org.openo.orchestrator.nfv.extsys.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.openo.orchestrator.nfv.extsys.Handle.VnfmHandler;
import org.openo.orchestrator.nfv.extsys.entity.db.VnfmData;
import org.openo.orchestrator.nfv.extsys.entity.rest.VnfmRestData;
import org.openo.orchestrator.nfv.extsys.exception.ExtsysException;
import org.openo.orchestrator.nfv.extsys.util.ExtsysDbUtil;
import org.openo.orchestrator.nfv.extsys.util.RestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/vnfms")
@Api(tags = {" vnfm Management "})
public class VnfmManager {

    VnfmHandler handler = new VnfmHandler();
    private static final Logger LOGGER = LoggerFactory.getLogger(VnfmManager.class);

    @Path("")
    @GET
    @ApiOperation(value = "get  all vnfm ")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response queryVnfmList() {
        LOGGER.info("start query all vnfm!");
        List<VnfmData> list;
        try {
            list = handler.getAll();
        } catch (ExtsysException e) {
            LOGGER.error("query all vnfm failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        if (list == null || list.size() <= 0) {
            LOGGER.info("query all vnfm end.no match condition record");
            return RestResponseUtil.getSuccessResponse(null);
        } else {
            LOGGER.info("query all vnfm end.size:" + list.size());
            ArrayList<VnfmRestData> restList = new ArrayList<VnfmRestData>();
            for (int i = 0; i < list.size(); i++) {
                restList.add(new VnfmRestData(list.get(i)));
            }
            return RestResponseUtil.getSuccessResponse(restList);
        }

    }

    @Path("/{vnfmId}")
    @GET
    @ApiOperation(value = "get vnfm by id")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response queryVnfmById(@ApiParam(value = "vnfm id") @PathParam("vnfmId") String vnfmId) {
        LOGGER.info("start query  vnfm by id." + vnfmId);
        List<VnfmData> list;
        try {
            list = handler.getVnfmById(vnfmId);
        } catch (ExtsysException e) {
            LOGGER.error("query  vnfm failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        if (list == null || list.size() <= 0) {
            LOGGER.info("query  vnfm end.no match condition record");
            return RestResponseUtil.getSuccessResponse(null);
        } else {
            LOGGER.info("query  vnfm end.info:" + ExtsysDbUtil.objectToString(list));
            return RestResponseUtil.getSuccessResponse(new VnfmRestData(list.get(0)));
        }
    }

    @Path("/{vnfmId}")
    @DELETE
    @ApiOperation(value = "delete a vnfm")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response delVnfm(@ApiParam(value = "vnfm id") @PathParam("vnfmId") String vnfmId) {
        LOGGER.info("start delete vnfm .id:" + vnfmId);
        try {
            handler.delete(vnfmId);
        } catch (ExtsysException e) {
            LOGGER.error("delete vnfm failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        LOGGER.info(" delete vnfm end !");
        return Response.noContent().build();
    }

    @PUT
    @Path("/{vnfmId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "update a vnfm")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response updateVnfms(@ApiParam(value = "vnfm", required = true) VnfmData vnfm,
            @ApiParam(value = "vnfm id", required = true) @PathParam("vnfmId") String vnfmId) {
        LOGGER.info(
                "start update vnfm .id:" + vnfmId + " info:" + ExtsysDbUtil.objectToString(vnfm));
        try {
            handler.update(vnfm, vnfmId);
        } catch (ExtsysException e) {
            LOGGER.error("update vnfm failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        LOGGER.info(" update vnfm end !");
        return RestResponseUtil.getSuccessResponse(null);
    }

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "create a vnfm")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "microservice not found",
                    response = String.class),
            @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
                    message = "Unprocessable MicroServiceInfo Entity ", response = String.class),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
                    message = "internal server error", response = String.class)})
    @Timed
    public Response addVnfms(@ApiParam(value = "vnfm", required = true) VnfmData vnfm) {
        LOGGER.info("start add vnfm" + " info:" + ExtsysDbUtil.objectToString(vnfm));
        VnfmData vnfmData;
        try {
            vnfmData = handler.add(vnfm);
        } catch (ExtsysException e) {
            LOGGER.error("add vnfm failed.errorMsg:" + e.getErrorMsg());
            return RestResponseUtil.getErrorResponse(e);
        }
        LOGGER.info(" add vnfm end !");
        return RestResponseUtil.getCreateSussceeResponse(new VnfmRestData(vnfmData));
    }
}
