package com.github.bobdeng.ssrf.model;

import com.github.bobdeng.ssrf.annotations.Param;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormWithFile {
    @Param("name")
    private String name;
    private String[] tags;
    private byte[] avatarData;
    private File file;
}
