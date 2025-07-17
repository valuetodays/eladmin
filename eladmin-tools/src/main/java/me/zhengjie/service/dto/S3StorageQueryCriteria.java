/*
*  Copyright 2019-2025 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
 */
package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.List;

/**
* @author Zheng Jie
 * @since 2025-06-25
**/
@Data
public class S3StorageQueryCriteria {

    @Query(type =  Query.Type.INNER_LIKE)
    @Schema(description = "文件名称")
    private String fileName;

    @Query(type = Query.Type.BETWEEN)
    @Schema(description = "创建时间")
    private List<Timestamp> createTime;

}