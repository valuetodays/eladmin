package me.zhengjie.reqresp;

import java.io.Serializable;

import lombok.Data;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-12
 */
@Data
public class GenPreviewResp implements Serializable {
    private String name;
    private String content;

    public static GenPreviewResp ofNameAndContent(String name, String content) {
        GenPreviewResp resp = new GenPreviewResp();
        resp.setName(name);
        resp.setContent(content);
        return resp;
    }
}
