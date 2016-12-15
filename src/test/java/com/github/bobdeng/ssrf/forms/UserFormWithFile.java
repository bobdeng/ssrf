package com.github.bobdeng.ssrf.forms;

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
public class UserFormWithFile {
    @Param("name")
    private String name;
    private String[] tags;
    private File avatar;
    private File[] album;
}
