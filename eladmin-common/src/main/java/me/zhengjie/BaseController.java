package me.zhengjie;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import cn.vt.auth.AuthUser;
import cn.vt.exception.CommonException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.jboss.resteasy.reactive.server.multipart.FileItem;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
public abstract class BaseController /*extends BaseCrudController */ {
    protected Response download(File file) {
        //response为HttpServletResponse对象
        String contentType = ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
//        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");

        return Response.ok(file, MediaType.valueOf(contentType))
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
    }

    protected String getFileContentAsString(MultipartFormDataInput dataInput, String fileId, Charset charset) {
        File file = getFileFormItem(dataInput, fileId);
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, charset);
        } catch (IOException e) {
            throw new CommonException(e);
        }
    }

    protected File getFileFormItem(MultipartFormDataInput dataInput, String fileId) {
        Map<String, Collection<FormValue>> values = dataInput.getValues();
        if (MapUtils.isEmpty(values)) {
            return null;
        }
        Collection<FormValue> fileCollection = values.get(fileId);
        FormValue[] fileFormValue = fileCollection.toArray(FormValue[]::new);
        FormValue formValue = fileFormValue[0];
        FileItem fileItem = formValue.getFileItem();
        java.nio.file.Path file = fileItem.getFile();
        return file.toFile();
    }

    protected AuthUser getCurrentAccount() {
        return null;
    }

    protected Long getCurrentAccountId() {
        return Long.valueOf(getCurrentAccount().getUserId());
    }

    protected void putLoginAccount(AuthUser authUser) {

    }
}
