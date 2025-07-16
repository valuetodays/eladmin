package me.zhengjie;

import java.io.File;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
public abstract class BaseController {
    public Response download(File file) {
        //response为HttpServletResponse对象
        String contentType = ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
//        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");

        return Response.ok(file, MediaType.valueOf(contentType))
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
    }
}
