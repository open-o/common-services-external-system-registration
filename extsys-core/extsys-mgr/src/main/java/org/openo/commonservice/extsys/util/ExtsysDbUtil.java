/**
 * Copyright 2016 ZTE Corporation.
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
package org.openo.commonservice.extsys.util;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ExtsysDbUtil {
  private final static Logger logger = LoggerFactory.getLogger(ExtsysDbUtil.class);

  public static String generateId() {
    return UUID.randomUUID().toString();
  }

  public static boolean isNotEmpty(String str) {
    return str != null && !"".equals(str) && str.length() > 0;
  }

  /**
   * change object to str.
   */
  public static String objectToString(Object obj) {
    Gson gson = new Gson();
    if (obj != null) {
      return gson.toJson(obj);
    } else {
      return null;
    }
  }

  public static String getNowTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(new Date());
  }
}
